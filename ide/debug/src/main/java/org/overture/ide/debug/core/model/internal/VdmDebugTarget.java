/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.overture.ide.debug.core.model.internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStepFilters;
import org.eclipse.debug.core.model.ITerminate;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overture.ide.debug.core.ExtendedDebugEventDetails;
import org.overture.ide.debug.core.IDbgpService;
import org.overture.ide.debug.core.IDebugConstants;
import org.overture.ide.debug.core.IDebugOptions;
import org.overture.ide.debug.core.VdmDebugPlugin;
import org.overture.ide.debug.core.dbgp.IDbgpSession;
import org.overture.ide.debug.core.dbgp.IDbgpStreamFilter;
import org.overture.ide.debug.core.dbgp.IDbgpThreadAcceptor;
import org.overture.ide.debug.core.launching.VdmLaunchConfigurationDelegate;
import org.overture.ide.debug.core.model.DefaultDebugOptions;
import org.overture.ide.debug.core.model.IDebugLaunchConstants;
import org.overture.ide.debug.core.model.IVdmBreakpointPathMapper;
import org.overture.ide.debug.core.model.IVdmDebugTarget;
import org.overture.ide.debug.core.model.IVdmDebugTargetListener;
import org.overture.ide.debug.core.model.IVdmDebugThreadConfigurator;
import org.overture.ide.debug.core.model.IVdmThread;
import org.overture.ide.debug.core.model.IVdmVariable;
import org.overture.ide.debug.logging.LogItem;
import org.overture.ide.debug.logging.LogView;
import org.overture.ide.debug.utils.CharOperation;

public class VdmDebugTarget extends VdmDebugElement implements IVdmDebugTarget,
		IVdmThreadManagerListener, IStepFilters
{

	/**
	 * @deprecated
	 * @see #getVdmProject()
	 */
//	private static final String LAUNCH_CONFIGURATION_ATTR_PROJECT = "project"; //$NON-NLS-1$

	private static final int THREAD_TERMINATION_TIMEOUT = 5000; // 5 seconds

	private final ListenerList listeners;

	private IVdmStreamProxy streamProxy;

	private IProcess process;

	private final ILaunch launch;

//	private String name;

	private boolean disconnected;

	private final IVdmThreadManager threadManager;

	final VdmBreakpointManager breakpointManager;

	private final IDbgpService dbgpService;
	private final String sessionId;

	private final String modelId;

	private static final WeakHashMap<VdmDebugTarget, String> targets = new WeakHashMap<VdmDebugTarget, String>();
	private String[] stepFilters;

	private boolean useStepFilters;

	private final Object processLock = new Object();

	private boolean initialized = false;
	private boolean retrieveGlobalVariables;
	private boolean retrieveClassVariables;
	private boolean retrieveLocalVariables;

	private final IDebugOptions options;

	private boolean logging = false;
	static private LogView logView;

	private IVdmProject vdmProject = null;

	public static List<VdmDebugTarget> getAllTargets()
	{
		synchronized (targets)
		{
			return new ArrayList<VdmDebugTarget>(targets.keySet());
		}
	}

	public static LogView getLogView()
	{
		return logView;
	}

	public VdmDebugTarget(String modelId, IDbgpService dbgpService,
			String sessionId, ILaunch launch, IProcess process)
	{
		this(modelId, dbgpService, sessionId, launch, process,
				DefaultDebugOptions.getDefaultInstance());
	}

	public VdmDebugTarget(String modelId, IDbgpService dbgpService,
			String sessionId, ILaunch launch, IProcess process,
			IDebugOptions options)
	{
		Assert.isNotNull(options);

		try
		{
			setLogging(launch);
		} catch (CoreException e)
		{
			// OK
		}

		this.modelId = modelId;

		this.listeners = new ListenerList();

		this.process = process;
		this.launch = launch;
		this.options = options;

		this.threadManager = new /* New */VdmThreadManager(this);
		this.threadManager.addListener(this);
		this.sessionId = sessionId;
		this.dbgpService = dbgpService;

		this.disconnected = false;
		// this.streamProxy = new VdmStreamProxy(getIOConsole());
		this.breakpointManager = new VdmBreakpointManager(this,
				createPathMapper());

		DebugEventHelper.fireCreateEvent(this);
		synchronized (targets)
		{
			targets.put(this, ""); //$NON-NLS-1$
		}
	}

	private IOConsole getIOConsole()
	{
		try
		{
			IOConsole console = new IOConsole(VdmLaunchConfigurationDelegate
					.getVdmProject(this.launch.getLaunchConfiguration())
					.toString(), null);
			console.activate();
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
					new IConsole[] { console });
			return console;
		} catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void shutdown()
	{
		try
		{
			terminate(true);
		} catch (DebugException e)
		{
			VdmDebugPlugin.log(e);
		}
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public IDebugTarget getDebugTarget()
	{
		return this;
	}

	public String getModelIdentifier()
	{
		return modelId;
	}

	public ILaunch getLaunch()
	{
		return launch;
	}

	// IDebugTarget
	public IProcess getProcess()
	{
		synchronized (processLock)
		{
			return process;
		}
	}

	public void setProcess(IProcess process)
	{
		synchronized (processLock)
		{
			this.process = process;
		}
	}

	public boolean hasThreads()
	{
		return threadManager.hasThreads();
	}

	public IThread[] getThreads()
	{
		return threadManager.getThreads();
	}

	public String getName()
	{
		return "VDM Application";
	}

	// ITerminate
	public boolean canTerminate()
	{
		synchronized (processLock)
		{
			return threadManager.canTerminate() || process != null
					&& process.canTerminate();
		}
	}

	public boolean isTerminated()
	{
		synchronized (processLock)
		{
			return threadManager.isTerminated()
					&& (process == null || process.isTerminated());
		}
	}

	protected static boolean waitTerminated(ITerminate terminate, int chunk,
			long timeout)
	{
		final long start = System.currentTimeMillis();
		while (!terminate.isTerminated())
		{
			if (System.currentTimeMillis() - start > timeout)
			{
				return false;
			}
			try
			{
				Thread.sleep(chunk);
			} catch (InterruptedException e)
			{
				// interrupted
			}
		}
		return true;
	}

	public void terminate() throws DebugException
	{
		terminate(true);
	}

	protected void terminate(boolean waitTermination) throws DebugException
	{
		fireTargetTerminating();

		threadManager.sendTerminationRequest();
		if (waitTermination)
		{
			final IProcess p = getProcess();
			final int CHUNK = 500;
			if (!(waitTerminated(threadManager, CHUNK,
					THREAD_TERMINATION_TIMEOUT) && (p == null || waitTerminated(
					p, CHUNK, THREAD_TERMINATION_TIMEOUT))))
			{
				// Debugging process is not answering, so terminating it
				if (p != null && p.canTerminate())
				{
					p.terminate();
				}
			}
		}

		threadManager.removeListener(this);
		breakpointManager.threadTerminated();

		DebugEventHelper.fireTerminateEvent(this);
	}

	// ISuspendResume
	public boolean canSuspend()
	{
		return threadManager.canSuspend();
	}

	public boolean isSuspended()
	{
		return threadManager.isSuspended();
	}

	public void suspend() throws DebugException
	{
		threadManager.suspend();
	}

	public boolean canResume()
	{
		return threadManager.canResume();
	}

	public void resume() throws DebugException
	{
		threadManager.resume();
	}

	// IDisconnect
	public boolean canDisconnect()
	{
		// Detach feature support!!!
		return false;
	}

	public void disconnect()
	{
		disconnected = true;
	}

	public boolean isDisconnected()
	{
		return disconnected;
	}

	// IMemoryBlockRetrieval
	public boolean supportsStorageRetrieval()
	{
		return false;
	}

	public IMemoryBlock getMemoryBlock(long startAddress, long length)
	{
		return null;
	}

	public IVdmVariable findVariable(String variableName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	// Request timeout
	public int getRequestTimeout()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void setRequestTimeout(int timeout)
	{
		// TODO Auto-generated method stub

	}

	// IBreakpointListener
	public void breakpointAdded(IBreakpoint breakpoint)
	{
		breakpointManager.breakpointAdded(breakpoint);
	}

	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta)
	{
		breakpointManager.breakpointChanged(breakpoint, delta);
	}

	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta)
	{
		breakpointManager.breakpointRemoved(breakpoint, delta);
	}

	// Streams
	public IVdmStreamProxy getStreamProxy()
	{
		return streamProxy;
	}

	public void setStreamProxy(IVdmStreamProxy proxy)
	{
		this.streamProxy = proxy;
		if (proxy != null)
		{
			proxy.setEncoding(getConsoleEncoding());
		}
	}

	// IDbgpThreadManagerListener
	public void threadAccepted(IVdmThread thread, boolean first)
	{
		if (first)
		{
			DebugEventHelper.fireExtendedEvent(this,
					ExtendedDebugEventDetails.BEFORE_CODE_LOADED);
			initialized = true;
			fireTargetInitialized();
		}
	}

	protected IVdmBreakpointPathMapper createPathMapper()
	{
		return new NopVdmbreakpointPathMapper();
	}

	public void allThreadsTerminated()
	{
		try
		{
			if (streamProxy != null)
			{
				streamProxy.close();
			}
			terminate(false);
		} catch (DebugException e)
		{
			VdmDebugPlugin.log(e);
		}
	}

	public String toString()
	{
		return "Debugging engine (id = " + this.sessionId + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	// IVdmDebugTarget
	public void runToLine(URI uri, int lineNumber) throws DebugException
	{
		breakpointManager.setBreakpointUntilFirstSuspend(uri, lineNumber);
		resume();
	}

	public boolean isInitialized()
	{
		return initialized;
	}

	protected void fireTargetInitialized()
	{
		Object[] list = listeners.getListeners();
		for (int i = 0; i < list.length; ++i)
		{
			((IVdmDebugTargetListener) list[i]).targetInitialized();
		}
	}

	protected void fireTargetTerminating()
	{
		Object[] list = listeners.getListeners();
		for (int i = 0; i < list.length; ++i)
		{
			((IVdmDebugTargetListener) list[i]).targetTerminating();
		}
	}

	public void addListener(IVdmDebugTargetListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(IVdmDebugTargetListener listener)
	{
		listeners.remove(listener);
	}

	public boolean supportsBreakpoint(IBreakpoint breakpoint)
	{
		return breakpointManager.supportsBreakpoint(breakpoint);
	}

	public void setFilters(String[] activeFilters)
	{
		this.stepFilters = activeFilters;
	}

	public String[] getFilters()
	{
		if (this.stepFilters != null)
		{
			return this.stepFilters;
		}
		return CharOperation.NO_STRINGS;
	}

	public boolean isUseStepFilters()
	{
		return useStepFilters;
	}

	public void setUseStepFilters(boolean useStepFilters)
	{
		this.useStepFilters = useStepFilters;
	}

	// /**
	// * @deprecated project should not be used to detect nature, since the
	// nature
	// * is already known before launch. Also, at some point we will
	// * have multiple natures in the same project, so it will not
	// * work correctly either.
	// */
	// protected IVdmProject getVdmProject() {
	// final ILaunchConfiguration configuration = launch
	// .getLaunchConfiguration();
	// if (configuration != null) {
	// try {
	// String projectName = configuration.getAttribute(
	// LAUNCH_CONFIGURATION_ATTR_PROJECT, (String) null);
	// if (projectName != null) {
	// projectName = projectName.trim();
	// if (projectName.length() > 0) {
	// IProject project = ResourcesPlugin.getWorkspace()
	// .getRoot().getProject(projectName);
	// return DLTKCore.create(project);
	// }
	// }
	// } catch (CoreException e) {
	// if (DLTKCore.DEBUG) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return null;
	// }

	public boolean breakOnFirstLineEnabled()
	{
		return IDebugLaunchConstants.isBreakOnFirstLine(launch);
		// return true;
	}

	public void toggleGlobalVariables(boolean enabled)
	{
		retrieveGlobalVariables = enabled;
		threadManager.refreshThreads();
	}

	public void toggleClassVariables(boolean enabled)
	{
		retrieveClassVariables = enabled;
		threadManager.refreshThreads();
	}

	public void toggleLocalVariables(boolean enabled)
	{
		retrieveLocalVariables = enabled;
		threadManager.refreshThreads();
	}

	public boolean retrieveClassVariables()
	{
		return retrieveClassVariables;
	}

	public boolean retrieveGlobalVariables()
	{
		return retrieveGlobalVariables;
	}

	public boolean retrieveLocalVariables()
	{
		return retrieveLocalVariables;
	}

	public String getConsoleEncoding()
	{
		String encoding = "UTF-8"; //$NON-NLS-1$
		try
		{
			encoding = getLaunch().getLaunchConfiguration().getAttribute(
					DebugPlugin.ATTR_CONSOLE_ENCODING, encoding);
		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		return encoding;
	}

	public void setVdmDebugThreadConfigurator(
			IVdmDebugThreadConfigurator configurator)
	{
		this.threadManager.setVdmThreadConfigurator(configurator);
	}

	public IDebugOptions getOptions()
	{
		return options;
	}

	public boolean isStepFiltersEnabled()
	{
		return isUseStepFilters();
	}

	public void setStepFiltersEnabled(boolean enabled)
	{
		setUseStepFilters(enabled);
	}

	public boolean supportsStepFilters()
	{
		return true;
	}

	/*
	 * @see org.eclipse.dltk.debug.core.model.IVdmDebugTarget#getSessions()
	 */
	public IDbgpSession[] getSessions()
	{
		return breakpointManager.getSessions();
	}

	public IVdmBreakpointPathMapper getPathMapper()
	{
		return breakpointManager.bpPathMapper;
	}

	/**
	 * @param streamFilters
	 */
	public void setStreamFilters(IDbgpStreamFilter[] streamFilters)
	{
		((VdmThreadManager) threadManager).setStreamFilters(streamFilters);
	}

	public IDbgpService getDbgpService()
	{
		return dbgpService;
	}

	public IDbgpThreadAcceptor getDbgpThreadAcceptor()
	{
		return threadManager;
	}

	/**
	 * @since 2.0
	 */
	public boolean isRemote()
	{
		return false;
	}

	public void setVdmProject(IVdmProject project)
	{
		this.vdmProject = project;
	}

	public IVdmProject getVdmProject()
	{
		return vdmProject;
	}

	private void setLogging(ILaunch launch) throws CoreException
	{
		if (launch.getLaunchConfiguration().getAttribute(
				IDebugConstants.VDM_LAUNCH_CONFIG_ENABLE_LOGGING, false))
		{
			logging = true;
			final IWorkbench wb = PlatformUI.getWorkbench();
			if (wb.getWorkbenchWindowCount() > 0)
			{

				wb.getDisplay().syncExec(new Runnable()
				{

					public void run()
					{
						IWorkbenchPage page = wb.getWorkbenchWindows()[0]
								.getActivePage();
						IViewPart v;
						try
						{
							v = page.showView(IDebugConstants.LogViewId);
							if (v instanceof LogView)
								logView = ((LogView) v);
						} catch (PartInitException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

			}

			logView.clear();
		}

	}

	// / Extract
	private boolean isCoverageEnabled() throws CoreException
	{
		return getLaunch().getLaunchConfiguration().getAttribute(
				IDebugConstants.VDM_LAUNCH_CONFIG_CREATE_COVERAGE, false);
	}

	private String getContent(IVdmSourceUnit source) throws CoreException,
			IOException
	{
		InputStreamReader reader = new InputStreamReader(source.getFile()
				.getContents());
		StringBuilder sb = new StringBuilder();

		int inLine;
		while ((inLine = reader.read()) != -1)
		{
			sb.append((char) inLine);
		}
		reader.close();
		return sb.toString();
	}

	public static void writeFile(File outputFolder, String fileName,
			String content) throws IOException
	{
		FileWriter outputFileReader = new FileWriter(new File(outputFolder,
				fileName));
		BufferedWriter outputStream = new BufferedWriter(outputFileReader);
		outputStream.write(content);
		outputStream.close();
	}

	protected File getOutputFolder(IVdmProject vdmProject)
	{
		IProject project = (IProject) vdmProject.getAdapter(IProject.class);
		Assert.isNotNull(project, "Project could not be adapted");
		File outputDir = new File(project.getLocation().toFile(), "generated");
		outputDir.mkdirs();
		return outputDir;
	}

	// / End Extract

	public void printLog(LogItem item)
	{
		if (logging)
		{
			logView.log(item);
			logView.setFocus();
		}
	}

	public void handleCustomTerminationCommands(IDbgpSession dbgpSession)
	{

		try
		{
			if (isCoverageEnabled())
			{
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy_MM_dd_HH_mm_ss");
				File coverageDir = new File(new File(
						getOutputFolder(vdmProject), "coverage"), dateFormat
						.format(new Date()));

				coverageDir.mkdirs();

				dbgpSession.getOvertureCommands().writeCoverage(coverageDir);

				for (IVdmSourceUnit source : this.vdmProject.getSpecFiles())
				{
					String name = source.getSystemFile().getName();

					writeFile(coverageDir, name + "cov", getContent(source));
				}
			}
			IProject project = (IProject) vdmProject.getAdapter(IProject.class);
			if (project != null)
			{
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		} catch (Exception e)
		{
			if (VdmDebugPlugin.DEBUG)
			{
				e.printStackTrace();
			}
		}

	}
}
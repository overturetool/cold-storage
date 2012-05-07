package org.overture.ide.plugins.traces.internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.overture.ast.definitions.ANamedTraceDefinition;
import org.overture.ast.node.INode;
import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overture.ide.plugins.traces.TracesXmlStoreReader;
import org.overture.ide.plugins.traces.views.TraceAstUtility;
import org.overturetool.ct.utils.TraceReductionType;

public class VdmjTracesHelper 
{
	String projectName;
	final String COVERAGE_DIR_NAME = "generated/coverage";
	public final IVdmProject project;
	Map<String, TracesXmlStoreReader> classTraceReaders = new HashMap<String, TracesXmlStoreReader>();
	File projectDir;
	Shell shell;

	private File coverageBaseDir;

	public VdmjTracesHelper(Shell shell, IVdmProject vdmProject)
			throws Exception
	{
		this.project = vdmProject;
		this.shell = shell;
	}

	public File getCTRunCoverageDir()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		File coverageDir = new File(coverageBaseDir, "CT_"
				+ dateFormat.format(new Date()));

		if (!coverageDir.exists())
			coverageDir.mkdirs();

		return coverageDir;
	}

	private void copySourceFilesForCoverage(File coverageDir)
			throws IOException, CoreException
	{
		for (IVdmSourceUnit source : this.project.getSpecFiles())
		{
			String name = source.getSystemFile().getName();

			writeFile(coverageDir, name + "cov", getContent(source));
		}
	}

	private String getContent(IVdmSourceUnit source) throws CoreException,
			IOException
	{
		InputStreamReader reader = new InputStreamReader(source.getFile().getContents());
		StringBuilder sb = new StringBuilder();

		int inLine;
		while ((inLine = reader.read()) != -1)
		{
			sb.append((char) inLine);
		}
		return sb.toString();
	}

	public static void writeFile(File outputFolder, String fileName,
			String content) throws IOException
	{
		FileWriter outputFileReader = new FileWriter(new File(outputFolder, fileName));
		BufferedWriter outputStream = new BufferedWriter(outputFileReader);
		outputStream.write(content);
		outputStream.close();
	}

	public void consolePrint(String message)
	{

		MessageConsole myConsole = findConsole("TracesConsole");
		MessageConsoleStream out = myConsole.newMessageStream();
		out.println(message);

	}

	private MessageConsole findConsole(String name)
	{
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	public void evaluateTraces(INode container, ANamedTraceDefinition traceDef,
			IProgressMonitor monitor, ITracesDisplay display)
			throws IOException, CoreException
	{
		evaluateTraces(container, traceDef, 0,null,0,monitor, display, false);
	}

	public void evaluateTraces(INode container, ANamedTraceDefinition traceDef,
			float subset, TraceReductionType traceReductionType, long seed,
			IProgressMonitor monitor, ITracesDisplay display) throws IOException, CoreException
	{
		evaluateTraces(container, traceDef, subset,traceReductionType,seed,monitor, display, true);
	}
	
	private void evaluateTraces(INode container, ANamedTraceDefinition traceDef,
			float subset, TraceReductionType traceReductionType, long seed,
			IProgressMonitor monitor, ITracesDisplay display, boolean useReduction) throws IOException, CoreException
	{
		List<TraceExecutionSetup> traceSetups = new Vector<TraceExecutionSetup>();

		if (container != null)
		{
			if (traceDef != null)
			{
				TraceExecutionSetup texe = new TraceExecutionSetup(project, TraceAstUtility.getContainerName(container), traceDef.getName().name, getCTRunCoverageDir(),subset, traceReductionType,seed,useReduction);
				traceSetups.add(texe);
			} else
			{
				for (ANamedTraceDefinition tDef : TraceAstUtility.getTraceDefinitions(container))
				{
					TraceExecutionSetup texe = new TraceExecutionSetup(project, TraceAstUtility.getContainerName(container), tDef.getName().name, getCTRunCoverageDir(),subset, traceReductionType,seed,useReduction);
					traceSetups.add(texe);
				}
			}
		}else
		{
			for (INode c : TraceAstUtility.getTraceContainers(project))
			{
				if (traceDef != null)
				{
					TraceExecutionSetup texe = new TraceExecutionSetup(project, TraceAstUtility.getContainerName(c), traceDef.getName().name, getCTRunCoverageDir(),subset, traceReductionType,seed,useReduction);
					traceSetups.add(texe);
				} else
				{
					for (ANamedTraceDefinition tDef : TraceAstUtility.getTraceDefinitions(c))
					{
						TraceExecutionSetup texe = new TraceExecutionSetup(project, TraceAstUtility.getContainerName(c), tDef.getName().name, getCTRunCoverageDir(),subset, traceReductionType,seed,useReduction);
						traceSetups.add(texe);
					}
				}
			}
		}

		execute(monitor, display, traceSetups);
	}

	private void execute(IProgressMonitor monitor, ITracesDisplay display,
			List<TraceExecutionSetup> texe) throws IOException, CoreException
	{
		MessageConsole myConsole = findConsole("TracesConsole");
		MessageConsoleStream out = myConsole.newMessageStream();

		for (TraceExecutionSetup traceExecutionSetup : texe)
		{
			new TraceTestEngine().launch(traceExecutionSetup, out, display);
			copySourceFilesForCoverage(traceExecutionSetup.coverageFolder);
		}

		try
		{
			IProject p = (IProject) project.getAdapter(IProject.class);
			p.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e)
		{

		}
	}

}

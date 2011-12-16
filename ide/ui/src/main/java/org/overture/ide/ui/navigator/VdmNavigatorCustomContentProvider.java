/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overture.ide.ui.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

/**
 * Please notice that the events send to this class is based on the navigatorContent:
 * <ul>
 * <li>triggerPoints</li>
 * <li>possibleChildren</li>
 * </ul>
 * 
 * @author kela
 */
@SuppressWarnings("restriction")
public class VdmNavigatorCustomContentProvider
		extends
		org.eclipse.ui.internal.navigator.resources.workbench.ResourceExtensionContentProvider
{
	private Viewer viewer;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.model.WorkbenchContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 * java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		super.inputChanged(viewer, oldInput, newInput);
		this.viewer = viewer;
	}

	/**
	 * Process the resource delta.
	 * 
	 * @param delta
	 */
	@Override
	protected void processDelta(IResourceDelta delta)
	{
		super.processDelta(delta);
		Control ctrl = viewer.getControl();
		if (ctrl == null || ctrl.isDisposed())
		{
			return;
		}

		final Collection<Runnable> runnables = new ArrayList<Runnable>();
		processDelta(delta, runnables);

		if (runnables.isEmpty())
		{
			return;
		}
		// Are we in the UIThread? If so spin it until we are done
		if (ctrl.getDisplay().getThread() == Thread.currentThread())
		{
			runUpdates(runnables);
		} else
		{
			ctrl.getDisplay().asyncExec(new Runnable()
			{
				/*
				 * (non-Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				public void run()
				{
					// Abort if this happens after disposes
					Control ctrl = viewer.getControl();
					if (ctrl == null || ctrl.isDisposed())
					{
						return;
					}

					runUpdates(runnables);
				}
			});
		}

	}

	/**
	 * Process a resource delta. Add any runnables
	 */
	private void processDelta(IResourceDelta delta,
			Collection<Runnable> runnables)
	{
		// he widget may have been destroyed
		// by the time this is run. Check for this and do nothing if so.
		Control ctrl = viewer.getControl();
		if (ctrl == null || ctrl.isDisposed())
		{
			return;
		}

		// Get the affected resource
		final IResource resource = delta.getResource();

		// If any children have changed type, just do a full refresh of this
		// parent,
		// since a simple update on such children won't work,
		// and trying to map the change to a remove and add is too dicey.
		// The case is: folder A renamed to existing file B, answering yes to
		// overwrite B.
		IResourceDelta[] affectedChildren = delta.getAffectedChildren(IResourceDelta.CHANGED);

		for (int i = 0; i < affectedChildren.length; i++)
		{
			if ((affectedChildren[i].getFlags() & IResourceDelta.TYPE) != 0)
			{
				// This is handled in the super class don't do it again.
				return;
			}
		}

		// Check the flags for changes the Navigator cares about.
		// See ResourceLabelProvider for the aspects it cares about.
		// Notice we do care about F_CONTENT or F_MARKERS currently here
		// but this is not the case in the ResourceExtensionContentProvider.
		int changeFlags = delta.getFlags();

		if ((changeFlags & IResourceDelta.MARKERS) != 0)
		{// && (changeFlags& IResourceDelta.CONTENT) != 0) {
			runnables.add(getRefreshRunnable(resource));
			// also refresh all parents
			IResource parent = resource;
			while ((parent = parent.getParent()) != null)
			{
				runnables.add(getRefreshRunnable(parent));
				if (parent instanceof IProject)
				{
					break;
				}
			}

			return;
		}

		// Handle changed children .
		for (int i = 0; i < affectedChildren.length; i++)
		{
			processDelta(affectedChildren[i], runnables);
		}
	}

	/**
	 * Return a runnable for refreshing a resource.
	 * 
	 * @param resource
	 * @return Runnable
	 */
	private Runnable getRefreshRunnable(final IResource resource)
	{
		return new Runnable()
		{
			public void run()
			{
				((StructuredViewer) viewer).update(resource, null);
			}
		};
	}

	/**
	 * Run all of the runnables that are the widget updates
	 * 
	 * @param runnables
	 */
	private void runUpdates(Collection<Runnable> runnables)
	{
		Iterator<Runnable> runnableIterator = runnables.iterator();
		while (runnableIterator.hasNext())
		{
			((Runnable) runnableIterator.next()).run();
		}

	}
}
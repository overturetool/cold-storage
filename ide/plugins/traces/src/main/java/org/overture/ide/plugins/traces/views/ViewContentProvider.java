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
package org.overture.ide.plugins.traces.views;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.part.ViewPart;
import org.overture.ide.plugins.traces.views.treeView.ClassTreeNode;
import org.overture.ide.plugins.traces.views.treeView.ITreeNode;
import org.overture.ide.plugins.traces.views.treeView.NotYetReadyTreeNode;
import org.overture.ide.plugins.traces.views.treeView.ProjectTreeNode;
import org.overture.ide.plugins.traces.views.treeView.TreeParent;
import org.overturetool.traces.utility.ITracesHelper;

public class ViewContentProvider implements IStructuredContentProvider,
ITreeContentProvider {
	private TreeParent invisibleRoot;
	// private ArrayList<ProjectTreeNode> projectTraceTreeNodes;
	Map<String, ITracesHelper> traceHelpers;
	ViewPart viewer;
	public ViewContentProvider(Map<String, ITracesHelper> trs,ViewPart p) {
		this.traceHelpers = trs;
		viewer =p;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		
		
		
	}

	public void dispose() {
	}

	public void addChild(ProjectTreeNode project) {
		invisibleRoot.addChild(project);
	}

	public Object[] getElements(Object parent) {
		//if (parent.equals(getViewSite())) {
			if (invisibleRoot == null) {
				initialize();
			}
			return getChildren(invisibleRoot);
		//}
		//return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof ITreeNode) {
			return ((ITreeNode) child).getParent();
		}
//		if (child instanceof ClassTreeNode) {
//			return ((ClassTreeNode) child).getParent();
//		}
//		if (child instanceof TraceTreeNode) {
//			return ((TraceTreeNode) child).getParent();
//		}
//		if (child instanceof TraceTestGroup) {
//			return ((TraceTestGroup) child).getParent();
//		}
//		if (child instanceof TraceTestTreeNode) {
//			return ((TraceTestTreeNode) child).getParent();
//		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof ITreeNode) {
			return ((ITreeNode) parent).getChildren().toArray();
		}
//		if (parent instanceof ProjectTreeNode) {
//			return ((ProjectTreeNode) parent).getChildren().toArray();
//		}
//		if (parent instanceof ClassTreeNode) {
//			return ((ClassTreeNode) parent).getChildren().toArray();
//		}
//		if (parent instanceof TraceTestGroup) {
//			return ((TraceTestGroup) parent).getChildren().toArray();
//		}
//		if (parent instanceof TraceTreeNode) {
//			return ((TraceTreeNode) parent).getChildren().toArray();
//		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
//		if (parent instanceof TreeParent) {
//			return ((TreeParent) parent).hasChildren();
//		}
//		if (parent instanceof ProjectTreeNode) {
//			return ((ProjectTreeNode) parent).hasChildren();
//		}
//		if (parent instanceof ClassTreeNode) {
//			return ((ClassTreeNode) parent).hasChildren();
//		}
		if (parent instanceof NotYetReadyTreeNode) {
			return false;
		}
		if (parent instanceof ITreeNode) {
			return ((ITreeNode) parent).hasChildren();
		}
//		if (parent instanceof TraceTreeNode) {
//			return ((TraceTreeNode) parent).hasChildren();
//		}
		return false;
	}

	/*
	 * We will set up a dummy model to initialize tree heararchy. In a real
	 * code, you will connect to a real model and expose its hierarchy.
	 */
	private void initialize() {
		invisibleRoot = new TreeParent("");
		// projectTraceTreeNodes = new ArrayList<ProjectTreeNode>();
		// String[] exts = new String[]{"vpp", "tex" , "vdm"}; // TODO get
		// extension from core xml..
		IWorkspaceRoot iworkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] iprojects = iworkspaceRoot.getProjects();
		// ArrayList<String> fileNameList = new ArrayList<String>();

		ProjectTreeNode projectTreeNode;

		// ArrayList<TreeParent> projectTree = new ArrayList<TreeParent>();
		for (int j = 0; j < iprojects.length; j++) {

			try {
				// if the project is a overture project
				if (TracesTreeView.isValidProject(iprojects[j])) {
					
					// create project node
					projectTreeNode = new ProjectTreeNode(iprojects[j]);
					
					ITracesHelper tr = traceHelpers.get(iprojects[j].getName());
					if (tr == null)
						continue;

					List<String> classes = tr.getClassNamesWithTraces();
					boolean isTraceProject = false;
					for (String className : classes) {
						if (className != null) {
							isTraceProject = true;
							 ClassTreeNode classTreeNode = new
							 ClassTreeNode(className);
							
							// // add to project and root
							 projectTreeNode.addChild(classTreeNode);
						}
					}
					if (isTraceProject && classes.size() > 0) {
						invisibleRoot.addChild(projectTreeNode);
					}
				}
			} catch (Exception e1) {
				System.out.println("Exception: " + e1.getMessage());
				e1.printStackTrace();
			}
		}
	}
}

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
package org.overture.ide.plugins.traces.views.treeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Display;
import org.overture.ide.plugins.traces.TracesXmlStoreReader.TraceInfo;
import org.overture.ide.plugins.traces.internal.VdmjTracesHelper;
import org.overturetool.traces.utility.ITracesHelper;
import org.overturetool.traces.utility.TraceHelperNotInitializedException;
import org.overturetool.traces.utility.TraceTestResult;
import org.overturetool.vdmj.definitions.NamedTraceDefinition;
import org.xml.sax.SAXException;


public class TraceTreeNode implements IAdaptable,ITreeNode
{

	private NamedTraceDefinition traceDefinition;
	private ITreeNode parent;
	private List<ITreeNode> children;
	private int testSkippedCount = 0;
	private int testTotal = 0;
	private ITracesHelper traceHelper;
	private TraceInfo info;

	public TraceTreeNode(NamedTraceDefinition traceDef,
			ITracesHelper traceHelper, String className) throws SAXException, IOException, ClassNotFoundException, TraceHelperNotInitializedException
	{
		this.traceDefinition = traceDef;
		this.setTraceHelper(traceHelper);
		this.children = new ArrayList<ITreeNode>();
		
		
		
		if(traceHelper instanceof VdmjTracesHelper)
		{
			setInfo(((VdmjTracesHelper)traceHelper).getTraceInfo(className, traceDef.getName()));
		}
		
		Integer totalTests = traceHelper.getTraceTestCount(className,
				traceDef.getName());
		this.setTestTotal(totalTests);

		this.setSkippedCount(traceHelper.getSkippedCount(className,
				traceDef.getName()));

		if (totalTests > 0)
			this.addChild(new NotYetReadyTreeNode());
	}

	public ITreeNode getParent()
	{
		return parent;
	}

	public NamedTraceDefinition getTraceDefinition()
	{
		return traceDefinition;
	}

	public void setSkippedCount(int skippedCount)
	{
		testSkippedCount = skippedCount;
	}

	@Override
	public String toString()
	{
		if (testSkippedCount != 0)
			return getName() + " (" + getTestTotal() + " skipped "
					+ testSkippedCount + ")";
		else
			return getName() + " (" + getTestTotal() + ")";
	}

	public String getName()
	{

		return traceDefinition.name.name;

	}

	public void setParent(ITreeNode parent)
	{
		this.parent = parent;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
	{
		return null;
	}

	public void addChild(ITreeNode child)
	{
		if (!children.contains(child))
		{
			boolean contains = false;
			for (ITreeNode node : getChildren())
			{
				if (node.getName().equals(child.getName()))
					contains = true;
			}
			if (!contains)
			{
				children.add(child);
				child.setParent(this);
			}
		}
	}

	public void removeChild(TraceTestTreeNode child)
	{
		children.remove(child);
		child.setParent(null);
	}

	public List<ITreeNode> getChildren()
	{
		return children;
	}

	public boolean hasChildren()
	{
		return children.size() > 0;
	}

	public boolean hasChild(String name)
	{
		for (ITreeNode node : children)
		{
			if (node.getName().equals(name))
				return true;

		}
		return false;
	}

	/**
	 * @param testTotal
	 *            the testTotal to set
	 */
	public void setTestTotal(int testTotal)
	{
		this.testTotal = testTotal;
	}

	/**
	 * @return the testTotal
	 */
	public int getTestTotal()
	{
		return testTotal;
	}

//	public void LoadTests() throws Exception
//	{
//		children.clear();
//
//		Long size = new Long(getTraceHelper().GetTraceTestCount(
//				parent.getName(),
//				getName()));
//
//		if (size <= TraceTestGroup.GROUP_SIZE)
//		{
//			List<TraceTestResult> traceStatus = getTraceHelper().GetTraceTests(
//					parent.getName(),
//					getName());
//			for (TraceTestResult traceTestStatus : traceStatus)
//			{
//				this.addChild(new TraceTestTreeNode(traceTestStatus));
//			}
//		} else
//		{
//			Double numberOfGroups = Math.ceil(size.doubleValue()
//					/ TraceTestGroup.GROUP_SIZE);
//			// Double t = TraceTestGroup.NumberOfLevels(size,
//			// TraceTestGroup.GROUP_SIZE);
//
//			if (numberOfGroups > TraceTestGroup.GROUP_SIZE)
//				numberOfGroups = TraceTestGroup.GROUP_SIZE.doubleValue();
//
//			Long testCountInGroup = (size) / numberOfGroups.longValue();
//			
//			if(testCountInGroup<TraceTestGroup.GROUP_SIZE && size>=TraceTestGroup.GROUP_SIZE)
//				testCountInGroup= TraceTestGroup.GROUP_SIZE; //top up all groups
//			
//			Long currentCount = new Long(0);
//			for (int i = 0; i < numberOfGroups - 1 && currentCount<size; i++)
//			{
//				TraceTestGroup group = new TraceTestGroup(currentCount + 1,
//						currentCount + testCountInGroup.longValue() + 1);
//				currentCount += testCountInGroup;
//				this.addChild(group);
//			}
//			if (!currentCount.equals( size))
//			{
//				TraceTestGroup group = new TraceTestGroup(currentCount + 1,
//						size + 1);
//				this.addChild(group);
//
//			}
//		}
//	}

	public void loadTests() throws Exception
	{
		children.clear();

		Long size = new Long(getTraceHelper().getTraceTestCount(
				parent.getName(),
				getName()));
		
		GroupSizeCalculator gs = new GroupSizeCalculator(size);

		if (!gs.hasGroups())
		{
			List<TraceTestResult> traceStatus = getTraceHelper().getTraceTests(
					parent.getName(),
					getName());
			for (TraceTestResult traceTestStatus : traceStatus)
			{
				this.addChild(new TraceTestTreeNode(traceTestStatus));
			}
		} else
		{
						
			Long currentCount = new Long(0);
			for (int i = 0; i < gs.getNumberOfGroups() - 1 && currentCount<size; i++)
			{
				final TraceTestGroup group = new TraceTestGroup(currentCount + 1,
						currentCount + gs.getGroupSize() + 1);
				currentCount += gs.getGroupSize();
				this.addChild(group);
				Display.getCurrent().syncExec(new Runnable()
				{
					
					public void run()
					{
						
						try
						{
							group.loadGroupStatus();
						} catch (Exception e)
						{
							e.printStackTrace();
						} 
					}
				});
				
			}
			if (!currentCount.equals( size))
			{
			final	TraceTestGroup group = new TraceTestGroup(currentCount + 1,
						size + 1);
				this.addChild(group);
				Display.getCurrent().syncExec(new Runnable()
				{
					
					public void run()
					{
						
						try
						{
							group.loadGroupStatus();
						} catch (Exception e)
						{
							e.printStackTrace();
						} 
					}
				});
			}
		}
	}
	
	public void unloadTests()
	{
		children.clear();
		children.add(new NotYetReadyTreeNode());
	}

	/**
	 * @param traceHelper
	 *            the traceHelper to set
	 */
	public void setTraceHelper(ITracesHelper traceHelper)
	{
		this.traceHelper = traceHelper;
	}

	/**
	 * @return the traceHelper
	 */
	public ITracesHelper getTraceHelper()
	{
		return traceHelper;
	}

	private void setInfo(TraceInfo info)
	{
		this.info = info;
	}

	public TraceInfo getInfo()
	{
		return info;
	}

	

}
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
package org.overture.ide.core.ast;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.overture.ide.core.ElementChangedEvent;
import org.overture.ide.core.IVdmElement;
import org.overture.ide.core.IVdmElementDelta;
import org.overture.ide.core.IVdmModel;
import org.overture.ide.core.VdmCore;
import org.overture.ide.core.VdmElementDelta;
import org.overture.ide.core.parser.SourceParserManager;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overturetool.vdmj.ast.IAstNode;
import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.definitions.ClassList;
import org.overturetool.vdmj.modules.Module;
import org.overturetool.vdmj.modules.ModuleList;

public class VdmModel implements IVdmModel
{
	// static int count = 0;
	// int id;
	protected boolean isTypeChecked = false;
	protected boolean isTypeCorrect = false;
	protected int workingCopyNotCommitedCount = 0;

	protected Date checkedTime;

	protected List<IVdmSourceUnit> vdmSourceUnits = new Vector<IVdmSourceUnit>();

	public VdmModel()
	{
		// count++;
		// id = count;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#getRootElementList()
	 */
	public synchronized List<IAstNode> getRootElementList()
	{
		List<IAstNode> list = new Vector<IAstNode>();
		for (IVdmSourceUnit unit : vdmSourceUnits)
		{
			list.addAll(unit.getParseList());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#getCheckedTime()
	 */
	public synchronized Date getCheckedTime()
	{
		return checkedTime;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#setChecked(boolean)
	 */
	public synchronized void setTypeCheckedStatus(boolean checked)
	{
		this.isTypeChecked = true;
		this.isTypeCorrect = checked;
		this.checkedTime = new Date();
		if (checked)
		{
			fireModelCheckedEvent();
		}
	}

	protected void fireModelCheckedEvent()
	{
		VdmCore.getDeltaProcessor().fire(this, new ElementChangedEvent(new VdmElementDelta(this, IVdmElementDelta.F_TYPE_CHECKED), ElementChangedEvent.DeltaType.POST_RECONCILE));
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#isChecked()
	 */
	public synchronized boolean isTypeCorrect()
	{
		return isTypeCorrect && isTypeChecked;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#hasFile(java.io.File)
	 */
	public synchronized boolean hasFile(File file)
	{
		Assert.isNotNull(null);
		return false;
	}

	public synchronized boolean hasFile(IVdmSourceUnit file)
	{
		return this.vdmSourceUnits.contains(file);

	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#getModuleList()
	 */
	public synchronized ModuleList getModuleList() throws NotAllowedException
	{
		ModuleList modules = new ModuleList();
		for (Object definition : getRootElementList())
		{
			if (definition instanceof Module)
				modules.add((Module) definition);
			else
				throw new NotAllowedException("Other definition than Module is found: "
						+ definition.getClass().getName());
		}

		modules.combineDefaults();

		return modules;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#getClassList()
	 */
	public synchronized ClassList getClassList() throws NotAllowedException
	{
		ClassList classes = new ClassList();
		for (Object definition : getRootElementList())
		{
			if (definition instanceof ClassDefinition)
				classes.add((ClassDefinition) definition);
			else
				throw new NotAllowedException("Other definition than ClassDefinition is found: "
						+ definition.getClass().getName());
		}
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#hasClassList()
	 */
	public synchronized boolean hasClassList()
	{
		for (Object definition : getRootElementList())
		{
			if (definition instanceof ClassDefinition)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#hasModuleList()
	 */
	public synchronized boolean hasModuleList()
	{
		for (Object definition : getRootElementList())
		{
			if (definition instanceof Module)
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.overture.ide.core.ast.IVdmElement#isParseCorrect()
	 */
	public synchronized boolean isParseCorrect()
	{
		boolean isParseCorrect = true;
		for (IVdmSourceUnit source : vdmSourceUnits)
		{
			if (source.hasParseErrors())
			{
				isParseCorrect = false;
				break;
			}
		}
		return isParseCorrect;
	}

	public boolean exists()
	{
		return getRootElementList().size() > 0;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
	{
		return null;
	}

	public synchronized void addVdmSourceUnit(IVdmSourceUnit unit)
	{
		this.isTypeChecked = false;
		this.isTypeCorrect = false;
		if (!vdmSourceUnits.contains(unit))
		{
			this.vdmSourceUnits.add(unit);
		} else
		{
			System.err.println("Add error: " + unit);
		}

	}

	public synchronized void remove(IVdmSourceUnit unit)
	{
		this.isTypeChecked = false;
		this.isTypeCorrect = false;
		if (vdmSourceUnits.contains(unit))
		{
			this.vdmSourceUnits.remove(unit);
		} else
		{
			System.err.println("Remove error: " + unit);
		}
	}

	public synchronized IVdmSourceUnit getVdmSourceUnit(IFile file)
	{
		for (IVdmSourceUnit unit : vdmSourceUnits)
		{
			if (unit.getFile().equals(file))
				return unit;
		}
		return null;
	}

	public int getElementType()
	{
		return IVdmElement.VDM_MODEL;
	}

	public synchronized void clean()
	{
		for (IVdmSourceUnit unit : vdmSourceUnits)
		{
			unit.clean();
		}
		// this.parseCurrectTable.clear();
		this.isTypeChecked = false;
		this.isTypeCorrect = false;

	}
	


	public void refresh(boolean completeRefresh, IProgressMonitor monitor)
	{
		int worked = 1;
		if (monitor != null)
		{
			monitor.beginTask("Refreshing model", vdmSourceUnits.size());
		}
		for (IVdmSourceUnit source : vdmSourceUnits)
		{
			if (!completeRefresh && source.getParseList().size() > 0)
			{
				continue;
			}

			try
			{
				SourceParserManager.parseFile(source);
			} catch (CoreException e)
			{
				if (VdmCore.DEBUG)
				{
					VdmCore.log("Error in VdmModel refresh", e);
				}
			} catch (IOException e)
			{
				if (VdmCore.DEBUG)
				{
					VdmCore.log("Error in VdmModel refresh", e);
				}
			}
			if (monitor != null)
			{
				monitor.worked(++worked);
			}
		}

		if (monitor != null)
		{
			monitor.done();
		}

	}

	public List<IVdmSourceUnit> getSourceUnits()
	{
		return this.vdmSourceUnits;
	}

	public synchronized VdmModelWorkingCopy getWorkingCopy()
	{
		workingCopyNotCommitedCount++;
		return new VdmModelWorkingCopy(this);
	}

	public boolean isTypeChecked()
	{
		return this.isTypeChecked;
	}

	public void setIsTypeChecked(boolean checked)
	{
		this.isTypeChecked = checked;
	}
	
	public synchronized boolean hasWorkingCopies()
	{
		return workingCopyNotCommitedCount>0;
	}

}

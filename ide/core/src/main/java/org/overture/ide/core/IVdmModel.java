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
package org.overture.ide.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.overture.ide.core.ast.NotAllowedException;
import org.overture.ide.core.ast.VdmModelWorkingCopy;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overturetool.vdmj.ast.IAstNode;
import org.overturetool.vdmj.definitions.ClassList;
import org.overturetool.vdmj.modules.ModuleList;

public interface IVdmModel extends IVdmElement
{

	// public abstract void setRootElementList(List<T> rootElementList);

	public abstract List<IAstNode> getRootElementList();

	public abstract Date getCheckedTime();

	/**
	 * Sets the status of the TC, after calling this the model is set to a TC state with the TC status parsed
	 * 
	 * @param checked
	 *            true if TC ok else false
	 */
	public abstract void setTypeCheckedStatus(boolean checked);

	/**
	 * Sets if the model has been TC checked. This is false if the source and model is out of sync, e.g. if the source
	 * loaded into the editor is changed but not yet saved.
	 * 
	 * @param checked
	 *            false if the model and source is out of sync and a TC must be performed before it is certain that it
	 *            still is TC OK.
	 */
	public abstract void setIsTypeChecked(boolean checked);

	/**
	 * Check if the model has been type checked without errors
	 * 
	 * @return true if no TC errors
	 */
	public abstract boolean isTypeCorrect();

	/**
	 * Checks if a the model has been type checked. This is not the same as it has no errors.
	 * 
	 * @return true if the model has been TC, this does not reveal if the model is TC ok for that see
	 *         <b>isTypeCorrect</b>
	 */
	public abstract boolean isTypeChecked();

	/***
	 * Updates the local list with a new Definition if it already exists the old one is replaced
	 * 
	 * @param module
	 *            the new definition
	 */
	// @SuppressWarnings("unchecked")
	// public abstract void update(List<T> modules);

	/***
	 * Check if any definition in the list has the file as source location
	 * 
	 * @param file
	 *            The file which should be tested against all definitions in the list
	 * @return true if the file has a definition in the list
	 */
	public abstract boolean hasFile(File file);

	public abstract boolean hasFile(IVdmSourceUnit file);

	public abstract ModuleList getModuleList() throws NotAllowedException;

	public abstract ClassList getClassList() throws NotAllowedException;

	public abstract boolean hasClassList();

	public abstract boolean hasModuleList();

	// public abstract void setParseCorrect(String file, Boolean isParseCorrect);

	public abstract boolean isParseCorrect();

	public abstract boolean exists();

	// public abstract IVdmModel filter(IFile file);

	public abstract IVdmSourceUnit getVdmSourceUnit(IFile file);

	public abstract void addVdmSourceUnit(IVdmSourceUnit unit);

	public abstract void clean();

	public abstract List<IVdmSourceUnit> getSourceUnits();

	public abstract VdmModelWorkingCopy getWorkingCopy();

	/**
	 * Refresh source unit (reparse, will require type check again then)
	 * 
	 * @param completeRefresh
	 *            true to reparse all source units. False only to reparse source unit with empty parse lists
	 * @param monitor
	 *            null or a progress monitor
	 */
	public void refresh(boolean completeRefresh, IProgressMonitor monitor);

	public abstract void remove(IVdmSourceUnit iVdmSourceUnit);
	
	/**
	 * Returns if any working copies has been issued from the current model. If this returns true
	 * any changes to the model might be overridden when a working copy is committed.
	 * @return returns true if working copies has been issued
	 */
	 boolean hasWorkingCopies();
}
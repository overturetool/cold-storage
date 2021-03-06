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
package org.overture.ide.core.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;
import org.overture.ide.internal.core.resources.VdmProject;

public class VdmProjectAdapterFactory implements IAdapterFactory
{

	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType)
	{
		if(adapterType == IVdmProject.class)
		{
			if(adaptableObject instanceof IProject){
				IProject project = (IProject) adaptableObject;
				if(VdmProject.isVdmProject(project))
				{
					return VdmProject.createProject(project);
				}
			}
		}
		
		if(adapterType == IProject.class)
		{
			if(adaptableObject instanceof VdmProject){
				VdmProject project = (VdmProject) adaptableObject;
				return project.project;
			}
		}
		
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getAdapterList()
	{		
		return new Class[]{IVdmProject.class, IProject.class};
	}

}

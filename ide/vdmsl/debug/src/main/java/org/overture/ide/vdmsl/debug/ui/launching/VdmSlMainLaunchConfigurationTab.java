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
package org.overture.ide.vdmsl.debug.ui.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import org.overture.ide.core.IVdmModel;
import org.overture.ide.core.ast.NotAllowedException;
import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.debug.ui.launching.AbstractVdmMainLaunchConfigurationTab;
import org.overture.ide.ui.utility.VdmTypeCheckerUi;
import org.overture.ide.vdmsl.debug.Activator;
import org.overture.ide.vdmsl.core.IVdmSlCoreConstants;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.messages.Console;
import org.overturetool.vdmj.messages.VDMErrorsException;
import org.overturetool.vdmj.modules.ModuleList;
import org.overturetool.vdmj.runtime.ModuleInterpreter;

public class VdmSlMainLaunchConfigurationTab extends
		AbstractVdmMainLaunchConfigurationTab
{

	@Override
	protected String getExpression(String module, String operation,
			boolean isStatic)
	{
		return module + STATIC_CALL_SEPERATOR + operation;
	}

	@Override
	protected boolean isSupported(IProject project) throws CoreException
	{
		return project.hasNature(IVdmSlCoreConstants.NATURE);
	}

	@Override
	protected boolean validateTypes(IVdmProject project, String expression)
	{
		try
		{
			Settings.dialect = Dialect.VDM_SL;
			Settings.release = project.getLanguageVersion();
			Console.charset = getProject().getDefaultCharset();
			IVdmModel model = project.getModel();
			if (!model.isTypeCorrect())
			{
				if (!VdmTypeCheckerUi.typeCheck(getShell(), project))
				{
					setErrorMessage("Type errors in Model");
					return false;
				}
			}
			ModuleList modules = model.getModuleList();
			modules.combineDefaults();
			ModuleInterpreter ci = new ModuleInterpreter(modules);
			if (expression.contains(STATIC_CALL_SEPERATOR))
			{
				ci.setDefaultName(expression.substring(0, expression.indexOf(STATIC_CALL_SEPERATOR))); // needed for static fn/op check
			}

			ci.typeCheck(expression);
			return true;
		} catch (NotAllowedException e)
		{
			setErrorMessage(e.toString());
			e.printStackTrace();
		} catch (VDMErrorsException e)
		{
			setErrorMessage(e.toString());
		} catch (Exception e)
		{
			if (e.getMessage() != null && e.getMessage().contains("not loaded"))
			{
				setErrorMessage(e.getMessage());
			}
			else
			{
				Activator.logError("Internal type check error", e);
				setErrorMessage("Internal type check error: " + e.toString());
			}
		}

		return false;
	}
	
	@Override
	protected String getModuleLabelName()
	{
		return "Module";
	}

}

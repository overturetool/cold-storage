package org.overture.ide.plugins.codegen.commands;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ide.core.IVdmModel;
import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overture.ide.plugins.codegen.Activator;
import org.overture.ide.plugins.codegen.vdm2cpp.PluginVdm2CppUtil;
import org.overture.ide.ui.utility.VdmTypeCheckerUi;
import org.overture.interpreter.messages.Console;
import org.overture.codegen.vdmcodegen.CodeGen;

public class Vdm2CppCommand extends AbstractHandler
{

	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		IVdmProject vdmProject = PluginVdm2CppUtil.getVdmProject(event);

		if (vdmProject == null)
			return null;

		final IVdmModel model = vdmProject.getModel();

		if (model == null || !model.isParseCorrect())
			return null;

		if (!model.isTypeChecked())
			VdmTypeCheckerUi.typeCheck(HandlerUtil.getActiveShell(event), vdmProject);

		if (!model.isTypeCorrect()
				|| !PluginVdm2CppUtil.isSupportedVdmDialect(vdmProject))
			return null;

		final CodeGen vdm2cpp = new CodeGen();

		try
		{			
			List<IVdmSourceUnit> sources = model.getSourceUnits();
			List<SClassDefinition> mergedParseLists = PluginVdm2CppUtil.mergeParseLists(sources);
						
			vdm2cpp.generateCode(mergedParseLists);

		} catch (AnalysisException ex)
		{
			Activator.log("Failed generating code", ex);
			return null;
		} catch (Exception ex)
		{
			Activator.log("Failed generating code", ex);
			return null;
		}
		
		//vdm2cpp.save(codeGenContextMap);

		return null;
	}

}
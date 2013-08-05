package org.overture.interpreter.assistant.module;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.overture.ast.definitions.ARenamedDefinition;
import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.statements.PStm;
import org.overture.interpreter.assistant.IInterpreterAssistantFactory;
import org.overture.interpreter.assistant.definition.AStateDefinitionAssistantInterpreter;
import org.overture.interpreter.assistant.definition.PDefinitionAssistantInterpreter;
import org.overture.interpreter.assistant.definition.PDefinitionListAssistantInterpreter;
import org.overture.interpreter.runtime.Context;
import org.overture.interpreter.runtime.ContextException;
import org.overture.interpreter.runtime.StateContext;
import org.overture.interpreter.util.ModuleListInterpreter;
import org.overture.pog.assistant.PogAssistantFactory;
import org.overture.pog.obligation.POContextStack;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.typechecker.assistant.module.AModuleModulesAssistantTC;

public class AModuleModulesAssistantInterpreter extends
		AModuleModulesAssistantTC
{
	protected static IInterpreterAssistantFactory af;

	@SuppressWarnings("static-access")
	public AModuleModulesAssistantInterpreter(IInterpreterAssistantFactory af)
	{
		super(af);
		this.af = af;
	}

	public static PStm findStatement(ModuleListInterpreter modules, File file,
			int lineno)
	{
		for (AModuleModules m : modules)
		{
			if (m.getName().getLocation().getFile().equals(file))
			{
				PStm stmt = findStatement(m, lineno);

				if (stmt != null)
				{
					return stmt;
				}
			}
		}

		return null;
	}

	public static PStm findStatement(AModuleModules m, int lineno)
	{
		return PDefinitionAssistantInterpreter.findStatement(m.getDefs(), lineno);
	}

	public static PExp findExpression(ModuleListInterpreter modules, File file,
			int lineno)
	{
		for (AModuleModules m : modules)
		{
			if (m.getName().getLocation().getFile().equals(file))
			{
				PExp exp = findExpression(m, lineno);

				if (exp != null)
				{
					return exp;
				}
			}
		}

		return null;
	}

	public static PExp findExpression(AModuleModules d, int lineno)
	{
		return PDefinitionListAssistantInterpreter.findExpression(d.getDefs(), lineno);
	}

	public static Context getStateContext(AModuleModules defaultModule)
	{
		AStateDefinition sdef = PDefinitionListAssistantInterpreter.findStateDefinition(defaultModule.getDefs());

		if (sdef != null)
		{
			return AStateDefinitionAssistantInterpreter.getStateContext(sdef);
		}

		return null;
	}

	/**
	 * Initialize the system for execution from this module. The initial {@link Context} is created, and populated with
	 * name/value pairs from the local definitions and the imported definitions. If state is defined by the module, this
	 * is also initialized, creating the state Context.
	 * 
	 * @return True if initialized OK.
	 */
	public static Set<ContextException> initialize(AModuleModules m,
			StateContext initialContext)
	{

		Set<ContextException> trouble = new HashSet<ContextException>();

		for (PDefinition d : m.getImportdefs())
		{
			if (d instanceof ARenamedDefinition)
			{
				try
				{
					initialContext.putList(PDefinitionAssistantInterpreter.getNamedValues(d, initialContext));
				} catch (ContextException e)
				{
					trouble.add(e); // Carry on...
				}
			}
		}

		for (PDefinition d : m.getDefs())
		{
			try
			{
				initialContext.putList(PDefinitionAssistantInterpreter.getNamedValues(d, initialContext));
			} catch (ContextException e)
			{
				trouble.add(e); // Carry on...
			}
		}

		try
		{
			AStateDefinition sdef = PDefinitionListAssistantInterpreter.findStateDefinition(m.getDefs());

			if (sdef != null)
			{
				AStateDefinitionAssistantInterpreter.initState(sdef, initialContext);
			}
		} catch (ContextException e)
		{
			trouble.add(e); // Carry on...
		}

		return trouble;

	}

	public static ProofObligationList getProofObligations(AModuleModules m)
	{
		return PDefinitionListAssistantInterpreter.getProofObligations(m.getDefs(), new POContextStack(new PogAssistantFactory()));
	}

}
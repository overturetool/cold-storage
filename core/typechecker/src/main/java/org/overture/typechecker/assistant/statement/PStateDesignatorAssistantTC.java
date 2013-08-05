package org.overture.typechecker.assistant.statement;

import org.overture.ast.definitions.PDefinition;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.ast.statements.PStateDesignator;
import org.overture.ast.typechecker.NameScope;
import org.overture.typechecker.TypeCheckInfo;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;


public class PStateDesignatorAssistantTC {
	protected static ITypeCheckerAssistantFactory af;

	@SuppressWarnings("static-access")
	public PStateDesignatorAssistantTC(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}
	public static PDefinition targetDefinition(PStateDesignator pStateDesignator, TypeCheckInfo question) {
		if (AIdentifierStateDesignator.kindPStateDesignator.equals(pStateDesignator.kindPStateDesignator()))
		{
			AIdentifierStateDesignator stateDesignator = (AIdentifierStateDesignator) pStateDesignator;
			return question.env.findName(stateDesignator.getName(),NameScope.STATE);
		} else {
			return null;
		}

	}
}
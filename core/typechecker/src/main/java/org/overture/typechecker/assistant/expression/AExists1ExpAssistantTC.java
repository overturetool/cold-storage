package org.overture.typechecker.assistant.expression;

import org.overture.ast.expressions.AExists1Exp;
import org.overture.ast.lex.LexNameList;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;
import org.overture.typechecker.assistant.pattern.PBindAssistantTC;

public class AExists1ExpAssistantTC {
	protected static ITypeCheckerAssistantFactory af;

	@SuppressWarnings("static-access")
	public AExists1ExpAssistantTC(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}
	public static LexNameList getOldNames(AExists1Exp expression) {
		LexNameList list = PBindAssistantTC.getOldNames(expression.getBind());
		list.addAll(PExpAssistantTC.getOldNames(expression.getPredicate()));
		return list;
	}

}
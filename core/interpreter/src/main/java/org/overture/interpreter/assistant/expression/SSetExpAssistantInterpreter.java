package org.overture.interpreter.assistant.expression;

import org.overture.ast.expressions.ASetCompSetExp;
import org.overture.ast.expressions.ASetEnumSetExp;
import org.overture.ast.expressions.ASetRangeSetExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.expressions.SSetExp;
import org.overture.interpreter.runtime.ObjectContext;
import org.overture.interpreter.values.ValueList;
import org.overture.typechecker.assistant.expression.SSetExpAssistantTC;

public class SSetExpAssistantInterpreter extends SSetExpAssistantTC
{

	public static ValueList getValues(SSetExp exp, ObjectContext ctxt)
	{
		switch (exp.kindSSetExp())
		{
			case SETCOMP:
				return ASetCompSetExpAssistantInterpreter.getValues((ASetCompSetExp)exp,ctxt);
			case SETENUM:
				return ASetEnumSetExpAssistantInterpreter.getValues((ASetEnumSetExp)exp,ctxt);
			default:
				return new ValueList();
		}
	}

	public static PExp findExpression(SSetExp exp, int lineno)
	{
		switch (exp.kindSSetExp())
		{
			case SETCOMP:
				return ASetCompSetExpAssistantInterpreter.findExpression((ASetCompSetExp)exp,lineno);
			case SETENUM:
				return ASetEnumSetExpAssistantInterpreter.findExpression((ASetEnumSetExp)exp,lineno);
			case SETRANGE:
				return ASetRangeSetExpAssistantInterpreter.findExpression((ASetRangeSetExp)exp,lineno);
			default:
				return null;
		}
	}
	
}
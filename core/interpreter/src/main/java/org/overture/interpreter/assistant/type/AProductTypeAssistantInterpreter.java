package org.overture.interpreter.assistant.type;

import org.overture.ast.types.AProductType;
import org.overture.interpreter.assistant.IInterpreterAssistantFactory;
import org.overture.interpreter.runtime.Context;
import org.overture.interpreter.runtime.ValueException;
import org.overture.interpreter.values.ValueList;
import org.overture.typechecker.assistant.type.AProductTypeAssistantTC;

public class AProductTypeAssistantInterpreter extends AProductTypeAssistantTC
{
	protected static IInterpreterAssistantFactory af;

	@SuppressWarnings("static-access")
	public AProductTypeAssistantInterpreter(IInterpreterAssistantFactory af)
	{
		super(af);
		this.af = af;
	}

	public static ValueList getAllValues(AProductType type, Context ctxt)
			throws ValueException
	{
		return PTypeListAssistant.getAllValues(type.getTypes(), ctxt);
	}

}
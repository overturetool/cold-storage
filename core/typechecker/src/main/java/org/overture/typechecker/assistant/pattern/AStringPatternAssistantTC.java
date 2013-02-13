package org.overture.typechecker.assistant.pattern;

import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.lex.LexStringToken;
import org.overture.ast.patterns.AStringPattern;
import org.overture.ast.types.ASeqSeqType;
import org.overture.ast.types.PType;

public class AStringPatternAssistantTC {

	public static PType getPossibleTypes(AStringPattern pattern) {
		ASeqSeqType t = AstFactory.newASeqSeqType(pattern.getLocation(),AstFactory.newACharBasicType(pattern.getLocation()));
		return t;
	}

	public static PExp getMatchingExpression(AStringPattern sp) {
		LexStringToken v = sp.getValue();
		return AstFactory.newAStringLiteralExp( (LexStringToken) v.clone());
	}

}
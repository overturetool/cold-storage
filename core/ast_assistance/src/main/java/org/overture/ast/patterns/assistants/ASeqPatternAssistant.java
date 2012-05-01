package org.overture.ast.patterns.assistants;

import org.overture.ast.patterns.ASeqPattern;
import org.overture.ast.patterns.PPattern;
import org.overturetool.vdmj.lex.LexNameList;


public class ASeqPatternAssistant {

	
	public static LexNameList getVariableNames(ASeqPattern pattern) {
		LexNameList list = new LexNameList();

		for (PPattern p: pattern.getPlist())
		{
			list.addAll(PPatternAssistant.getVariableNames(p));
		}

		return list;
	}

	
}
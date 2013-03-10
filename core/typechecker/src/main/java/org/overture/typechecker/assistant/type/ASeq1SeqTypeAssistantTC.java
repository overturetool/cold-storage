package org.overture.typechecker.assistant.type;

import org.overture.ast.types.ASeq1SeqType;

public class ASeq1SeqTypeAssistantTC {

	public static String toDisplay(ASeq1SeqType exptype) {
		return exptype.getEmpty() ? "[]" : "seq1 of (" + exptype.getSeqof() + ")";
	}

}
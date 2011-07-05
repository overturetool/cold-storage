package org.overture.runtime;

import org.overture.ast.types.SNumericBasicType;

public class HelperNumericBasicType {

	public static int getWeight(SNumericBasicType subn) {
		switch(subn.kindSNumericBasicType())
		{
			case INT:
				return 2;
			case NAT:
				return 1;
			case NATONE:
				return 0;
			case RATIONAL:
				return 3;
			case REAL:
				return 4;
		}			
		return -1;
	}

}
package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlRecordConstructor extends IOmlExpression
{
	abstract IOmlName getName() throws CGException;
	@SuppressWarnings("rawtypes")
	abstract Vector getExpressionList() throws CGException;
}


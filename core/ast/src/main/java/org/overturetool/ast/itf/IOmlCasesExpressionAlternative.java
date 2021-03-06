package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlCasesExpressionAlternative extends IOmlNode
{
	@SuppressWarnings("rawtypes")
	abstract Vector getPatternList() throws CGException;
	abstract IOmlExpression getExpression() throws CGException;
}


package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlWaitingExpression extends IOmlExpression
{
	@SuppressWarnings("rawtypes")
	abstract Vector getNameList() throws CGException;
}


package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlCallStatement extends IOmlStatement
{
	abstract IOmlObjectDesignator getObjectDesignator() throws CGException;
	abstract Boolean hasObjectDesignator() throws CGException;
	abstract IOmlName getName() throws CGException;
	@SuppressWarnings("rawtypes")
	abstract Vector getExpressionList() throws CGException;
}


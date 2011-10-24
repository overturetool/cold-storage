package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlFunctionTypeInstantiation extends IOmlExpression
{
	abstract IOmlName getName() throws CGException;
	@SuppressWarnings("unchecked")
	abstract Vector getTypeList() throws CGException;
}

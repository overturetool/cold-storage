package org.overturetool.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlInheritanceClause extends IOmlNode
{
	abstract Vector getIdentifierList() throws CGException;
}


package org.overturetool.jml.ast.itf;

import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IJmlExceptionalSpec extends IJmlOperationTrailer
{
	abstract IJmlScope getPrivacy() throws CGException;
	abstract Vector getList() throws CGException;
}


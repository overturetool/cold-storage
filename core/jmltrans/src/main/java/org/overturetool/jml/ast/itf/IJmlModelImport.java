package org.overturetool.jml.ast.itf;

import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IJmlModelImport extends IJmlNode
{
	abstract Boolean getModel() throws CGException;
	abstract String getImport() throws CGException;
}


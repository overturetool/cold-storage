package org.overturetool.ast.itf;

import jp.co.csk.vdm.toolbox.VDM.*;

public abstract interface IOmlUnaryOperator extends IOmlNode
{
	abstract void setValue(Long val) throws CGException;
	abstract Long getValue() throws CGException;
	abstract String getStringValue() throws CGException;
}


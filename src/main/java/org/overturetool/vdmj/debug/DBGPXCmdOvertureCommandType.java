package org.overturetool.vdmj.debug;

public enum DBGPXCmdOvertureCommandType
{
	INIT("init"),
	CREATE("create"),
	CURRENT_LINE("currentline"),
	SOURCE("source"),
	COVERAGE("coverage"),
	WRITE_COVERAGE("writecoverage"),
	LATEX("latex"),
	POG("pog"),
	STACK("stack"),
	TRACE("trace"),
	LIST("list"),
	FILES("files"),
	CLASSES("classes"),
	MODULES("modules"),
	DEFAULT("default"),
	LOG("log");
	
	public String value;

	DBGPXCmdOvertureCommandType(String value)
	{
		this.value = value;
	}

	public static DBGPXCmdOvertureCommandType lookup(String string) throws DBGPException
	{
		for (DBGPXCmdOvertureCommandType cmd: values())
		{
			if (cmd.value.equals(string))
			{
				return cmd;
			}
		}

		throw new DBGPException(DBGPErrorCode.PARSE, string);
	}

	@Override
	public String toString()
	{
		return value;
	}
}

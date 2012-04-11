package org.overturetool.vdmj.definitions;

import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.syntax.DefinitionReader;
import org.overturetool.vdmj.syntax.ParserException;

public class ChannelClassDefinition extends BUSClassDefinition 
{
	private static final long serialVersionUID = 1L;
	
	public ChannelClassDefinition() throws ParserException, LexException 	
	{	
		super(
				new LexNameToken("CLASS", "Channel", new LexLocation()),
				new LexNameList(),
				operationDefs());
		}

		private static String defs =
			"operations " +
			"public Channel:(<FCFS>|<CSMACD>) * real * set of CPU ==> Channel " +
			"	Channel(policy, speed, cpus) == is not yet specified;";

		private static DefinitionList operationDefs()
			throws ParserException, LexException
		{
			LexTokenReader ltr = new LexTokenReader(defs, Dialect.VDM_PP);
			DefinitionReader dr = new DefinitionReader(ltr);
			dr.setCurrentModule("Channel");
			return dr.readDefinitions();
		}
}

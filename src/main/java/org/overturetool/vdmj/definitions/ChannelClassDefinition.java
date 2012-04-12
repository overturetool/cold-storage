package org.overturetool.vdmj.definitions;

import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.syntax.DefinitionReader;
import org.overturetool.vdmj.syntax.ParserException;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.values.BUSValue;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.NameValuePairMap;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.QuoteValue;
import org.overturetool.vdmj.values.ValueList;

public class ChannelClassDefinition extends BUSClassDefinition 
{
	private static final long serialVersionUID = 1L;
	public static ChannelClassDefinition instance = null;
	
	public ChannelClassDefinition() throws ParserException, LexException 	
	{	
		super(
				new LexNameToken("CLASS", "Channel", new LexLocation()),
				new LexNameList(),
				operationDefs());
				instance = this;
		}

		private static String defs =
			"operations " +
			"public Channel:real * set of CPU ==> Channel " +
			"	Channel(speed, cpus) == is not yet specified;";

		private static DefinitionList operationDefs()
			throws ParserException, LexException
		{
			LexTokenReader ltr = new LexTokenReader(defs, Dialect.VDM_PP);
			DefinitionReader dr = new DefinitionReader(ltr);
			dr.setCurrentModule("Channel");
			return dr.readDefinitions();
		}
		
		@Override
		public ObjectValue newInstance(
			Definition ctorDefinition, ValueList argvals, Context ctxt)
		{
			NameValuePairList nvpl = definitions.getNamedValues(ctxt);
			NameValuePairMap map = new NameValuePairMap();
			map.putAll(nvpl);
			
			//channels systems are always FCFS priority
			argvals.add(0, new QuoteValue("FCFS"));

			return new BUSValue((ClassType)classtype, map, argvals);
		}
}

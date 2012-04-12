package org.overturetool.vdmj.definitions;

import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ContextException;
import org.overturetool.vdmj.runtime.ObjectContext;
import org.overturetool.vdmj.syntax.DefinitionReader;
import org.overturetool.vdmj.syntax.ParserException;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.values.CPUValue;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.NameValuePairMap;
import org.overturetool.vdmj.values.NaturalValue;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.QuoteValue;
import org.overturetool.vdmj.values.SeqValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;
import org.overturetool.vdmj.values.VoidValue;

public class ConstituentClassDefinition extends CPUClassDefinition 
{
	private static final long serialVersionUID = 1L;
	public static ConstituentClassDefinition instance = null;
	
	public ConstituentClassDefinition() throws ParserException, LexException
	{
		super(
			new LexNameToken("CLASS", "Constituent", new LexLocation()),
			new LexNameList(), operationDefs());
		instance = this;
	}
	
	private static String defs =
		"operations " +
		"public Constituent: real ==> Constituent " +
		"	Constituent(speed) == is not yet specified; " +
		"public deploy: ? ==> () " +
		"	deploy(obj) == is not yet specified; " +
		"public deploy: ? * seq of char ==> () " +
		"	deploy(obj, name) == is not yet specified; " +
		"public setPriority: ? * nat ==> () " +
		"	setPriority(opname, priority) == is not yet specified;";

	@Override
	public ObjectValue newInstance(
		Definition ctorDefinition, ValueList argvals, Context ctxt)
	{
		NameValuePairList nvpl = definitions.getNamedValues(ctxt);
		NameValuePairMap map = new NameValuePairMap();
		map.putAll(nvpl);
		
		//constituent systems are always FCFS priority
		argvals.add(0, new QuoteValue("FCFS"));
		return new CPUValue((ClassType)getType(), map, argvals);
	}
	
	private static DefinitionList operationDefs()
		throws ParserException, LexException
	{
		LexTokenReader ltr = new LexTokenReader(defs, Dialect.VDM_PP);
		DefinitionReader dr = new DefinitionReader(ltr);
		dr.setCurrentModule("Constituent");
		return dr.readDefinitions();
	}
	
	public static Value deploy(Context ctxt)
	{
		try
		{
			ObjectContext octxt = (ObjectContext)ctxt;
    		CPUValue cpu = (CPUValue)octxt.self;
    		ObjectValue obj = (ObjectValue)octxt.lookup(varName("obj"));
    		CPUValue oldcpu = obj.getCPU();
    		
    		if(cpu.isTerminated())
    		{
    			throw new ContextException(4136, "Cannot deploy to Constituent: Constituent has been removed from the system.", ctxt.location, ctxt);
    		}
    		
    		obj.redeploy(cpu);
    		oldcpu.undeploy(obj); 	//object will no longer be deployed on the old cpu
    		cpu.deploy(obj);

  			return new VoidValue();
		}
		catch (Exception e)
		{
			throw new ContextException(4136, "Cannot deploy to Constituent: " + e.toString(), ctxt.location, ctxt);
		}
	}

	public static Value setPriority(Context ctxt)
	{
		try
		{
    		ObjectContext octxt = (ObjectContext)ctxt;
    		CPUValue cpu = (CPUValue)octxt.self;
    		SeqValue opname = (SeqValue)octxt.lookup(varName("opname"));
    		NaturalValue priority = (NaturalValue)octxt.check(varName("priority"));

    		cpu.setPriority(opname.stringValue(ctxt), priority.intValue(ctxt));
   			return new VoidValue();
		}
		catch (Exception e)
		{
			throw new ContextException(
				4137, "Cannot set priority: " + e.getMessage(), ctxt.location, ctxt);
		}
	}

	private static LexNameToken varName(String name)
	{
		return new LexNameToken("Constituent", name, new LexLocation());
	}
	
}

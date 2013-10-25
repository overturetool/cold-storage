package org.overture.typechecker.utilities.type;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.types.ANamedInvariantType;
import org.overture.ast.types.ASetType;
import org.overture.ast.types.AUnionType;
import org.overture.ast.types.AUnknownType;
import org.overture.ast.types.PType;
import org.overture.ast.types.SInvariantType;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;
import org.overture.typechecker.assistant.type.ANamedInvariantTypeAssistantTC;
import org.overture.typechecker.assistant.type.ASetTypeAssistantTC;
import org.overture.typechecker.assistant.type.AUnionTypeAssistantTC;
import org.overture.typechecker.assistant.type.AUnknownTypeAssistantTC;

/**
 * Used to determine if a type is a set type
 * 
 * @author kel
 */
public class SetBasisChecker extends TypeUnwrapper<Boolean>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ITypeCheckerAssistantFactory af;

	public SetBasisChecker(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}
	
	@Override
	public Boolean caseASetType(ASetType type) throws AnalysisException
	{
		return ASetTypeAssistantTC.isSet(type);
	}
	
	@Override
	public Boolean defaultSInvariantType(SInvariantType type)
			throws AnalysisException
	{
		if (type instanceof ANamedInvariantType)
		{
			return ANamedInvariantTypeAssistantTC.isSet((ANamedInvariantType) type);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public Boolean caseAUnionType(AUnionType type) throws AnalysisException
	{
		return AUnionTypeAssistantTC.isSet(type);
	}
	
	@Override
	public Boolean caseAUnknownType(AUnknownType type) throws AnalysisException
	{
		return AUnknownTypeAssistantTC.isSet((AUnknownType) type);
	}
	
	@Override
	public Boolean defaultPType(PType type) throws AnalysisException
	{
		return false;
	}
}
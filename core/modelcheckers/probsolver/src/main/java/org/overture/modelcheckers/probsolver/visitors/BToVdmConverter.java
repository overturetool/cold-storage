package org.overture.modelcheckers.probsolver.visitors;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.overture.ast.expressions.AMkTypeExp;
import org.overture.ast.expressions.AUndefinedExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.lex.LexIdentifierToken;
import org.overture.ast.lex.LexIntegerToken;
import org.overture.ast.lex.LexLocation;
import org.overture.ast.statements.AAssignmentStm;
import org.overture.ast.statements.PStm;
import org.overture.ast.types.AFieldField;
import org.overture.ast.types.ARecordInvariantType;
import org.overture.ast.types.ASetType;
import org.overture.ast.types.PType;

import de.be4.classicalb.core.parser.analysis.DepthFirstAdapter;
import de.be4.classicalb.core.parser.node.AEmptySetExpression;
import de.be4.classicalb.core.parser.node.AIntegerExpression;
import de.be4.classicalb.core.parser.node.ARecEntry;
import de.be4.classicalb.core.parser.node.ARecExpression;
import de.be4.classicalb.core.parser.node.ASetExtensionExpression;
import de.be4.classicalb.core.parser.node.Node;
import de.be4.classicalb.core.parser.node.PExpression;
import de.be4.classicalb.core.parser.node.PRecEntry;

public class BToVdmConverter extends DepthFirstAdapter
{

	final static ILexLocation loc = new LexLocation();
	public PExp result = new AUndefinedExp();

	private final PType expectedType;

	private BToVdmConverter(PType stateType)
	{
		this.expectedType = stateType;
	}

	/**
	 * This converts a B construct to the corresponding VDM construct typed by expectedType. The expected type is needed
	 * in cases like mk_ type (make record
	 * 
	 * @param expectedType
	 * @param node
	 * @return
	 */
	public static PExp convert(PType expectedType, Node node)
	{
		BToVdmConverter visitor = new BToVdmConverter(expectedType);
		node.apply(visitor);
		return visitor.result;
	}

	/**
	 * This constructs a state assignment ala s: S := mk_S(..,..);<br/>
	 * Since this isn't supported in VDM it generated an atomic block to update each field in the state individually
	 * 
	 * @param stateType
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PStm getStateAssignment(ARecordInvariantType stateType,
			AMkTypeExp value)
	{

		List<AAssignmentStm> assignments = new Vector<AAssignmentStm>();

		Iterator<AFieldField> fieldItr = ((LinkedList<AFieldField>) stateType.getFields().clone()).iterator();
		Iterator<PExp> valueItr = ((LinkedList<PExp>) value.getArgs().clone()).iterator();

		while (fieldItr.hasNext())
		{
			AFieldField f = fieldItr.next();
			PExp v = valueItr.next();
			AAssignmentStm assign = AstFactory.newAAssignmentStm(loc, AstFactory.newAIdentifierStateDesignator(f.getTagname()), v);
			assign.setType(AstFactory.newAVoidType(loc));
			assign.setTargetType(AstFactory.newAUnknownType(loc));
			assignments.add(assign);
		}

		return AstFactory.newAAtomicStm(loc, assignments);
	}

	/**
	 * Creates a new return statement with the approbate return expression
	 * 
	 * @param resultType
	 * @param exp
	 * @return
	 */
	public static PStm getReturnStatement(PType resultType, PExp exp)
	{
		return AstFactory.newAReturnStm(loc, exp);
	}

	@Override
	public void caseARecExpression(ARecExpression node)
	{
		// This only works if the only record that is returned is the final type
		if (expectedType instanceof ARecordInvariantType)
		{
			List<PExp> arg_ = new Vector<PExp>();

			for (PRecEntry entry : node.getEntries())
			{
				ARecEntry re = (ARecEntry) entry;
				String fieldName = re.getIdentifier().toString().trim();
				PType fieldType = null;
				for (AFieldField f : ((ARecordInvariantType) expectedType).getFields())
				{
					if (f.getTagname().getName().equals(fieldName))
					{
						fieldType = f.getType();
						break;
					}
				}
				arg_.add(convert(fieldType, re.getValue()));
			}

			Collections.reverse(arg_);

			AMkTypeExp res = AstFactory.newAMkTypeExp(((ARecordInvariantType) expectedType).getName().clone(), arg_);
			res.setRecordType((ARecordInvariantType) expectedType);

			result = res;
		}

	}

	@Override
	public void caseARecEntry(ARecEntry node)
	{
		result = AstFactory.newAFieldExp(convert(expectedType, node.getValue()), new LexIdentifierToken(node.getIdentifier().toString().trim(), false, loc));
	}

	@Override
	public void caseAEmptySetExpression(AEmptySetExpression node)
	{
		result = AstFactory.newASetEnumSetExp(loc);
	}

	@Override
	public void caseASetExtensionExpression(ASetExtensionExpression node)
	{
		List<PExp> exps = new Vector<PExp>();
		PType type = ((ASetType) expectedType).getSetof();
		for (PExpression pExp : node.getExpressions())
		{
			exps.add(convert(type, pExp));
		}
		result = AstFactory.newASetEnumSetExp(loc, exps);
	}

	@Override
	public void caseAIntegerExpression(AIntegerExpression node)
	{
		result = AstFactory.newAIntLiteralExp(new LexIntegerToken(node.getLiteral().getText(), loc));
	}

	public void defaultIn(Node node)
	{
		// System.err.println("Hit unsupported node: "
		// + node.getClass().getSimpleName() + " - " + node);
	}

}
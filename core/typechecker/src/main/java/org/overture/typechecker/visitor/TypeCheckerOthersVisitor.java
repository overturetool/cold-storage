package org.overture.typechecker.visitor;

import java.util.LinkedList;
import java.util.Set;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.intf.IQuestionAnswer;
import org.overture.ast.definitions.AExternalDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.intf.lex.ILexIdentifierToken;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.node.Node;
import org.overture.ast.patterns.ADefPatternBind;
import org.overture.ast.patterns.ASetBind;
import org.overture.ast.patterns.ATypeBind;
import org.overture.ast.statements.AApplyObjectDesignator;
import org.overture.ast.statements.AFieldObjectDesignator;
import org.overture.ast.statements.AFieldStateDesignator;
import org.overture.ast.statements.AForPatternBindStm;
import org.overture.ast.statements.AIdentifierObjectDesignator;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.ast.statements.AMapSeqStateDesignator;
import org.overture.ast.statements.ANewObjectDesignator;
import org.overture.ast.statements.ASelfObjectDesignator;
import org.overture.ast.statements.ATixeStmtAlternative;
import org.overture.ast.statements.ATrapStm;
import org.overture.ast.typechecker.NameScope;
import org.overture.ast.types.AClassType;
import org.overture.ast.types.AFieldField;
import org.overture.ast.types.AFunctionType;
import org.overture.ast.types.AOperationType;
import org.overture.ast.types.ARecordInvariantType;
import org.overture.ast.types.ASetType;
import org.overture.ast.types.PType;
import org.overture.ast.types.SMapType;
import org.overture.ast.types.SSeqType;
import org.overture.ast.util.PTypeSet;
import org.overture.typechecker.Environment;
import org.overture.typechecker.TypeCheckInfo;
import org.overture.typechecker.TypeCheckerErrors;
import org.overture.typechecker.TypeComparator;
import org.overture.typechecker.assistant.type.PTypeAssistantTC;

public class TypeCheckerOthersVisitor extends AbstractTypeCheckVisitor
{

	public TypeCheckerOthersVisitor(
			IQuestionAnswer<TypeCheckInfo, PType> typeCheckVisitor)
	{
		super(typeCheckVisitor);
	}

	@Override
	public PType caseADefPatternBind(ADefPatternBind node,
			TypeCheckInfo question) throws AnalysisException
	{
		node.setDefs(null);

		PType type = null;

		Node parent = node.getAncestor(AForPatternBindStm.class);
		if (parent != null)
		{
			type = ((AForPatternBindStm) parent).getSeqType().getSeqof();
		} else if ((parent = node.getAncestor(ATrapStm.class)) != null)
		{
			type = ((ATrapStm) parent).getType();
		} else if ((parent = node.getAncestor(ATixeStmtAlternative.class)) != null)
		{
			type = ((ATixeStmtAlternative) parent).getExp();
		}

		if (node.getBind() != null)
		{
			if (node.getBind() instanceof ATypeBind)
			{
				ATypeBind typebind = (ATypeBind) node.getBind();
				typebind.setType(question.assistantFactory.createPTypeAssistant().typeResolve(typebind.getType(), null, THIS, question));

				// resolve pattern such that it is resolved before it is cloned later in newAMultiBindListDefinition
				if (node.getBind().getPattern() != null)
				{
					question.assistantFactory.createPPatternAssistant().typeResolve(node.getBind().getPattern(), THIS, question);
				}

				if (!TypeComparator.compatible(typebind.getType(), type))
				{
					TypeCheckerErrors.report(3198, "Type bind not compatible with expression", node.getBind().getLocation(), node.getBind());
					TypeCheckerErrors.detail2("Bind", typebind.getType(), "Exp", type);
				}
			} else
			{
				ASetBind setbind = (ASetBind) node.getBind();
				ASetType settype = PTypeAssistantTC.getSet(setbind.getSet().apply(THIS, question));
				if (!TypeComparator.compatible(type, settype.getSetof()))
				{
					TypeCheckerErrors.report(3199, "Set bind not compatible with expression", node.getBind().getLocation(), node.getBind());
					TypeCheckerErrors.detail2("Bind", settype.getSetof(), "Exp", type);
				}
			}

			PDefinition def = AstFactory.newAMultiBindListDefinition(node.getBind().getLocation(), question.assistantFactory.createPBindAssistant().getMultipleBindList(node.getBind()));

			def.apply(THIS, question);
			LinkedList<PDefinition> defs = new LinkedList<PDefinition>();
			defs.add(def);
			node.setDefs(defs);
		} else
		{
			assert type != null : "Can't typecheck a pattern without a type";

			question.assistantFactory.createPPatternAssistant().typeResolve(node.getPattern(), THIS, question);
			node.setDefs(question.assistantFactory.createPPatternAssistant().getDefinitions(node.getPattern(), type, NameScope.LOCAL));
		}

		return null;
	}

	@Override
	public PType caseAFieldStateDesignator(AFieldStateDesignator node,
			TypeCheckInfo question) throws AnalysisException
	{

		PType type = node.getObject().apply(THIS, question);
		PTypeSet result = new PTypeSet();
		boolean unique = !PTypeAssistantTC.isUnion(type);
		ILexIdentifierToken field = node.getField();

		if (PTypeAssistantTC.isRecord(type))
		{

			ARecordInvariantType rec = PTypeAssistantTC.getRecord(type);
			AFieldField rf = question.assistantFactory.createARecordInvariantTypeAssistant().findField(rec, field.getName());

			if (rf == null)
			{
				TypeCheckerErrors.concern(unique, 3246, "Unknown field name, '"
						+ field + "'", node.getLocation(), field);
				result.add(AstFactory.newAUnknownType(field.getLocation()));
			} else
			{
				result.add(rf.getType());
			}
		}

		if (PTypeAssistantTC.isClass(type))
		{
			AClassType ctype = PTypeAssistantTC.getClassType(type);
			String cname = ctype.getName().getName();

			node.setObjectfield(new LexNameToken(cname, field.getName(), node.getObject().getLocation()));
			PDefinition fdef = question.assistantFactory.createPDefinitionAssistant().findName(ctype.getClassdef(), node.getObjectfield(), NameScope.STATE);
			//SClassDefinitionAssistantTC.findName(ctype.getClassdef(), node.getObjectfield(), NameScope.STATE);

			if (fdef == null)
			{
				TypeCheckerErrors.concern(unique, 3260, "Unknown class field name, '"
						+ field + "'", field.getLocation(), field);
				result.add(AstFactory.newAUnknownType(node.getLocation()));

			} else if (question.assistantFactory.createSClassDefinitionAssistant().isAccessible(question.env, fdef, false))
			{

				result.add(fdef.getType());

			} else
			{

				TypeCheckerErrors.concern(unique, 3092, "Inaccessible member "
						+ field.getName() + " of class " + cname, field.getLocation(), field);
				result.add(AstFactory.newAUnknownType(node.getLocation()));
			}
		}

		if (result.isEmpty())
		{
			TypeCheckerErrors.report(3245, "Field assignment is not of a record or object type", node.getLocation(), node);
			TypeCheckerErrors.detail2("Expression", node.getObject(), "Type", type);
			node.setType(AstFactory.newAUnknownType(field.getLocation()));
			return node.getType();
		}

		node.setType(result.getType(node.getLocation()));
		return node.getType();
	}

	@Override
	public PType caseAIdentifierStateDesignator(
			AIdentifierStateDesignator node, TypeCheckInfo question)
	{
		Environment env = question.env;

		if (env.isVDMPP())
		{
			// We generate an explicit name because accessing a variable
			// by name in VDM++ does not "inherit" values from a superclass.

			ILexNameToken name = node.getName();
			ILexNameToken exname = name.getExplicit(true);
			PDefinition def = env.findName(exname, NameScope.STATE);

			if (def == null)
			{

				Set<PDefinition> matches = env.findMatches(exname);

				if (!matches.isEmpty())
				{
					PDefinition match = matches.iterator().next(); // Just take first

					if (question.assistantFactory.createPDefinitionAssistant().isFunction(match))
					{
						TypeCheckerErrors.report(3247, "Function apply not allowed in state designator", name.getLocation(), name);
					} else
					{
						TypeCheckerErrors.report(3247, "Operation call not allowed in state designator", name.getLocation(), name);
					}

					node.setType(match.getType());
					return node.getType();
				} else
				{
					TypeCheckerErrors.report(3247, "Symbol '" + name
							+ "' is not an updatable variable", name.getLocation(), name);
				}

				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (!question.assistantFactory.createPDefinitionAssistant().isUpdatable(def))
			{
				TypeCheckerErrors.report(3301, "Variable '" + name
						+ "' in scope is not updatable", name.getLocation(), name);
				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (def.getClassDefinition() != null)
			{
				if (!question.assistantFactory.createSClassDefinitionAssistant().isAccessible(env, def, true))
				{
					TypeCheckerErrors.report(3180, "Inaccessible member '"
							+ name + "' of class "
							+ def.getClassDefinition().getName().getName(), name.getLocation(), name);
					node.setType(AstFactory.newAUnknownType(name.getLocation()));
					return node.getType();
				} else if (!question.assistantFactory.createPDefinitionAssistant().isStatic(def)
						&& env.isStatic())
				{
					TypeCheckerErrors.report(3181, "Cannot access " + name
							+ " from a static context", name.getLocation(), name);
					node.setType(AstFactory.newAUnknownType(name.getLocation()));
					return node.getType();
				}
			}

			node.setType(question.assistantFactory.createPDefinitionAssistant().getType(def));
			return node.getType();
		} else
		{
			ILexNameToken name = node.getName();
			PDefinition def = env.findName(name, NameScope.STATE);

			if (def == null)
			{
				TypeCheckerErrors.report(3247, "Unknown state variable '"
						+ name + "' in assignment", name.getLocation(), name);
				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (question.assistantFactory.createPDefinitionAssistant().isFunction(def))
			{
				TypeCheckerErrors.report(3247, "Function apply not allowed in state designator", name.getLocation(), name);
				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (question.assistantFactory.createPDefinitionAssistant().isOperation(def))
			{
				TypeCheckerErrors.report(3247, "Operation call not allowed in state designator", name.getLocation(), name);
				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (!question.assistantFactory.createPDefinitionAssistant().isUpdatable(def))
			{
				TypeCheckerErrors.report(3301, "Variable '" + name
						+ "' in scope is not updatable", name.getLocation(), name);
				node.setType(AstFactory.newAUnknownType(name.getLocation()));
				return node.getType();
			} else if (def instanceof AExternalDefinition)
			{
				AExternalDefinition d = (AExternalDefinition) def;

				if (d.getReadOnly())
				{
					TypeCheckerErrors.report(3248, "Cannot assign to 'ext rd' state "
							+ name, name.getLocation(), name);
				}
			}
			// else just state access in (say) an explicit operation

			node.setType(question.assistantFactory.createPDefinitionAssistant().getType(def));
			return node.getType();
		}
	}

	@Override
	public PType caseAMapSeqStateDesignator(AMapSeqStateDesignator node,
			TypeCheckInfo question) throws AnalysisException
	{
		PType etype = node.getExp().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, NameScope.NAMESANDSTATE));
		PType rtype = node.getMapseq().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env));
		PTypeSet result = new PTypeSet();

		if (PTypeAssistantTC.isMap(rtype))
		{
			node.setMapType(question.assistantFactory.createPTypeAssistant().getMap(rtype));

			if (!TypeComparator.compatible(node.getMapType().getFrom(), etype))
			{
				TypeCheckerErrors.report(3242, "Map element assignment of wrong type", node.getLocation(), node);
				TypeCheckerErrors.detail2("Expect", node.getMapType().getFrom(), "Actual", etype);
			} else
			{
				result.add(node.getMapType().getTo());
			}
		}

		if (PTypeAssistantTC.isSeq(rtype))
		{
			node.setSeqType(PTypeAssistantTC.getSeq(rtype));

			if (!PTypeAssistantTC.isNumeric(etype))
			{
				TypeCheckerErrors.report(3243, "Seq index is not numeric", node.getLocation(), node);
				TypeCheckerErrors.detail("Actual", etype);
			} else
			{
				result.add(node.getSeqType().getSeqof());
			}
		}

		if (PTypeAssistantTC.isFunction(rtype))
		{
			// Error case, but improves errors if we work out the return type
			AFunctionType ftype = PTypeAssistantTC.getFunction(rtype);
			result.add(ftype.getResult());
		}

		if (PTypeAssistantTC.isOperation(rtype))
		{
			// Error case, but improves errors if we work out the return type
			AOperationType otype = PTypeAssistantTC.getOperation(rtype);
			result.add(otype.getResult());
		}

		if (result.isEmpty())
		{
			TypeCheckerErrors.report(3244, "Expecting a map or a sequence", node.getLocation(), node);
			node.setType(AstFactory.newAUnknownType(node.getLocation()));
			return node.getType();
		}

		node.setType(result.getType(node.getLocation()));
		return node.getType();
	}

	@Override
	public PType caseASelfObjectDesignator(ASelfObjectDesignator node,
			TypeCheckInfo question)
	{
		PDefinition def = question.env.findName(node.getSelf(), NameScope.NAMES);

		if (def == null)
		{
			TypeCheckerErrors.report(3263, "Cannot reference 'self' from here", node.getSelf().getLocation(), node.getSelf());
			return AstFactory.newAUnknownType(node.getSelf().getLocation());
		}

		return question.assistantFactory.createPDefinitionAssistant().getType(def);
	}

	@Override
	public PType caseAApplyObjectDesignator(AApplyObjectDesignator node,
			TypeCheckInfo question) throws AnalysisException
	{
		LinkedList<PType> argtypes = new LinkedList<PType>();

		for (PExp a : node.getArgs())
		{
			argtypes.add(a.apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, NameScope.NAMESANDSTATE)));
		}

		PType type = node.getObject().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, null, argtypes));
		boolean unique = !PTypeAssistantTC.isUnion(type);
		PTypeSet result = new PTypeSet();

		if (PTypeAssistantTC.isMap(type))
		{
			SMapType map = question.assistantFactory.createPTypeAssistant().getMap(type);
			result.add(question.assistantFactory.createAApplyObjectDesignatorAssistant().mapApply(node, map, question.env, NameScope.NAMESANDSTATE, unique, THIS));
		}

		if (PTypeAssistantTC.isSeq(type))
		{
			SSeqType seq = PTypeAssistantTC.getSeq(type);
			result.add(question.assistantFactory.createAApplyObjectDesignatorAssistant().seqApply(node, seq, question.env, NameScope.NAMESANDSTATE, unique, THIS));
		}

		if (PTypeAssistantTC.isFunction(type))
		{
			AFunctionType ft = PTypeAssistantTC.getFunction(type);
			question.assistantFactory.createPTypeAssistant().typeResolve(ft, null, THIS, new TypeCheckInfo(question.assistantFactory, question.env));
			result.add(question.assistantFactory.createAApplyObjectDesignatorAssistant().functionApply(node, ft, question.env, NameScope.NAMESANDSTATE, unique, THIS));
		}

		if (PTypeAssistantTC.isOperation(type))
		{
			AOperationType ot = PTypeAssistantTC.getOperation(type);
			question.assistantFactory.createPTypeAssistant().typeResolve(ot, null, THIS, new TypeCheckInfo(question.assistantFactory, question.env));
			result.add(question.assistantFactory.createAApplyObjectDesignatorAssistant().operationApply(node, ot, question.env, NameScope.NAMESANDSTATE, unique, THIS));
		}

		if (result.isEmpty())
		{
			TypeCheckerErrors.report(3249, "Object designator is not a map, sequence, function or operation", node.getLocation(), node);
			TypeCheckerErrors.detail2("Designator", node.getObject(), "Type", type);
			return AstFactory.newAUnknownType(node.getLocation());
		}

		return result.getType(node.getLocation());
	}

	@Override
	public PType caseANewObjectDesignator(ANewObjectDesignator node,
			TypeCheckInfo question) throws AnalysisException
	{
		return node.getExpression().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, NameScope.NAMESANDSTATE, question.qualifiers));
	}

	@Override
	public PType caseAIdentifierObjectDesignator(
			AIdentifierObjectDesignator node, TypeCheckInfo question)
			throws AnalysisException
	{
		return node.getExpression().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, NameScope.NAMESANDSTATE, question.qualifiers));
	}

	@Override
	public PType caseAFieldObjectDesignator(AFieldObjectDesignator node,
			TypeCheckInfo question) throws AnalysisException
	{

		PType type = node.getObject().apply(THIS, new TypeCheckInfo(question.assistantFactory, question.env, null, question.qualifiers));
		PTypeSet result = new PTypeSet();
		boolean unique = !PTypeAssistantTC.isUnion(type);

		if (PTypeAssistantTC.isClass(type))
		{
			AClassType ctype = PTypeAssistantTC.getClassType(type);

			if (node.getClassName() == null)
			{
				node.setField(new LexNameToken(ctype.getName().getName(), node.getFieldName().getName(), node.getFieldName().getLocation()));
			} else
			{
				node.setField(node.getClassName());
			}

			ILexNameToken field = node.getField();
			field.setTypeQualifier(question.qualifiers);
			PDefinition fdef = question.assistantFactory.createPDefinitionAssistant().findName(ctype.getClassdef(), field, NameScope.NAMESANDSTATE);

			if (fdef == null)
			{
				TypeCheckerErrors.concern(unique, 3260, "Unknown class member name, '"
						+ field + "'", node.getLocation(), node);
				result.add(AstFactory.newAUnknownType(node.getLocation()));
			} else
			{
				result.add(question.assistantFactory.createPDefinitionAssistant().getType(fdef));
			}
		}

		if (PTypeAssistantTC.isRecord(type))
		{
			String sname = node.getFieldName() != null ? node.getFieldName().getName()
					: node.getClassName().toString();
			ARecordInvariantType rec = PTypeAssistantTC.getRecord(type);
			AFieldField rf = question.assistantFactory.createARecordInvariantTypeAssistant().findField(rec, sname);

			if (rf == null)
			{
				TypeCheckerErrors.concern(unique, 3261, "Unknown field name, '"
						+ sname + "'", node.getLocation(), node);
				result.add(AstFactory.newAUnknownType(node.getLocation()));
			} else
			{
				result.add(rf.getType());
			}
		}

		if (result.isEmpty())
		{
			TypeCheckerErrors.report(3262, "Field assignment is not of a class or record type", node.getLocation(), node);
			TypeCheckerErrors.detail2("Expression", node.getObject(), "Type", type);
			return AstFactory.newAUnknownType(node.getLocation());
		}

		return result.getType(node.getLocation());

	}

}

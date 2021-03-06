package org.overture.tools.treegen.typecheck;

// project specific imports
import java.util.*;
import org.overture.tools.treegen.ast.itf.*;

public class TreeChecker {
	
	// keep track of number of errors found
	public int errors = 0;
	
	// switch to enable deep logging
	public boolean debug = false;
	
	// create a mapping to hold all class definitions
	public HashMap<String,ClassDefinition> cls = new HashMap<String,ClassDefinition>();
	
	// create a set of all type names encountered during first pass
	public HashSet<String> ttns = new HashSet<String>();
	
	// create a set of all shorthand union types
	public HashSet<String> shuts = new HashSet<String>();
	
	// create a map to store all free type name variables per class definition
	public HashMap<String,HashSet<String>> tns = new HashMap<String,HashSet<String>>();

	// create a set to store all reserved types
	public HashSet<String> restps = new HashSet<String>();
	
	// constructor
	public TreeChecker()
	{
		// initialize the reserved type table
		restps.add("Node");
		restps.add("Visitor");
		restps.add("Document");
	}
	
	public void performCheck(List<ITreeGenAstClassDefinition> defs)
	{
		// reset the number of errors
		errors = 0;
		
		// iterate over all parsed class definitions and build map
		for (ITreeGenAstClassDefinition def : defs) {
			
			// create a new entry for the class definition
			ClassDefinition cd = new ClassDefinition();
			// copy the command-line options
			cd.opts = def.getOpts();
			// remember the class name
			cd.class_name = def.getClassName();
			// reset the super class (will be instantiated later)
			cd.super_class = null;
			
			// insert the entry in the mapping
			cls.put(def.getClassName(), cd);
		}
		
		// iterate again and perform the check on each class
		for (ITreeGenAstClassDefinition def : defs) {
			// performCheckClassDefinition will also establish the class hierarchy
			performCheckClassDefinition(def);
		}
		
		// pass two: check all free type name variables encountered during first pass
		performCheckFreeVariables();
		
		// pass three: link all types to the super types if they are defined
		for (ClassDefinition cd: cls.values()) {
			for (String tpnm: cd.types.keySet()) {
				// does this class have a super class
				if (cd.super_class != null) {
					// link each type to its named super type if it exists
					cd.types.get(tpnm).super_type = cd.super_class.getTypeByName(tpnm);
				}
			}
		}
		
		// pass four: override the value definitions
		for (ClassDefinition cd: cls.values()) overrideValues(cd);
	}
	
	public void overrideValues(ClassDefinition cd)
	{
		// check for the java directory setting in the command-line options
		if (cd.opts.getJavaDirectory().length() > 0) {
			cd.values.put("javadir", new String(cd.opts.getJavaDirectory()));
		}
		
		// check for the VDM directory setting in the command-line options
		if (cd.opts.getVppDirectory().length() > 0) {
			cd.values.put("vdmppdir", new String(cd.opts.getVppDirectory()));
		}
		
		// check for the package setting in the command-line options
		if (cd.opts.getPackage().length() > 0) {
			cd.values.put("package", new String(cd.opts.getPackage()));
		}
		
		// check for the top-level setting in the command-line options
		if (cd.opts.getToplevel().length() > 0) {
			// first clear the current list
			cd.tplvl.clear();
			
			// construct the new list
			for (String tplvlnm : cd.opts.getToplevel().split(",")) {
				// add each individual top-level name
				cd.tplvl.add(tplvlnm);
			}			
		}
		
		// insert the option to split the generated VDM++ files
		if (cd.opts.getSplitVpp()) {
			cd.values.put("split", new String("true"));
		} else {
			if (!cd.values.containsKey("split"))
				cd.values.put("split", new String("false"));
		}
	}
	
	public void performCheckClassDefinition (ITreeGenAstClassDefinition def)
	{
		// diagnostics
		System.out.println("Analyzing class "+ def.getClassName());
		
		// check consistency
		if (!def.getSuperClass().isEmpty()) {
			if (cls.containsKey(def.getSuperClass())) {
				// diagnostics message
				System.out.println ("Superclass "+def.getSuperClass()+" is defined for class "+def.getClassName());
				
				// link the two class definitions
				ClassDefinition cd1 = cls.get(def.getClassName());
				ClassDefinition cd2 = cls.get(def.getSuperClass());
				
				// add cd2 as the super class of cd1
				cd1.super_class = cd2;
				
				// add cd1 as the sub class of cd2
				cd2.sub_classes.add(cd1);
			} else {
				// diagnostics error message
				System.out.println ("** ERROR : Superclass "+def.getSuperClass()+" does not exist for type "+def.getClassName());
				
				// increase the error count
				errors++;
			}
		} else {
			// diagnostics message
			System.out.println("Class "+def.getClassName()+" does not have a superclass");
		}
		
		// clear the set of free type name variables encountered in this class definition
		ttns.clear();
		
		// process the embedded definitions
		performCheckDefinition(cls.get(def.getClassName()), def.getDefs());
		
		// store a copy of the set of free type name variables
		tns.put(def.getClassName(), new HashSet<String>(ttns));
	}
	
	public void performCheckDefinition(ClassDefinition cd, List<? extends ITreeGenAstDefinitions> tgads)
	{
		// pass one: process all value and variable definitions
		for (ITreeGenAstDefinitions tgad : tgads) {
			// check for value definitions
			if (tgad instanceof ITreeGenAstValueDefinition) {
				performCheckValueDefinition(cd, (ITreeGenAstValueDefinition) tgad);
			}
			// check for variable definitions
			if (tgad instanceof ITreeGenAstVariableDefinition) {
				performCheckVariableDefinition(cd, (ITreeGenAstVariableDefinition) tgad);
			}
		}
		
		// pass two: process all short-hand and record definitions
		for (ITreeGenAstDefinitions tgad : tgads) {
			// keep track of processing status
			boolean check = false;
			
			// mark value and value definitions as passed in phase one
			if  ((tgad instanceof ITreeGenAstValueDefinition) ||
				 (tgad instanceof ITreeGenAstVariableDefinition)) {
				check = true;
			}
			
			// check shorthand definitions
			if (tgad instanceof ITreeGenAstShorthandDefinition) {
				performCheckShorthandDefinition(cd, (ITreeGenAstShorthandDefinition) tgad);
				check = true;
			}
			
			// check record definitions
			if (tgad instanceof ITreeGenAstCompositeDefinition) {
				performCheckCompositeDefinition(cd, (ITreeGenAstCompositeDefinition) tgad);
				check = true;
			}
			
			// sanity check: did we process this element?
			if (check == false) {
				// diagnostics error message
				System.out.println("** ERROR : Could not resolve type of embedded definition");
				
				// increase the error count
				errors++;
			}
		}
	}
	
	public void performCheckValueDefinition(ClassDefinition cd, ITreeGenAstValueDefinition tgavd)
	{
		// obtain the name of the value definition
		String vdnm = tgavd.getKey();
		
		// diagnostics message
		if (debug) System.out.println("Checking value definition "+cd.class_name+"."+vdnm);
		
		// check for redefinition of the value
		if (cd.values.containsKey(vdnm)) {
			// flag error: value multiple defined
			System.out.println("** ERROR : Value definition '"+vdnm+"' is multiple defined");
			
			// increase error count
			errors++;
		} else {
			// treat the top-level entry differently
			if (vdnm.compareTo("toplevel") == 0) {
				// handle the (comma separated) top-level entry definitions
				for (String tplvlnm : tgavd.getValue().split(",")) {
					// add each individual top-level name
					cd.tplvl.add(tplvlnm);
				}
			} else {
				// add the value definition to the class definition
				cd.values.put(vdnm, tgavd.getValue());
			}
		}
	}
	
	public void performCheckVariableDefinition(ClassDefinition cd, ITreeGenAstVariableDefinition tgavd)
	{
		// diagnostics message
		if (debug) System.out.println("Checking variable definition " +cd.class_name+"."+tgavd.getKey());
		
		// retrieve the type of the member variable
		Type theType = retrieveType(tgavd.getType());

		if (theType == null) {
			// flag error: type conversion failed
			System.out.println("** ERROR : Type conversion failed in instance variable '"+tgavd.getKey()+"'");
			
			// increase error count
			errors++;
		} else {
			
			if (cd.variables.containsKey(tgavd.getKey())) {
				// flag error: member variable multiple defined
				System.out.println("** ERROR :  Instance variable '"+tgavd.getKey()+"' is multiple defined");
				
				// increase error count
				errors++;
			} else {
				// additional consistency check for java types (init clause must be declared)
				if (theType instanceof JavaType) {
					// retrieve the embedded java type
					JavaType theJavaType = (JavaType) theType;
					
					// initialise the embedded type
					if (tgavd.getValue() == null) {
						// flag error: init clause is empty
						System.out.println("** ERROR : Init clause must be provided with java type for instance variable '"+tgavd.getKey()+"'");
						
						// increase error count
						errors++;
					} else if (tgavd.getValue().isEmpty()) {
						// flag error: init clause is empty
						System.out.println("** ERROR : Init clause must be provided with java type for instance variable '"+tgavd.getKey()+"'");
						
						// increase error count
						errors++;					
					} else {
						// set the embedded java type
						theJavaType.java_type = tgavd.getValue();
					}
				}
				
				// create the new member variable
				MemberVariable theVariable = new MemberVariable(theType, tgavd.getValue());
				
				// add the member variable to the look-up table
				cd.variables.put(tgavd.getKey(), theVariable);
			}
		}
	}
	
	public void performCheckShorthandDefinition(ClassDefinition cd, ITreeGenAstShorthandDefinition tgashd)
	{
		// retrieve the shorthand name
		String shnm = tgashd.getShorthandName();
		
		// diagnostics message
		if (debug) System.out.println("Checking shorthand definition "+cd.class_name+"."+shnm);
		
		// check for allowed type name
		if (restps.contains(shnm)) {
			// flag error: type name is reserved
			System.out.println("** ERROR : Shorthand type '"+shnm+"' is a reserved type name!");
			
			// increase the error count
			errors++;
		} else {
			// retrieve the type of the shorthand definition
			Type theType = retrieveType(tgashd.getType());
			
			// check for consistent type conversion
			if (theType == null) {
				// flag error (type conversion failed)
				System.out.println("** ERROR : Shorthand type '"+shnm+"' cannot be converted!");
				
				// increase the error count
				errors++;
			} else {
				// check for a single type name as a right hand side
				if (theType.isTypeName()) {
					// create a new union type
					UnionType fixedType = new UnionType();
					
					// add the type name to the union type (as the single element)
					fixedType.union_type.add(theType);
					
					// put it back as the result
					theType = fixedType;
				}
				
				// check for single quoted type as a right hand side
				if (theType.isQuotedType()) {
					// create a new union type
					UnionType fixedType = new UnionType();
					
					// add the type name to the union type (as the single element)
					fixedType.union_type.add(theType);
										
					// put it back as the result
					theType = fixedType;
				}
				
				// store the type in the class definition
				if (cd.types.containsKey(tgashd.getShorthandName())) {
					// flag error (multiple defined type)
					System.out.println("** ERROR : Shorthand type '"+shnm+"' is multiple defined!");
					
					// increase the error count
					errors++;
				} else {
					// store the short-hand (does NOT need to be a union type)
					cd.types.put(shnm, theType);
					
					// check for a type name union (defining the subtype relationships)
					if (theType.isUnionType()) {
						// convert by casting
						UnionType theUnionType = (UnionType) theType;
						
						// check for type name union
						if (theUnionType.isTypeNameUnion()) {
							for (String tnm: theUnionType.getTypeNames()) {
								if (cd.subtypes.containsKey(tnm)) {
									// flag error: type must have unique supertype
									System.out.println("** ERROR : Type name union "+tnm+" is embedded in two other union types");
									
									// increase the error count
									errors++;
								} else {
									// insert each type name in the sub type look-up table
									cd.subtypes.put(tnm, shnm);
								}
							}
						}
					} else {
						// if it is not a union type, then it MUST be a string type
						if (!theType.isStringType()) {
							// flag the error
							System.out.println("** ERROR : Shorthand type '"+shnm+"' is neither a union type nor a seq of char!");
							
							// increase the error count
							errors++;
						}
					}
				}
			}
		}
	}
	
	public void performCheckCompositeDefinition(ClassDefinition cd, ITreeGenAstCompositeDefinition tgacd)
	{
		// diagnostics message
		if (debug) System.out.println("Checking composite definition "+cd.class_name+"."+tgacd.getCompositeName());
		
		// obtain the record type name
		String recnm = tgacd.getCompositeName();
		
		// check for allowed type name
		if (restps.contains(recnm)) {
			// flag error: type name is reserved
			System.out.println("** ERROR : Composite definition '"+recnm+"' is a reserved type name!");
			
			// increase the error count
			errors++;
		} else {
			// consistency check
			if (cd.types.containsKey(recnm)) {
				// flag error: type multiple defined
				System.out.println("** ERROR : Composite definition '"+recnm+"' is multiple defined");
				
				// increase the error count
				errors++;
			} else {
				// create the record type
				RecordType theRecord = new RecordType(recnm);
				
				// iterate over the fields of the record
				for (ITreeGenAstCompositeField tgfld : tgacd.getFields()) {
					// obtain the embedded field type
					Type theType = retrieveType(tgfld.getType());
					
					// consistency check
					if (theType == null) {
						// flag error: field type conversion failed
						System.out.println("** ERROR : Field type conversion failed in '"+recnm+"."+tgfld.getFieldName()+"'");
						
						// increase error count
						errors++;
					} else {
						// continue consistency checking
						if (theType.isJavaType()) {
							// cast to proper type
							JavaType theJavaType = (JavaType) theType;
							
							// check for non-empty initialiser
							if (tgfld.getValue().length() == 0) {
								// flag error: token type should have an initialiser
								System.out.println("** ERROR : Token field '"+recnm+"."+tgfld.getFieldName()+"' must have an initialiser");
								
								// increase the error count
								errors++;
							} else {
								// set the initialiser
								theJavaType.java_type = tgfld.getValue();
							}
						} else {
							// insist on empty initialiser
							if (tgfld.getValue().length() != 0) {
								// flag error: type should not have an initialiser
								System.out.println("** ERROR : Field '"+recnm+"."+tgfld.getFieldName()+"' cannot have an initialiser");
								
								// increate the error count
								errors++;
							}
						}
						
						// add the field to the record type definition
						theRecord.fields.add(new Field(tgfld.getFieldName(), theType));
					}
					
					// check for name clash with variables defined in the base class
					if (cd.variables.containsKey(tgfld.getFieldName())) {
						// flag error: field name clashes with instance variable name
						System.out.println("** ERROR : Field name '"+recnm+"."+tgfld.getFieldName()+
								"' clashes with instance variable '"+tgfld.getFieldName()+"'");
						
						// increase error count
						errors++;
					}
				}
				
				// insert the record type in the look-up table
				cd.types.put(recnm, theRecord);
			}
		}
	}
	
	public void performCheckFreeVariables()
	{
		// iterate over the list of all classes
		for (String clnm: tns.keySet()) {
			// retrieve the class
			ClassDefinition cd = cls.get(clnm);
			
			if (cd == null) {
				// flag error: class cannot be found
				System.out.println("** ERROR : Internal error: class '"+clnm+"' cannot be found");
				
				// increase error count
				errors++;
			} else {
				// iterate over the list of free variables
				for (String fvnm: tns.get(clnm)) {
					// perform the look-up
					if (cd.getTypeByName(fvnm) == null) {
						// flag error: type is not defined
						System.out.println("** ERROR : Type '"+clnm+"."+fvnm+"' is not defined anywhere");
						
						// increase the error count
						errors++;
					}
				}
				
				// check for required top-level definitions
				if (cd.getToplevel().isEmpty()) {
					// flag error: top-level definition must not be empty
					System.out.println("** ERROR : Class "+clnm+" does not have a top-level definition");
					
					// increase the error count
					errors++;
				}
				
				// iterate over the list of top-level types
				for (String tplvl: cd.getToplevel()) {
					if (cd.getTypeByName(tplvl) == null) {
						// flag error: top-level type is not defined
						System.out.println("** ERROR : Top-level (value) type '"+tplvl+"' is not defined anywhere");
						
						// increase the error count
						errors++;
					}
				}
			}
		}
	}
	
	public Type retrieveType(ITreeGenAstTypeSpecification tgats)
	{
		if (tgats instanceof ITreeGenAstTypeName) {
			return retrieveTypename((ITreeGenAstTypeName) tgats);
		} else if (tgats instanceof ITreeGenAstQuotedType) {
			return retrieveQuotedType((ITreeGenAstQuotedType) tgats);
		} else if (tgats instanceof ITreeGenAstUnionType) {
			return retrieveUnionType((ITreeGenAstUnionType) tgats);
		} else if (tgats instanceof ITreeGenAstOptionalType) {
			return retrieveOptionalType((ITreeGenAstOptionalType) tgats);
		} else if (tgats instanceof ITreeGenAstSeqType) {
			return retrieveSeqType((ITreeGenAstSeqType) tgats);
		} else if (tgats instanceof ITreeGenAstSetType) {
			return retrieveSetType((ITreeGenAstSetType) tgats);
		} else if (tgats instanceof ITreeGenAstMapType) {
			return retrieveMapType((ITreeGenAstMapType) tgats); 
		} else return null;
	}

	public Type retrieveTypename(ITreeGenAstTypeName tgatn) {
		
		// check for basic type: Boolean values
		if (tgatn.getName().compareToIgnoreCase("bool") == 0) return new BooleanType();
		
		// check for basic type: natural numbers
		if (tgatn.getName().compareToIgnoreCase("nat") == 0) return new NatType();
		
		// check for basic type: integer numbers
		if (tgatn.getName().compareToIgnoreCase("int") == 0) return new NatType();
		
		// check for basic type: real numbers
		if (tgatn.getName().compareToIgnoreCase("real") == 0) return new RealType();
		
		// check for basic type: characters
		if (tgatn.getName().compareToIgnoreCase("char") == 0) return new CharType();

		// check for special type: embedded java (in VDM represented as a token type
		if (tgatn.getName().compareToIgnoreCase("token") == 0) return new JavaType();

		// ok: it is a true type name, add it to the list
		ttns.add(tgatn.getName());
		
		// default: create and return the new type name
		return new TypeName(tgatn.getName());
	}
	
	public Type retrieveQuotedType(ITreeGenAstQuotedType tgaqt) {
		
		// create and return the new quoted type
		return new QuotedType(tgaqt.getQuote());
	}
	
	public Type retrieveUnionType(ITreeGenAstUnionType tgaut) {
		
		// first convert the embedded types recursively
		Type lhs = retrieveType(tgaut.getLhs());
		Type rhs = retrieveType(tgaut.getRhs());
		
		// compose the result type (flatten the type)
		if (lhs instanceof UnionType) {
			// cast to proper (union) type
			UnionType lhsut = (UnionType) lhs;
			
			if (rhs instanceof UnionType) {
				// cast to proper type
				UnionType rhsut = (UnionType) rhs;
				
				// merge lhs and rhs unions
				lhsut.union_type.addAll(rhsut.union_type);
				
			} else {
				// add rhs to the union
				lhsut.union_type.add(rhs);
			}

			// return the result
			return lhsut;
		} else {
			if (rhs instanceof UnionType) {
				// cast to proper type
				UnionType rhsut = (UnionType) rhs;
				
				// add lhs to the union
				rhsut.union_type.add(lhs);
				
				// return the union
				return rhsut;
			} else {
				// compose a new union type
				UnionType nt = new UnionType();
				
				// add lhs and rhs to the union type
				nt.union_type.add(lhs);
				nt.union_type.add(rhs);
				
				// return the new constructed union
				return nt;
			}
		}
	}
	
	public Type retrieveOptionalType(ITreeGenAstOptionalType tgaot) {
		
		// obtain the embedded type
		Type res = retrieveType(tgaot.getType());
		
		if (res == null) {
			// flag error (type conversion failed)
			System.out.println("** ERROR : Optional type cannot be converted");
			
			// increase the error count
			errors++;
		} else {
			// flag this type as optional
			res.opt = true;
		}
		
		// return the derived type
		return res;
	}
	
	public Type retrieveSetType(ITreeGenAstSetType tgast) {
		
		// retrieve the embedded type
		Type est = retrieveType(tgast.getType());
		
		if (est == null) {
			// flag error: type conversion failed
			System.out.println ("** ERROR : Set type cannot be converted");
			
			// increase error count
			errors++;
			
			// default: return error
			return null;
		} else {
			// create and return the new set type
			return new SetType(est);
		}
	}
	
	public Type retrieveSeqType(ITreeGenAstSeqType tgast) {
		
		// retrieve the embedded type
		Type est = retrieveType(tgast.getType());
		
		if (est == null) {
			// flag error: type conversion failed
			System.out.println ("** ERROR : Sequence type cannot be converted");
			
			// increase error count
			errors++;
			
			// default: return error
			return null;
		} else {
			// create and return the new set type
			return new SeqType(est);
		}
	}
	
	public Type retrieveMapType(ITreeGenAstMapType tgamt) {
		
		// retrieve the embedded domain type
		Type dom = retrieveType(tgamt.getDomType());
		
		// consistency check 
		if (dom == null) {
			// flag error
			System.out.println ("** ERROR : Domain type conversion failed in map type");
			
			// increase error count
			errors++;
		}
		
		// retrieve the embedded domain type
		Type rng = retrieveType(tgamt.getRngType());
		
		// consistency check 
		if (rng == null) {
			// flag error
			System.out.println ("** ERROR : Range type conversion failed in map type");
			
			// increase error count
			errors++;
		}
		
		// construct the return type
		if ((dom != null) && (rng != null)) {
			// return the new map type
			return new MapType(dom, rng);
		} else {
			// default: flag error to caller
			return null;
		}
	}

}

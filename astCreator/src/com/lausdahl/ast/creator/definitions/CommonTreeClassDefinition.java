package com.lausdahl.ast.creator.definitions;

import java.util.List;
import java.util.Vector;

import com.lausdahl.ast.creator.Environment;
import com.lausdahl.ast.creator.definitions.Field.AccessSpecifier;
import com.lausdahl.ast.creator.methods.CloneMethod;
import com.lausdahl.ast.creator.methods.CloneWithMapMethod;
import com.lausdahl.ast.creator.methods.ConstructorMethod;
import com.lausdahl.ast.creator.methods.DefaultConstructorMethod;
import com.lausdahl.ast.creator.methods.GetMethod;
import com.lausdahl.ast.creator.methods.KindMethod;
import com.lausdahl.ast.creator.methods.KindNodeMethod;
import com.lausdahl.ast.creator.methods.Method;
import com.lausdahl.ast.creator.methods.RemoveChildMethod;
import com.lausdahl.ast.creator.methods.SetMethod;
import com.lausdahl.ast.creator.methods.ToStringMethod;

public class CommonTreeClassDefinition extends BaseClassDefinition implements
		IClassDefinition
{
	// public static final List<CommonTreeClassDefinition> classes = new Vector<CommonTreeClassDefinition>();
	Environment env;

	private ClassType type = ClassType.Alternative;
	// public IClassDefinition superClass;
	public String rawName;

	public CommonTreeClassDefinition(String rawName,
			IClassDefinition superClass, ClassType type, Environment env)
	{
		super("");

		this.rawName = rawName;
		setSuperClass(superClass);

		this.type = type;
		this.env = env;
		super.name = getName();

//		if (type != ClassType.Production /* && !fields.isEmpty() */ && this.type != ClassType.SubProduction)
		{
			methods.add(new ConstructorMethod(this, env));
			if (type != ClassType.Token)
			{
				methods.add(new DefaultConstructorMethod(this, env));
			}
		}

		if (type != ClassType.Token)
		{
			methods.add(new RemoveChildMethod(this, fields, env));
		}

		methods.add(new ToStringMethod(this, env));

		if (this.type != ClassType.Production && this.type != ClassType.SubProduction)
		{
			methods.add(new CloneMethod(this, type, env));
			methods.add(new CloneWithMapMethod(this, type, env));
		}

		if (this.type == ClassType.Production)
		{
			methods.add(new KindNodeMethod(this, env));
		}

//		if (this.type != ClassType.Token/*&& this.type != ClassType.SubProduction*/)
//		{
//			methods.add(new KindMethod(this, env));
//		}
		
		switch (this.type)
		{
			case Alternative:
				
			case Custom:
				methods.add(new KindMethod(this,false,env));
				break;
			case Production:
				methods.add(new KindMethod(this,true ,env));
				break;
			case SubProduction:
				methods.add(new KindMethod(this,false,env));
				methods.add(new KindMethod(this,true,env));
				break;
			case Token:
				break;
			
			
		}

		
		env.addClass(this);
	}

	public void setSuperClass(IClassDefinition superClass)
	{
		this.superDef = superClass;
		if (superClass != null)
		{
			setPackageName(this.superDef.getPackageName());
			imports.add(this.superDef);
		}
	}

	public ClassType getType()
	{
		return this.type;
	}

	public String getName()
	{
		if (rawName != null)
		{
			String name = firstLetterUpper(rawName);
			switch (type)
			{
				case Alternative:
					name = "A" + name
							+ getSuperClassDefinition().getName().substring(1);
					break;
				case Production:
					name = "P" + name;
					break;
				case SubProduction:
					name = "S" + name+ getSuperClassDefinition().getName().substring(1);;
					break;
				case Token:
					name = "T" + name;
					break;
			}

			String tmp = javaClassName(name.replace(namePostfix, "")
					+ namePostfix);
			if (VDM && tmp.contains("."))
			{
				return tmp.substring(tmp.lastIndexOf('.') + 1);
			} else
			{
				return tmp;
			}
		}
		return null;
	}

	@Override
	public boolean isAbstract()
	{
		return type == ClassType.Production || type == ClassType.SubProduction;
	}

	@Override
	public boolean isFinal()
	{
		return type == ClassType.Token;
	}

	@Override
	public IClassDefinition getSuperDef()
	{
		return getSuperClassDefinition();
	}

	public IClassDefinition getSuperClassDefinition()
	{
		switch (type)
		{
			case SubProduction:
			case Alternative:
				if (superDef != null)
				{
					return superDef;
				}
				break;
			case Production:
				return env.node;

			case Token:
				return env.token;

			default:
				return env.node;

		}
		return superDef;
	}

	@Override
	public void addField(Field field)
	{
		super.addField(field);
		Method setM = new SetMethod(this, field, env);
		methods.add(setM);

		Method getM = new GetMethod(this, field, env);
		methods.add(getM);
		if(type==ClassType.Production|| type==ClassType.SubProduction)
		{
			field.accessspecifier = AccessSpecifier.Protected;
		}
	}

	@Override
	public boolean hasSuper()
	{
		return true;// this.getSuperClassDefinition() != null;
	}

	public String getEnumName()
	{
		return javaClassName(rawName).toUpperCase();
	}

	public String getEnumTypeName()
	{
		if(type==ClassType.Production)
		{
			
		
		return "E" + BaseClassDefinition.firstLetterUpper(rawName)
				+ namePostfix;
		}else
		{
			return "E" + BaseClassDefinition.firstLetterUpper(rawName)+BaseClassDefinition.firstLetterUpper(((CommonTreeClassDefinition)getSuperClassDefinition()).rawName)
			+ namePostfix;
		}
	}

	@Override
	public String getPackageName()
	{
		switch (type)
		{
			case Production:
				return super.getPackageName();
			case Alternative:
				return getSuperDef().getPackageName();
			case Custom:

			case Token:
			case Unknown:

			default:
				break;
		}
		return super.getPackageName();
	}
	
	public List<Field> getInheritedFields()
	{
		List<Field> fields = new Vector<Field>();
		IClassDefinition sDef = getSuperDef();
		if(sDef instanceof CommonTreeClassDefinition)
		{
			fields.addAll(((CommonTreeClassDefinition)sDef).getInheritedFields());
			fields.addAll(sDef.getFields());
		}
		
		return fields;
	}
}
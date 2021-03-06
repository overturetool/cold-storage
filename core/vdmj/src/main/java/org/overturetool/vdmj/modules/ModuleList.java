/*******************************************************************************
 *
 *	Copyright (C) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overturetool.vdmj.modules;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.overturetool.vdmj.Release;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.debug.DBGPReader;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.NamedTraceDefinition;
import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.LexIdentifierToken;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.messages.Console;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.ContextException;
import org.overturetool.vdmj.runtime.StateContext;
import org.overturetool.vdmj.statements.Statement;
import org.overturetool.vdmj.syntax.ModuleReader;
import org.overturetool.vdmj.util.Utils;


@SuppressWarnings("serial")
public class ModuleList extends Vector<Module>
{
	public ModuleList()
	{
		// empty
	}

	public ModuleList(List<Module> modules)
	{
		addAll(modules);
	}

	@Override
	public String toString()
	{
		return Utils.listToString(this);
	}

	public Set<File> getSourceFiles()
	{
		Set<File> files = new HashSet<File>();

		for (Module def: this)
		{
			files.addAll(def.files);
		}

		return files;
	}

	public Module findModule(LexIdentifierToken sought)
	{
		for (Module m: this)
		{
			if (m.name.equals(sought))
			{
				return m;
			}
		}

   		return null;
	}

	public Statement findStatement(File file, int lineno)
	{
		for (Module m: this)
		{
			Statement stmt = m.findStatement(file, lineno);

			if (stmt != null)
			{
				return stmt;
			}
		}

		return null;
	}

	public Expression findExpression(File file, int lineno)
	{
		for (Module m: this)
		{
			Expression exp = m.findExpression(file, lineno);

			if (exp != null)
			{
				return exp;
			}
		}

		return null;
	}

	public StateContext initialize(DBGPReader dbgp)
	{
		StateContext initialContext = null;

		if (isEmpty())
		{
			initialContext = new StateContext(
				new LexLocation(), "global environment");
		}
		else
		{
			initialContext =
				new StateContext(this.get(0).name.location, "global environment");
		}

		initialContext.setThreadState(dbgp, null);
		Set<ContextException> problems = null;
		int retries = 5;

		do
		{
			problems = new HashSet<ContextException>();

        	for (Module m: this)
    		{
        		Set<ContextException> e = m.initialize(initialContext);

        		if (e != null)
        		{
        			problems.addAll(e);
        		}
     		}
		}
		while (--retries > 0 && !problems.isEmpty());

		if (!problems.isEmpty())
		{
			ContextException toThrow = problems.iterator().next();

			for (ContextException e: problems)
			{
				Console.err.println(e);

				if (e.number != 4034)	// Not in scope err
				{
					toThrow = e;
				}
			}

			throw toThrow;
		}

		return initialContext;
	}

	public ProofObligationList getProofObligations()
	{
		ProofObligationList obligations = new ProofObligationList();

		for (Module m: this)
		{
			obligations.addAll(m.getProofObligations());
		}

		obligations.trivialCheck();
		return obligations;
	}

	public void setLoaded()
	{
		for (Module m: this)
		{
			m.typechecked = true;
		}
	}

	public int notLoaded()
	{
		int count = 0;

		for (Module m: this)
		{
			if (!m.typechecked) count++;
		}

		return count;
	}

	public int combineDefaults()
	{
		int rv = 0;

		if (!isEmpty())
		{
			Module def = new Module();

			if (Settings.release == Release.VDM_10)
			{
				// In VDM-10, we implicitly import all from the other
				// modules included with the flat specifications (if any).

    			List<ImportFromModule> imports = new Vector<ImportFromModule>();

    			for (Module m: this)
    			{
    				if (!m.isFlat)
    				{
    					imports.add(ModuleReader.importAll(m.name));
    				}
    			}

    			if (!imports.isEmpty())
    			{
    				def = new Module(def.name,
    					new ModuleImports(def.name, imports), null, def.defs);
    			}
			}

			ModuleList named = new ModuleList();

			for (Module m: this)
			{
				if (m.isFlat)
				{
					def.defs.addAll(m.defs);
					def.files.add(m.name.location.file);
					def.typechecked |= m.typechecked;
				}
				else
				{
					named.add(m);
				}
			}

			if (!def.defs.isEmpty())
			{
				clear();
				add(def);
				addAll(named);

				for (Definition d: def.defs)
				{
					if (!d.isTypeDefinition())
					{
						d.markUsed();	// Mark top-level items as used
					}
				}
			}
		}

		return rv;
	}

	public NamedTraceDefinition findTraceDefinition(LexNameToken name)
	{
		for (Module m: this)
		{
			for (Definition d: m.defs)
			{
				if (name.equals(d.name))
				{
					if (d instanceof NamedTraceDefinition)
					{
						return (NamedTraceDefinition)d;
					}
					else
					{
						return null;
					}
				}
			}
		}

		return null;
	}
}

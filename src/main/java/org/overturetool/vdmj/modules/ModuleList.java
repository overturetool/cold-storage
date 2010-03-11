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

import org.overturetool.vdmj.debug.DBGPReader;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.LexIdentifierToken;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.ContextException;
import org.overturetool.vdmj.runtime.StateContext;
import org.overturetool.vdmj.scheduler.ResourceScheduler;
import org.overturetool.vdmj.scheduler.SystemClock;
import org.overturetool.vdmj.statements.Statement;
import org.overturetool.vdmj.util.Utils;
import org.overturetool.vdmj.values.BUSValue;
import org.overturetool.vdmj.values.CPUValue;


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

	public StateContext initialize(ResourceScheduler scheduler, DBGPReader dbgp)
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

		scheduler.init();
		SystemClock.init();
		CPUValue.init(scheduler);
		BUSValue.init();

		initialContext.setThreadState(dbgp, null);
		ContextException problems = null;
		int retries = 2;

		do
		{
			problems = null;

        	for (Module m: this)
    		{
        		ContextException e = m.initialize(initialContext);

        		if (e != null)
        		{
        			problems = e;
        		}
     		}
		}
		while (--retries > 0 && problems != null);

		if (problems != null)
		{
			throw problems;		// ... out of pram :)
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
			ModuleList named = new ModuleList();

			for (Module m: this)
			{
				if (m.name.name.equals("DEFAULT"))
				{
					def.defs.addAll(m.defs);
					def.files.add(m.name.location.file);
				}
				else
				{
					named.add(m);
				}
			}

			if (!def.defs.isEmpty())
			{
				clear();
				addAll(named);
				add(def);

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
}

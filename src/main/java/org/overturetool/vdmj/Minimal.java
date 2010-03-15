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

package org.overturetool.vdmj;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.commands.CommandReader;
import org.overturetool.vdmj.commands.ModuleCommandReader;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.modules.ModuleList;
import org.overturetool.vdmj.runtime.ModuleInterpreter;
import org.overturetool.vdmj.syntax.ModuleReader;
import org.overturetool.vdmj.typechecker.ModuleTypeChecker;
import org.overturetool.vdmj.typechecker.TypeChecker;


public class Minimal
{
	public static void main(String[] args) throws Exception
	{
		Settings.dialect = Dialect.VDM_SL;
		File file = new File(args[0]);
		LexTokenReader ltr = new LexTokenReader(file, Dialect.VDM_SL);
		ModuleReader mr = new ModuleReader(ltr);
		ModuleList modules = mr.readModules();

		if (mr.getErrorCount() == 0)
		{
    		TypeChecker tc = new ModuleTypeChecker(modules);
    		tc.typeCheck();

    		if (TypeChecker.getErrorCount() == 0)
    		{
    			ModuleInterpreter interpreter = new ModuleInterpreter(modules);
    			interpreter.init(null);
    			CommandReader reader = new ModuleCommandReader(interpreter, "$ ");
    			List<File> files = new Vector<File>();
    			files.add(file);
    			reader.run(files);
    		}
		}
	}
}

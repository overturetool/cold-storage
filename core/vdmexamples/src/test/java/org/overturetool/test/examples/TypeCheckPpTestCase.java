/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overturetool.test.examples;

import java.io.File;
import java.util.List;

import org.overturetool.test.examples.vdmj.TypeCheckerProxy;
import org.overturetool.test.examples.vdmj.VdmjFactories.IMessageConverter;
import org.overturetool.test.framework.examples.IMessage;
import org.overturetool.test.framework.examples.Message;
import org.overturetool.test.framework.examples.Result;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.definitions.ClassList;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.messages.VDMMessage;
import org.overturetool.vdmj.typechecker.ClassTypeChecker;
import org.overturetool.vdmj.typechecker.TypeChecker;

public class TypeCheckPpTestCase extends ParserPpTestCase
{
	public TypeCheckPpTestCase()
	{
	}

	public TypeCheckPpTestCase(File file)
	{
		super(file);
	}

	@Override
	public void test() throws Exception
	{
		if(mode==ContentModed.None)
		{
			return;
		}
		
		Result<List<ClassDefinition>> parserRes =parse();
		
		ClassList classes = new ClassList();
		if(parserRes.result==null)
		{
			fail("No output from parser");
		}
		classes.addAll(parserRes.result);
		
		Result<Object> res = TypeCheckerProxy.typeCheck(new ClassTypeChecker(classes), new IMessageConverter()
		{
			
			public IMessage convertMessage(Object m)
			{
				VDMMessage msg = (VDMMessage) m;
				return new Message(msg.number, msg.location.startLine, msg.location.endPos, msg.message);
			}
		});
		
		
		compareResults(res.warnings,res.errors,res.result);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Settings.dialect = Dialect.VDM_PP;
		TypeChecker.clearErrors();
	}
}
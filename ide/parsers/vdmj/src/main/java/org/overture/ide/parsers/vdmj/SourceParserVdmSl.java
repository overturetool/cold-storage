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
package org.overture.ide.parsers.vdmj;

import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.CoreException;
import org.overture.ide.core.parser.AbstractParserParticipant;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.ast.IAstNode;
import org.overturetool.vdmj.config.Properties;
import org.overturetool.vdmj.lex.BacktrackInputReader.ReaderType;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.messages.InternalException;
import org.overturetool.vdmj.modules.Module;
import org.overturetool.vdmj.modules.ModuleList;
import org.overturetool.vdmj.syntax.ModuleReader;

public class SourceParserVdmSl extends AbstractParserParticipant
{

	@Override
	protected ParseResult startParse(IVdmSourceUnit file, String source,
			String charset)
	{
		file.setType(IVdmSourceUnit.VDM_MODULE_SPEC);
		Settings.dialect = Dialect.VDM_SL;
		try
		{
			Settings.release = file.getProject().getLanguageVersion();
		} catch (CoreException e1)
		{
			if (Activator.DEBUG)
			{
				e1.printStackTrace();
			}
		}
//		Settings.dynamictypechecks = file.getProject().hasDynamictypechecks();
//		Settings.invchecks = file.getProject().hasInvchecks();
//		Settings.postchecks = file.getProject().hasPostchecks();
//		Settings.prechecks = file.getProject().hasPrechecks();

		Properties.init();
		Properties.parser_tabstop = 1;
		
		ModuleList modules = new ModuleList();
		modules.clear();
		LexLocation.resetLocations();
//		int perrs = 0;
//		int pwarn = 0;

		ModuleReader reader = null;
		ParseResult result = new ParseResult();
		try
		{

			ReaderType streamReaderType = AbstractParserParticipant.findStreamReaderType(file.getFile());
			
			LexTokenReader ltr = new LexTokenReader(source, Settings.dialect, file.getSystemFile(), charset,streamReaderType);
			reader = new ModuleReader(ltr);
			modules.addAll(reader.readModules());

			List<IAstNode> nodes = new Vector<IAstNode>();
			for (Module module : modules)
			{
				nodes.add(module);
			}
			if (nodes.size() > 0)
			{
				result.setAst(nodes);
			} else
			{
//				perrs++;
				result.setFatalError(new Exception("No VDM source in file"));
			}

		} catch (InternalException e)
		{
//			perrs++;
			result.setFatalError(e);
		} catch (Throwable e)
		{
			e.printStackTrace();
//			perrs++;
			result.setFatalError(e);
		}

		if (reader != null && reader.getErrorCount() > 0)
		{
//			perrs += reader.getErrorCount();

			result.setErrors(reader.getErrors());
		}

		if (reader != null && reader.getWarningCount() > 0)
		{
//			pwarn += reader.getWarningCount();

			result.setWarnings(reader.getWarnings());
		}

		result.setAllLocation(LexLocation.getAllLocations());
		result.setLocationToAstNodeMap(LexLocation.getLocationToAstNodeMap());

		return result;
	}

	

}

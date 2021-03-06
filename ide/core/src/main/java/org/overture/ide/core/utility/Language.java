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
package org.overture.ide.core.utility;

import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.lex.Dialect;

public class Language implements ILanguage
{
	private String name;
	private String nature;
	private final List<String> contentTypes = new Vector<String>();
	private Dialect dialect = null;

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setNature(String nature)
	{
		this.nature = nature;
	}

	public String getNature()
	{
		return nature;
	}

	public void addContentType(String contentType)
	{
		this.contentTypes.add( contentType);
	}

	public List<String> getContentTypes()
	{
		return contentTypes;
	}

	public void setDialect(Dialect dialect)
	{
		this.dialect = dialect;
	}

	public Dialect getDialect()
	{
		return dialect;
	}

}

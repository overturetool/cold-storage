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

package org.overture.ide.debug.utils.xml;

public class XMLDataNode extends XMLNode
{
	public final String cdata;

	public XMLDataNode(String cdata)
	{
		this.cdata = dequote(cdata);
	}

	private static String dequote(String in)
	{
		return in
    		.replaceAll("&amp;", "&")
    		.replaceAll("&lt;", "<")
    		.replaceAll("&gt;", ">")
    		.replaceAll("&quot;", "\\\"");
	}

	@Override
	public String toString()
	{
		return "<![CDATA[" + cdata + "]]>";
	}
}

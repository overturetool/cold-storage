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
package org.overture.ide.ui.editor.syntax;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class PrefixedUnderscoreRule implements IRule
{

	public static final int EOF = -1;

	IToken fToken = null;

	private String fPrefix;

	public PrefixedUnderscoreRule(String prefix, IToken token)
	{
		fToken = token;
		fPrefix = prefix;
	}

	public IToken evaluate(ICharacterScanner scanner)
	{
		StringBuffer sb = new StringBuffer();
		int c = -1;// scanner.read();
		// sb.append((char)c);
		// if (c == EOF) {
		// scanner.unread();
		// return Token.EOF;
		// }

		int readCount = 0;
		for (int i = 0; i < fPrefix.toCharArray().length; i++)
		{
			c = scanner.read();
			if (c == EOF)
			{
				scanner.unread();
				return Token.EOF;
			}
			sb.append((char) c);
			readCount++;
			if (c != fPrefix.toCharArray()[i])
			{

				for (int j = 0; j < readCount; j++)
				{
					scanner.unread();
				}

				return Token.UNDEFINED;

			}
		}

		if (readCount == fPrefix.length())
		{
			c = scanner.read();
			sb.append((char) c);
			readCount++;
			if (c == '_')
			{
				c = scanner.read();
				sb.append((char) c);
				scanner.unread();// we just need to check the next char not consume it
				if (Character.isJavaIdentifierStart(c)||Character.isWhitespace(c)||c=='(')
				{
					return fToken;
				}
			}
		}

		for (int j = 0; j < readCount; j++)
		{
			scanner.unread();
		}

		return Token.UNDEFINED;

		// if(readCount == fPrefix.length()-1)
		// {
		// // c = scanner.read();
		// // sb.append((char)c);
		// // readCount++;
		// if(c=='_')
		// {
		// c = scanner.read();
		// sb.append((char)c);
		// scanner.unread();//we just need to check the next char not consume it
		// if(Character.isJavaIdentifierStart(c))
		// {
		// return fToken;
		// }
		// }
		// }
		//		
		// for (int i = 0; i < readCount; i++)
		// {
		// scanner.unread();
		// }

		// return Token.UNDEFINED;

	}
}
package org.overture.ast.intf.lex;

import org.overture.ast.lex.LexLocation;

public interface ILexIdentifierToken extends ILexToken
{


	public ILexNameToken getClassName();

	public boolean getOld();

	
	



	public boolean isOld();

	public String getName();

	
	public LexLocation getLocation();


	
	
	

}
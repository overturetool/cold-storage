// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 30-07-2009 16:59:35
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NotSupportedConstructException.java

package jp.co.csk.vdm.toolbox.VDM;


// Referenced classes of package jp.co.csk.vdm.toolbox.VDM:
//            CGException

public class NotSupportedConstructException extends CGException
{
	/**
	 * Overture Tool
	 */
	private static final long serialVersionUID = 8373799002740093350L;
	
    public NotSupportedConstructException()
    {
        super("The construct is not supported.");
    }

    public NotSupportedConstructException(Object obj)
    {
        super(obj.toString());
    }
}
//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at Mon 07-Jul-2008 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Fri 06-Jun-2008 09:02:11)
//
// Supported compilers:
// jdk1.4
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.jml.ast.imp;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;

import java.util.*;
import org.overturetool.jml.ast.itf.*;
@SuppressWarnings(all) 
// ***** VDMTOOLS END Name=imports



public class JmlClassKind extends JmlNode implements IJmlClassKind {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=val KEEP=NO
  private Long val = null;
// ***** VDMTOOLS END Name=val


// ***** VDMTOOLS START Name=JmlClassKind KEEP=NO
  public JmlClassKind () throws CGException {
    try {
      val = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=JmlClassKind


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("ClassKind");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IJmlVisitor pVisitor) throws CGException {
    pVisitor.visitClassKind((IJmlClassKind) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=JmlClassKind KEEP=NO
  public JmlClassKind (final Long pv) throws CGException {

    try {
      val = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    setValue(pv);
  }
// ***** VDMTOOLS END Name=JmlClassKind


// ***** VDMTOOLS START Name=JmlClassKind KEEP=NO
  public JmlClassKind (final Long pv, final Long pline, final Long pcolumn) throws CGException {

    try {
      val = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setValue(pv);
      setPosition(pline, pcolumn);
    }
  }
// ***** VDMTOOLS END Name=JmlClassKind


// ***** VDMTOOLS START Name=setValue KEEP=NO
  public void setValue (final Long pval) throws CGException {
    val = UTIL.NumberToLong(UTIL.clone(pval));
  }
// ***** VDMTOOLS END Name=setValue


// ***** VDMTOOLS START Name=getValue KEEP=NO
  public Long getValue () throws CGException {
    return val;
  }
// ***** VDMTOOLS END Name=getValue


// ***** VDMTOOLS START Name=getStringValue KEEP=NO
  public String getStringValue () throws CGException {

    String rexpr_1 = null;
    rexpr_1 = JmlClassKindQuotes.getQuoteName(val);
    return rexpr_1;
  }
// ***** VDMTOOLS END Name=getStringValue
  
  public String toString() {

	  try {
		  return getStringValue();
	  } catch (CGException e) {
		  

		  e.printStackTrace();
	  }
	  return "";
  }

}
;
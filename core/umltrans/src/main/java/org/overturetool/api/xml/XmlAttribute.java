


//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-11-27 by the VDM++ to JAVA Code Generator
// (v8.2 - Fri 29-May-2009 11:13:11)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.api.xml;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=NO

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.ast.itf.*;
import org.overturetool.ast.imp.*;
import org.overturetool.api.io.*;
import org.overturetool.api.io.*;
import org.overturetool.api.xml.*;
import org.overturetool.umltrans.api.*;
import org.overturetool.umltrans.*;
import org.overturetool.umltrans.uml.*;
import org.overturetool.umltrans.uml2vdm.*;
import org.overturetool.umltrans.vdm2uml.*;
// ***** VDMTOOLS END Name=imports



@SuppressWarnings({"all","unchecked","unused"})
public class XmlAttribute {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=name KEEP=NO
  public String name = null;
// ***** VDMTOOLS END Name=name

// ***** VDMTOOLS START Name=val KEEP=NO
  public String val = null;
// ***** VDMTOOLS END Name=val


// ***** VDMTOOLS START Name=vdm_init_XmlAttribute KEEP=NO
  private void vdm_init_XmlAttribute () throws CGException {
    try {

      name = new String("");
      val = new String("");
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_XmlAttribute


// ***** VDMTOOLS START Name=XmlAttribute KEEP=NO
  public XmlAttribute () throws CGException {
    vdm_init_XmlAttribute();
  }
// ***** VDMTOOLS END Name=XmlAttribute


// ***** VDMTOOLS START Name=XmlAttribute#2|String|String KEEP=NO
  public XmlAttribute (final String pname, final String pval) throws CGException {

    vdm_init_XmlAttribute();
    {

      name = UTIL.ConvertToString(UTIL.clone(pname));
      val = UTIL.ConvertToString(UTIL.clone(pval));
    }
  }
// ***** VDMTOOLS END Name=XmlAttribute#2|String|String


// ***** VDMTOOLS START Name=accept#1|XmlVisitor KEEP=NO
  public void accept (final XmlVisitor pxv) throws CGException {
    pxv.VisitXmlAttribute((XmlAttribute) this);
  }
// ***** VDMTOOLS END Name=accept#1|XmlVisitor

}
;
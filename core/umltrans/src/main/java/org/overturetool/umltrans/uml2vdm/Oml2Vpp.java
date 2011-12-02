


//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-12-04 by the VDM++ to JAVA Code Generator
// (v8.2 - Fri 29-May-2009 11:13:11)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.umltrans.uml2vdm;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.ast.itf.*;
import org.overturetool.ast.imp.*;
import org.overturetool.ast.transformation.Oml2VppVisitor;
import org.overturetool.api.io.*;
import org.overturetool.api.io.*;
import org.overturetool.api.*;
import org.overturetool.api.xml.*;
import org.overturetool.umltrans.*;
import org.overturetool.umltrans.api.Util;
import org.overturetool.umltrans.uml.*;
import org.overturetool.umltrans.uml2vdm.*;
import org.overturetool.umltrans.vdm2uml.*;
import org.overturetool.umltrans.api.*;
// ***** VDMTOOLS END Name=imports



@SuppressWarnings({"all","unchecked","unused"})
public class Oml2Vpp {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_Oml2Vpp KEEP=NO
  private void vdm_init_Oml2Vpp () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_Oml2Vpp


// ***** VDMTOOLS START Name=Oml2Vpp KEEP=NO
  public Oml2Vpp () throws CGException {
    vdm_init_Oml2Vpp();
  }
// ***** VDMTOOLS END Name=Oml2Vpp


// ***** VDMTOOLS START Name=Save#3|String|IOmlDocument|StatusLog KEEP=NO
  public void Save (final String fileName, final IOmlDocument doc, final StatusLog log) throws CGException {

    Vector classes = null;
    IOmlSpecifications obj_6 = null;
    obj_6 = (IOmlSpecifications) doc.getSpecifications();
    classes = obj_6.getClassList();
    {

      HashSet iset_7 = new HashSet();
      HashSet set_15 = new HashSet();
      Enumeration enm_16 = classes.elements();
      while ( enm_16.hasMoreElements())
        set_15.add(enm_16.nextElement());
      iset_7 = set_15;
      IOmlClass cl = null;
      for (Iterator enm_34 = iset_7.iterator(); enm_34.hasNext(); ) {

        IOmlClass elem_8 = (IOmlClass) enm_34.next();
        cl = (IOmlClass) elem_8;
        {

          Oml2VppVisitor visitor = new Oml2VppVisitor();
          visitor.useNewLineSeparator(new Boolean(true));
          visitor.visitClass((IOmlClass) cl);
          String tmpArg_v_22 = null;
          String var1_23 = null;
          String var2_25 = null;
          String par_26 = null;
          par_26 = cl.getIdentifier();
          var2_25 = Normalize(par_26);
          var1_23 = fileName.concat(var2_25);
          tmpArg_v_22 = var1_23.concat(new String(".vdmpp"));
          Util.CreateFile(tmpArg_v_22);
          String tmpArg_v_29 = null;
          tmpArg_v_29 = visitor.result;
          Util.WriteFile(tmpArg_v_29);
          Util.CloseFile();
          String par_33 = null;
          par_33 = cl.getIdentifier();
          log.endClass(par_33);
        }
      }
    }
  }
// ***** VDMTOOLS END Name=Save#3|String|IOmlDocument|StatusLog


// ***** VDMTOOLS START Name=Normalize#1|String KEEP=YES
  public String Normalize (final String fileName) throws CGException {
	  String name ="";
	  boolean first = true;
	  for(Character c : fileName.toCharArray())
		  if(c.isLetterOrDigit(c) && !first || first && c.isLetter(c))
			  name +=c;
	  if(name.length()==0)
		  return "tmp";
	  else
		 return name;
  }
// ***** VDMTOOLS END Name=Normalize#1|String

}
;
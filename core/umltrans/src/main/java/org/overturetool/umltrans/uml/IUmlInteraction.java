


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
package org.overturetool.umltrans.uml;

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
public abstract class IUmlInteraction extends IUmlNode {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_IUmlInteraction KEEP=NO
  private void vdm_init_IUmlInteraction () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_IUmlInteraction


// ***** VDMTOOLS START Name=IUmlInteraction KEEP=NO
  public IUmlInteraction () throws CGException {
    vdm_init_IUmlInteraction();
  }
// ***** VDMTOOLS END Name=IUmlInteraction


// ***** VDMTOOLS START Name=getName KEEP=NO
  abstract public String getName () throws CGException ;
// ***** VDMTOOLS END Name=getName


// ***** VDMTOOLS START Name=getLifeLines KEEP=NO
  abstract public HashSet getLifeLines () throws CGException ;
// ***** VDMTOOLS END Name=getLifeLines


// ***** VDMTOOLS START Name=getFragments KEEP=NO
  abstract public HashSet getFragments () throws CGException ;
// ***** VDMTOOLS END Name=getFragments


// ***** VDMTOOLS START Name=getMessages KEEP=NO
  abstract public Vector getMessages () throws CGException ;
// ***** VDMTOOLS END Name=getMessages

}
;
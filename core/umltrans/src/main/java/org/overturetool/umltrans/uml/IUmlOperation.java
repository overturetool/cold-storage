


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
public abstract class IUmlOperation extends IUmlNode {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_IUmlOperation KEEP=NO
  private void vdm_init_IUmlOperation () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_IUmlOperation


// ***** VDMTOOLS START Name=IUmlOperation KEEP=NO
  public IUmlOperation () throws CGException {
    vdm_init_IUmlOperation();
  }
// ***** VDMTOOLS END Name=IUmlOperation


// ***** VDMTOOLS START Name=getName KEEP=NO
  abstract public String getName () throws CGException ;
// ***** VDMTOOLS END Name=getName


// ***** VDMTOOLS START Name=getVisibility KEEP=NO
  abstract public IUmlVisibilityKind getVisibility () throws CGException ;
// ***** VDMTOOLS END Name=getVisibility


// ***** VDMTOOLS START Name=getMultiplicity KEEP=NO
  abstract public IUmlMultiplicityElement getMultiplicity () throws CGException ;
// ***** VDMTOOLS END Name=getMultiplicity


// ***** VDMTOOLS START Name=getIsQuery KEEP=NO
  abstract public Boolean getIsQuery () throws CGException ;
// ***** VDMTOOLS END Name=getIsQuery


// ***** VDMTOOLS START Name=getType KEEP=NO
  abstract public IUmlType getType () throws CGException ;
// ***** VDMTOOLS END Name=getType


// ***** VDMTOOLS START Name=hasType KEEP=NO
  abstract public Boolean hasType () throws CGException ;
// ***** VDMTOOLS END Name=hasType


// ***** VDMTOOLS START Name=getIsStatic KEEP=NO
  abstract public Boolean getIsStatic () throws CGException ;
// ***** VDMTOOLS END Name=getIsStatic


// ***** VDMTOOLS START Name=getOwnedParameters KEEP=NO
  abstract public IUmlParameters getOwnedParameters () throws CGException ;
// ***** VDMTOOLS END Name=getOwnedParameters


// ***** VDMTOOLS START Name=hasOwnedParameters KEEP=NO
  abstract public Boolean hasOwnedParameters () throws CGException ;
// ***** VDMTOOLS END Name=hasOwnedParameters

}
;
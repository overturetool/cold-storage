//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2008-11-08 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Fri 24-Oct-2008 08:59:25)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.potrans;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
// ***** VDMTOOLS END Name=imports



public abstract class HolDefinition extends MLExpression {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_HolDefinition KEEP=NO
  private void vdm_init_HolDefinition () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_HolDefinition


// ***** VDMTOOLS START Name=HolDefinition KEEP=NO
  public HolDefinition () throws CGException {
    vdm_init_HolDefinition();
  }
// ***** VDMTOOLS END Name=HolDefinition


// ***** VDMTOOLS START Name=setTypeInformation#1|HolTypeDescription KEEP=NO
  public void setTypeInformation (final HolTypeDescription var_1_1) throws CGException {}
// ***** VDMTOOLS END Name=setTypeInformation#1|HolTypeDescription


// ***** VDMTOOLS START Name=provides KEEP=NO
  abstract public HashSet provides () throws CGException ;
// ***** VDMTOOLS END Name=provides


// ***** VDMTOOLS START Name=requires KEEP=NO
  abstract public HashSet requires () throws CGException ;
// ***** VDMTOOLS END Name=requires

}
;

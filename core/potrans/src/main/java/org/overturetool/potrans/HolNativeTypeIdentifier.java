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



public class HolNativeTypeIdentifier extends HolTypeIdentifier {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_HolNativeTypeIdentifier KEEP=NO
  private void vdm_init_HolNativeTypeIdentifier () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_HolNativeTypeIdentifier


// ***** VDMTOOLS START Name=HolNativeTypeIdentifier KEEP=NO
  public HolNativeTypeIdentifier () throws CGException {
    vdm_init_HolNativeTypeIdentifier();
  }
// ***** VDMTOOLS END Name=HolNativeTypeIdentifier


// ***** VDMTOOLS START Name=HolNativeTypeIdentifier#1|String KEEP=NO
  public HolNativeTypeIdentifier (final String newName) throws CGException {

    vdm_init_HolNativeTypeIdentifier();
    {

      name = (HolIdentifier) UTIL.clone(new HolIdentifier(newName));
      type = null;
    }
  }
// ***** VDMTOOLS END Name=HolNativeTypeIdentifier#1|String


// ***** VDMTOOLS START Name=HolNativeTypeIdentifier#1|HolIdentifier KEEP=NO
  public HolNativeTypeIdentifier (final HolIdentifier newId) throws CGException {

    vdm_init_HolNativeTypeIdentifier();
    {

      name = (HolIdentifier) UTIL.clone(newId);
      type = null;
    }
  }
// ***** VDMTOOLS END Name=HolNativeTypeIdentifier#1|HolIdentifier


// ***** VDMTOOLS START Name=setTypeInformation#1|HolTypeDescription KEEP=NO
  public void setTypeInformation (final HolTypeDescription var_1_1) throws CGException {}
// ***** VDMTOOLS END Name=setTypeInformation#1|HolTypeDescription


// ***** VDMTOOLS START Name=requires KEEP=NO
  public HashSet requires () throws CGException {
    return new HashSet();
  }
// ***** VDMTOOLS END Name=requires


// ***** VDMTOOLS START Name=print#1|Object KEEP=NO
  public String print (final Object var_1_1) throws CGException {

    String rexpr_3 = null;
    rexpr_3 = name.print();
    return rexpr_3;
  }
// ***** VDMTOOLS END Name=print#1|Object

}
;

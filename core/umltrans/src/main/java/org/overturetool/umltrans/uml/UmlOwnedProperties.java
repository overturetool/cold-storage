


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
public class UmlOwnedProperties extends IUmlOwnedProperties {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivPropetityList KEEP=NO
  private HashSet ivPropetityList = new HashSet();
// ***** VDMTOOLS END Name=ivPropetityList


// ***** VDMTOOLS START Name=vdm_init_UmlOwnedProperties KEEP=NO
  private void vdm_init_UmlOwnedProperties () throws CGException {
    try {
      ivPropetityList = new HashSet();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlOwnedProperties


// ***** VDMTOOLS START Name=UmlOwnedProperties KEEP=NO
  public UmlOwnedProperties () throws CGException {
    vdm_init_UmlOwnedProperties();
  }
// ***** VDMTOOLS END Name=UmlOwnedProperties


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("OwnedProperties");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitOwnedProperties((IUmlOwnedProperties) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlOwnedProperties#1|HashSet KEEP=NO
  public UmlOwnedProperties (final HashSet p1) throws CGException {

    vdm_init_UmlOwnedProperties();
    setPropetityList(p1);
  }
// ***** VDMTOOLS END Name=UmlOwnedProperties#1|HashSet


// ***** VDMTOOLS START Name=UmlOwnedProperties#3|HashSet|Long|Long KEEP=NO
  public UmlOwnedProperties (final HashSet p1, final Long line, final Long column) throws CGException {

    vdm_init_UmlOwnedProperties();
    {

      setPropetityList(p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlOwnedProperties#3|HashSet|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    String fname = new String("propetityList");
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setPropetityList((HashSet) data.get(fname));
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getPropetityList KEEP=NO
  public HashSet getPropetityList () throws CGException {
    return ivPropetityList;
  }
// ***** VDMTOOLS END Name=getPropetityList


// ***** VDMTOOLS START Name=setPropetityList#1|HashSet KEEP=NO
  public void setPropetityList (final HashSet parg) throws CGException {
    ivPropetityList = (HashSet) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setPropetityList#1|HashSet


// ***** VDMTOOLS START Name=addPropetityList#1|IUmlNode KEEP=NO
  public void addPropetityList (final IUmlNode parg) throws CGException {
    ivPropetityList.add(parg);
  }
// ***** VDMTOOLS END Name=addPropetityList#1|IUmlNode

}
;
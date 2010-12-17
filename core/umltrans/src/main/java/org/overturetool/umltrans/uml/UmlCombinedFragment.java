


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
public class UmlCombinedFragment extends IUmlCombinedFragment {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivName KEEP=NO
  private String ivName = null;
// ***** VDMTOOLS END Name=ivName

// ***** VDMTOOLS START Name=ivInteractionOperator KEEP=NO
  private IUmlInteractionOperatorKind ivInteractionOperator = null;
// ***** VDMTOOLS END Name=ivInteractionOperator

// ***** VDMTOOLS START Name=ivOperand KEEP=NO
  private Vector ivOperand = null;
// ***** VDMTOOLS END Name=ivOperand

// ***** VDMTOOLS START Name=ivCovered KEEP=NO
  private HashSet ivCovered = new HashSet();
// ***** VDMTOOLS END Name=ivCovered


// ***** VDMTOOLS START Name=vdm_init_UmlCombinedFragment KEEP=NO
  private void vdm_init_UmlCombinedFragment () throws CGException {
    try {

      ivName = UTIL.ConvertToString(new String());
      ivInteractionOperator = null;
      ivOperand = new Vector();
      ivCovered = new HashSet();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlCombinedFragment


// ***** VDMTOOLS START Name=UmlCombinedFragment KEEP=NO
  public UmlCombinedFragment () throws CGException {
    vdm_init_UmlCombinedFragment();
  }
// ***** VDMTOOLS END Name=UmlCombinedFragment


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("CombinedFragment");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitCombinedFragment((IUmlCombinedFragment) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlCombinedFragment#4|String|IUmlInteractionOperatorKind|Vector|HashSet KEEP=NO
  public UmlCombinedFragment (final String p1, final IUmlInteractionOperatorKind p2, final Vector p3, final HashSet p4) throws CGException {

    vdm_init_UmlCombinedFragment();
    {

      setName(p1);
      setInteractionOperator((IUmlInteractionOperatorKind) p2);
      setOperand(p3);
      setCovered(p4);
    }
  }
// ***** VDMTOOLS END Name=UmlCombinedFragment#4|String|IUmlInteractionOperatorKind|Vector|HashSet


// ***** VDMTOOLS START Name=UmlCombinedFragment#6|String|IUmlInteractionOperatorKind|Vector|HashSet|Long|Long KEEP=NO
  public UmlCombinedFragment (final String p1, final IUmlInteractionOperatorKind p2, final Vector p3, final HashSet p4, final Long line, final Long column) throws CGException {

    vdm_init_UmlCombinedFragment();
    {

      setName(p1);
      setInteractionOperator((IUmlInteractionOperatorKind) p2);
      setOperand(p3);
      setCovered(p4);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlCombinedFragment#6|String|IUmlInteractionOperatorKind|Vector|HashSet|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("name");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setName(UTIL.ConvertToString(data.get(fname)));
    }
    {

      String fname = new String("interactionOperator");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setInteractionOperator((IUmlInteractionOperatorKind) data.get(fname));
    }
    {

      String fname = new String("operand");
      Boolean cond_22 = null;
      cond_22 = new Boolean(data.containsKey(fname));
      if (cond_22.booleanValue()) 
        setOperand((Vector) data.get(fname));
    }
    {

      String fname = new String("covered");
      Boolean cond_31 = null;
      cond_31 = new Boolean(data.containsKey(fname));
      if (cond_31.booleanValue()) 
        setCovered((HashSet) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getName KEEP=NO
  public String getName () throws CGException {
    return ivName;
  }
// ***** VDMTOOLS END Name=getName


// ***** VDMTOOLS START Name=setName#1|String KEEP=NO
  public void setName (final String parg) throws CGException {
    ivName = UTIL.ConvertToString(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setName#1|String


// ***** VDMTOOLS START Name=getInteractionOperator KEEP=NO
  public IUmlInteractionOperatorKind getInteractionOperator () throws CGException {
    return (IUmlInteractionOperatorKind) ivInteractionOperator;
  }
// ***** VDMTOOLS END Name=getInteractionOperator


// ***** VDMTOOLS START Name=setInteractionOperator#1|IUmlInteractionOperatorKind KEEP=NO
  public void setInteractionOperator (final IUmlInteractionOperatorKind parg) throws CGException {
    ivInteractionOperator = (IUmlInteractionOperatorKind) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setInteractionOperator#1|IUmlInteractionOperatorKind


// ***** VDMTOOLS START Name=getOperand KEEP=NO
  public Vector getOperand () throws CGException {
    return ivOperand;
  }
// ***** VDMTOOLS END Name=getOperand


// ***** VDMTOOLS START Name=setOperand#1|Vector KEEP=NO
  public void setOperand (final Vector parg) throws CGException {
    ivOperand = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setOperand#1|Vector


// ***** VDMTOOLS START Name=addOperand#1|IUmlNode KEEP=NO
  public void addOperand (final IUmlNode parg) throws CGException {
    ivOperand.add(parg);
  }
// ***** VDMTOOLS END Name=addOperand#1|IUmlNode


// ***** VDMTOOLS START Name=getCovered KEEP=NO
  public HashSet getCovered () throws CGException {
    return ivCovered;
  }
// ***** VDMTOOLS END Name=getCovered


// ***** VDMTOOLS START Name=setCovered#1|HashSet KEEP=NO
  public void setCovered (final HashSet parg) throws CGException {
    ivCovered = (HashSet) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setCovered#1|HashSet


// ***** VDMTOOLS START Name=addCovered#1|IUmlNode KEEP=NO
  public void addCovered (final IUmlNode parg) throws CGException {
    ivCovered.add(parg);
  }
// ***** VDMTOOLS END Name=addCovered#1|IUmlNode

}
;
//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at Sat 16-Aug-2008 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Fri 06-Jun-2008 09:09:07)
//
// Supported compilers:
// jdk1.4
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.ast.imp;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.ast.itf.*;
// ***** VDMTOOLS END Name=imports



public class OmlSpecificationStatement extends OmlStatement implements IOmlSpecificationStatement {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivExternals KEEP=NO
  private IOmlExternals ivExternals = null;
// ***** VDMTOOLS END Name=ivExternals

// ***** VDMTOOLS START Name=ivPreExpression KEEP=NO
  private IOmlExpression ivPreExpression = null;
// ***** VDMTOOLS END Name=ivPreExpression

// ***** VDMTOOLS START Name=ivPostExpression KEEP=NO
  private IOmlExpression ivPostExpression = null;
// ***** VDMTOOLS END Name=ivPostExpression

// ***** VDMTOOLS START Name=ivExceptions KEEP=NO
  private IOmlExceptions ivExceptions = null;
// ***** VDMTOOLS END Name=ivExceptions


// ***** VDMTOOLS START Name=OmlSpecificationStatement KEEP=NO
  public OmlSpecificationStatement () throws CGException {
    try {

      ivExternals = null;
      ivPreExpression = null;
      ivPostExpression = null;
      ivExceptions = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlSpecificationStatement


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("SpecificationStatement");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitSpecificationStatement((IOmlSpecificationStatement) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlSpecificationStatement KEEP=NO
  public OmlSpecificationStatement (final IOmlExternals p1, final IOmlExpression p2, final IOmlExpression p3, final IOmlExceptions p4) throws CGException {

    try {

      ivExternals = null;
      ivPreExpression = null;
      ivPostExpression = null;
      ivExceptions = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setExternals((IOmlExternals) p1);
      setPreExpression((IOmlExpression) p2);
      setPostExpression((IOmlExpression) p3);
      setExceptions((IOmlExceptions) p4);
    }
  }
// ***** VDMTOOLS END Name=OmlSpecificationStatement


// ***** VDMTOOLS START Name=OmlSpecificationStatement KEEP=NO
  public OmlSpecificationStatement (final IOmlExternals p1, final IOmlExpression p2, final IOmlExpression p3, final IOmlExceptions p4, final Long line, final Long column) throws CGException {

    try {

      ivExternals = null;
      ivPreExpression = null;
      ivPostExpression = null;
      ivExceptions = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setExternals((IOmlExternals) p1);
      setPreExpression((IOmlExpression) p2);
      setPostExpression((IOmlExpression) p3);
      setExceptions((IOmlExceptions) p4);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlSpecificationStatement


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("externals");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setExternals((IOmlExternals) data.get(fname));
    }
    {

      String fname = new String("pre_expression");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setPreExpression((IOmlExpression) data.get(fname));
    }
    {

      String fname = new String("post_expression");
      Boolean cond_22 = null;
      cond_22 = new Boolean(data.containsKey(fname));
      if (cond_22.booleanValue()) 
        setPostExpression((IOmlExpression) data.get(fname));
    }
    {

      String fname = new String("exceptions");
      Boolean cond_31 = null;
      cond_31 = new Boolean(data.containsKey(fname));
      if (cond_31.booleanValue()) 
        setExceptions((IOmlExceptions) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getExternals KEEP=NO
  public IOmlExternals getExternals () throws CGException {

    if (!this.pre_getExternals().booleanValue()) 
      UTIL.RunTime("Run-Time Error:Precondition failure in getExternals");
    return (IOmlExternals) ivExternals;
  }
// ***** VDMTOOLS END Name=getExternals


// ***** VDMTOOLS START Name=pre_getExternals KEEP=NO
  public Boolean pre_getExternals () throws CGException {
    return hasExternals();
  }
// ***** VDMTOOLS END Name=pre_getExternals


// ***** VDMTOOLS START Name=hasExternals KEEP=NO
  public Boolean hasExternals () throws CGException {
    return new Boolean(!UTIL.equals(ivExternals, null));
  }
// ***** VDMTOOLS END Name=hasExternals


// ***** VDMTOOLS START Name=setExternals KEEP=NO
  public void setExternals (final IOmlExternals parg) throws CGException {
    ivExternals = (IOmlExternals) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setExternals


// ***** VDMTOOLS START Name=getPreExpression KEEP=NO
  public IOmlExpression getPreExpression () throws CGException {

    if (!this.pre_getPreExpression().booleanValue()) 
      UTIL.RunTime("Run-Time Error:Precondition failure in getPreExpression");
    return (IOmlExpression) ivPreExpression;
  }
// ***** VDMTOOLS END Name=getPreExpression


// ***** VDMTOOLS START Name=pre_getPreExpression KEEP=NO
  public Boolean pre_getPreExpression () throws CGException {
    return hasPreExpression();
  }
// ***** VDMTOOLS END Name=pre_getPreExpression


// ***** VDMTOOLS START Name=hasPreExpression KEEP=NO
  public Boolean hasPreExpression () throws CGException {
    return new Boolean(!UTIL.equals(ivPreExpression, null));
  }
// ***** VDMTOOLS END Name=hasPreExpression


// ***** VDMTOOLS START Name=setPreExpression KEEP=NO
  public void setPreExpression (final IOmlExpression parg) throws CGException {
    ivPreExpression = (IOmlExpression) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setPreExpression


// ***** VDMTOOLS START Name=getPostExpression KEEP=NO
  public IOmlExpression getPostExpression () throws CGException {
    return (IOmlExpression) ivPostExpression;
  }
// ***** VDMTOOLS END Name=getPostExpression


// ***** VDMTOOLS START Name=setPostExpression KEEP=NO
  public void setPostExpression (final IOmlExpression parg) throws CGException {
    ivPostExpression = (IOmlExpression) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setPostExpression


// ***** VDMTOOLS START Name=getExceptions KEEP=NO
  public IOmlExceptions getExceptions () throws CGException {

    if (!this.pre_getExceptions().booleanValue()) 
      UTIL.RunTime("Run-Time Error:Precondition failure in getExceptions");
    return (IOmlExceptions) ivExceptions;
  }
// ***** VDMTOOLS END Name=getExceptions


// ***** VDMTOOLS START Name=pre_getExceptions KEEP=NO
  public Boolean pre_getExceptions () throws CGException {
    return hasExceptions();
  }
// ***** VDMTOOLS END Name=pre_getExceptions


// ***** VDMTOOLS START Name=hasExceptions KEEP=NO
  public Boolean hasExceptions () throws CGException {
    return new Boolean(!UTIL.equals(ivExceptions, null));
  }
// ***** VDMTOOLS END Name=hasExceptions


// ***** VDMTOOLS START Name=setExceptions KEEP=NO
  public void setExceptions (final IOmlExceptions parg) throws CGException {
    ivExceptions = (IOmlExceptions) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setExceptions

}
;
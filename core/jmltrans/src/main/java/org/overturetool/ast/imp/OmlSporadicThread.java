//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at Wed 11-Jun-2008 by the VDM++ to JAVA Code Generator
// (v8.0.1b - Mon 28-Jan-2008 10:17:36)
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
@SuppressWarnings(all) 
// ***** VDMTOOLS END Name=imports



public class OmlSporadicThread extends OmlThreadSpecification implements IOmlSporadicThread {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivArgs KEEP=NO
  private Vector ivArgs = null;
// ***** VDMTOOLS END Name=ivArgs

// ***** VDMTOOLS START Name=ivName KEEP=NO
  private IOmlName ivName = null;
// ***** VDMTOOLS END Name=ivName


// ***** VDMTOOLS START Name=OmlSporadicThread KEEP=NO
  public OmlSporadicThread () throws CGException {
    try {

      ivArgs = new Vector();
      ivName = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlSporadicThread


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("SporadicThread");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitSporadicThread((IOmlSporadicThread) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlSporadicThread KEEP=NO
  public OmlSporadicThread (final Vector p1, final IOmlName p2) throws CGException {

    try {

      ivArgs = new Vector();
      ivName = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setArgs(p1);
      setName((IOmlName) p2);
    }
  }
// ***** VDMTOOLS END Name=OmlSporadicThread


// ***** VDMTOOLS START Name=OmlSporadicThread KEEP=NO
  public OmlSporadicThread (final Vector p1, final IOmlName p2, final Long line, final Long column) throws CGException {

    try {

      ivArgs = new Vector();
      ivName = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setArgs(p1);
      setName((IOmlName) p2);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlSporadicThread


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("args");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setArgs((Vector) data.get(fname));
    }
    {

      String fname = new String("name");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setName((IOmlName) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getArgs KEEP=NO
  public Vector getArgs () throws CGException {
    return ivArgs;
  }
// ***** VDMTOOLS END Name=getArgs


// ***** VDMTOOLS START Name=setArgs KEEP=NO
  public void setArgs (final Vector parg) throws CGException {
    ivArgs = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setArgs


// ***** VDMTOOLS START Name=addArgs KEEP=NO
  public void addArgs (final IOmlNode parg) throws CGException {
    ivArgs.add(parg);
  }
// ***** VDMTOOLS END Name=addArgs


// ***** VDMTOOLS START Name=getName KEEP=NO
  public IOmlName getName () throws CGException {
    return (IOmlName) ivName;
  }
// ***** VDMTOOLS END Name=getName


// ***** VDMTOOLS START Name=setName KEEP=NO
  public void setName (final IOmlName parg) throws CGException {
    ivName = (IOmlName) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setName

  //TODO: SporadicThread
  public String toString() {
	  return "TODO: SporadicThread";
  }
}
;
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



public class OmlBlockStatement extends OmlStatement implements IOmlBlockStatement {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivDclStatementList KEEP=NO
  private Vector ivDclStatementList = null;
// ***** VDMTOOLS END Name=ivDclStatementList

// ***** VDMTOOLS START Name=ivStatementList KEEP=NO
  private Vector ivStatementList = null;
// ***** VDMTOOLS END Name=ivStatementList


// ***** VDMTOOLS START Name=OmlBlockStatement KEEP=NO
  public OmlBlockStatement () throws CGException {
    try {

      ivDclStatementList = new Vector();
      ivStatementList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlBlockStatement


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("BlockStatement");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitBlockStatement((IOmlBlockStatement) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlBlockStatement KEEP=NO
  public OmlBlockStatement (final Vector p1, final Vector p2) throws CGException {

    try {

      ivDclStatementList = new Vector();
      ivStatementList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setDclStatementList(p1);
      setStatementList(p2);
    }
  }
// ***** VDMTOOLS END Name=OmlBlockStatement


// ***** VDMTOOLS START Name=OmlBlockStatement KEEP=NO
  public OmlBlockStatement (final Vector p1, final Vector p2, final Long line, final Long column) throws CGException {

    try {

      ivDclStatementList = new Vector();
      ivStatementList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setDclStatementList(p1);
      setStatementList(p2);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlBlockStatement


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("dcl_statement_list");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setDclStatementList((Vector) data.get(fname));
    }
    {

      String fname = new String("statement_list");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setStatementList((Vector) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getDclStatementList KEEP=NO
  public Vector getDclStatementList () throws CGException {
    return ivDclStatementList;
  }
// ***** VDMTOOLS END Name=getDclStatementList


// ***** VDMTOOLS START Name=setDclStatementList KEEP=NO
  public void setDclStatementList (final Vector parg) throws CGException {
    ivDclStatementList = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setDclStatementList


// ***** VDMTOOLS START Name=addDclStatementList KEEP=NO
  public void addDclStatementList (final IOmlNode parg) throws CGException {
    ivDclStatementList.add(parg);
  }
// ***** VDMTOOLS END Name=addDclStatementList


// ***** VDMTOOLS START Name=getStatementList KEEP=NO
  public Vector getStatementList () throws CGException {
    return ivStatementList;
  }
// ***** VDMTOOLS END Name=getStatementList


// ***** VDMTOOLS START Name=setStatementList KEEP=NO
  public void setStatementList (final Vector parg) throws CGException {
    ivStatementList = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setStatementList


// ***** VDMTOOLS START Name=addStatementList KEEP=NO
  public void addStatementList (final IOmlNode parg) throws CGException {
    ivStatementList.add(parg);
  }
// ***** VDMTOOLS END Name=addStatementList

  public String toString() {
	  String dcl = new String();
	  String stat = new String();
	  int i,j;
	  if (this.ivDclStatementList.size()>0) {
		  for(i=0;i<this.ivDclStatementList.size()-1;i++) {

			  OmlDclStatement d = (OmlDclStatement) this.ivDclStatementList.get(i);
			  dcl += d.toString() + ";\n";
		  } 
		  OmlDclStatement d = (OmlDclStatement) this.ivDclStatementList.get(this.ivDclStatementList.size());
		  dcl += d.toString() + ";";
	  }
	  if (this.ivStatementList.size()>0) {
		  for(i=0;i<this.ivStatementList.size()-1;i++) {

			  OmlStatement d = (OmlStatement) this.ivStatementList.get(i);
			  stat += d.toString() + ";\n";
		  } 
		  OmlStatement d = (OmlStatement) this.ivStatementList.get(this.ivStatementList.size()-1);
		  stat += d.toString() + ";";
	  }
	  return "(" + dcl + stat + ")";
  }
}
;

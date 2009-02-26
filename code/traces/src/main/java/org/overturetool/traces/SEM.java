//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-02-14 by the VDM++ to JAVA Code Generator
// (v8.2b - Tue 03-Feb-2009 11:50:55)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.traces;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.ast.imp.*;
import org.overturetool.ast.itf.*;
// ***** VDMTOOLS END Name=imports



public class SEM {
// ***** VDMTOOLS START Name=VAL KEEP=NO
  public static interface VAL {
  }
// ***** VDMTOOLS END Name=VAL
;
// ***** VDMTOOLS START Name=BasicVal KEEP=NO
  public static interface BasicVal extends VAL {
  }
// ***** VDMTOOLS END Name=BasicVal
;


// ***** VDMTOOLS START Name=BOOL KEEP=NO
  public static class BOOL implements BasicVal, Record {

    public Boolean v;


    public BOOL () {}


    public BOOL (Boolean p1) {
      v = p1;
    }


    public Object clone () {
      return new BOOL(v);
    }


    public String toString () {
      return "mk_SEM`BOOL(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof BOOL)) 
        return false;
      else {

        BOOL temp = (BOOL) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=BOOL
;


// ***** VDMTOOLS START Name=NUM KEEP=NO
  public static class NUM implements BasicVal, Record {

    public Double v;


    public NUM () {}


    public NUM (Double p1) {
      v = p1;
    }


    public Object clone () {
      return new NUM(v);
    }


    public String toString () {
      return "mk_SEM`NUM(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof NUM)) 
        return false;
      else {

        NUM temp = (NUM) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=NUM
;


// ***** VDMTOOLS START Name=CHAR KEEP=NO
  public static class CHAR implements BasicVal, Record {

    public Character v;


    public CHAR () {}


    public CHAR (Character p1) {
      v = p1;
    }


    public Object clone () {
      return new CHAR(v);
    }


    public String toString () {
      return "mk_SEM`CHAR(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof CHAR)) 
        return false;
      else {

        CHAR temp = (CHAR) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=CHAR
;


// ***** VDMTOOLS START Name=QUOTE KEEP=NO
  public static class QUOTE implements BasicVal, Record {

    public String v;


    public QUOTE () {}


    public QUOTE (String p1) {
      v = p1;
    }


    public Object clone () {
      return new QUOTE(v);
    }


    public String toString () {
      return "mk_SEM`QUOTE(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof QUOTE)) 
        return false;
      else {

        QUOTE temp = (QUOTE) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=QUOTE
;


// ***** VDMTOOLS START Name=NIL KEEP=NO
  public static class NIL implements BasicVal, Record {

    public NIL () {}


    public Object clone () {
      return new NIL();
    }


    public String toString () {
      return "mk_SEM`NIL()";
    }


    public boolean equals (Object obj) {
      return obj instanceof NIL;
    }


    public int hashCode () {
      return 0;
    }

  }
// ***** VDMTOOLS END Name=NIL
;


// ***** VDMTOOLS START Name=SEQ KEEP=NO
  public static class SEQ implements VAL, Record {

    public Vector v;


    public SEQ () {}


    public SEQ (Vector p1) {
      v = p1;
    }


    public Object clone () {
      return new SEQ((Vector) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`SEQ(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof SEQ)) 
        return false;
      else {

        SEQ temp = (SEQ) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=SEQ
;


// ***** VDMTOOLS START Name=SET KEEP=NO
  public static class SET implements VAL, Record {

    public HashSet v;


    public SET () {}


    public SET (HashSet p1) {
      v = p1;
    }


    public Object clone () {
      return new SET((HashSet) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`SET(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof SET)) 
        return false;
      else {

        SET temp = (SET) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=SET
;


// ***** VDMTOOLS START Name=MAP KEEP=NO
  public static class MAP implements VAL, Record {

    public HashMap v;


    public MAP () {}


    public MAP (HashMap p1) {
      v = p1;
    }


    public Object clone () {
      return new MAP((HashMap) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`MAP(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof MAP)) 
        return false;
      else {

        MAP temp = (MAP) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=MAP
;


// ***** VDMTOOLS START Name=TUPLE KEEP=NO
  public static class TUPLE implements VAL, Record {

    public Vector v;


    public TUPLE () {}


    public TUPLE (Vector p1) {
      v = p1;
    }


    public Object clone () {
      return new TUPLE((Vector) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`TUPLE(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof TUPLE)) 
        return false;
      else {

        TUPLE temp = (TUPLE) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=TUPLE
;


// ***** VDMTOOLS START Name=REC KEEP=NO
  public static class REC implements VAL, Record {

    public DEF.Name tag;

    public Vector v;


    public REC () {}


    public REC (DEF.Name p1, Vector p2) {

      tag = p1;
      v = p2;
    }


    public Object clone () {
      return new REC((DEF.Name) UTIL.clone(tag), (Vector) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`REC(" + UTIL.toString(tag) + "," + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof REC)) 
        return false;
      else {

        REC temp = (REC) obj;
        return UTIL.equals(tag, temp.tag) && UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return (tag == null ? 0 : tag.hashCode()) + (v == null ? 0 : v.hashCode());
    }

  }
// ***** VDMTOOLS END Name=REC
;


// ***** VDMTOOLS START Name=TOKEN KEEP=NO
  public static class TOKEN implements VAL, Record {

    public VAL v;


    public TOKEN () {}


    public TOKEN (VAL p1) {
      v = p1;
    }


    public Object clone () {
      return new TOKEN((VAL) UTIL.clone(v));
    }


    public String toString () {
      return "mk_SEM`TOKEN(" + UTIL.toString(v) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof TOKEN)) 
        return false;
      else {

        TOKEN temp = (TOKEN) obj;
        return UTIL.equals(v, temp.v);
      }
    }


    public int hashCode () {
      return v == null ? 0 : v.hashCode();
    }

  }
// ***** VDMTOOLS END Name=TOKEN
;


// ***** VDMTOOLS START Name=OBJ KEEP=NO
  public static class OBJ implements VAL, Record {

    public String nm;

    public Vector e_ul;


    public OBJ () {}


    public OBJ (String p1, Vector p2) {

      nm = p1;
      e_ul = p2;
    }


    public Object clone () {
      return new OBJ(nm, (Vector) UTIL.clone(e_ul));
    }


    public String toString () {
      return "mk_SEM`OBJ(" + UTIL.toString(nm) + "," + UTIL.toString(e_ul) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof OBJ)) 
        return false;
      else {

        OBJ temp = (OBJ) obj;
        return UTIL.equals(nm, temp.nm) && UTIL.equals(e_ul, temp.e_ul);
      }
    }


    public int hashCode () {
      return (nm == null ? 0 : nm.hashCode()) + (e_ul == null ? 0 : e_ul.hashCode());
    }

  }
// ***** VDMTOOLS END Name=OBJ
;


// ***** VDMTOOLS START Name=OBJ_uRef KEEP=NO
  public static class OBJ_uRef implements VAL, Record {

    public Long f1;


    public OBJ_uRef () {}


    public OBJ_uRef (Long p1) {
      f1 = p1;
    }


    public Object clone () {
      return new OBJ_uRef(f1);
    }


    public String toString () {
      return "mk_SEM`OBJ_uRef(" + UTIL.toString(f1) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof OBJ_uRef)) 
        return false;
      else {

        OBJ_uRef temp = (OBJ_uRef) obj;
        return UTIL.equals(f1, temp.f1);
      }
    }


    public int hashCode () {
      return f1 == null ? 0 : f1.hashCode();
    }

  }
// ***** VDMTOOLS END Name=OBJ_uRef
;

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=cacheval KEEP=NO
  private static HashMap cacheval = new HashMap();
// ***** VDMTOOLS END Name=cacheval


// ***** VDMTOOLS START Name=vdm_init_SEM KEEP=NO
  private void vdm_init_SEM () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_SEM


// ***** VDMTOOLS START Name=SEM KEEP=NO
  public SEM () throws CGException {
    vdm_init_SEM();
  }
// ***** VDMTOOLS END Name=SEM


// ***** VDMTOOLS START Name=VAL2IOmlExpr#1|VAL KEEP=NO
  static public IOmlExpression VAL2IOmlExpr (final VAL val) throws CGException {

    Boolean cond_2 = null;
    cond_2 = new Boolean(cacheval.containsKey(val));
    if (cond_2.booleanValue()) 
      return (IOmlExpression) (IOmlExpression) cacheval.get(val);
    else {

      Object e = null;
      boolean succ_7 = true;
      {

        Boolean v = null;
        succ_7 = true;
        if (val instanceof BOOL) {

          Vector e_l_10 = new Vector();
          e_l_10.add(((BOOL) val).v);
          if (succ_7 = (1 == e_l_10.size())) 
            v = (Boolean) e_l_10.get(0);
        }
        else 
          succ_7 = false;
        if (succ_7) {

          OmlBooleanLiteral l = (OmlBooleanLiteral) new OmlBooleanLiteral(v);
          e = (Object) new OmlSymbolicLiteralExpression(l);
        }
      }
      if (!succ_7) {

        Double v = null;
        succ_7 = true;
        if (val instanceof NUM) {

          Vector e_l_16 = new Vector();
          e_l_16.add(((NUM) val).v);
          if (succ_7 = (1 == e_l_16.size())) 
            v = UTIL.NumberToReal(e_l_16.get(0));
        }
        else 
          succ_7 = false;
        if (succ_7) {

          OmlRealLiteral l = (OmlRealLiteral) new OmlRealLiteral(v);
          e = (Object) new OmlSymbolicLiteralExpression(l);
        }
      }
      if (!succ_7) {

        Character v = null;
        succ_7 = true;
        if (val instanceof CHAR) {

          String e_l_22 = new String();
          e_l_22 = e_l_22 + ((CHAR) val).v;
          if (succ_7 = (1 == e_l_22.length())) 
            v = new Character(e_l_22.charAt(0));
        }
        else 
          succ_7 = false;
        if (succ_7) {

          OmlCharacterLiteral l = (OmlCharacterLiteral) new OmlCharacterLiteral(v);
          e = (Object) new OmlSymbolicLiteralExpression(l);
        }
      }
      if (!succ_7) {

        String v = null;
        succ_7 = true;
        if (val instanceof QUOTE) {

          Vector e_l_28 = new Vector();
          e_l_28.add(((QUOTE) val).v);
          if (succ_7 = (1 == e_l_28.size())) 
            v = UTIL.ConvertToString(e_l_28.get(0));
        }
        else 
          succ_7 = false;
        if (succ_7) {

          OmlQuoteLiteral l = (OmlQuoteLiteral) new OmlQuoteLiteral(v);
          e = (Object) new OmlSymbolicLiteralExpression(l);
        }
      }
      if (!succ_7) {

        succ_7 = true;
        if (val instanceof NIL) {

          Vector e_l_34 = new Vector();
          succ_7 = (0 == e_l_34.size());
        }
        else 
          succ_7 = false;
        if (succ_7) {

          OmlNilLiteral l = (OmlNilLiteral) new OmlNilLiteral();
          e = (Object) new OmlSymbolicLiteralExpression(l);
        }
      }
      if (!succ_7) {

        HashSet v = new HashSet();
        succ_7 = true;
        if (val instanceof SET) {

          Vector e_l_39 = new Vector();
          e_l_39.add(((SET) val).v);
          if (succ_7 = (1 == e_l_39.size())) 
            v = (HashSet) e_l_39.get(0);
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector e_ul = VALSet2IOmlExpr(v);
          e = (Object) new OmlSetEnumeration(e_ul);
        }
      }
      if (!succ_7) {

        Vector v = null;
        succ_7 = true;
        if (val instanceof SEQ) {

          Vector e_l_45 = new Vector();
          e_l_45.add(((SEQ) val).v);
          if (succ_7 = (1 == e_l_45.size())) 
            v = (Vector) UTIL.ConvertToList(e_l_45.get(0));
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector e_ul = VALSeq2IOmlExpr(v);
          e = (Object) new OmlSequenceEnumeration(e_ul);
        }
      }
      if (!succ_7) {

        HashMap v = new HashMap();
        succ_7 = true;
        if (val instanceof MAP) {

          Vector e_l_51 = new Vector();
          e_l_51.add(((MAP) val).v);
          if (succ_7 = (1 == e_l_51.size())) 
            v = (HashMap) e_l_51.get(0);
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector e_ul = VALMap2IOmlExpr(v);
          e = (Object) new OmlMapEnumeration(e_ul);
        }
      }
      if (!succ_7) {

        Vector v = null;
        succ_7 = true;
        if (val instanceof TUPLE) {

          Vector e_l_57 = new Vector();
          e_l_57.add(((TUPLE) val).v);
          if (succ_7 = (1 == e_l_57.size())) 
            v = (Vector) UTIL.ConvertToList(e_l_57.get(0));
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector e_ul = VALSeq2IOmlExpr(v);
          e = (Object) new OmlTupleConstructor(e_ul);
        }
      }
      if (!succ_7) {

        DEF.Name t = null;
        Vector v = null;
        succ_7 = true;
        if (val instanceof REC) {

          Vector e_l_63 = new Vector();
          e_l_63.add(((REC) val).tag);
          e_l_63.add(((REC) val).v);
          if (succ_7 = (2 == e_l_63.size())) {

            t = (DEF.Name) e_l_63.get(0);
            v = (Vector) e_l_63.get(2 - 1);
          }
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector e_ul = VALSeq2IOmlExpr(v);
          OmlName nm = null;
          String arg_69 = null;
          arg_69 = (t).clnm;
          String arg_70 = null;
          arg_70 = (t).tnm;
          nm = new OmlName(arg_69, arg_70);
          e = (Object) new OmlRecordConstructor(nm, e_ul);
        }
      }
      if (!succ_7) {

        VAL v = null;
        succ_7 = true;
        if (val instanceof TOKEN) {

          Vector e_l_75 = new Vector();
          e_l_75.add(((TOKEN) val).v);
          if (succ_7 = (1 == e_l_75.size())) 
            v = (VAL) e_l_75.get(0);
        }
        else 
          succ_7 = false;
        if (succ_7) {

          IOmlExpression v1 = (IOmlExpression) (IOmlExpression) VAL2IOmlExpr((VAL) v);
          e = (Object) new OmlTokenExpression(v1);
        }
      }
      if (!succ_7) {

        String id = null;
        Vector vl = null;
        succ_7 = true;
        if (val instanceof OBJ) {

          Vector e_l_81 = new Vector();
          e_l_81.add(((OBJ) val).nm);
          e_l_81.add(((OBJ) val).e_ul);
          if (succ_7 = (2 == e_l_81.size())) {

            id = UTIL.ConvertToString((Vector) UTIL.ConvertToList(e_l_81.get(0)));
            vl = (Vector) UTIL.ConvertToList(e_l_81.get(2 - 1));
          }
        }
        else 
          succ_7 = false;
        if (succ_7) {

          Vector v1 = VALSeq2IOmlExpr(vl);
          OmlName nm = (OmlName) new OmlName(null, id);
          e = (Object) new OmlNewExpression(nm, new Vector(), v1);
        }
      }
      if (!succ_7) 
        UTIL.RunTime("Run-Time Error:Can not evaluate an undefined expression");
      {

        cacheval.put(val, e);
        return (IOmlExpression) e;
      }
    }
  }
// ***** VDMTOOLS END Name=VAL2IOmlExpr#1|VAL


// ***** VDMTOOLS START Name=VALSet2IOmlExpr#1|HashSet KEEP=NO
  static public Vector VALSet2IOmlExpr (final HashSet val_us) throws CGException {

    Vector e_ul = new Vector();
    {

      VAL val = null;
      for (Iterator enm_11 = val_us.iterator(); enm_11.hasNext(); ) {

        Object elem_3 = enm_11.next();
        val = (VAL) elem_3;
        {

          Vector rhs_6 = null;
          Vector var1_7 = null;
          var1_7 = new Vector();
          var1_7.add((IOmlExpression) VAL2IOmlExpr((VAL) val));
          rhs_6 = (Vector) var1_7.clone();
          rhs_6.addAll(e_ul);
          e_ul = (Vector) UTIL.ConvertToList(UTIL.clone(rhs_6));
        }
      }
    }
    return e_ul;
  }
// ***** VDMTOOLS END Name=VALSet2IOmlExpr#1|HashSet


// ***** VDMTOOLS START Name=VALMap2IOmlExpr#1|HashMap KEEP=NO
  static public Vector VALMap2IOmlExpr (final HashMap val_um) throws CGException {

    Vector e_ul = new Vector();
    {

      HashSet iset_2 = new HashSet();
      iset_2.clear();
      iset_2.addAll(val_um.keySet());
      VAL val = null;
      for (Iterator enm_21 = iset_2.iterator(); enm_21.hasNext(); ) {

        Object elem_3 = enm_21.next();
        val = (VAL) elem_3;
        {

          IOmlExpression d = (IOmlExpression) (IOmlExpression) VAL2IOmlExpr((VAL) val);
          IOmlExpression tmpVal_11 = null;
          tmpVal_11 = (IOmlExpression) (IOmlExpression) VAL2IOmlExpr((VAL) (VAL) val_um.get(val));
          IOmlExpression r = null;
          r = (IOmlExpression) tmpVal_11;
          Vector rhs_15 = null;
          Vector var1_16 = null;
          var1_16 = new Vector();
          var1_16.add(new OmlMaplet(d, r));
          rhs_15 = (Vector) var1_16.clone();
          rhs_15.addAll(e_ul);
          e_ul = (Vector) UTIL.ConvertToList(UTIL.clone(rhs_15));
        }
      }
    }
    return e_ul;
  }
// ***** VDMTOOLS END Name=VALMap2IOmlExpr#1|HashMap


// ***** VDMTOOLS START Name=VALSeq2IOmlExpr#1|Vector KEEP=NO
  static public Vector VALSeq2IOmlExpr (final Vector val_ul) throws CGException {

    Vector varRes_2 = null;
    {

      Vector res_l_3 = new Vector();
      HashSet resBind_s_5 = new HashSet();
      HashSet riseq_9 = new HashSet();
      int max_10 = val_ul.size();
      for (int i_11 = 1; i_11 <= max_10; i_11++) 
        riseq_9.add(new Long(i_11));
      resBind_s_5 = riseq_9;
      Vector bind_l_4 = null;
      bind_l_4 = UTIL.Sort(resBind_s_5);
      Long i = null;
      for (Iterator enm_17 = bind_l_4.iterator(); enm_17.hasNext(); ) {

        Long e_7 = UTIL.NumberToLong(enm_17.next());
        i = e_7;
        IOmlExpression reselem_12 = null;
        VAL par_13 = null;
        if ((1 <= i.intValue()) && (i.intValue() <= val_ul.size())) 
          par_13 = (VAL) val_ul.get(i.intValue() - 1);
        else 
          UTIL.RunTime("Run-Time Error:Illegal index");
        reselem_12 = (IOmlExpression) VAL2IOmlExpr((VAL) par_13);
        res_l_3.add(reselem_12);
      }
      varRes_2 = res_l_3;
    }
    return varRes_2;
  }
// ***** VDMTOOLS END Name=VALSeq2IOmlExpr#1|Vector

}
;

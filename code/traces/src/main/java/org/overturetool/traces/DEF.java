//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-02-18 by the VDM++ to JAVA Code Generator
// (v8.2b - Fri 13-Feb-2009 09:10:36)
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



public class DEF {


// ***** VDMTOOLS START Name=Name KEEP=NO
  public static class Name implements Record {

    public String clnm;

    public String tnm;


    public Name () {}


    public Name (String p1, String p2) {

      clnm = p1;
      tnm = p2;
    }


    public Object clone () {
      return new Name(clnm, tnm);
    }


    public String toString () {
      return "mk_DEF`Name(" + UTIL.toString(clnm) + "," + UTIL.toString(tnm) + ")";
    }


    public boolean equals (Object obj) {
      if (!(obj instanceof Name)) 
        return false;
      else {

        Name temp = (Name) obj;
        return UTIL.equals(clnm, temp.clnm) && UTIL.equals(tnm, temp.tnm);
      }
    }


    public int hashCode () {
      return (clnm == null ? 0 : clnm.hashCode()) + (tnm == null ? 0 : tnm.hashCode());
    }

  }
// ***** VDMTOOLS END Name=Name
;

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=valm KEEP=NO
  private HashMap valm = new HashMap();
// ***** VDMTOOLS END Name=valm

// ***** VDMTOOLS START Name=inherit KEEP=NO
  private HashMap inherit = new HashMap();
// ***** VDMTOOLS END Name=inherit

// ***** VDMTOOLS START Name=recdefs KEEP=NO
  private HashMap recdefs = new HashMap();
// ***** VDMTOOLS END Name=recdefs


// ***** VDMTOOLS START Name=vdm_init_DEF KEEP=NO
  private void vdm_init_DEF () throws CGException {
    try {

      valm = new HashMap();
      inherit = new HashMap();
      recdefs = new HashMap();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_DEF


// ***** VDMTOOLS START Name=DEF KEEP=NO
  public DEF () throws CGException {
    vdm_init_DEF();
  }
// ***** VDMTOOLS END Name=DEF


// ***** VDMTOOLS START Name=DEF#1|IOmlSpecifications KEEP=NO
  public DEF (final IOmlSpecifications spec) throws CGException {

    vdm_init_DEF();
    ExpandSpec((IOmlSpecifications) spec);
  }
// ***** VDMTOOLS END Name=DEF#1|IOmlSpecifications


// ***** VDMTOOLS START Name=ExpandSpec#1|IOmlSpecifications KEEP=NO
  public void ExpandSpec (final IOmlSpecifications spec) throws CGException {

    Vector sq_2 = null;
    sq_2 = spec.getClassList();
    IOmlClass cl = null;
    for (Iterator enm_30 = sq_2.iterator(); enm_30.hasNext(); ) {

      IOmlClass elem_3 = (IOmlClass) enm_30.next();
      cl = (IOmlClass) elem_3;
      {

        String id = null;
        id = cl.getIdentifier();
        Vector vdm_super = null;
        Boolean cond_10 = null;
        cond_10 = cl.hasInheritanceClause();
        if (cond_10.booleanValue()) {

          IOmlInheritanceClause obj_11 = null;
          obj_11 = (IOmlInheritanceClause) cl.getInheritanceClause();
          vdm_super = obj_11.getIdentifierList();
        }
        else 
          vdm_super = new Vector();
        Vector body = null;
        body = cl.getClassBody();
        {

          valm.put(id, ExpandValueMap(body));
          HashMap rhs_17 = new HashMap();
          HashMap modmap_18 = new HashMap();
          modmap_18 = ExpandRecTypeDefs(id, body);
          rhs_17 = new HashMap(recdefs);
          rhs_17.putAll(modmap_18);
          recdefs = (HashMap) UTIL.clone(rhs_17);
          HashSet rhs_23 = new HashSet();
          HashSet set_25 = new HashSet();
          Enumeration enm_26 = vdm_super.elements();
          while ( enm_26.hasMoreElements())
            set_25.add(enm_26.nextElement());
          rhs_23 = set_25;
          inherit.put(id, rhs_23);
        }
      }
    }
  }
// ***** VDMTOOLS END Name=ExpandSpec#1|IOmlSpecifications


// ***** VDMTOOLS START Name=ExpandValueMap#1|Vector KEEP=NO
  private HashMap ExpandValueMap (final Vector body_ul) throws CGException {

    HashMap v_um = new HashMap();
    {

      IOmlDefinitionBlock body = null;
      for (Iterator enm_14 = body_ul.iterator(); enm_14.hasNext(); ) {

        IOmlDefinitionBlock elem_3 = (IOmlDefinitionBlock) enm_14.next();
        body = (IOmlDefinitionBlock) elem_3;
        if (new Boolean(body instanceof OmlValueDefinitions).booleanValue()) {

          HashMap rhs_8 = new HashMap();
          HashMap modmap_9 = new HashMap();
          modmap_9 = ExpandValueDef((IOmlValueDefinitions) body);
          rhs_8 = new HashMap(v_um);
          rhs_8.putAll(modmap_9);
          v_um = (HashMap) UTIL.clone(rhs_8);
        }
      }
    }
    return v_um;
  }
// ***** VDMTOOLS END Name=ExpandValueMap#1|Vector


// ***** VDMTOOLS START Name=ExpandValueDef#1|IOmlValueDefinitions KEEP=NO
  private HashMap ExpandValueDef (final IOmlValueDefinitions body) throws CGException {

    HashMap v_um = new HashMap();
    {

      Vector sq_2 = null;
      sq_2 = body.getValueList();
      IOmlValueDefinition vdef = null;
      for (Iterator enm_19 = sq_2.iterator(); enm_19.hasNext(); ) {

        IOmlValueDefinition elem_3 = (IOmlValueDefinition) enm_19.next();
        vdef = (IOmlValueDefinition) elem_3;
        {

          IOmlValueShape shape = null;
          shape = (IOmlValueShape) vdef.getShape();
          IOmlPattern pat = null;
          pat = (IOmlPattern) shape.getPattern();
          IOmlExpression tmpVal_9 = null;
          tmpVal_9 = (IOmlExpression) shape.getExpression();
          IOmlExpression expr = null;
          expr = (IOmlExpression) tmpVal_9;
          if (new Boolean(pat instanceof OmlPatternIdentifier).booleanValue()) {

            HashMap rhs_12 = new HashMap();
            HashMap modmap_13 = new HashMap();
            modmap_13 = MatchPatId2Expr((IOmlPatternIdentifier) pat, (IOmlExpression) expr);
            rhs_12 = new HashMap(v_um);
            rhs_12.putAll(modmap_13);
            v_um = (HashMap) UTIL.clone(rhs_12);
          }
          else {

            UTIL.RunTime("Run-Time Error:Can not evaluate an error statement");
            return new HashMap();
          }
        }
      }
    }
    return v_um;
  }
// ***** VDMTOOLS END Name=ExpandValueDef#1|IOmlValueDefinitions


// ***** VDMTOOLS START Name=ExpandRecTypeDefs#2|String|Vector KEEP=NO
  private HashMap ExpandRecTypeDefs (final String clnm, final Vector body_ul) throws CGException {

    HashMap r_um = new HashMap();
    {

      IOmlDefinitionBlock body = null;
      for (Iterator enm_16 = body_ul.iterator(); enm_16.hasNext(); ) {

        IOmlDefinitionBlock elem_4 = (IOmlDefinitionBlock) enm_16.next();
        body = (IOmlDefinitionBlock) elem_4;
        if (new Boolean(body instanceof OmlTypeDefinitions).booleanValue()) {

          HashMap rhs_9 = new HashMap();
          HashMap modmap_10 = new HashMap();
          modmap_10 = ExpandTypeDefs(clnm, (IOmlTypeDefinitions) body);
          rhs_9 = new HashMap(r_um);
          rhs_9.putAll(modmap_10);
          r_um = (HashMap) UTIL.clone(rhs_9);
        }
      }
    }
    return r_um;
  }
// ***** VDMTOOLS END Name=ExpandRecTypeDefs#2|String|Vector


// ***** VDMTOOLS START Name=ExpandTypeDefs#2|String|IOmlTypeDefinitions KEEP=NO
  private HashMap ExpandTypeDefs (final String clnm, final IOmlTypeDefinitions body) throws CGException {

    HashMap r_um = new HashMap();
    {

      Vector sq_3 = null;
      sq_3 = body.getTypeList();
      IOmlTypeDefinition tdef = null;
      for (Iterator enm_18 = sq_3.iterator(); enm_18.hasNext(); ) {

        IOmlTypeDefinition elem_4 = (IOmlTypeDefinition) enm_18.next();
        tdef = (IOmlTypeDefinition) elem_4;
        {

          IOmlTypeShape shape = null;
          shape = (IOmlTypeShape) tdef.getShape();
          if (new Boolean(shape instanceof OmlComplexType).booleanValue()) {

            HashMap rhs_11 = new HashMap();
            HashMap modmap_12 = new HashMap();
            modmap_12 = ExpandComplexTypeDef(clnm, (IOmlComplexType) shape);
            rhs_11 = new HashMap(r_um);
            rhs_11.putAll(modmap_12);
            r_um = (HashMap) UTIL.clone(rhs_11);
          }
        }
      }
    }
    return r_um;
  }
// ***** VDMTOOLS END Name=ExpandTypeDefs#2|String|IOmlTypeDefinitions


// ***** VDMTOOLS START Name=ExpandComplexTypeDef#2|String|IOmlComplexType KEEP=NO
  private HashMap ExpandComplexTypeDef (final String clnm, final IOmlComplexType shape) throws CGException {

    String id = null;
    id = shape.getIdentifier();
    Vector f_ul = null;
    f_ul = shape.getFieldList();
    HashMap rexpr_6 = new HashMap();
    Name tmpVar1_7 = null;
    tmpVar1_7 = new Name(clnm, id);
    HashMap tmpVar2_10 = new HashMap();
    HashMap res_m_11 = new HashMap();
    {

      HashSet e_set_18 = new HashSet();
      HashSet riseq_20 = new HashSet();
      int max_21 = f_ul.size();
      for (int i_22 = 1; i_22 <= max_21; i_22++) 
        riseq_20.add(new Long(i_22));
      e_set_18 = riseq_20;
      Long i = null;
      {
        for (Iterator enm_24 = e_set_18.iterator(); enm_24.hasNext(); ) {

          Long elem_23 = UTIL.NumberToLong(enm_24.next());
          i = elem_23;
          String md_12 = null;
          IOmlField obj_13 = null;
          if ((1 <= i.intValue()) && (i.intValue() <= f_ul.size())) 
            obj_13 = (IOmlField) f_ul.get(i.intValue() - 1);
          else 
            UTIL.RunTime("Run-Time Error:Illegal index");
          md_12 = obj_13.getIdentifier();
          res_m_11.put(md_12, i);
        }
      }
    }
    tmpVar2_10 = res_m_11;
    rexpr_6 = new HashMap();
    rexpr_6.put(tmpVar1_7, tmpVar2_10);
    return rexpr_6;
  }
// ***** VDMTOOLS END Name=ExpandComplexTypeDef#2|String|IOmlComplexType


// ***** VDMTOOLS START Name=LookUp#2|String|String KEEP=NO
  public IOmlExpression LookUp (final String clnm, final String defnm) throws CGException {

    Boolean cond_3 = null;
    cond_3 = new Boolean(valm.containsKey(clnm));
    if (cond_3.booleanValue()) {

      HashMap vm = (HashMap) valm.get(clnm);
      Boolean cond_10 = null;
      cond_10 = new Boolean(vm.containsKey(defnm));
      if (cond_10.booleanValue()) 
        return (IOmlExpression) (IOmlExpression) vm.get(defnm);
      else {

        Boolean cond_13 = null;
        cond_13 = new Boolean(inherit.containsKey(clnm));
        if (cond_13.booleanValue()) {

          HashSet supers = (HashSet) inherit.get(clnm);
          {

            {

              String sup = null;
              for (Iterator enm_38 = supers.iterator(); enm_38.hasNext(); ) {

                String elem_21 = UTIL.ConvertToString(enm_38.next());
                sup = elem_21;
                {

                  Boolean cond_24 = null;
                  Boolean var1_25 = null;
                  var1_25 = new Boolean(valm.containsKey(sup));
                  {
                    if ((cond_24 = var1_25).booleanValue()) {

                      Boolean var2_28 = null;
                      var2_28 = new Boolean(((HashMap) valm.get(sup)).containsKey(defnm));
                      cond_24 = var2_28;
                    }
                  }
                  if (cond_24.booleanValue()) 
                    return (IOmlExpression) (IOmlExpression) ((HashMap) valm.get(sup)).get(defnm);
                }
              }
            }
            UTIL.RunTime("Run-Time Error:Can not evaluate an error statement");
            return new OmlExpression();
          }
        }
        else {

          UTIL.RunTime("Run-Time Error:Can not evaluate an error statement");
          return new OmlExpression();
        }
      }
    }
    else {

      UTIL.RunTime("Run-Time Error:Can not evaluate an error statement");
      return new OmlExpression();
    }
  }
// ***** VDMTOOLS END Name=LookUp#2|String|String


// ***** VDMTOOLS START Name=LookUpRecSel#3|String|IOmlFieldSelect KEEP=NO
  public SEM.VAL LookUpRecSel (final SEM.REC recval, final String selid, final IOmlFieldSelect expr) throws CGException {

    Name tag = null;
    tag = (recval).tag;
    Boolean cond_7 = null;
    cond_7 = new Boolean(recdefs.containsKey(tag));
    if (cond_7.booleanValue()) {

      HashMap sel_um = (HashMap) recdefs.get(tag);
      Boolean cond_19 = null;
      cond_19 = new Boolean(sel_um.containsKey(selid));
      if (cond_19.booleanValue()) {

        Long num = UTIL.NumberToLong(sel_um.get(selid));
        SEM.VAL rexpr_31 = null;
        Vector tmp_l_32 = null;
        tmp_l_32 = (recval).v;
        if ((1 <= num.intValue()) && (num.intValue() <= tmp_l_32.size())) 
          rexpr_31 = (SEM.VAL) tmp_l_32.get(num.intValue() - 1);
        else 
          UTIL.RunTime("Run-Time Error:Illegal index");
        return (SEM.VAL) rexpr_31;
      }
      else {

        RTERR.ReportError(expr, RTERR.RECORD_uFIELD_uID_uUNKNOWN);
        SEM.NUM rexpr_25 = null;
        rexpr_25 = new SEM.NUM(UTIL.NumberToReal(new Long(1)));
        return (SEM.VAL) rexpr_25;
      }
    }
    else {

      RTERR.ReportError(expr, RTERR.RECORD_uFIELD_uID_uUNKNOWN);
      SEM.NUM rexpr_13 = null;
      rexpr_13 = new SEM.NUM(UTIL.NumberToReal(new Long(1)));
      return (SEM.VAL) rexpr_13;
    }
  }
// ***** VDMTOOLS END Name=LookUpRecSel#3|String|IOmlFieldSelect


// ***** VDMTOOLS START Name=UpdateRecVal#4|String|IOmlExpression KEEP=NO
  public SEM.REC UpdateRecVal (final SEM.REC recval, final String selid, final SEM.VAL val, final IOmlExpression expr) throws CGException {

    Name tag = null;
    tag = (recval).tag;
    Boolean cond_8 = null;
    cond_8 = new Boolean(recdefs.containsKey(tag));
    if (cond_8.booleanValue()) {

      HashMap sel_um = (HashMap) recdefs.get(tag);
      Boolean cond_23 = null;
      cond_23 = new Boolean(sel_um.containsKey(selid));
      if (cond_23.booleanValue()) {

        Long num = UTIL.NumberToLong(sel_um.get(selid));
        SEM.REC rexpr_38 = null;
        SEM.REC tmpRE_40 = (SEM.REC) UTIL.clone(recval);
        Vector val_42 = null;
        Vector seqmap_45 = null;
        seqmap_45 = (recval).v;
        HashMap modmap_43 = new HashMap();
        modmap_43 = new HashMap();
        modmap_43.put(num, val);
        val_42 = (Vector) UTIL.clone(seqmap_45);
        for (Iterator enm_52 = modmap_43.keySet().iterator(); enm_52.hasNext(); ) {

          Object dom_53 = enm_52.next();
          Long edom_49 = UTIL.NumberToLong(dom_53);
          SEM.VAL erng_50 = (SEM.VAL) modmap_43.get(dom_53);
          if (edom_49.intValue() > val_42.size() || edom_49.intValue() < 1) 
            UTIL.RunTime("Run-Time Error:Illegal index in sequence modifier");
          else 
            val_42.set(edom_49.intValue() - 1, erng_50);
        }
        ((SEM.REC) tmpRE_40).v = val_42;
        rexpr_38 = tmpRE_40;
        return rexpr_38;
      }
      else {

        RTERR.ReportError(expr, RTERR.RECORD_uFIELD_uID_uUNKNOWN);
        SEM.REC rexpr_29 = null;
        Name tmpVar_30 = null;
        tmpVar_30 = new Name(new String(""), new String(""));
        rexpr_29 = new SEM.REC(tmpVar_30, new Vector());
        return rexpr_29;
      }
    }
    else {

      RTERR.ReportError(expr, RTERR.RECORD_uFIELD_uID_uUNKNOWN);
      SEM.REC rexpr_14 = null;
      Name tmpVar_15 = null;
      tmpVar_15 = new Name(new String(""), new String(""));
      rexpr_14 = new SEM.REC(tmpVar_15, new Vector());
      return rexpr_14;
    }
  }
// ***** VDMTOOLS END Name=UpdateRecVal#4|String|IOmlExpression


// ***** VDMTOOLS START Name=MatchPatId2Expr#2|IOmlPatternIdentifier|IOmlExpression KEEP=NO
  static private HashMap MatchPatId2Expr (final IOmlPatternIdentifier patid, final IOmlExpression expr) throws CGException {

    HashMap varRes_3 = new HashMap();
    {

      String id = null;
      id = patid.getIdentifier();
      varRes_3 = new HashMap();
      varRes_3.put(id, expr);
    }
    return varRes_3;
  }
// ***** VDMTOOLS END Name=MatchPatId2Expr#2|IOmlPatternIdentifier|IOmlExpression

}
;

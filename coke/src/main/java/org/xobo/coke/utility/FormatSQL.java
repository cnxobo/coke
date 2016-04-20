package org.xobo.coke.utility;

import org.hibernate.jdbc.util.BasicFormatterImpl;

public class FormatSQL {
  // BuildMyString.com generated code. Please enjoy your string responsibly.

  static String sb = "SELECT" +
      "   T.COMPANY_CODE," +
      "   T.ORG_CHINESE_NAME," +
      "   T.BILL_YM," +
      "   T.BILL_CODE," +
      "   T.arr_amount," +
      "   T.AMOUNT_TOTAL," +
      "   (T.AMOUNT_TOTAL - T.arr_amount) as AMOUNT_REALITY," +
      "   T.AMOUNT_VERIFY," +
      "   (T.AMOUNT_TOTAL - T.AMOUNT_VERIFY) as AMOUNT_UNVERIFY," +
      "   (T.AMOUNT_TOTAL - T.arr_amount - T.AMOUNT_VERIFY) as  AMOUNT_REALITY_UNVERIFY," +
      "   T.AMOUNT_ADJ," +
      "   T.r_CS," +
      "   T.BILL_TEMPLATE_NAME" +
      "FROM " +
      "(SELECT" +
      "  a.PAYER_ID," +
      "  a.BILL_CODE," +
      "   t3.COMPANY_CODE," +
      "   t3.ORG_CHINESE_NAME," +
      "  a.BILL_YM," +
      "   a.TOTAL_AGENT_AMOUNT + a.TOTAL_NOTAGENT_AMOUNT - a.AMOUNT_ADJ AMOUNT_TOTAL," +
      "   ifnull(t1.AMOUNT_VERIFY, 0) AMOUNT_VERIFY," +
      "   ifnull(t1.AMOUNT_ADJ, 0) AMOUNT_ADJ," +
      "   a.TOTAL_AGENT_AMOUNT + a.TOTAL_NOTAGENT_AMOUNT - a.AMOUNT_ADJ - ifnull(t1.AMOUNT_VERIFY, 0) arr_amount,"
      +
      "  t2.yewuyuan as r_CS," +
      "  t4.BILL_TEMPLATE_NAME" +
      "FROM" +
      "   EF_ARAP a" +
      "LEFT JOIN (" +
      "   SELECT" +
      "       v2.BILL_ID," +
      "       sum(IFNULL(v2.AMOUNT_VERIFY, 0)) AMOUNT_VERIFY," +
      "       sum(IFNULL(v2.AMOUNT_ADJ, 0)) AMOUNT_ADJ," +
      "       v2.AMOUNT_BILL" +
      "   FROM" +
      "       EF_RECEIP_VERIFY v1" +
      "   INNER JOIN EF_RECEIP_VERIFY_BILL_DETAIL v2 ON v1.VERIFY_ID = v2.VERIFY_ID" +
      "   AND v2.IS_DELETED = 0 " +
      "   INNER JOIN EF_ARAP v3 ON v2.BILL_ID = v3.BILL_ID" +
      "   AND v3.ORG_ID = :orgId" +
      "   AND v3.PAYEE_ID = :orgId" +
      " [and v3.PAYER_ID = :payerId]" +
      " [and v3.BILL_YM>= :billYmSt] " +
      " [and v3.BILL_YM<= :billYmEd] " +
      "   WHERE" +
      "       v1.`STATUS` = 'E' and v1.IS_DELETED=0" +
      "   GROUP BY" +
      "       v2.BILL_ID" +
      ") t1 ON a.BILL_ID = t1.BILL_ID" +
      "AND ifnull(t1.AMOUNT_BILL,0) > ifnull(t1.AMOUNT_VERIFY,0) - ifnull(t1.AMOUNT_ADJ,0)" +
      "INNER JOIN (select s.SEND_PROVIDER_ID,GROUP_CONCAT(DISTINCT u.CNAME) as yewuyuan" +
      "from BD_SMALL_CONTRACT s,ORG_USER u where s.IS_DELETED=0 and s.RECEIVE_PROVIDER_ID= :orgId "
      +
      "and s.RECEIVE_CS=u.USER_ID" +
      "[and s.SEND_PROVIDER_ID= :payerId]" +
      "[and s.RECEIVE_CS= :receiveCs]" +
      "and EXISTS (" +
      "                   SELECT" +
      "                       'x'" +
      "                   FROM" +
      "                       BD_CONTRACT_BUSINESSMAN cbm," +
      "                       V_USER_SCOPE scp" +
      "                   WHERE" +
      "                       s.S_CONTRACT_ID = cbm.S_CONTRACT_ID" +
      "                   AND cbm.BUSINESSMAN_ID = scp.USERID2" +
      "                   AND cbm.START_DATE <= SYSDATE()" +
      "                   AND (" +
      "                       cbm.END_DATE IS NULL" +
      "                       OR cbm.END_DATE >= SYSDATE()" +
      "                   )" +
      "                   AND scp.USERID1 = :userId" +
      "               )" +
      "GROUP BY s.SEND_PROVIDER_ID ) t2 on a.PAYER_ID=t2.SEND_PROVIDER_ID" +
      "INNER JOIN V_COMPANY t3 on a.PAYER_ID=t3.ORG_ID and t3.COMPANY_CREATE_ORG_ID=:orgId and t3.IS_GROUP=0"
      +
      "LEFT JOIN BD_BILL_TEMPLATE t4 on a.BILL_TEMPLATE_ID=t4.BILL_TEMPLATE_ID" +
      "WHERE" +
      "   a.PAYEE_ID = :orgId" +
      "AND a.ORG_ID = :orgId" +
      "AND a.IS_DELETED = 0" +
      "AND a.STATUS_VERIFY < 3 and a.STATUS_BILL='V'" +
      "[and a.PAYER_ID = :payerId]" +
      "[and a.BILL_YM>=:billYmSt] " +
      "[and a.BILL_YM<=:billYmEd]" +
      ") T" +
      "where T.AMOUNT_TOTAL >0" +
      "[and T.arr_amount>=:arrAmountSt]" +
      "[and T.arr_amount<=:arrAmountEd]" +
      "ORDER BY T.COMPANY_CODE";



  public static void main(String[] args) {
    BasicFormatterImpl basicFormatterImpl = new BasicFormatterImpl();
    System.out.println(sb);
    String result = basicFormatterImpl.format(sb);
    System.out.println(result);
  }

}

--CREATE SEQUENCE seq$test_external_oper NOCACHE;
DECLARE
  l_res     varchar2(100);
  l_msg     varchar2(1000);
  l_trans_type          varchar2(100) := 'PAYBERRY';
  l_id_oper NUMBER;
  l_id_trans            varchar2(100); 
  receiver_type         varchar2(50);
  l_revert_trans        varchar2(100);
  l_amount              NUMBER := 110200;
  
  -- При проверке 1537-1539
  l_pay_element_id      varchar2(50) := '1539';
  l_receiver            varchar2(100) := '9900990010014';
  -- При проверке 1540
  --l_pay_element_id      varchar2(50) := '1539';
  --l_receiver            varchar2(100) := '+380966152691';
  
  l_provider_id         varchar2(10) := '576'; -- ИД провайдера, выданный Пэйберри
  reg_date  DATE;
  output_field1_name    varchar2(50); 
  output_field1_value   varchar2(250); 
  output_field2_name    varchar2(50); 
  output_field2_value   varchar2(250);
  output_field3_name    varchar2(50);
  output_field3_value   varchar2(250);
  output_field4_name    varchar2(50);
  output_field4_value   varchar2(250);
  output_field5_name    varchar2(50);
  output_field5_value   varchar2(250);
BEGIN
  SELECT seq$test_external_oper.NEXTVAL INTO l_id_trans FROM dual;
  SELECT seq$test_external_oper.NEXTVAL INTO l_revert_trans FROM dual;
  
  l_res := pack$external_payment.check_card(
    P_CD_TRANS_PAY_TYPE=>l_trans_type, 
    P_RECEIVER=>l_receiver, 
    P_AMOUNT_EXTERNAL_OPER=>l_amount,
    P_PAY_ELEMENT_ID=>l_pay_element_id, 
    P_PROVIDER_ID=>l_provider_id, 
    P_TERMINAL_ID=>11, 
    P_TERMINAL_TRANSACTION_ID=>NULL, 
    P_INPUT_FIELD1_NAME=>NULL, 
    P_INPUT_FIELD1_VALUE=>NULL, 
    P_INPUT_FIELD2_NAME=>NULL, 
    P_INPUT_FIELD2_VALUE=>NULL,  
    P_INPUT_FIELD3_NAME=>NULL, 
    P_INPUT_FIELD3_VALUE=>NULL,  
    P_INPUT_FIELD4_NAME=>NULL, 
    P_INPUT_FIELD4_VALUE=>NULL,  
    P_INPUT_FIELD5_NAME=>NULL,  
    P_INPUT_FIELD5_VALUE=>NULL, 
    P_RECEIVER_TYPE=>receiver_type, 
    P_OUTPUT_FIELD1_NAME=>output_field1_name, 
    P_OUTPUT_FIELD1_VALUE=>output_field1_value, 
    P_OUTPUT_FIELD2_NAME=>output_field2_name, 
    P_OUTPUT_FIELD2_VALUE=>output_field2_value, 
    P_OUTPUT_FIELD3_NAME=>output_field3_name, 
    P_OUTPUT_FIELD3_VALUE=>output_field3_value, 
    P_OUTPUT_FIELD4_NAME=>output_field4_name, 
    P_OUTPUT_FIELD4_VALUE=>output_field4_value, 
    P_OUTPUT_FIELD5_NAME=>output_field5_name, 
    P_OUTPUT_FIELD5_VALUE=>output_field5_value, 
    P_RESULT_MESSAGE=>l_msg
  );
  dbms_output.put_line('check1: l_res='||l_res||', receiver_type='||receiver_type||', field1_name='||output_field1_name||', field1_value='||output_field1_value||', l_msg='||l_msg);
  
  l_res := pack$external_payment.check_card(
    P_CD_TRANS_PAY_TYPE=>l_trans_type, 
    P_RECEIVER=>l_receiver, 
    P_AMOUNT_EXTERNAL_OPER=>l_amount,
    P_PAY_ELEMENT_ID=>l_pay_element_id, 
    P_PROVIDER_ID=>l_provider_id, 
    P_TERMINAL_ID=>11, 
    P_TERMINAL_TRANSACTION_ID=>NULL, 
    P_INPUT_FIELD1_NAME=>'SMS_CODE', 
    P_INPUT_FIELD1_VALUE=>'12345', 
    P_INPUT_FIELD2_NAME=>NULL, 
    P_INPUT_FIELD2_VALUE=>NULL,  
    P_INPUT_FIELD3_NAME=>NULL, 
    P_INPUT_FIELD3_VALUE=>NULL,  
    P_INPUT_FIELD4_NAME=>NULL, 
    P_INPUT_FIELD4_VALUE=>NULL,  
    P_INPUT_FIELD5_NAME=>NULL,  
    P_INPUT_FIELD5_VALUE=>NULL, 
    P_RECEIVER_TYPE=>receiver_type, 
    P_OUTPUT_FIELD1_NAME=>output_field1_name, 
    P_OUTPUT_FIELD1_VALUE=>output_field1_value, 
    P_OUTPUT_FIELD2_NAME=>output_field2_name, 
    P_OUTPUT_FIELD2_VALUE=>output_field2_value, 
    P_OUTPUT_FIELD3_NAME=>output_field3_name, 
    P_OUTPUT_FIELD3_VALUE=>output_field3_value, 
    P_OUTPUT_FIELD4_NAME=>output_field4_name, 
    P_OUTPUT_FIELD4_VALUE=>output_field4_value, 
    P_OUTPUT_FIELD5_NAME=>output_field5_name, 
    P_OUTPUT_FIELD5_VALUE=>output_field5_value, 
    P_RESULT_MESSAGE=>l_msg
  );
  dbms_output.put_line('check2: l_res='||l_res||', receiver_type='||receiver_type||', field1_name='||output_field1_name||', field1_value='||output_field1_value||', l_msg='||l_msg);  

  
  l_res := pack$external_payment.check_card(
    P_CD_TRANS_PAY_TYPE=>l_trans_type, 
    P_RECEIVER=>l_receiver, 
    P_AMOUNT_EXTERNAL_OPER=>l_amount,
    P_PAY_ELEMENT_ID=>l_pay_element_id, 
    P_PROVIDER_ID=>l_provider_id, 
    P_TERMINAL_ID=>11, 
    P_TERMINAL_TRANSACTION_ID=>NULL, 
    P_INPUT_FIELD1_NAME=>'SMS_CODE', 
    P_INPUT_FIELD1_VALUE=>'12345', 
    P_INPUT_FIELD2_NAME=>NULL, 
    P_INPUT_FIELD2_VALUE=>NULL,  
    P_INPUT_FIELD3_NAME=>NULL, 
    P_INPUT_FIELD3_VALUE=>NULL,  
    P_INPUT_FIELD4_NAME=>NULL, 
    P_INPUT_FIELD4_VALUE=>NULL,  
    P_INPUT_FIELD5_NAME=>NULL,  
    P_INPUT_FIELD5_VALUE=>NULL, 
    P_RECEIVER_TYPE=>receiver_type, 
    P_OUTPUT_FIELD1_NAME=>output_field1_name, 
    P_OUTPUT_FIELD1_VALUE=>output_field1_value, 
    P_OUTPUT_FIELD2_NAME=>output_field2_name, 
    P_OUTPUT_FIELD2_VALUE=>output_field2_value, 
    P_OUTPUT_FIELD3_NAME=>output_field3_name, 
    P_OUTPUT_FIELD3_VALUE=>output_field3_value, 
    P_OUTPUT_FIELD4_NAME=>output_field4_name, 
    P_OUTPUT_FIELD4_VALUE=>output_field4_value, 
    P_OUTPUT_FIELD5_NAME=>output_field5_name, 
    P_OUTPUT_FIELD5_VALUE=>output_field5_value, 
    P_RESULT_MESSAGE=>l_msg
  );
  dbms_output.put_line('check3: l_res='||l_res||', receiver_type='||receiver_type||', field1_name='||output_field1_name||', field1_value='||output_field1_value||', l_msg='||l_msg);  
  --RETURN;
  
  l_res := pack$external_payment.add_external_oper(
    P_CD_TRANS_PAY_TYPE=>l_trans_type, 
    P_ID_EXTERNAL_POINT_TRANS=>l_id_trans, 
    P_CHEQUE_NUMBER=>'1234', 
    P_PAYER=>NULL, 
    P_RECEIVER=>l_receiver, 
    P_DATE_EXTERNAL_OPER=>SYSDATE, 
    P_AMOUNT_EXTERNAL_OPER=>l_amount, 
    P_CD_CURRENCY=>643, 
    P_DESC_EXTERNAL_OPER=>NULL, 
    P_STATE_EXTERNAL_OPER=>'SUCCESS', 
    P_ID_EXTERNAL_OPER_FILE=>NULL, 
    P_PAY_ELEMENT_ID=>l_pay_element_id, 
    P_PROVIDER_ID=>l_provider_id, 
    P_TERMINAL_ID=>11, 
    P_TERMINAL_TRANSACTION_ID=>NULL, 
    P_INPUT_FIELD1_NAME=>'SMS_CODE', 
    P_INPUT_FIELD1_VALUE=>'12345', 
    P_INPUT_FIELD2_NAME=>NULL, 
    P_INPUT_FIELD2_VALUE=>NULL,  
    P_INPUT_FIELD3_NAME=>NULL, 
    P_INPUT_FIELD3_VALUE=>NULL,  
    P_INPUT_FIELD4_NAME=>NULL, 
    P_INPUT_FIELD4_VALUE=>NULL,  
    P_INPUT_FIELD5_NAME=>NULL,  
    P_INPUT_FIELD5_VALUE=>NULL, 
    P_ID_EXTERNAL_OPER=>l_id_oper, 
    P_RED_DATE_EXTERNAL_OPER=>reg_date, 
    P_OUTPUT_FIELD1_NAME=>output_field1_name, 
    P_OUTPUT_FIELD1_VALUE=>output_field1_value, 
    P_OUTPUT_FIELD2_NAME=>output_field2_name, 
    P_OUTPUT_FIELD2_VALUE=>output_field2_value, 
    P_OUTPUT_FIELD3_NAME=>output_field3_name, 
    P_OUTPUT_FIELD3_VALUE=>output_field3_value, 
    P_OUTPUT_FIELD4_NAME=>output_field4_name, 
    P_OUTPUT_FIELD4_VALUE=>output_field4_value, 
    P_OUTPUT_FIELD5_NAME=>output_field5_name, 
    P_OUTPUT_FIELD5_VALUE=>output_field5_value, 
    P_RESULT_MESSAGE=>l_msg
  );
  dbms_output.put_line('payment: l_res='||l_res||', l_id_oper='||l_id_oper||', field1_name='||output_field1_name||', field1_value="'||output_field1_value||'", field2_name='||output_field2_name||', field2_value="'||output_field2_value||'", l_msg='||l_msg);
  
  --RETURN;
  
  IF l_res = '0' THEN 
    l_res := pack$external_payment.revert_external_oper(
      P_ID_EXTERNAL_POINT_TRANS=>l_revert_trans, 
      P_ID_REVERT_TRANS=>l_id_trans, 
      P_REVERT_DATE=>SYSDATE, 
      P_RECEIVER=>l_receiver, 
      P_AMOUNT_EXTERNAL_OPER=>l_amount, 
      P_CD_CURRENCY=>643, 
      P_ID_EXTERNAL_OPER=>l_id_oper, 
      P_RED_REVERT_DATE=>reg_date, 
      P_RESULT_MESSAGE=>l_msg
    );
    dbms_output.put_line('revert: l_res='||l_res||', l_id_oper='||l_id_oper||', l_msg='||l_msg);
  END IF;
END;

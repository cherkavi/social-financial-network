DECLARE
    -- Значения, возвращаемые функциями
    C_OPERATION_OK              VARCHAR2(1)       := '0';
    C_OPERATION_ERROR           VARCHAR2(1)       := '1';
    C_NEED_END_INFO              VARCHAR2(10)     := '20000';
    C_MEMBERSHIP_FEE_ERROR       VARCHAR2(10)     := '20001';
    C_ISNT_ENOUGH_POINTS         VARCHAR2(10)     := '20002';
    C_CARD_NOT_GIVEN             VARCHAR2(10)     := '20003';
    C_CARD_NOT_QUESTIONED        VARCHAR2(10)     := '20004';
    C_CARD_NOT_ACTIVATED         VARCHAR2(10)     := '20005';
    C_NEED_PIN                   VARCHAR2(10)     := '20006';
    C_NEED_SMS_CONFIRMATION      VARCHAR2(10)     := '20007';
    C_NEED_ACTIVATION_CODE       VARCHAR2(10)     := '20008';
    C_ENOUGH_MEANS               VARCHAR2(10)     := '20009';
    C_NEED_MANY_FEE              VARCHAR2(10)     := '20010';
    C_SMS_CONFIRM_CREATED        VARCHAR2(10)     := '20011';
    C_CONFIRM_RETURN             VARCHAR2(10)     := '20099';
  p_id_term                     VARCHAR2(50) := '20001'; -- ИД терминала  
  p_cd_card1                    VARCHAR2(40) := '9900990010014'; -- Номер карты
  p_oper_type                   VARCHAR2(50) := 'PAYMENT';  -- Тип операции (разрещенные значения)
                                                            -- 'PAYMENT' - оплата за товар
                                                            -- 'POINT_FEE' - пополнение баллов
                                                            -- 'SHARE_FEE' - паевой взнос
                                                            -- 'MEMBERSHIP_FEE' - членский взнос
                                                            -- 'MEMBERSHIP_TARGET_FEE' - членский целевой взнос
  p_date_format                 VARCHAR2(50) := 'DD.MM.RRRR'; -- Формат даты
  p_id_card_status              VARCHAR2(10); -- ИД вида карты (не используется)  
  p_membership_month_sum        VARCHAR2(50); -- Сумма ежемесячного членского взноса (в рублях с копейками)  
  p_membership_last_date        VARCHAR2(50); -- Дата, до которой оплачен членский взнос  
  p_membership_nopay_month      VARCHAR2(50); -- Количество неоплаченных месяцев членского взноса  
  p_membership_need_pay_sum     VARCHAR2(50); -- Сумма членского взноса, которую необходимо оплатить  
  p_membership_max_pay_month    VARCHAR2(50); -- Максимальное количество месяцев, за которые можно оплатить членский взнос  
  p_membership_cd_currency      VARCHAR2(50); -- Валюта, в которой принимается членский взнос  
  p_membership_fee_margin       VARCHAR2(50); -- Комиссия партнера за прием членского взноса 
  p_promo_code                  VARCHAR2(50) :=''; -- Промо-код
  p_replenish_kind              VARCHAR2(50) := '';  -- Дополнительная операция
                                                        -- 'SHARE_FEE' - паевой взнос
                                                        -- 'GET_FROM_SHARE_ACCOUNT' - списание с паевого фонда   
  p_pay_type_primary            VARCHAR2(50) := 'CASH';  -- Способ оплаты:
                                                           -- 'CASH' - наличными
                                                           -- 'BANK_CARD' - банковской картой
                                                           -- 'SMPU_CARD' - картой СМПУ
                                                           -- 'INVOICE' - выставляется счет
   p_bank_trn                     VARCHAR2 (50) := ''; -- Номер чека при оплате банковской картой   
   p_pay_total                    VARCHAR2 (50) := ''; -- Общая сумма оплаты (в рублях с копейками)   
   p_share_account_sum            VARCHAR2 (50) := ''; -- Сумма паевого взноса (в рублях с копейками)  
   p_share_fee_margin             VARCHAR2 (50) := ''; -- Комиссия на прием паевого взноса (в рублях с копейками)   
   p_pay_sum                      VARCHAR2 (50) := '100.00'; -- Сумма оплаты за товар (в рублях с копейками)   
   p_percent_point                VARCHAR2 (50) := ''; -- % начисленных баллов   
   p_sum_point                    VARCHAR2 (50) := ''; -- Сумма начисленных баллов   
   p_entered_sum                  VARCHAR2 (50) := '150.00'; -- Внесенная сумма при оплате наличными (в рублях с копейками)   
   p_sum_change                   VARCHAR2 (50) := '50.00'; -- Сумма сдачи при оплате наличными (в рублях с копейками)   
   p_change_to_share_account      VARCHAR2 (50) := 'Y'; -- 'Y' - зачислить сдачу в паевой фонд   
   p_can_use_share_account        VARCHAR2 (50) := 'Y'; -- 'Y' - разрешено использовать паевой фонд при оплате картой СМПУ
   p_id_telgr                     VARCHAR2 (50); -- ИД сформированной операции   
   p_sum_share_fee_need           VARCHAR2 (50); -- Сумма, которую необходимо внести в паевой фонд при недостаточном количестве баллов (в рублях с копейками)   
   p_share_fee_need_margin        VARCHAR2 (50); -- Комиссия за прием паевого взноса (в рублях с копейками)   
   p_change_margin                VARCHAR2 (50); -- Количество за зачисление наличных в паевой фонд (в рублях с копейками)   
   p_total_margin                 VARCHAR2 (50); -- Общая сумма комиссии (в рублях с копейками)   
   p_calc_point_total             VARCHAR2 (50); -- Сумма начисленных баллов всего   
   p_calc_point_shareholder       VARCHAR2 (50); -- Сумма начисленных баллов пайщику   
   p_sum_put_to_share_account     VARCHAR2 (50); -- Зачислено в паевой фонд (в рублях с копейками)   
   p_sum_get_from_share_account   VARCHAR2 (50); -- Списано с паевого фонда (в рублях с копейками)   
   p_phone_mobile                 VARCHAR2 (50); -- Моб. телефон пайщика, на который отправлен СМС с кодом подтверждения   
   p_can_send_pin_in_sms          VARCHAR2 (50); -- 'Y' - на моб.телефон пайщика отправлен СМС с кодом
   p_confirmation_type            VARCHAR2 (50) := 'NONE'; -- Способ подтверждения
   -- 'NONE' - в p_confirmation_code не передается дополнительный параметр (если предыдущая операция вернула C_NEED_END_INFO)
   -- 'PIN' - в p_confirmation_code передан ПИН (если предыдущая операция вернула C_NEED_SMS_CONFIRMATION или C_SMS_CONFIRM_CREATED)
   -- 'SMS' - в p_confirmation_code передан код, полученный из СМС (если предыдущая операция вернула C_NEED_PIN)
   -- 'ACTIVATION_CODE' - в p_confirmation_code передан код активации (если предыдущая операция вернула C_NEED_ACTIVATION_CODE)
   p_confirmation_code            VARCHAR2 (50) := ''; -- Код подтверждения
   p_payment_description          VARCHAR2 (50) := ''; -- (необязательный) Назначение платежа   
   p_result_message               VARCHAR2 (4000);  -- Исходящее сообщение   
   l_result                       VARCHAR2 (50); -- Результат работы функции   
   l_error          EXCEPTION;
   l_normal_end     EXCEPTION;
BEGIN
  l_result := pack$ws_ui.oper_check_card(
        -- Входящие параметры
        p_id_term
       ,p_cd_card1
       ,p_oper_type
       ,p_date_format
       -- Исходящие параметры
       ,p_id_card_status
       ,p_membership_month_sum
       ,p_membership_last_date
       ,p_membership_nopay_month
       ,p_membership_need_pay_sum
       ,p_membership_max_pay_month
       ,p_membership_cd_currency
       ,p_membership_fee_margin
       ,p_result_message
  );  
  DBMS_OUTPUT.put_line ('oper_check_card: l_result=' || l_result || ', p_result_message=' || p_result_message);  
  -- Если ошибка - выходим
  IF NOT (l_result IN (C_OPERATION_OK, C_MEMBERSHIP_FEE_ERROR)) THEN 
    RAISE l_error;
  END IF;  
  l_result :=
      pack$ws_ui.payment_for_goods (
      -- Входящие параметры
      p_id_term,
      p_cd_card1,
      p_promo_code,
      p_replenish_kind,
      p_pay_type_primary,
      p_bank_trn,
      p_pay_total,
      p_share_account_sum,
      p_share_fee_margin,
      p_pay_sum,
      p_percent_point,
      p_sum_point,
      p_entered_sum,
      p_sum_change,
      p_membership_need_pay_sum,
      p_membership_fee_margin,
      p_change_to_share_account,
      p_can_use_share_account,
      -- Исходящие параметры
      p_id_telgr,
      p_sum_share_fee_need,
      p_share_fee_need_margin,
      p_change_margin,
      p_total_margin,
      p_calc_point_total,
      p_calc_point_shareholder,
      p_sum_put_to_share_account,
      p_sum_get_from_share_account,
      p_phone_mobile,
      p_can_send_pin_in_sms,
      p_result_message
   );  
  DBMS_OUTPUT.put_line ('payment_for_goods: l_result=' || l_result || ', p_result_message=' || p_result_message);  
  -- Если ошибка - выходим
  IF NOT (l_result IN (C_OPERATION_OK, C_NEED_END_INFO)) THEN 
    RAISE l_error;
  ELSE 
    -- Операция завершена успешно, подтверждения не требуется
    IF l_result = C_OPERATION_OK THEN
      RAISE l_normal_end;
    END IF;
  END IF;  
  -- Сюда еще нужно добавить обработчик событий подтвержден кодом активации, СМС и ПИН - но это потом
  IF l_result = C_NEED_END_INFO THEN 
    l_result :=
      pack$ws_ui.oper_confirm (
      -- Входящие параметры
      p_id_telgr,
      p_confirmation_type,
      p_confirmation_code,
      p_payment_description,
      -- Исходящие параметры
      p_phone_mobile,
      p_can_send_pin_in_sms,
      p_result_message
   );
   DBMS_OUTPUT.put_line ('oper_confirm: l_result=' || l_result || ', p_result_message=' || p_result_message);
  END IF;
EXCEPTION
  WHEN l_error THEN
    DBMS_OUTPUT.put_line ('Wrong result');
  WHEN l_normal_end THEN 
    DBMS_OUTPUT.put_line ('Success result');
END;

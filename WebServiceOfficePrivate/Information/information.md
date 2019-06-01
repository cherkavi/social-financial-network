		Работа с договорами:

1. Авторизация договора
Уже реализовано в личном кабинете

2. Получение информации по договору
Уже реализовано в личном кабинете

3. Обновление информации по договору
Уже реализовано в личном кабинете

4. Получение перечня кошельков бон-карты
Запрос:
SELECT id_club_card_purse, number_club_card_purse, 
       cd_club_card_purse_type, name_club_card_purse_type,
       cd_currency, name_currency, sname_currency,
       value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE card_serial_number = '...' -- Вместо ... подставить серийный номер карты
   AND id_issuer = ... -- Вместо ... подставить ИД эмитента карты
   AND id_payment_system = ... -- Вместо ... подставить ИД платежной системы карты
 ORDER BY number_club_card_purse
Пример:
SELECT id_club_card_purse, number_club_card_purse, 
       cd_club_card_purse_type, name_club_card_purse_type,
       cd_currency, name_currency, sname_currency,
       value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE card_serial_number = '9DFF579878161755'
   AND id_issuer = 346
   AND id_payment_system = 1
 ORDER BY number_club_card_purse;



		Работа со счетом типа Боны:

1. Получение баланса
Для получения баланса нужно использовать поля функции PACK_UI_OFFICE.check_nat_prs
(эта функция вызывается при проверке пользователя по имени бон-карты):
- p_bal_cur OUT NUMBER                       /** Доступно бонов на карте (копейки) */
- p_bal_acc OUT NUMBER                       /** Накоплено бонов на карте (копейки) */
- p_bal_bon_per OUT VARCHAR2                 /** Накоплено бонов за период (копейки) */
- p_bal_disc_per OUT VARCHAR2                /** Накоплено скидки (копейки) */
- p_next_date_mov OUT DATE                   /** Следующая дата перевода бонов в доступные */
- p_next_date_calc OUT DATE                  /** Следующая дата перевода категорий */

2. Пополнение баланса с платежных сервисов
ПОКА НЕ РЕАЛИЗОВАНО

3. Вывод истории пополнения баланса с платежных сервисов
ПОКА НЕ РЕАЛИЗОВАНО

4. Получение информации по покупкам - 
Уже реализовано в личном кабинете в виде показа транзакций по бон-карте

5. Получение информации по товарам
1. Получение информации по коду бон-карты
Запрос:
SELECT id_furshet_purchase, card_serial_number, id_issuer,
       id_payment_system, id_trans, id_term, s_area_id,
       cashid, check_s_num, count_purch, sum_check, purch_num,
       purch_datetime, purch_refund, purch_text, purch_group,
       purch_barc, purch_price, purch_count, measure,
       purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,
       sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr
  FROM bc_reports.v_fuhshet_purchases_all
 WHERE card_serial_number = '...' -- Вместо ... подставить серийный номер карты
   AND id_issuer = ... -- Вместо ... подставить ИД эмитента карты
   AND id_payment_system = ... -- Вместо ... подставить ИД платежной системы карты
 ORDER BY purch_datetime, purch_text;
Пример:
SELECT id_furshet_purchase, card_serial_number, id_issuer,
       id_payment_system, id_trans, id_term, s_area_id,
       cashid, check_s_num, count_purch, sum_check, purch_num,
       purch_datetime, purch_refund, purch_text, purch_group,
       purch_barc, purch_price, purch_count, measure,
       purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,
       sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr
  FROM bc_reports.v_fuhshet_purchases_all
 WHERE card_serial_number = '9DFF579878161755'
   AND id_issuer = 346
   AND id_payment_system = 1
 ORDER BY purch_datetime, purch_text;
2. Получение информации по ИД транзакции
SELECT id_furshet_purchase, card_serial_number, id_issuer,
       id_payment_system, id_trans, id_term, s_area_id,
       cashid, check_s_num, count_purch, sum_check, purch_num,
       purch_datetime, purch_refund, purch_text, purch_group,
       purch_barc, purch_price, purch_count, measure,
       purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,
       sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr
  FROM bc_reports.v_fuhshet_purchases_all
 WHERE id_trans = '...' -- Вместо ... подставить ИД транзакции
ORDER BY purch_datetime, purch_text;
Пример:
SELECT id_furshet_purchase, card_serial_number, id_issuer,
       id_payment_system, id_trans, id_term, s_area_id,
       cashid, check_s_num, count_purch, sum_check, purch_num,
       purch_datetime, purch_refund, purch_text, purch_group,
       purch_barc, purch_price, purch_count, measure,
       purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,
       sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr
  FROM bc_reports.v_fuhshet_purchases_all
 WHERE id_trans = 14024602
 ORDER BY purch_datetime, purch_text;

	Работа со счетом типа Супербоны:

1. Получение баланса
ИД кошелька получаем в запросе на перечень кошельков
Запрос:
SELECT cd_currency, name_currency, sname_currency, value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE id_club_card_purse = ... -- Вместо ... вставить ИД кошелька
 ORDER BY number_club_card_purse;
Пример:
SELECT cd_currency, name_currency, sname_currency, value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE id_club_card_purse = 14
 ORDER BY number_club_card_purse;

2. Получение информации по истории пополнения и расходах
ИД кошелька получаем в запросе на перечень кошельков
Запрос:
SELECT id_oper, source_oper, type_oper, type_oper_tsl,
       basis_for_oper, state_oper, state_oper_tsl,
       cd_currency, name_currency,
       sname_currency, oper_date, oper_amount
  FROM bc_office.vc_club_card_purse_oper_all a
 WHERE id_club_card_purse = ... -- Вместо ... вставить ИД кошелька
 ORDER BY oper_date;
Пример:
SELECT id_oper, source_oper, type_oper, type_oper_tsl,
       basis_for_oper, state_oper, state_oper_tsl,
       cd_currency, name_currency,
       sname_currency, oper_date, oper_amount
  FROM bc_office.vc_club_card_purse_oper_all a
 WHERE id_club_card_purse = 4
 ORDER BY oper_date

3. Получение информации по подаркам
Запрос:
SELECT id_nat_prs_gift, cd_nat_prs_gift_state, name_nat_prs_gift_state,
       is_club_event_gift, id_club_event_gift, id_club_event,
       name_club_event, id_gift, cd_gift, name_gift,
       cd_currency, sname_currency, cost_gift, cost_gift_frmt,
       date_reserve, date_reserve_frmt, id_nat_prs_gift_request,
       basis_for_gift, date_given, date_given_frmt, id_lg_gift,
       desc_lg_gift, cd_lg_currency, sname_lg_currency,
       cost_lg_gift, cost_lg_gift_frmt, id_gifts_given_place,
       write_off_goods_action, card_serial_number, card_id_issuer,
       card_id_payment_system, id_club_card_purse, date_returned,
       date_returned_frmt, reason_return, date_canceled,
       date_canceled_frmt, reason_cancel, write_off_amount,
       write_off_amount_frmt, id_club
  FROM bc_office.vc_nat_prs_gifts_all
 WHERE id_club_card_purse = ... -- Вместо ... вставить ИД кошелька
 ORDER BY date_reserve;
Пример:
SELECT id_nat_prs_gift, cd_nat_prs_gift_state, name_nat_prs_gift_state,
       is_club_event_gift, id_club_event_gift, id_club_event,
       name_club_event, id_gift, cd_gift, name_gift,
       cd_currency, sname_currency, cost_gift, cost_gift_frmt,
       date_reserve, date_reserve_frmt, id_nat_prs_gift_request,
       basis_for_gift, date_given, date_given_frmt, id_lg_gift,
       desc_lg_gift, cd_lg_currency, sname_lg_currency,
       cost_lg_gift, cost_lg_gift_frmt, id_gifts_given_place,
       write_off_goods_action, card_serial_number, card_id_issuer,
       card_id_payment_system, id_club_card_purse, date_returned,
       date_returned_frmt, reason_return, date_canceled,
       date_canceled_frmt, reason_cancel, write_off_amount,
       write_off_amount_frmt, id_club
  FROM bc_office.vc_nat_prs_gifts_all
 WHERE id_club_card_purse = 14
 ORDER BY date_reserve;

4. Передача заказа подарка
Пакет PACK_UI_OFFICE

/*
   Функція додає заявку на подарунок з особистого кабінету
   Історія змін:
   Розробник    Дата       Опис
   Кравчук В.В. 28.03.2012 Функцію створено
*/
FUNCTION add_office_gift_request(
     p_id_nat_prs IN VARCHAR2         -- ИД Клиента
    ,p_id_club_card_purse IN VARCHAR2  -- ИД Кошелька
    ,p_id_club_event_gift IN VARCHAR2    -- ИД подарка клубной акции
    ,p_id_nat_prs_gift_request OUT NUMBER        -- ИД сформированной заявки на подарок
    ,p_result_msg OUT VARCHAR2      -- Результат добавления
) RETURN VARCHAR2 –- Возвращаемые значения: 0 – заявку добавлено, не 0 – ошибка, содержимое ошибки в p_result_msg

Пример:
DECLARE
  p_res varchar2(10);
  p_id_nat_prs_gift_request NUMBER;
  p_result_msg VARCHAR2(1000);  
BEGIN
  p_res := bc_office.pack_ui_office.add_office_gift_request(
      718336
     ,8
     ,76
     ,p_id_nat_prs_gift_request
     ,p_result_msg);
END;


		Работа со счетом типа Ibon

1. Получение баланса
ИД кошелька получаем в запросе на перечень кошельков
Запрос:
SELECT cd_currency, name_currency, sname_currency, value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE id_club_card_purse = ... -- Вместо ... вставить ИД кошелька
 ORDER BY number_club_card_purse;
Пример:
SELECT cd_currency, name_currency, sname_currency, value_club_card_purse
  FROM bc_office.vc_club_card_purse_all
 WHERE id_club_card_purse = 14
 ORDER BY number_club_card_purse;

2. Получение информации по истории пополнения и расходах
ИД кошелька получаем в запросе на перечень кошельков
Запрос:
SELECT id_oper, source_oper, type_oper, type_oper_tsl,
       basis_for_oper, state_oper, state_oper_tsl,
       cd_currency, name_currency,
       sname_currency, oper_date, oper_amount
  FROM bc_office.vc_club_card_purse_oper_all a
 WHERE id_club_card_purse = ... -- Вместо ... вставить ИД кошелька
 ORDER BY oper_date;
Пример:
SELECT id_oper, source_oper, type_oper, type_oper_tsl,
       basis_for_oper, state_oper, state_oper_tsl,
       cd_currency, name_currency,
       sname_currency, oper_date, oper_amount
  FROM bc_office.vc_club_card_purse_oper_all a
 WHERE id_club_card_purse = 4
 ORDER BY oper_date

3. Пополнение с платежных сервисов
ПОКА НЕ РЕАЛИЗОВАНО

4. Пополнени с бонов
ПОКА НЕ РЕАЛИЗОВАНО

5. Вывод в боны
ПОКА НЕ РЕАЛИЗОВАНО

6. Покупка товаров и услуг за Ibon
ПОКА НЕ РЕАЛИЗОВАНО


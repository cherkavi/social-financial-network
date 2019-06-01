select distinct type from all_source;
select * from user_constraints;
select * from all_source where name = 'PACK_BC_SMS_SEND' and type = 'PACKAGE' order by line;
select * from all_source where name = 'PACK_BC_SMS' and type = 'PACKAGE' order by line;

select * 
from d_ds_sms
order by id_sms_message desc;


delete from d_ds_sms where id_sms_message>769;
commit;

select * from bc_admin.VC_DS_SMS_PROFILE_ALL 
select * from bc_admin.VC_DS_PROFILE_STATE_ALL
select * from bc_admin.VC_DS_SMS_ACTION_ALL  
select * from bc_admin.VC_DS_SMS_DELIV_STATUS
select * from bc_admin.VC_DS_SMS_NEW_ALL

VC_DS_SMS_SEND_STATUS

select distinct CD_SMS_MESSAGE_TYPE from d_ds_sms

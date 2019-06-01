select * from all_source where name = 'PACK_BC_SMS' and type = 'PACKAGE' order by line;
select * from all_source where name = 'PACK_BC_SMS_SEND' and type = 'PACKAGE' order by line;

-- status GOES
select * from bc_admin.VC_DS_SMS_NEW_ALL;

-- all statuses
select * from bc_admin.VC_DS_SMS_SEND_STATUS;
select * from bc_admin.VC_DS_SMS_DELIV_STATUS;

-- mess
select * from D_DS_SMS;
select * from bc_admin.vc_ds_sms_new_all;
select * from bc_admin.VC_DS_PROFILE_STATE_ALL;
select * from bc_admin.VC_DS_SMS_ACTION_ALL

select * from bc_admin.VC_DS_SMS_DELIV_STATUS;

select * from bc_admin.VC_DS_SMS_PROFILE_ALL;
select * from bc_admin.VC_DS_SMS_SEND_STATUS;

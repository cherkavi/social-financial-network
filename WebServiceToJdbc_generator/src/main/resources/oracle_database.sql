select a.object_name,a.argument_name,a.position,a.sequence,a.data_type,a.in_out, a.*
from all_arguments a 
where 1=1 
and upper(object_name)=upper('CHECK_NAT_PRS')
and upper(owner)=upper('BC_ADMIN')
order by a.position


select * from all_source 
where owner='BC_ADMIN'
and name 
-- where name='CHECK_NAT_PRS'
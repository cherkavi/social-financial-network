package bonclub.office_private.web_gui.user_area.panel_messenger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.Connector;
import bonclub.office_private.database.wrap.Message_Recieve;
import bonclub.office_private.database.wrap.Message_Send;
import bonclub.office_private.database.wrap.Message_Text;
import bonclub.office_private.database.wrap.Reference_Recipient;
import bonclub.office_private.database.wrap.Users;

public class MessengerUtility {

	/** вывести отладочную информацию */
	private void out_debug(String value){
		System.out.print("Utility");
		System.out.print(" DEBUG ");
		System.out.println(value);
	}
	/** вывести информацию об ошибке */
	private void out_error(String value){
		System.out.print("Utility");
		System.out.print(" ERROR ");
		System.out.println(value);
	}
	
	private ConnectUtility connector;
	
	public MessengerUtility(OfficePrivateApplication application){
		this.connector=this.getConnectUtility(application);
	}
	
	@Override
	public void finalize(){
		try{
			this.connector.close();
		}catch(Exception ex){
		}
	}
	
	public void close(){
		this.connector.close();
	}
	
	private ConnectUtility getConnectUtility(OfficePrivateApplication application){
		Connector connector=application.getHibernateConnection();
		return new ConnectUtility(connector);
	}
	
	
	
	/** получить список всех получателей сообщения по его уникальному коду */
	@SuppressWarnings("unchecked")
	public List<Users> getUsersByMessageSend(int message_send_id){
		out_debug("getUsersByMessageSend: "+message_send_id);
		List<Users> return_value=null;
		Session session=connector.getSession();
		out_debug("getUsersByMessageSend: Session:"+session);
		try{
			List list=session.createSQLQuery("select {u.*} " +
											 "from reference_recipient " +
											 "inner join users {u} on u.id=reference_recipient.id_recipient " +
											 "where reference_recipient.id_message_send="+message_send_id
											 ).addEntity("u",Users.class).list();
			out_debug("getUsersByMessageSend: list is getted");
			return_value=(List<Users>)list;
		}catch(Exception ex){
			out_error("getUsersByMessageSend:"+ex.getMessage());
		}finally{
		}
		if(return_value==null){
			return_value=new ArrayList<Users>();
		};
		return return_value;
	}
	
	
	/** получить список пользователей согласно имени списка */
	@SuppressWarnings("unchecked")
	public List<Users> filterUserList(String nick, String full_name, String short_name){
		List<Users> return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			Criteria criteria=session.createCriteria(Users.class);
			if( (nick!=null)&&(nick.length()>0) ){
				criteria.add(Restrictions.like("nick", "%"+nick+"%"));
			}
			if( (full_name!=null)&&(full_name.length()>0) ){
				criteria.add(Restrictions.like("surname", "%"+full_name+"%"));
			}
			if( (short_name!=null)&&(short_name.length()>0) ){
				criteria.add(Restrictions.like("name", "%"+short_name+"%"));
			}
			return_value=(List<Users>)criteria.list();
		}catch(Exception ex){
			out_error("filterUserList: Exception:"+ex.getMessage());
		}finally{
		}
		if(return_value==null){
			return_value=new ArrayList<Users>();
		};
		return return_value;
	}
	
	/** послать письмо всем получателям, которые есть в списке 
	 * @param message_text_value - текст сообщения 
	 * @param user_source_id - уникальный идентификатор пользователя, который отправляет сообщение 
	 * @param recipient список получателей данного сообщения
	 * @return
	 */
	public boolean sendMessage(String message_text_value, 
									  int user_source_id, 
									  List<Users> recipient){
		boolean return_value=false;
		if((message_text_value!=null)&&(recipient!=null)&&(recipient.size()>0)){
			Session session=null;
			Transaction transaction=null;
			try{
				session=connector.getSession();
				out_debug("sendMessage: "+session);
				transaction=session.beginTransaction();
				/** дата передачи/приема сообщения */
				Date date_send=new Date();
				// TODO алгоритм посылки сообщения
				out_debug("sendMessage: создать запись в MESSAGE_TEXT");
				Message_Text message_text=new Message_Text(message_text_value);
				session.save(message_text);
				if(message_text.getId()<=0){
					throw new Exception(" Message_Text Error ");
				}
				
				out_debug("sendMessage: создать запись в MESSAGE_SEND");
				Message_Send message_send=new Message_Send();
				message_send.setDate_create(date_send);
				message_send.setId_message_text(message_text.getId());
				message_send.setId_sender(user_source_id);
				session.save(message_send);
				if(message_send.getId()<=0){
					throw new Exception(" Message_Send Error ");
				}
				
				out_debug("sendMessage: создать запись в REFERENCE_RECIPIENT и в MESSAGE_RECEIVE ");
				Reference_Recipient[] reference_recipient=new Reference_Recipient[recipient.size()];
				Message_Recieve[] message_receive=new Message_Recieve[recipient.size()];
				for(int counter=0;counter<recipient.size();counter++){
					out_debug("sendMessage: создать запись в REFERENCE_RECIPIENT:"+counter);
					reference_recipient[counter]=new Reference_Recipient();
					reference_recipient[counter].setId_message_send(message_send.getId());
					reference_recipient[counter].setId_recipient(recipient.get(counter).getKod());
					session.save(reference_recipient[counter]);
					if(reference_recipient[counter].getId()<=0){
						throw new Exception("  Reference_Recipient not Save:"+counter);
					}
					out_debug("sendMessage: создать запись в MESSAGE_RECIEVE:"+counter);
					message_receive[counter]=new Message_Recieve();
					message_receive[counter].setDate_recieve(date_send);
					message_receive[counter].setFlag(0);
					message_receive[counter].setId_message_text(message_text.getId());
					message_receive[counter].setId_user(recipient.get(counter).getKod());
					message_receive[counter].setId_user_sender(message_send.getId_sender());
					session.save(message_receive[counter]);
					if(message_receive[counter].getId()<=0){
						throw new Exception("  Message_Receive not Save:"+counter);
					}
					
					//session.flush();
				}
				transaction.commit();
				return_value=true;
			}catch(Exception ex){
				transaction.rollback();
				out_error("sendMessage: "+ex.getMessage());
			}finally{
			}
		}else{
			// ошибка во время проверки обязательных параметров 
		}
		return return_value;
	}
	
	/** получить список исходящих сообщений для данного пользователя */
	@SuppressWarnings("unchecked")
	public List<Message_Send> getOutputMessage(int user_id){
		out_debug("getOutputMessage Id:"+user_id);
		List<Message_Send> return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("getOutputMessage: "+session);
			List list=session.createCriteria(Message_Send.class)
			                                              .add(Restrictions.eq("id_sender", new Integer(user_id)))
			                                              .list();
			out_debug("getOutputMessage list is getted");
			return_value=(List<Message_Send>)list;			
		}catch(Exception ex){
			out_debug("getOutputMessage: "+ex.getMessage());
		}finally{
		}
		return return_value;
	}
	
	/** получить список входящих сообщений по данному пользователю */
	@SuppressWarnings("unchecked")
	public List<Message_Recieve> getInputMessage(int user_id){
		out_debug("getInputMessage, User ID:"+user_id);
		List<Message_Recieve> return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("getInputMessage: Session:"+session);
			return_value=(List<Message_Recieve>)session.createCriteria(Message_Recieve.class)
													              .add(Restrictions.eq("id_user", new Integer(user_id))).list();
		}catch(Exception ex){
			out_debug("getInputMessage: "+ex.getMessage());
		}finally{
		}
		return return_value;
	}
	
	/** удалить сообщение из списка принятых/полученных сообщений (из таблицы Message_Recieve)
	 * @param id_message_recieve - уникальный идентификатор сообщения 
	 * @return возвращает true, если сообщение успешно удалено 
	 * */
	public boolean removeMessageRecieve(int id_message_recieve){
		boolean return_value=false;
		out_debug("removeMessageRecieve: begin");
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("removeMessageRecieve:"+session);
			Transaction transaction=session.beginTransaction();
			Message_Recieve message_recieve=(Message_Recieve)session.get(Message_Recieve.class, 
																		 new Integer(id_message_recieve)
			                                                             );
			session.delete(message_recieve);
			transaction.commit();
			return_value=true;
		}catch(Exception ex){
			// не удалось получить запись из таблицы Message_Recieve
		}finally{
		}
		return return_value;
	}
	
	/** удалить сообщение из списка посланных/отправленных сообщений (Message_Send, Reference_Reciept)*/
	@SuppressWarnings("unchecked")
	public boolean removeMessageSend(int id_message_send){
		boolean return_value=false;
		out_debug("removeMessageSend: begin");
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("removeMesssageSend: "+session);
			Transaction transaction=session.beginTransaction();
			Message_Send message_send=(Message_Send)session.get(Message_Send.class, new Integer(id_message_send));
			List<Reference_Recipient> reference_recipient_list=session.createCriteria(Reference_Recipient.class).add(Restrictions.eq("id_message_send", new Integer(id_message_send))).list();
			for(int counter=0;counter<reference_recipient_list.size();counter++){
				session.delete(reference_recipient_list.get(counter));
			}
			session.delete(message_send);
			transaction.commit();
			return_value=true;
		}catch(Exception ex){
			// не удалось получить запись из таблицы Message_Send
		}finally{
		}
		return return_value;
	}
	
	/** получить текст входящего сообщения
	 * @param message_id - уникальный идентификатор входящего сообщения 
	 */
	public String getInputMessageText(int message_id){
		out_debug("getInputMessageText:"+message_id);
		String return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("getInputMessageText Session:"+session);
			SQLQuery query=session.createSQLQuery("select message_text.text_value from message_recieve "+
							    "inner join message_text on message_text.id=message_recieve.id_message_text "+
							    "where message_recieve.id=:message_id");
			query.setParameter("message_id", new Integer(message_id));
			out_debug("getInputMessageText: getList");
			List<?> list=query.list();
			out_debug("getInputMessageText: getValue");
			return_value=list.get(0).toString();
		}catch(Exception ex){
			out_error("getInputMessageText: "+ex.getMessage());
		}finally{
		}
		return (return_value==null)?"":return_value;
	}
	
	/** получить текст исходящего сообщения
	 * @param message_id - уникальный идентификатор входящего сообщения 
	 */
	public String getOutputMessageText(int message_id){
		out_debug("getOutputMessageText:"+message_id);
		String return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			out_debug("getOutputMessageText: Session:"+session);
			SQLQuery query=session.createSQLQuery("select message_text.text_value from message_send "+
											 	  "inner join message_text on message_text.id=message_send.id_message_text "+
											 	  "where message_send.id=:message_id");
			out_debug("getOutputMessageText: "+query);
			query.setParameter("message_id", new Integer(message_id));
			out_debug("getOutputMessageText: getList ");
			List<?> list=query.list();
			out_debug("getOutputMessageText: getValue ");
			return_value=list.get(0).toString();
		}catch(Exception ex){
			out_error("getOutputMessageText: "+ex.getMessage());
		}finally{
		}
		return (return_value==null)?"":return_value;
	}
	
	/** получить отправителя входящего сообщения сообщения 
	 * @param id_message_input - уникальный идентификатор входящего сообщения
	 * */
	@SuppressWarnings({"unchecked"})
	public String getMessageInputSender(int id_message_input){
		out_debug("getMessageInputSender>>> id:"+id_message_input);
		String return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			SQLQuery query=session.createSQLQuery("select {u.*} "+
												  "from message_recieve "+
												  "inner join users {u} on message_recieve.id_user_sender=u.id "+
												  "where message_recieve.id=:message_recieve");
			query.setParameter("message_recieve", new Integer(id_message_input));
			query.addEntity("u",Users.class);
			List<Users> list=(List<Users>)query.list();
			return_value=list.get(0).getNick();
		}catch(Exception ex){
			out_error("getMessageInputSender Exception:"+ex.getMessage());
		}finally{
		}
		return (return_value==null)?"":return_value;
	}
	
	/** получить получателей исходящего сообщения
	 * @param id_message_output - уникальный идентификатор исходящего сообщения
	 * @param delimeter - разделительная строка для получателей
	 * @return возвращается строка со всеми получателями данного сообщения  
	 */
	public  String getMessageOutputRecipient(int id_message_output,String delimeter){
		List<Users> list=getUsersByMessageSend(id_message_output);
		if((list!=null)&&(list.size()>0)){
			StringBuffer return_value=new StringBuffer();
			for(int counter=0;counter<list.size();counter++){
				return_value.append(list.get(counter).getNick());
				if(counter<(list.size()-1)){
					return_value.append(delimeter);
				}
			}
			return return_value.toString();
		}else{
			return "";
		}
	}
	

	/** получить данные (USERS) по текущему пользователю в виде database.wrap.Users 
	 * @param id_user - уникальный идентификатор для пользователя
	 * @return Users по текущему пользователю, либо null, если таковой не найден
	 * */
	public Users getUsers(int id_user){
		Users return_value=null;
		Session session=null;
		try{
			session=connector.getSession();
			return_value=(Users)session.get(Users.class, new Integer(id_user));
		}catch(Exception ex){
			out_error("getUsers: Exception:"+ex.getMessage());
		}finally{
		}
		return return_value;
	}
	
	/** сохранить Login и Password по текущему пользователю 
	 * @param id_user уникальный идентификатор пользователя 
	 * @param login - логин
	 * @param password - пароль
	 * */
	public boolean saveUsersLoginAndPassword(int id_user, Object login, Object password){
		out_debug("Login:"+login+"       Password:"+password);
		boolean return_value=false;
		Session session=null;
		try{
			session=connector.getSession();
			boolean need_update=false;
			Users current_user=getUsers(id_user);
			if(login!=null){
				current_user.setLogin((String)login);
				need_update=true;
			};
			if(password!=null){
				current_user.setPassword((String)password);
				need_update=true;
			}
			
			if(need_update==true){
				out_debug("current_user.getLogin:"+current_user.getLogin());
				session=connector.getSession();
				Transaction transaction=session.beginTransaction();
				session.update(current_user);
				transaction.commit();
				return_value=true;
			}else{
				return_value=true;
			}
		}catch(Exception ex){
			out_error("saveUsersLoginAndPassword:"+ex.getMessage());
			return_value=false;
		}finally{
		}
		return return_value;
	}
	
	/** получить данные из таблицы в виде ключ-значение 
	 * @param table_name - имя таблицы, из которой будут получены значения
	 * @param fields - поля, которые необходимо получить 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object[]> getAllRecordsFromTable(String table_name, String ... fields){
		List<Object[]> return_value=new ArrayList<Object[]>();
		Session session=null;
		try{
			session=connector.getSession();
			StringBuffer query=new StringBuffer();
			query.append("SELECT ");
			for(int counter=0;counter<fields.length;counter++){
				query.append(fields[counter]);
				if(counter!=(fields.length-1)){
					query.append(", ");
				}
			}
			query.append(" FROM "+table_name);
			return_value=(List<Object[]>)session.createSQLQuery(query.toString()).list();
		}catch(Exception ex){
			out_error("getAllRecordsFromTable: "+ex.getMessage());
		}finally{
		}
		return return_value;
	}
	
	/** получить в виде List(Object[]) данные из таблицы USER_DATA_INFORMATION_TYPE <br>
	 * <b> ID, NAME </b>
	 * */
	public List<Object[]> getDataInformationType(){
		List<Object[]> return_value=getAllRecordsFromTable("USER_DATA_INFORMATION_TYPE","ID","NAME");
		return (return_value==null)?(new ArrayList<Object[]>()):return_value;
	}
	
	/** получить в виде List(Object[]) данные из таблицы USER_DATA_GROUP */
	public List<Object[]> getDataGroup(){
		List<Object[]> return_value=getAllRecordsFromTable("USER_DATA_GROUP","ID","NAME");
		return (return_value==null)?(new ArrayList<Object[]>()):return_value;
	}
}

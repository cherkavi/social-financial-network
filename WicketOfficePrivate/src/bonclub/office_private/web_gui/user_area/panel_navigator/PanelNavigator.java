package bonclub.office_private.web_gui.user_area.panel_navigator;

import java.sql.ResultSet;

import org.apache.wicket.behavior.SimpleAttributeModifier;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.user_area.UserArea;

import wicket_utility.ActionExecutor;

/** панель, отображающая текущее действие пользователя (в каком именно меню он находится ) */
public class PanelNavigator extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	private final Model<String> modelPrivateData;
	private final Model<String> modelProfile;
	private final Model<String> modelPost;
	private final Model<String> modelBookkeeping;
	private final Model<String> modelClubEvent;
	private final Model<String> modelExit;
	private final String classMark;
	private final String classUnmark;
	private final Model<Boolean> modelPostVisible=new Model<Boolean>(new Boolean(Boolean.FALSE));
	private final Model<String> modelPostCreate;
	private final Model<String> modelPostInput;
	private final Model<String> modelPostOutput;
	private final Model<String> modelLabelPost;
	
	/** панель, отображающая текущее действие пользователя (в каком именно меню он находится ) 
	 * @param panelId - уникальный идентификатор панели 
	 * @param executor - исполнитель для передачи данных о выборе в родительские панели 
	 * @param classMark - класс, который маркирует выделенный раздел меню 
	 * @param classUnmark - класс, который маркирует не выделенный раздел меню 
	 */
	public PanelNavigator(String panelId, 
						  ActionExecutor executor,
						  String classMark,
						  String classUnmark){
		super(panelId);
		this.executor=executor;

		this.classMark=classMark;
		this.classUnmark=classUnmark;

		modelPrivateData=new Model<String>(this.classUnmark);
		modelProfile=new Model<String>(this.classUnmark);
		modelPost=new Model<String>(this.classUnmark);
		modelBookkeeping=new Model<String>(this.classUnmark);
		modelClubEvent=new Model<String>(this.classUnmark);
		modelExit=new Model<String>(this.classUnmark);
		modelPostCreate=new Model<String>(this.classUnmark);
		modelPostInput=new Model<String>(this.classUnmark);
		modelPostOutput=new Model<String>(this.classUnmark);
		modelLabelPost=new Model<String>("");
		initComponents();
	}
	
	private void initComponents(){
		// Основные данные
		this.add(new WebMarkupContainer("wrap_link_private_data"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelPrivateData.getObject()
													 )
						 );
			}
		}.add(new Link<Object>("link_private_data"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(false);
				sendToExecutor(UserArea.eventPrivateData);
			}
		}));
		
		// Профиль пользователя 
		this.add(new WebMarkupContainer("wrap_link_profile"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelProfile.getObject()
													 )
						 );
			}
		}.add(new Link<Object>("link_profile"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(false);
				sendToExecutor(UserArea.eventProfile);
			}
		}));
		
		// Клубная почта
		WebMarkupContainer post=new WebMarkupContainer("wrap_link_post"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelPost.getObject()
													 )
						 );
				int unreadCount=PanelNavigator.this.getCountOfUnreadMessage();
				if(unreadCount>0){
					modelLabelPost.setObject(PanelNavigator.this.getString("post")+" ("+unreadCount+")");
				}else{
					modelLabelPost.setObject(PanelNavigator.this.getString("post"));
				}
			}
		};
		Link<Object> linkPost=new Link<Object>("link_post"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(true);
				//PanelNavigator.this.get("wrap_link_post").render();
				sendToExecutor(UserArea.eventPost);
			}
			
			public boolean isVisible(){
				return !(PanelNavigator.this.modelPostVisible.getObject());
			}
		};
		post.add(linkPost);
		
		Label labelPost=new Label("label_post",modelLabelPost);
		linkPost.add(labelPost);
		
		
		// Клубная почта. Блок ссылок 
		WebMarkupContainer postBlock=new WebMarkupContainer("panel_post_menu"){
			private final static long serialVersionUID=1L; 
			@Override
			public boolean isVisible(){
				return PanelNavigator.this.modelPostVisible.getObject(); 
			}
		};
		// Клубная почта. Создать письмо  
		postBlock.add(new Link<Object>("link_post_create"){
			private final static long serialVersionUID=1L;
			public void onClick(){
				sendToExecutor(UserArea.eventPostCreate);
			}
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelPostCreate.getObject()
													 )
						 );
			}
		});
		// Клубная почта. Входящие  
		postBlock.add(new Link<Object>("link_post_input"){
			private final static long serialVersionUID=1L;
			public void onClick(){
				sendToExecutor(UserArea.eventPostInput);
			}
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelPostInput.getObject()
													 )
						 );
			}
		});
		// Клубная почта. Исходящие		
		postBlock.add(new Link<Object>("link_post_output"){
			private final static long serialVersionUID=1L;
			public void onClick(){
				sendToExecutor(UserArea.eventPostOutput);
			}
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelPostOutput.getObject()
													 )
						 );
			}
		});
		post.add(postBlock);
		this.add(post);
		
		// Бухгалтерия
		this.add(new WebMarkupContainer("wrap_link_bookkeeping"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelBookkeeping.getObject()
													 )
						 );
			}
		}.add(new Link<Object>("link_bookkeeping"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(false);
				sendToExecutor(UserArea.eventBookkeeping);
			}
		}));
		
		// Клубные акции
		this.add(new WebMarkupContainer("wrap_link_club_event"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelClubEvent.getObject()
													 )
						 );
			}
		}.add(new Link<Object>("link_club_event"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(false);
				sendToExecutor(UserArea.eventClubEvent);
			}
		}));

		// Выход
		this.add(new WebMarkupContainer("wrap_link_exit"){
			private final static long serialVersionUID=1L;
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.add(new SimpleAttributeModifier("class",
													 modelExit.getObject()
													 )
						 );
			}
		}.add(new Link<Object>("link_exit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				PanelNavigator.this.modelPostVisible.setObject(false);
				sendToExecutor(UserArea.eventExit);
			}
		}));
	}
	
	/** послать ключ в виде параметра ActionName на Executor */
	private void sendToExecutor(String key){
		this.executor.action(key, null);
	}
	
	/** установить выделение для 
	 * <table border=1>
	 * 	<tr>
	 * 		<td> 0 </td> <td> Вход в кабинет </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 1 </td> <td> Забыли пароль? </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 2 </td> <td> Регистрация </td> 
	 * 	</tr>
	 * 	<tr>
	 * 		<td> 3 </td> <td> О системе </td> 
	 * 	</tr>
	 * </table>
	 * */
	public void setSelected(int index){
		this.modelPrivateData.setObject(this.classUnmark);
		this.modelProfile.setObject(this.classUnmark);
		this.modelPost.setObject(this.classUnmark);
		this.modelBookkeeping.setObject(this.classUnmark);
		this.modelClubEvent.setObject(this.classUnmark);
		this.modelExit.setObject(this.classUnmark);
		this.modelPostCreate.setObject(this.classUnmark);
		this.modelPostInput.setObject(this.classUnmark);
		this.modelPostOutput.setObject(this.classUnmark);
		
		switch(index){
			case 0:  this.modelPrivateData.setObject(this.classMark);break;
			case 1:  this.modelProfile.setObject(this.classMark);break;
			//case 2:  this.modelPost.setObject(this.classMark);break;
			case 3:  this.modelBookkeeping.setObject(this.classMark);break;
			case 4:  this.modelClubEvent.setObject(this.classMark);break;
			case 5:  this.modelExit.setObject(this.classMark);break;

			case 20: this.modelPostCreate.setObject(this.classMark);break;
			case 21: this.modelPostInput.setObject(this.classMark);break;
			case 22: this.modelPostOutput.setObject(this.classMark);break;
			default: break;
		}
	}
	
	
	/** получить кол-во непрочитанных сообщений, которые есть у пользователя */
	private int getCountOfUnreadMessage(){
		int returnValue=0;
		ConnectUtility connector=((OfficePrivateApplication)this.getApplication()).getConnectUtility();
		Integer userId=((OfficePrivateSession)this.getSession()).getCustomerId();
		try{
			// получить кол-во не прочитанных сообщений по данному пользователю
			ResultSet rs=connector.getConnection().createStatement().executeQuery("select count(*) from message_recieve where id_user="+userId+" and flag=0");
			if(rs.next()){
				returnValue=rs.getInt(1);
			}
		}catch(Exception ex){
			System.err.println("PanelNavigator#getCountOfUnreadMessage: "+ex.getMessage());
		}finally{
			try{
				connector.close();
			}catch(Exception ex){};
		}
		
		
		return returnValue;
	}
	
	
}

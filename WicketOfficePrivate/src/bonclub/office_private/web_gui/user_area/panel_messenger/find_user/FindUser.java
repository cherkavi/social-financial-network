package bonclub.office_private.web_gui.user_area.panel_messenger.find_user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import wicket_utility.ActionExecutor;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.wrap.Users;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.session.UserList;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerUtility;
import bonclub.office_private.web_gui.user_area.panel_messenger.output.PanelSelect;

public class FindUser extends Panel{
	private final static long serialVersionUID=1L;
	
	private void out_debug(String information){
		System.out.print("FindUser ");
		System.out.print(" DEBUG: ");
		System.out.println(information);
	}
	
	private void out_error(String information){
		System.out.print("FindUser ");
		System.out.print(" ERROR: ");
		System.out.println(information);
	}
	
	private DataModel field_data_model=new DataModel();
	private List<Users> field_user_list=new ArrayList<Users>();
	private ActionExecutor executor;
	
	public List<Users> getUser_list(){
		return this.field_user_list;
	}
	public void setUser_list(List<Users> list){
		this.field_user_list=list;
	}
	
	/** Map, который содержит соответствия CheckBox и User - для получения выделения пользователей */
	//private HashMap<User,?> field_selected_property=new HashMap<User,?>(); 
	
	@SuppressWarnings("unchecked")
	public FindUser(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		TextField<String> field_find_nick=new TextField<String>("find_nick");
		TextField<String> field_find_full_name=new TextField<String>("find_full_name");
		TextField<String> field_find_short_name=new TextField<String>("find_short_name");

		SortableDataProvider provider=new SortableDataProvider(){
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<Users> iterator(int first, int count) {
				return FindUser.this.getUser_list().subList(first, first+count).iterator();
			}

			@Override
			public IModel model(Object row) {
				return new Model((Serializable)((Users)row));
			}

			@Override
			public int size() {
				return FindUser.this.getUser_list().size();
			}
		};
		
		// TODO: возможно вторая ссылка на класс
		IColumn[] columns=new IColumn[]{
				//new PropertyColumn(new Model("1:ID"),"id"),
				new AbstractColumn(new Model(this.getString("table.selected"))){ // "Selected"
					private static final long serialVersionUID = 1L;
					@Override
					public void populateItem(Item cellItem, String componentId, IModel rowModel) {
						Users users=(Users)rowModel.getObject();
						if(users.getSelected()==true){
							cellItem.add(new Label(componentId,"selected"));
						}else{
							cellItem.add(new PanelSelect(componentId,
														 new PropertyModel(users,"selected")));
						}
					}
				},
				new PropertyColumn(new Model(this.getString("table.nick")),"nick"),
				new PropertyColumn(new Model(this.getString("table.name_full")),"surname"),
				new PropertyColumn(new Model(this.getString("table.name_short")),"name")
		};
		DefaultDataTable user_table=new DefaultDataTable("user_table",columns,provider,3);
		//NoRecordsToolbar noRecords=new NoRecordsToolbar(user_table, new Model<String>(FindUser.this.getString("form_find.user_table.no-records-found")));
		//user_table.addBottomToolbar();
		//user_table.remove("toolbar");
		//user_table.add(noRecords);
		
		/** button find */
		Button button_find=new Button("button_find"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				on_button_find_click();
			}
		};
		button_find.add(new SimpleAttributeModifier("value",this.getString("button_find.caption")));
		/** button add */
		Button button_add=new Button("button_add"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_add_click();
			}
		};
		button_add.add(new SimpleAttributeModifier("value",this.getString("button_add.caption")));
		/** button cancel */
		Button button_cancel=new Button("button_cancel"){
			private static final long serialVersionUID = 1L;
			public void onSubmit(){
				on_button_cancel_click();
			}
		};
		button_cancel.add(new SimpleAttributeModifier("value",this.getString("button_cancel.caption")));
		
		Form<Object> form_find=new Form<Object>("form_find",
								new CompoundPropertyModel<Object>(field_data_model));
		
		form_find.add(field_find_nick);
		form_find.add(field_find_full_name);
		form_find.add(field_find_short_name);
		form_find.add(button_find);
		form_find.add(user_table);
		form_find.add(button_add);
		form_find.add(button_cancel);
		this.add(form_find);
	}
	
	/** реакция на нажатие клавиши Add */
	private void on_button_add_click(){
		out_debug("   button add click ");
		try{
			OfficePrivateSession session=(OfficePrivateSession)this.getSession();
			UserList list=session.getUserList("RECIPIENT");
			if(list==null){
				list=new UserList("RECIPIENT");
				session.addUserList(list);
			}
			try{
				for(int counter=0;counter<this.field_user_list.size();counter++){
					if(this.field_user_list.get(counter).getSelected()){
						list.addUser(this.field_user_list.get(counter));
					}
				}
			}catch(NullPointerException ex){
				out_error("on_button_add_click NullPointerException:"+ex.getMessage());
			}
			
		}catch(Exception ex){
			out_error("on_button_add_click Exception:"+ex.getMessage());
		}
		this.executor.action(UserArea.eventPostRecipientList, null);
	}
	
	/** реакция на нажатие клавиши Cancel */
	private void on_button_cancel_click(){
		this.executor.action(UserArea.eventPostRecipientList, null);
	}
	
	
	/** реакция на получение объектом ответа от формы */
	private void on_button_find_click(){
		if(this.field_data_model!=null){
			out_debug("Find Nick:"+field_data_model.getFind_nick());
			out_debug("Find Full Name:"+field_data_model.getFind_full_name());
			out_debug("Find Short Name:"+field_data_model.getFind_short_name());
			MessengerUtility utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
			this.field_user_list=utility.filterUserList( (field_data_model.getFind_nick()==null)?"":field_data_model.getFind_nick(), 
													    (field_data_model.getFind_full_name()==null)?"":field_data_model.getFind_full_name(), 
													    (field_data_model.getFind_short_name()==null)?"":field_data_model.getFind_short_name());
			utility.close();
		}else{
			out_debug("on_button_find_click: field_data_model is null");
		}
	}
}


/** модель данных для формы, которая содержит все события */
class DataModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private String find_nick;
	private String find_full_name;
	private String find_short_name;
	private String button_find;
	
	public String getButton_find() {
		return button_find;
	}

	public void setButton_find(String button_find) {
		this.button_find = button_find;
	}

	public String getFind_nick() {
		return find_nick;
	}

	public void setFind_nick(String find_nick) {
		this.find_nick = find_nick;
	}

	public String getFind_full_name() {
		return find_full_name;
	}

	public void setFind_full_name(String field_full_name) {
		this.find_full_name = field_full_name;
	}

	public String getFind_short_name() {
		return find_short_name;
	}

	public void setFind_short_name(String find_short_name) {
		this.find_short_name = find_short_name;
	}
	
}

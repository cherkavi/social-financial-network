package bonclub.office_private.web_gui.user_area.panel_messenger.output;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.ActionExecutor;

import bonclub.office_private.web_gui.user_area.UserArea;

/** панель для перехода на просмотр сообщения */
public class PanelLink extends Panel{
	private static final long serialVersionUID=1L;
	
	/** панель, которая содержит ссылку для перехода на отображение самого сообщения 
	 * @param id - уникальный Wicket:id
	 * @param caption - список пользователей
	 * @param id_message_send - уникальный идентификатор из базы данных
	 * @param is_message_send - true - is output message; false - is input message
	 */
	public PanelLink(final ActionExecutor executor,
			 final Panel panel_parent,
			 String id, 
			 String caption,
			 final Integer id_message_send,
			 final Boolean is_message_send){
		
		super(id);
		Link<Object> page_link=new Link<Object>("link"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				//ShowMessage show_message=new ShowMessage(return_page,id_message_send,is_message_send);
				//setResponsePage(show_message);
				executor.action(UserArea.eventPost, new Object[]{panel_parent, id_message_send, is_message_send});
			}
		};
		page_link.add(new Label("link_text",caption));
		this.add(page_link);
	}

}

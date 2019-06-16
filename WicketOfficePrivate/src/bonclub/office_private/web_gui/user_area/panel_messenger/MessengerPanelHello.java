package bonclub.office_private.web_gui.user_area.panel_messenger;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/** панель, для отображения сообщения для пользователя */
public class MessengerPanelHello extends Panel{
	private final static long serialVersionUID=1L;
	private Model<String> labelMainModel=new Model<String>();
	
	/** панель, для отображения сообщения для пользователя 
	 * @param panelId - уникальный идентификатор панели 
	 * @param message - сообщение, которое нужно вывести на экран для пользователя 
	 */
	public MessengerPanelHello(String panelId, String message){
		super(panelId);
		String messageForShow=null;
		if(message==null){
			// отобразить привествие
			labelMainModel.setObject(this.getString("label_hello"));
		}else{
			messageForShow=message;
			labelMainModel.setObject(message);
		}
		
		initComponents(messageForShow);
	}
	
	/** Установить новое сообщение на панели */
	public void setMessage(String message){
		if(message==null){
			this.labelMainModel.setObject("");
		}else{
			this.labelMainModel.setObject(message);
		}
	}
	
	/** инициализация всех визуальных компонентов */
	private void initComponents(String message){
		this.add(new Label("label_hello",labelMainModel));
	}
	
}

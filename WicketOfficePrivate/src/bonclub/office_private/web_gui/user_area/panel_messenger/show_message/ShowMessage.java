package bonclub.office_private.web_gui.user_area.panel_messenger.show_message;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import wicket_utility.ActionExecutor;

import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.web_gui.user_area.UserArea;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerUtility;
import bonclub.office_private.web_gui.user_area.panel_messenger.show_message.input.PanelMessageInput;
import bonclub.office_private.web_gui.user_area.panel_messenger.show_message.output.PanelMessageOutput;

/** �������� HTML, ������� ���������� ������ �� ��������� ���������*/
public class ShowMessage extends Panel{
	private final static long serialVersionUID=1L;
	
	/** panel wich showing input message - recieve message*/
	private PanelMessageInput field_panel_message_input=null;
	private PanelMessageOutput field_panel_message_output=null;
	/** ������, ������� �������� ������ ���������, � �� ������� ����� ��������� */
	private Panel field_return_panel;
	private ActionExecutor executor;
	
	/** ���������� ��������� �� ���������� id_message -
	 * @param executor - �����������, ������� ��������� ���������
	 * @param panelId - ���������� ������������� ������
	 * @param  parentPanel - ������, �� ������� ����� ���������
	 * @param message_id - ���������� ����� ��������� �� ������� MESSAGE_SEND ��� �� ������� MESSAGE_RECIEVE 
	 * @param is_messge_send ���� ����� true ���������� ����� ������ �� ������� MESSAGE_SEND, ���� false - MESSAGE_RECIEVE
	 * */
	public ShowMessage(ActionExecutor executor, String panelId, Panel parentPanel,Integer message_id,boolean is_message_send){
		super(panelId);
		this.executor=executor;
		this.field_return_panel=parentPanel;
		MessengerUtility utility=new MessengerUtility((OfficePrivateApplication)this.getApplication());
		if(is_message_send){
			// MESSAGE_SEND
			// TODO Replace to PanelMessageOutput()
			this.field_panel_message_output=new PanelMessageOutput("message",
																 utility.getMessageOutputRecipient(message_id, ", "),
																 utility.getOutputMessageText(message_id));
			this.add(this.field_panel_message_output);
		}else{
			// MESSAGE_RECIEVE
			this.field_panel_message_input=new PanelMessageInput("message",
																 utility.getMessageInputSender(message_id),
																 utility.getInputMessageText(message_id));
			this.add(this.field_panel_message_input);
		}
		utility.close();
		Form<Object> form=new Form<Object>("form");
		Button button_return=new Button("button_return"){
			private static final long serialVersionUID=1L;
			public void onSubmit(){
				//System.out.println("   ShowMessage: WebPage return:"+field_return_page);
				//setResponsePage(field_return_page);
				ShowMessage.this.executor.action(UserArea.eventShowPanel,new Object[]{ShowMessage.this.field_return_panel});
			}
		};
		form.add(button_return);
		this.add(form);
	}
}

package bonpay.partner.web_gui.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/** ������, ������� ���������� ������ ������ ���� � ���� ��������� ����������*/
public class PanelMessageError extends Panel{
	private final static long serialVersionUID=1L;
	/** ������, ������� �������� ���������� �������� */
	private RedirectAction action;
	
	/** ������, ������� ���������� ���������� �� ��������� �������� �� ������� ������������,
	 * ���� �� ������������� ������������� ����������
	 * @param panelId - ���������� ������������� ������
	 * @param errorInformation - ��������� ����������, ������� ������ ���� ��������
	 * @param buttonCaption - ������� �� ������ OK
	 * @param action - ������, ���������� ������ ��� ��������� 
	 * <table border=1>
	 * 	<tr> <td> <b>ok</b></td> <td> ������� �� ������ OK</td> </tr>
	 * </table>
	 */
	public PanelMessageError(String panelId,
							 String errorInformation,
							 String buttonCaption,
							 RedirectAction action){
		super(panelId);
		this.action=action;
		initComponents(errorInformation,buttonCaption);
	}
	
	/** �������������� ������������� ����������� */
	private void initComponents(String errorInformation,String buttonCaption){
		this.add(new Label("error_information",errorInformation));
		Link linkOk=new Link("link_ok"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				onButtonOk();
			}
		};
		linkOk.add(new Label("caption_link_ok",buttonCaption));
		this.add(linkOk);
	}
	
	/** ������� �� ������� ������ OK */
	private void onButtonOk(){
		this.action.action(this, "ok",null,null,null);
	}
}

package bonpay.partner.web_gui.partner_admin.panel_link;

import org.apache.wicket.markup.html.panel.Panel;

import bonpay.partner.web_gui.common.RedirectAction;

/** ������ "��������"*/
public class News extends Panel{
	private final static long serialVersionUID=1L;
	private RedirectAction action;

	/** ������ "��������"*/
	public News(String panelId,RedirectAction action){
		super(panelId);
		this.action=action;
		initComponents();
	}
	
	private void initComponents(){
	}
}

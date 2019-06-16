package bonpay.partner.web_gui.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/** панель, которая отображает ошибку любого рода в виде текстовой информации*/
public class PanelMessageError extends Panel{
	private final static long serialVersionUID=1L;
	/** объект, который содержит дальнейшие переходы */
	private RedirectAction action;
	
	/** панель, которая отображает информацию об ошибочном действии со стороны пользователя,
	 * либо же невозможность использования интерфейса
	 * @param panelId - уникальный идентификатор панели
	 * @param errorInformation - текстовая информация, которая должна быть выведена
	 * @param buttonCaption - надпись на кнопке OK
	 * @param action - объект, содержащий ссылки для переходов 
	 * <table border=1>
	 * 	<tr> <td> <b>ok</b></td> <td> нажатие на кнопку OK</td> </tr>
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
	
	/** первоначальная инициализация компонентов */
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
	
	/** реакция на нажатие кнопки OK */
	private void onButtonOk(){
		this.action.action(this, "ok",null,null,null);
	}
}

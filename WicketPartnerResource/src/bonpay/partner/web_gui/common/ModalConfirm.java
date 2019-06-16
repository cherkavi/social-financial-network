package bonpay.partner.web_gui.common;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

public class ModalConfirm extends WebPage implements PageCreator{
	private ModalWindow parent;
	private Label labelMessage;
	private AjaxButton buttonOk;
	private AjaxButton buttonCancel;
	private boolean isOk=false;
	private CallAction callActionCancel;
	private CallAction callActionOk;
	
	/** создать страницу для модального отображения */
	public ModalConfirm(){
		this.initParameters();
	}
	
	/** страница для модального отображения */
	public ModalConfirm(ModalWindow parent){
		this.initParameters();
		this.setModalWindow(parent);
	}

	private void initParameters(){
		this.labelMessage=new Label("label_message",new Model(""));
		Form form=new Form("ajax_form");
		form.setOutputMarkupId(true);
		
		this.buttonOk=new AjaxButton("button_ok"){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				onButtonOk(target);
			}
		};
		this.buttonCancel=new AjaxButton("button_cancel"){
			private static final long serialVersionUID=1L;
			protected void onSubmit(AjaxRequestTarget target, Form form){
				onButtonCancel(target);
			}
		};
		this.add(this.labelMessage);
		form.add(this.buttonOk);
		form.add(this.buttonCancel);
		this.add(form);
	}
	
	private void onButtonOk(AjaxRequestTarget target){
		this.isOk=true;
		if(this.callActionOk!=null){
			this.callActionOk.execute(target);		
		}
		target.appendJavascript("window.location.reload()");
		target.appendJavascript("window.location.refresh(true)");
		target.appendJavascript("window.parent.refresh(true)");
		target.appendJavascript("window.parent.reload()");
		this.parent.close(target);
	}
	
	private void onButtonCancel(AjaxRequestTarget target){
		this.isOk=false;
		if(this.callActionCancel!=null){
			this.callActionCancel.execute(target);		
		}
		this.parent.close(target);
	}
	
	/** установить действие для нажатия на кнопку ButtonOk */
	public void setCallActionButtonOk(CallAction callButtonOk){
		this.callActionOk=callButtonOk;
	}
	
	/** установить действие для нажатия на кнопку ButtonCancel */
	public void setCallActionButtonCancel(CallAction callButtonCancel){
		this.callActionCancel=callButtonCancel;
	}
	
	/** установить текст сообщения для отображения */
	public void setMessage(String message){
		this.labelMessage.setModelObject(message);
	}
	
	/** установить текст на кнопке OK */
	public void setButtonOkCaption(String caption){
		this.buttonOk.add(new SimpleAttributeModifier("value",caption));
	}
	
	/** установить текст на кнопке Cancel */
	public void setButtonCancelCaption(String caption){
		this.buttonCancel.add(new SimpleAttributeModifier("value",caption));
	}
	
	/** установить родительское окно */
	public void setModalWindow(ModalWindow parent){
		this.parent=parent;
	}
	
	/** {@link PageCreator} метод, который возвращает страницу, которую нужно отобразить
	 * в качестве модальной 
	 */
	@Override
	public Page createPage() {
		return this;
	}
	
	/** получить результат вызова модального окна */
	public boolean isButtonOkPressed(){
		return this.isOk;
	}
	
}

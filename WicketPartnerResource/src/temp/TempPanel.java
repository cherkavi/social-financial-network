package temp;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class TempPanel extends Panel{
	private final static long serialVersionUID=1L;
	public TempPanel(String id){
		super(id);
		initComponents();
	}

	private WebMarkupContainer ajaxPlatform;
	
	private void initComponents(){
		this.ajaxPlatform=new WebMarkupContainer("ajax_platform");
		this.ajaxPlatform.setOutputMarkupId(true);
		this.ajaxPlatform.add(new Label("label_counter",getNextCount()));
		this.add(this.ajaxPlatform);
		this.add(new AjaxLink("ajax_link"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				onButtonClick(target);
			}
		});
	}
	
	private void onButtonClick(AjaxRequestTarget target){
		this.ajaxPlatform.removeAll();
		this.ajaxPlatform.add(new Label("label_counter",getNextCount()));
		target.addComponent(this.ajaxPlatform);
	}
	
	private int counter=0;
	private String getNextCount(){
		return Integer.toString(counter++);
	}
	
	
}

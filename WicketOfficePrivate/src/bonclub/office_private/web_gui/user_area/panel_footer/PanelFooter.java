package bonclub.office_private.web_gui.user_area.panel_footer;

import org.apache.wicket.markup.html.link.Link;

import org.apache.wicket.markup.html.panel.Panel;

import bonclub.office_private.web_gui.user_area.UserArea;

import wicket_utility.ActionExecutor;

/** панель, которая отображает Footer для Login*/
public class PanelFooter extends Panel{
	private final static long serialVersionUID=1L;
	private ActionExecutor executor;
	
	public PanelFooter(String panelId, ActionExecutor executor){
		super(panelId);
		this.executor=executor;
		initComponents();
	}
	
	private void initComponents(){
		this.add(new Link<Object>("link_private_data"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onPrivateData();
			}
		});
		this.add(new Link<Object>("link_profile"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onProfile();
			}
		});
		this.add(new Link<Object>("link_post"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onPost();
			}
		});
		this.add(new Link<Object>("link_bookkeeping"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onBookkeeping();
			}
		});
		this.add(new Link<Object>("link_club_event"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onClubEvent();
			}
		});

		this.add(new Link<Object>("link_exit"){
			private final static long serialVersionUID=1L;
			@Override
			public void onClick() {
				onExit();
			}
		});
	}
	private void onPrivateData(){
		this.executor.action(UserArea.eventPrivateData, null);
	}
	private void onProfile(){
		this.executor.action(UserArea.eventProfile, null);
	}
	private void onPost(){
		this.executor.action(UserArea.eventPost, null);
	}
	private void onBookkeeping(){
		this.executor.action(UserArea.eventBookkeeping, null);
	}
	private void onClubEvent(){
		this.executor.action(UserArea.eventClubEvent, null);
	}
	private void onExit(){
		this.executor.action(UserArea.eventExit, null);
	}
	
}

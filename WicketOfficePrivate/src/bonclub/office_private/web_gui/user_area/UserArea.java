package bonclub.office_private.web_gui.user_area;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import wicket_utility.ActionExecutor;
import wicket_utility.MessagePanel;
import bonclub.office_private.OfficePrivateApplication;
import bonclub.office_private.database.ConnectUtility;
import bonclub.office_private.database.wrap.Parameters;
import bonclub.office_private.session.OfficePrivateSession;
import bonclub.office_private.web_gui.BasePage;
import bonclub.office_private.web_gui.login.Login;
import bonclub.office_private.web_gui.user_area.panel_navigator.PanelNavigator;
import bonclub.office_private.web_gui.user_area.panel_footer.PanelFooter;
import bonclub.office_private.web_gui.user_area.panel_header.PanelHeader;
import bonclub.office_private.web_gui.user_area.panel_main.private_data.PrivateData;
import bonclub.office_private.web_gui.user_area.panel_main.profile.Profile;
import bonclub.office_private.web_gui.user_area.panel_main.profile_edit.ProfileEdit;
import bonclub.office_private.web_gui.user_area.panel_messenger.MessengerPanelHello;
import bonclub.office_private.web_gui.user_area.panel_messenger.create.Create;
import bonclub.office_private.web_gui.user_area.panel_messenger.find_user.FindUser;
import bonclub.office_private.web_gui.user_area.panel_messenger.input.Input;
import bonclub.office_private.web_gui.user_area.panel_messenger.output.Output;
import bonclub.office_private.web_gui.user_area.panel_messenger.recipient_list.RecipientList;
import bonclub.office_private.web_gui.user_area.panel_messenger.show_message.ShowMessage;

/** рабочая страница вошедшего пользователя */
public class UserArea extends BasePage implements ActionExecutor{
	private final static long serialVersionUID=1L;
	protected final static String idPanelHeader="panel_header";
	protected final static String idPanelMain="panel_main";
	protected final static String idPanelFooter="panel_footer";
	protected final static String idPanelNavigator="panel_navigator";
	
	public final static String eventBonCard="bon_card";
	public final static String eventNakopilka="nakopilka";
	public final static String eventOffice="office";
	
	public final static String eventPrivateData="private_data";
	public final static String eventProfile="profile";
	public final static String eventPost="post";
	public final static String eventPostInformation="post_information";
	public final static String eventPostShowMessage="post_show_message";
	public final static String eventPostCreate="post_create";
	public final static String eventPostInput="post_input";
	public final static String eventPostOutput="post_output";
	public final static String eventBookkeeping="bookkeeping";
	public final static String eventClubEvent="club_event";
	public final static String eventExit="exit";
	
	public final static String eventShowPanel="show_panel";
	
	public final static String eventProfileRequestEdit="profile_request_edit";
	public final static String eventProfileEditEnd="profile_edit_end";
	public final static String eventPostRecipientList="post_recipient_list";
	public final static String eventPostFindUser="eventPostFindUser";

	/** панель Header*/
	private Panel panelHeader;
	/** панель Footer*/
	private Panel panelFooter;
	/** панель навигации */
	private PanelNavigator panelNavigator;
	/** главная панель */
	private Panel panelMain;
	
	public UserArea(){
		// получить уникальный код пользователя 
		this.add(this.getPanelHeader());
		this.add(this.getPanelNavigator());
		this.add(this.getPanelMain());
		this.add(this.getPanelFooter());
	}

	private Panel getPanelFooter() {
		if(this.panelFooter==null){
			this.panelFooter=new PanelFooter(idPanelFooter, this);
		}
		return this.panelFooter;
	}

	private Panel getPanelMain() {
		if(this.panelMain==null){
			// добавить в качестве основной панели Личные данные
			this.panelMain=new PrivateData(idPanelMain, this);
			//this.panelMain=new Profile(idPanelMain, this,0);
			//this.panelMain=new ProfileEdit(idPanelMain, this,0);
			this.panelNavigator.setSelected(0);
		}
		return this.panelMain;
	}

	private Panel getPanelNavigator() {
		if(this.panelNavigator==null){
			this.panelNavigator=new PanelNavigator(idPanelNavigator,
					   this,
					   "login_main_panel_navigator_mark",
					   "login_main_panel_navigator_unmark"
					   );
		}
		return this.panelNavigator;
	}

	private Panel getPanelHeader() {
		if(this.panelHeader==null){
			this.panelHeader=new PanelHeader(idPanelHeader,this);
		}
		return this.panelHeader;
	}
	
	private String getParameterFromDatabase(String parameter){
		String returnValue="";
		ConnectUtility connectUtility=((OfficePrivateApplication)this.getApplication()).getConnectUtility();
		Session session=connectUtility.getSession();
		try{
			returnValue=((Parameters)(session.createCriteria(Parameters.class).add(Restrictions.eq("name", parameter))).list().get(0)).getValue();
		}catch(Exception ex){
			System.out.println("Login#getParameterFromDatabase Exception: "+ex.getMessage());
		}
		connectUtility.close();
		return returnValue;
	}
	

	@Override
	public void action(String actionName, Object argument) {
		 if(actionName.equals(eventBonCard)){
			//System.out.println("eventBonCard");
			//setResponsePage(new RedirectPage("http://www.bon-karta.com.ua/node/7"));
			setResponsePage(new RedirectPage(getParameterFromDatabase("URL_BON_CARD")));
		 };
		 if(actionName.equals(eventNakopilka)){
			//System.out.println("eventNakopilka");
			//setResponsePage(new RedirectPage("http://www.bon-karta.com.ua/node/8"));
			setResponsePage(new RedirectPage(getParameterFromDatabase("URL_BON_CARD_NAKOPILKA")));
		 };
		 if(actionName.equals(eventOffice)){
			 //System.out.println("eventOffice");
			 //setResponsePage(new RedirectPage("http://www.bon-karta.com.ua/"));
		 };

		 // -----------------------------
		 if(actionName.equals(eventPrivateData)){
			 //System.out.println("eventPrivateData");
			 this.remove(idPanelMain);
			 this.add(new PrivateData(idPanelMain, this));
			 this.panelNavigator.setSelected(0);
		 };
		 if(actionName.equals(eventProfile)){
			 //System.out.println("eventProfile");
			 this.remove(idPanelMain);
			 this.add(new Profile(idPanelMain, this));
			 this.panelNavigator.setSelected(1);
		 };
		 if(actionName.equals(eventPost)){
			 //System.out.println("eventPost");
			 this.remove(idPanelMain);
			 this.add(new MessengerPanelHello(idPanelMain, null));
			 this.panelNavigator.setSelected(21);
		 };
		 if(actionName.equals(eventPostInformation)){
			 String[] arguments=(String[])argument;
			 this.remove(idPanelMain);
			 this.add(new MessengerPanelHello(idPanelMain, arguments[0]));
			 this.panelNavigator.setSelected(2);
		 };
		 if(actionName.equals(eventPostShowMessage)){
			 Object[] arguments=(Object[])argument;
			 Panel panel_parent=(Panel)arguments[0]; 
			 Integer id_message_send=(Integer)arguments[1];
			 Boolean is_message_send=(Boolean)arguments[2];
			 this.remove(idPanelMain);
			 this.add(new ShowMessage(this,idPanelMain, panel_parent, id_message_send, is_message_send));
			 this.panelNavigator.setSelected(2);
		 };
		 if(actionName.equals(eventPostRecipientList)){
			 this.remove(idPanelMain);
			 this.add(new RecipientList(idPanelMain,this));
			 //this.panelNavigator.setSelected(20);
		 };
		 if(actionName.equals(eventPostFindUser)){
			 this.remove(idPanelMain);
			 this.add(new FindUser(idPanelMain,this));
			 //this.panelNavigator.setSelected(20);
		 };
		 
		 if(actionName.equals(eventPostCreate)){
			 this.remove(idPanelMain);
			 this.add(new Create(idPanelMain,this));
			 this.panelNavigator.setSelected(20);
		 };
		 if(actionName.equals(eventPostInput)){
			 this.panelNavigator.setSelected(21);
			 this.remove(idPanelMain);
			 this.add(new Input(idPanelMain, this));
		 };
		 if(actionName.equals(eventPostOutput)){
			 this.panelNavigator.setSelected(22);
			 this.remove(idPanelMain);
			 this.add(new Output(idPanelMain, this));
		 };
		 
		 if(actionName.equals(eventBookkeeping)){
			 // Бухгалтерия
			 //System.out.println("eventBookkeeping");
			 this.panelNavigator.setSelected(3);
			 this.remove(idPanelMain);
			 this.add(new MessagePanel(idPanelMain,this.getString("message_bookkeeping")));
		 };
		 if(actionName.equals(eventClubEvent)){
			 // Клубные акции
			 //System.out.println("eventClubEvent");
			 this.panelNavigator.setSelected(4);
			 this.remove(idPanelMain);
			 this.add(new MessagePanel(idPanelMain,this.getString("message_clubevent")));
		 };
		 if(actionName.equals(eventExit)){
			 //System.out.println("eventExit");
			 ((OfficePrivateSession)this.getSession()).setCustomerId(null);
			 //this.panelNavigator.setSelected(5);
			 this.setResponsePage(Login.class); 
		 };
		 // --------------------------------
		 if(actionName.equals(eventProfileRequestEdit)){
			 //System.out.println("event profile request edit");
			 this.remove(idPanelMain);
			 this.add(new ProfileEdit(idPanelMain, this));
			 this.panelNavigator.setSelected(1);
		 }

		 if(actionName.equals(eventProfileEditEnd)){
			 //System.out.println("event profile EDIT END");
			 this.remove(idPanelMain);
			 this.add(new Profile(idPanelMain,this));
			 this.panelNavigator.setSelected(1);
		 }
		 
		 if(actionName.equals(eventShowPanel)){
			 this.remove(idPanelMain);
			 Object[] arguments=(Object[])argument;
			 this.add((Panel)arguments[0]);
		 }
	}
	
}

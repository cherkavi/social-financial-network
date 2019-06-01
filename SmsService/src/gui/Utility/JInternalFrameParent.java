package gui.Utility;


import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public abstract class JInternalFrameParent extends JInternalFrame 
										   implements InternalFrameListener, 
										   			  ModalParent{
	private static final long serialVersionUID=1L;
	/** рабочий стол, на котором располагаются все фреймы*/
	private JDesktopPane field_desktop;
	/** объект, который получает упревление при закрытии окна */
	private ModalParent field_modal_parent;
	/** общий объект, который содержит необходимую информацию для отображения данных */
	private CommonObject field_common_object;
	/**
	 * Родительское окно для всех модальных окон
	 * @param parent - родительский фрейм, которому нужно передать управление
	 * @param common_object - объект, который содержит необходимую для отображения данных информацию  
	 * @param title - заголовок для данного фрейма
	 * @param width - ширина фрейма
	 * @param height - высота фрейма
	 * если width=0 && height==0 тогда - максимальный размер 
	 */
	public JInternalFrameParent(JInternalFrameParent parent,
								CommonObject common_object,
								String title,
								int width,
								int height){
		super(title,
			  true, 
			  true, 
			  true, 
			  false);
		this.setSize(width, height);
		this.field_modal_parent=(ModalParent)parent;
		this.field_desktop=parent.getDesktop();
		this.field_common_object=common_object;

		parent.setVisible(false);
		this.addInternalFrameListener(this);
		this.field_desktop.add(this);
		if((width==0)&&(height==0)){
			try{
				this.setMaximum(true);
			}catch(Exception ex){};
		}else{
			set_frame_to_center(this, this.getDesktop(), 0, 0);
		}
		this.setVisible(true);
	}

	/**
	 * Родительское окно для всех модальных окон
	 * @param desktop - рабочий стол 
	 * @param title - заголовок для данного фрейма
	 * @param width - ширина фрейма 
	 * @param height - высота фрейма
	 * если width=0 && height==0 тогда - максимальный размер 
	 */
	public JInternalFrameParent(JDesktopPane desktop,
							    CommonObject common_object,
							    String title,
							    int width,
							    int height){
		super(title,
			  true, 
			  true, 
			  true, 
			  false);
		this.setSize(width, height);
		this.field_modal_parent=null;
		this.field_desktop=desktop;
		this.field_common_object=common_object;
		
		this.addInternalFrameListener(this);
		this.field_desktop.add(this);
		if((width==0)&&(height==0)){
			try{
				this.setMaximum(true);
			}catch(Exception ex){};
		}else{
			set_frame_to_center(this, this.getDesktop(), 0, 0);
		}
		this.setVisible(true);
	}
	
    /**
     * установка JInternalFrame в центре рабочего стола со смещением слева и справа
     */
    public static void set_frame_to_center(JInternalFrame internalframe,
                                           JDesktopPane desktop,
                                           int offset_left,
                                           int offset_top){
        Dimension screenSize = desktop.getSize();
        internalframe.setBounds((int)(screenSize.getWidth()/2-offset_left/2-internalframe.getWidth()/2),
                                (int)(screenSize.getHeight()/2-offset_top/2-internalframe.getHeight()/2),
                                (int)internalframe.getWidth(),
                                (int)internalframe.getHeight());
    }
	
	public CommonObject getCommonObject(){
		return this.field_common_object;
	}
	
	public void setCommonObject(CommonObject common_object){
		this.field_common_object=common_object;
	}
	
	/** получить ссылку на текущий рабочий стол */
	protected JDesktopPane getDesktop(){
		return this.field_desktop;
	}
	
	/** получить ссылку на объект, которому нужно передать управление в случае закрытия данного JInternalFrame*/
	protected ModalParent getModalParent(){
		return this.field_modal_parent;
	}
	/** необходимо реализовать все визуальные компоненты, и добавить в итоге их на ContentPane */
	protected abstract void initComponents();
	
	public void internalFrameActivated(InternalFrameEvent e) {
	}
	
	public void internalFrameClosed(InternalFrameEvent e) {
	}
	
	public void internalFrameClosing(InternalFrameEvent e) {
		if(this.getModalParent()==null){
			System.exit(0);
		}else{
			this.getModalParent().modalClosing(this, 0);
		}
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	
	public void modalClosing(JInternalFrame frame, int result) {
		frame.setVisible(false);
		this.setVisible(true);
	}

	public void hideModal() {
		this.setVisible(false);
	}

	public void showModal() {
		this.setVisible(true);
	}
	
}

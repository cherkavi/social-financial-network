package gui.Utility;
import javax.swing.JInternalFrame;

/** интерфейс, который получает от модального окна сообщение о закрытии*/
public interface ModalParent {
	/** передача управления родительскому окну */
	public void modalClosing(JInternalFrame frame, int result);
	/** спрятать данное окно */
	public void hideModal();
	/** показать данное окно */
	public void showModal();
}

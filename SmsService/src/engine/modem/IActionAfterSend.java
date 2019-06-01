package engine.modem;

/** содержит необходимые методы для передачи управления после отправки сообщения модемом */
public interface IActionAfterSend {
	/** метод, который должен быть вызван сразу после отправки сообщения модемом, 
	 * и присваивания этому сообщению уникального номера RefNo в контексте модема
	 * @param message - сообщение, которое было отправлено
	 * @param errorMessage - 
	 * <ul><b>null</b> - сообщение было отправлено </ul>
	 * <ul><b>text</b> - ошибка отправки сообщения </ul>  
	 * */
	public void actionAfterSend(MessageOutput message, String errorMessage);
}

package engine.modem;

/** отправка сообщений */
public interface ISender {
	/** добавить сообщение в очередь для отправки */
	public void sendMessage(MessageOutput message);
}

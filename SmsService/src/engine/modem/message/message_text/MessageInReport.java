package engine.modem.message.message_text;

public class MessageInReport extends MessageIn{

	/** статус о доставке не содержит текста письма */
	@Override
	public String getText() {
		return null;
	}
}

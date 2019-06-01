package engine.modem;

public interface IGatewayMessageNotify {
	/** оповестить всех слушателей об изменениях в виде текстового сообщения */
	public void notifyMessage(String message);

}

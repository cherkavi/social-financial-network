package engine.modem;

import org.smslib.AGateway.GatewayStatuses;


/** интерфейс, который реализует объект, способный принимать сообщения о статусе */
public interface IGatewayStatusNotifier {
	/** оповещение об изменении статуса Gateway - через который осуществляется соединение с базой */
	public void gateWayNotifier(GatewayStatuses status);
}

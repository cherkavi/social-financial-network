package bc.applet.CardManager.manager.store;

import bc.applet.CardManager.manager.ClientIdentifier;
import bc.applet.CardManager.manager.actions.Action;

/**
 * определяет методы, которые служат для записи и восстановления объекта из внешних хранилищ
 * @author cherkashinv
 */
public interface Store {
	/** записать объект в хранилище (в базу данных, в )*/
	public boolean writeObject(ClientIdentifier identifier, Action manager);

	/** прочитать объект из хранилища (из базы данных, или из файла на диске....)
	 * @return null если объект не был прочитан, либо произошла ошибка чтения...
	 * */
	public Action readObject(ClientIdentifier identifier);
	
	/** удалить объект из хранилища */
	public boolean deleteObject(ClientIdentifier identifier);
}

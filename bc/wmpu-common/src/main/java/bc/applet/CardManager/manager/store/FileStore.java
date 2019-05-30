package bc.applet.CardManager.manager.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import bc.applet.CardManager.manager.ClientIdentifier;
import bc.applet.CardManager.manager.actions.Action;



public class FileStore implements Store{
	private final static Logger LOGGER=Logger.getLogger(FileStore.class);
	
	private static String field_directory="c:\\";
	
	/** записать объект в хранилище */
	public boolean writeObject(ClientIdentifier identifier, Action manager){
		boolean return_value=false;
		// метод записи объекта в файл
		try{
			String path_to_file=field_directory+identifier.getUniqueName()+".bin";
			File f=new File(path_to_file);
			LOGGER.debug("Create FileOutputStream");
			FileOutputStream fos=new FileOutputStream(f);
			LOGGER.debug("Create ObjectOutputStream");
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			LOGGER.debug("write object");
			oos.writeObject(manager);
			LOGGER.debug("close FileOutputStream");
			fos.close();
			LOGGER.debug("close ObjectOutputStream");
			oos.close();
			return_value=true;
		}catch(Exception ex){
			return_value=false;
			LOGGER.error("Ошибка при записи клиентского объекта в хранилище "+ex.getMessage());
		}
		return return_value;
	}

	/** прочитать объект из хранилища (из базы данных, или из файла на диске....)*/
	public Action readObject(ClientIdentifier identifier){
		Action return_value=null;
		// метод чтения объекта из файла 
		try{
			String path_to_file=field_directory+identifier.getUniqueName()+".bin";
			File f=new File(path_to_file);
			if(f.exists()==true){
				LOGGER.debug("FileInputStream create");
				FileInputStream fis=new FileInputStream(f);
				LOGGER.debug("ObjectInputStream create");
				ObjectInputStream ois=new ObjectInputStream(fis);
				LOGGER.debug("Read Object from FileInputStream ");
				return_value=(Action)ois.readObject();
				LOGGER.debug("Close ObjectInputStream");
				ois.close();
				LOGGER.debug("Close FileInputStream");
				fis.close();
			}else{
				// return null;
				LOGGER.error("Файл не прочитан:"+path_to_file);
			}
		}catch(Exception ex){
			LOGGER.error("Ошибка при чтении клиентского объекта из хранилища:"+ex.getMessage());
		}
		return return_value;
	}

	@Override
	public boolean deleteObject(ClientIdentifier identifier) {
		String path_to_file=field_directory+identifier.getUniqueName()+".bin";
		File file=new File(path_to_file);
		return file.delete();
	}
	
}

package bc.data_terminal.server.database;

/** файл, который был создан на основании данных из базы данных */
public class CreatedFile {
	/** им€ файла, по которому данные сохранены в данной операционной системе */
	private String thisFileName;
	/** им€ файла по которому файл будет предоставлен удаленной операционной системе */
	private String remoteFileName;
	/** уникальный идентификатор файла, по которому он чЄтко идентифицируетс€ в базе данных */
	private int idFileTransfer=0; 
	
	/** файл, который был создан на основании данных из базы данных 
	 * @param thisFileName - им€ файла в данной операционной системе 
	 * @param idFileTransfer - уникальный номер, по которому четко идентифицируютс€ данные в базе данных 
	 * */
	public CreatedFile(String thisFileName, int idFileTransfer){
		this.thisFileName=thisFileName;
		this.idFileTransfer=idFileTransfer;
	}

	/** файл, который был создан на основании данных из базы данных 
	 * @param remoteFileName - им€ по которому будет предоставлен файл в удаленной системе  
	 * @param thisFileName - им€ файла в данной операционной системе 
	 * @param idFileTransfer - уникальный номер, по которому четко идентифицируютс€ данные в базе данных 
	 */
	public CreatedFile(String remoteFileName, String thisFileName, int idFileTransfer){
		this.remoteFileName=remoteFileName;
		this.thisFileName=thisFileName;
		this.idFileTransfer=idFileTransfer;
	}

	/**
	 * @return the thisFileName
	 */
	public String getThisFileName() {
		return thisFileName;
	}

	/**
	 * @param thisFileName the thisFileName to set
	 */
	public void setThisFileName(String thisFileName) {
		this.thisFileName = thisFileName;
	}

	/**
	 * @return the remoteFileName
	 */
	public String getRemoteFileName() {
		return remoteFileName;
	}

	/**
	 * @param remoteFileName the remoteFileName to set
	 */
	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	/**
	 * @return the idFileTransfer
	 */
	public int getIdFileTransfer() {
		return idFileTransfer;
	}

	/**
	 * @param idFileTransfer the idFileTransfer to set
	 */
	public void setIdFileTransfer(int idFileTransfer) {
		this.idFileTransfer = idFileTransfer;
	}
	
	
}

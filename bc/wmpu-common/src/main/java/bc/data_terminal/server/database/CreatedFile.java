package bc.data_terminal.server.database;

/** ����, ������� ��� ������ �� ��������� ������ �� ���� ������ */
public class CreatedFile {
	/** ��� �����, �� �������� ������ ��������� � ������ ������������ ������� */
	private String thisFileName;
	/** ��� ����� �� �������� ���� ����� ������������ ��������� ������������ ������� */
	private String remoteFileName;
	/** ���������� ������������� �����, �� �������� �� ����� ���������������� � ���� ������ */
	private int idFileTransfer=0; 
	
	/** ����, ������� ��� ������ �� ��������� ������ �� ���� ������ 
	 * @param thisFileName - ��� ����� � ������ ������������ ������� 
	 * @param idFileTransfer - ���������� �����, �� �������� ����� ���������������� ������ � ���� ������ 
	 * */
	public CreatedFile(String thisFileName, int idFileTransfer){
		this.thisFileName=thisFileName;
		this.idFileTransfer=idFileTransfer;
	}

	/** ����, ������� ��� ������ �� ��������� ������ �� ���� ������ 
	 * @param remoteFileName - ��� �� �������� ����� ������������ ���� � ��������� �������  
	 * @param thisFileName - ��� ����� � ������ ������������ ������� 
	 * @param idFileTransfer - ���������� �����, �� �������� ����� ���������������� ������ � ���� ������ 
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

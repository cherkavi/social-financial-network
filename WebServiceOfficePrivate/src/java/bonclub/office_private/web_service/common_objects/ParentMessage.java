package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.util.Date;

/** сообщение из родительской базы данных  */
public class ParentMessage implements Serializable{
	private final static long serialVersionUID=1L;
	
	/** сообщение из родительской базы данных  */
	public ParentMessage(){
	}
	
	private Integer ID_MESSAGE;
	private Integer ID_NAT_PRS;
	private String FULL_NAME_NAT_PRS;
	private String EMAIL;
	private String TITLE_MESSAGE;
	private String TEXT_MESSAGE;
	private Date BEGIN_ACTION_DATE;
	private Date END_ACTION_DATE;
	private Integer ID_CL_PATTERN;
	private Integer SENDINGS_QUANTITY;
	private Integer ERROR_SENDINGS_QUANTITY;
	private String STATE_RECORD;
	private Date CREATION_DATE;
	private Integer CREATED_BY;
	private Integer ID_CLUB;
	private String TYPE_MESSAGE;
	private String BASIS_FOR_OPERATION;
	private Integer ID_QUEST_RECORD;
	/** HAS_ATTACHED_FILES Y/N */
	private boolean attachedFiles;
	private String MESSAGE_FILE_NAME;
	private String STORED_MESSAGE_FILE_NAME;
	/** IS_ARCHIVE Y/N */
	private boolean archive;
	public Integer getID_MESSAGE() {
		return ID_MESSAGE;
	}
	public void setID_MESSAGE(Integer iDMESSAGE) {
		ID_MESSAGE = iDMESSAGE;
	}
	public Integer getID_NAT_PRS() {
		return ID_NAT_PRS;
	}
	public void setID_NAT_PRS(Integer iDNATPRS) {
		ID_NAT_PRS = iDNATPRS;
	}
	public String getFULL_NAME_NAT_PRS() {
		return FULL_NAME_NAT_PRS;
	}
	public void setFULL_NAME_NAT_PRS(String fULLNAMENATPRS) {
		FULL_NAME_NAT_PRS = fULLNAMENATPRS;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getTITLE_MESSAGE() {
		return TITLE_MESSAGE;
	}
	public void setTITLE_MESSAGE(String tITLEMESSAGE) {
		TITLE_MESSAGE = tITLEMESSAGE;
	}
	public String getTEXT_MESSAGE() {
		return TEXT_MESSAGE;
	}
	public void setTEXT_MESSAGE(String tEXTMESSAGE) {
		TEXT_MESSAGE = tEXTMESSAGE;
	}
	public Date getBEGIN_ACTION_DATE() {
		return BEGIN_ACTION_DATE;
	}
	public void setBEGIN_ACTION_DATE(Date bEGINACTIONDATE) {
		BEGIN_ACTION_DATE = bEGINACTIONDATE;
	}
	public Date getEND_ACTION_DATE() {
		return END_ACTION_DATE;
	}
	public void setEND_ACTION_DATE(Date eNDACTIONDATE) {
		END_ACTION_DATE = eNDACTIONDATE;
	}
	public Integer getID_CL_PATTERN() {
		return ID_CL_PATTERN;
	}
	public void setID_CL_PATTERN(Integer iDCLPATTERN) {
		ID_CL_PATTERN = iDCLPATTERN;
	}
	public Integer getSENDINGS_QUANTITY() {
		return SENDINGS_QUANTITY;
	}
	public void setSENDINGS_QUANTITY(Integer sENDINGSQUANTITY) {
		SENDINGS_QUANTITY = sENDINGSQUANTITY;
	}
	public Integer getERROR_SENDINGS_QUANTITY() {
		return ERROR_SENDINGS_QUANTITY;
	}
	public void setERROR_SENDINGS_QUANTITY(Integer eRRORSENDINGSQUANTITY) {
		ERROR_SENDINGS_QUANTITY = eRRORSENDINGSQUANTITY;
	}
	public String getSTATE_RECORD() {
		return STATE_RECORD;
	}
	public void setSTATE_RECORD(String sTATERECORD) {
		STATE_RECORD = sTATERECORD;
	}
	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Date cREATIONDATE) {
		CREATION_DATE = cREATIONDATE;
	}
	public Integer getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(Integer cREATEDBY) {
		CREATED_BY = cREATEDBY;
	}
	public Integer getID_CLUB() {
		return ID_CLUB;
	}
	public void setID_CLUB(Integer iDCLUB) {
		ID_CLUB = iDCLUB;
	}
	public String getTYPE_MESSAGE() {
		return TYPE_MESSAGE;
	}
	public void setTYPE_MESSAGE(String tYPEMESSAGE) {
		TYPE_MESSAGE = tYPEMESSAGE;
	}
	public String getBASIS_FOR_OPERATION() {
		return BASIS_FOR_OPERATION;
	}
	public void setBASIS_FOR_OPERATION(String bASISFOROPERATION) {
		BASIS_FOR_OPERATION = bASISFOROPERATION;
	}
	public Integer getID_QUEST_RECORD() {
		return ID_QUEST_RECORD;
	}
	public void setID_QUEST_RECORD(Integer iDQUESTRECORD) {
		ID_QUEST_RECORD = iDQUESTRECORD;
	}
	public boolean isAttachedFiles() {
		return attachedFiles;
	}
	public void setAttachedFiles(boolean attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	public String getMESSAGE_FILE_NAME() {
		return MESSAGE_FILE_NAME;
	}
	public void setMESSAGE_FILE_NAME(String mESSAGEFILENAME) {
		MESSAGE_FILE_NAME = mESSAGEFILENAME;
	}
	public String getSTORED_MESSAGE_FILE_NAME() {
		return STORED_MESSAGE_FILE_NAME;
	}
	public void setSTORED_MESSAGE_FILE_NAME(String sTOREDMESSAGEFILENAME) {
		STORED_MESSAGE_FILE_NAME = sTOREDMESSAGEFILENAME;
	}
	public boolean isArchive() {
		return archive;
	}
	public void setArchive(boolean archive) {
		this.archive = archive;
	}
}

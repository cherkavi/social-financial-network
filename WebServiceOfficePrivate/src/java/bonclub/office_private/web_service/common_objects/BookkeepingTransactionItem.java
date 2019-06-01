package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Данные для получения из объекта: <br />
 * bc_admin.vc_trans_joffice_all
 * один элемент из всего объекта 
 * */
public class BookkeepingTransactionItem implements Serializable{
	private final static long serialVersionUID=1L;
	
	private Integer ID_TERM_SES; 
	private Integer ID_TELGR;
	private Integer ID_TERM;
	private Integer ID_SERVICE_PLACE;
	private String NAME_SERVICE_PLACE;
    private Integer ID_SAM; 
    private Integer NT_SAM; 
    private Integer ID_TRANS; 
    private String TYPE_TRANS_TXT;
	private Integer VER_TRANS;
	private String CARD_SERIAL_NUMBER;
	private Integer CD_CURRENCY;
	private String NAME_CURRENCY;
	private Integer NT_ICC;
	private Integer NT_EXT; 
	private Integer TC_P; 
	private Integer VK_ENC; 
	private String ACTION; 
	private Integer CLUB_ST_PRV;
	private Integer ERR_TX; 
	private String ERR_TX_DESC; 
	private Integer MAC_ICC; 
	private Integer MAC_PDA;
	private Integer ID_STORNED_TRANS; 
	private Integer ID_DOUBLE_TRANS; 
	private Integer ID_ISSUER; 
	private Integer ID_PAYMENT_SYSTEM; 
	private Integer ID_PURCHASE;
	private String CHANGED_BY_USER; 
	private String USER_WHO_HAS_CHANGED; 
	private String BASES_FOR_CHANGES; 
	private String ENTERED_MANUALLY; 
	private String IMID; 
	private String SPECID; 
	private String CLUBCARD; 
	private String CARDID;
	private String ID_CLUB; 
	private String LANGUAGE; 
	private String CREATION_DATE; 
	private String CREATED_BY; 
	private String LAST_UPDATE_DATE; 
	private Integer LAST_UPDATE_BY;
	private Integer IS_REJECTED; 
	private Integer ID_TERMINAL_MANUFACTURER; 
	private Integer ID_DEVICE_TYPE; 
	private Integer ID_DEALER; 
	private Integer ID_TERM_OWNER;
	private Integer ID_FINANCE_ACQUIRER; 
	private Integer ID_TECHNICAL_ACQUIRER; 
	private Integer ID_CARD_STATUS; 
	private Integer ID_BON_CATEGORY;
	private Integer ID_DISC_CATEGORY; 
	private Integer ID_LOYALITY_HISTORY; 
	private Integer ID_LOYALITY_BON_LINE; 
	private Integer ID_LOYALITY_DISC_LINE;
	private String STATE_TRANS_TSL;
	private String SYS_DATE_FULL_FRMT; 
	private Date SYS_DATE; 
	private String SYS_TIME_FRMT; 
	private String CLUB_DISC_PRV_FRMT; 
	private String CLUB_BON_PRV_FRMT;
	private String DATE_ACC_PRV_FRMT; 
	private String DATE_MOV_PRV_FRMT; 
	private String DATE_CALC_PRV_FRMT; 
	private String BAL_ACC_PRV_FRMT;
	private String BAL_CUR_PRV_FRMT; 
	private String BAL_BON_PER_PRV_FRMT;
	private String BAL_DISC_PER_PRV_FRMT; 
	private String OPR_SUM_FRMT;
	private String SUM_PAY_CASH_FRMT; 
	private String SUM_PAY_CARD_FRMT;
	private String SUM_PAY_BON_FRMT; 
	private Float FL_EXT_LOYL; 
	private String CLUB_SUM_FRMT; 
	private String SUM_BON_FRMT; 
	private String SUM_BON_CASH_FRMT; 
	private String SUM_BON_CARD_FRMT;
	private String SUM_BON_BON_FRMT; 
	private String SUM_BON_DISC_FRMT; 
	private String SUM_DISC_FRMT; 
	private String CASH_CARD_NR; 
	private String C_NR; 
	private String C_CHECK_NR; 
	private String CLUB_ST; 
	private String CLUB_DISC_FRMT;
	private String CLUB_BON_FRMT; 
	private String DATE_ACC_FRMT; 
	private String DATE_MOV_FRMT;
	private String BAL_ACC_FRMT; 
	private String BAL_CUR_FRMT; 
	private String BAL_BON_PER_FRMT;
	private String BAL_DISC_PER_FRMT; 
	private String IS_POSTING_TSL;
	private String DATE_LAST_USERS_CHANGES_FRMT;
	private String SUM_1_1_3_1_FRMT; 
	private String SUM_1_1_3_2_FRMT;
	private String SUM_1_1_3_3_FRMT;
	private String SUM_1_1_3_4_FRMT;
	private String SUM_1_1_8_1_FRMT; 
	private String SUM_1_1_8_2_FRMT;
	private String SUM_1_1_8_3_FRMT;
	private String SUM_1_1_8_4_FRMT;
	
	public BookkeepingTransactionItem(){
	}

	public Integer getID_TERM_SES() {
		return ID_TERM_SES;
	}

	public void setID_TERM_SES(Integer iDTERMSES) {
		ID_TERM_SES = iDTERMSES;
	}

	public Integer getID_TELGR() {
		return ID_TELGR;
	}

	public void setID_TELGR(Integer iDTELGR) {
		ID_TELGR = iDTELGR;
	}

	public Integer getID_TERM() {
		return ID_TERM;
	}

	public void setID_TERM(Integer iDTERM) {
		ID_TERM = iDTERM;
	}

	public Integer getID_SERVICE_PLACE() {
		return ID_SERVICE_PLACE;
	}

	public void setID_SERVICE_PLACE(Integer iDSERVICEPLACE) {
		ID_SERVICE_PLACE = iDSERVICEPLACE;
	}

	public String getNAME_SERVICE_PLACE() {
		return NAME_SERVICE_PLACE;
	}

	public void setNAME_SERVICE_PLACE(String nAMESERVICEPLACE) {
		NAME_SERVICE_PLACE = nAMESERVICEPLACE;
	}

	public Integer getID_SAM() {
		return ID_SAM;
	}

	public void setID_SAM(Integer iDSAM) {
		ID_SAM = iDSAM;
	}

	public Integer getNT_SAM() {
		return NT_SAM;
	}

	public void setNT_SAM(Integer nTSAM) {
		NT_SAM = nTSAM;
	}

	public Integer getID_TRANS() {
		return ID_TRANS;
	}

	public void setID_TRANS(Integer iDTRANS) {
		ID_TRANS = iDTRANS;
	}

	public String getTYPE_TRANS_TXT() {
		return TYPE_TRANS_TXT;
	}

	public void setTYPE_TRANS_TXT(String tYPETRANSTXT) {
		TYPE_TRANS_TXT = tYPETRANSTXT;
	}

	public Integer getVER_TRANS() {
		return VER_TRANS;
	}

	public void setVER_TRANS(Integer vERTRANS) {
		VER_TRANS = vERTRANS;
	}

	public String getCARD_SERIAL_NUMBER() {
		return CARD_SERIAL_NUMBER;
	}

	public void setCARD_SERIAL_NUMBER(String cARDSERIALNUMBER) {
		CARD_SERIAL_NUMBER = cARDSERIALNUMBER;
	}

	public Integer getCD_CURRENCY() {
		return CD_CURRENCY;
	}

	public void setCD_CURRENCY(Integer cDCURRENCY) {
		CD_CURRENCY = cDCURRENCY;
	}

	public String getNAME_CURRENCY() {
		return NAME_CURRENCY;
	}

	public void setNAME_CURRENCY(String nAMECURRENCY) {
		NAME_CURRENCY = nAMECURRENCY;
	}

	public Integer getNT_ICC() {
		return NT_ICC;
	}

	public void setNT_ICC(Integer nTICC) {
		NT_ICC = nTICC;
	}

	public Integer getNT_EXT() {
		return NT_EXT;
	}

	public void setNT_EXT(Integer nTEXT) {
		NT_EXT = nTEXT;
	}

	public Integer getTC_P() {
		return TC_P;
	}

	public void setTC_P(Integer tCP) {
		TC_P = tCP;
	}

	public Integer getVK_ENC() {
		return VK_ENC;
	}

	public void setVK_ENC(Integer vKENC) {
		VK_ENC = vKENC;
	}

	public String getACTION() {
		return ACTION;
	}

	public void setACTION(String aCTION) {
		ACTION = aCTION;
	}

	public Integer getCLUB_ST_PRV() {
		return CLUB_ST_PRV;
	}

	public void setCLUB_ST_PRV(Integer cLUBSTPRV) {
		CLUB_ST_PRV = cLUBSTPRV;
	}

	public Integer getERR_TX() {
		return ERR_TX;
	}

	public void setERR_TX(Integer eRRTX) {
		ERR_TX = eRRTX;
	}

	public String getERR_TX_DESC() {
		return ERR_TX_DESC;
	}

	public void setERR_TX_DESC(String eRRTXDESC) {
		ERR_TX_DESC = eRRTXDESC;
	}

	public Integer getMAC_ICC() {
		return MAC_ICC;
	}

	public void setMAC_ICC(Integer mACICC) {
		MAC_ICC = mACICC;
	}

	public Integer getMAC_PDA() {
		return MAC_PDA;
	}

	public void setMAC_PDA(Integer mACPDA) {
		MAC_PDA = mACPDA;
	}

	public Integer getID_STORNED_TRANS() {
		return ID_STORNED_TRANS;
	}

	public void setID_STORNED_TRANS(Integer iDSTORNEDTRANS) {
		ID_STORNED_TRANS = iDSTORNEDTRANS;
	}

	public Integer getID_DOUBLE_TRANS() {
		return ID_DOUBLE_TRANS;
	}

	public void setID_DOUBLE_TRANS(Integer iDDOUBLETRANS) {
		ID_DOUBLE_TRANS = iDDOUBLETRANS;
	}

	public Integer getID_ISSUER() {
		return ID_ISSUER;
	}

	public void setID_ISSUER(Integer iDISSUER) {
		ID_ISSUER = iDISSUER;
	}

	public Integer getID_PAYMENT_SYSTEM() {
		return ID_PAYMENT_SYSTEM;
	}

	public void setID_PAYMENT_SYSTEM(Integer iDPAYMENTSYSTEM) {
		ID_PAYMENT_SYSTEM = iDPAYMENTSYSTEM;
	}

	public Integer getID_PURCHASE() {
		return ID_PURCHASE;
	}

	public void setID_PURCHASE(Integer iDPURCHASE) {
		ID_PURCHASE = iDPURCHASE;
	}

	public String getCHANGED_BY_USER() {
		return CHANGED_BY_USER;
	}

	public void setCHANGED_BY_USER(String cHANGEDBYUSER) {
		CHANGED_BY_USER = cHANGEDBYUSER;
	}

	public String getUSER_WHO_HAS_CHANGED() {
		return USER_WHO_HAS_CHANGED;
	}

	public void setUSER_WHO_HAS_CHANGED(String uSERWHOHASCHANGED) {
		USER_WHO_HAS_CHANGED = uSERWHOHASCHANGED;
	}

	public String getBASES_FOR_CHANGES() {
		return BASES_FOR_CHANGES;
	}

	public void setBASES_FOR_CHANGES(String bASESFORCHANGES) {
		BASES_FOR_CHANGES = bASESFORCHANGES;
	}

	public String getENTERED_MANUALLY() {
		return ENTERED_MANUALLY;
	}

	public void setENTERED_MANUALLY(String eNTEREDMANUALLY) {
		ENTERED_MANUALLY = eNTEREDMANUALLY;
	}

	public String getIMID() {
		return IMID;
	}

	public void setIMID(String iMID) {
		IMID = iMID;
	}

	public String getSPECID() {
		return SPECID;
	}

	public void setSPECID(String sPECID) {
		SPECID = sPECID;
	}

	public String getCLUBCARD() {
		return CLUBCARD;
	}

	public void setCLUBCARD(String cLUBCARD) {
		CLUBCARD = cLUBCARD;
	}

	public String getCARDID() {
		return CARDID;
	}

	public void setCARDID(String cARDID) {
		CARDID = cARDID;
	}

	public String getID_CLUB() {
		return ID_CLUB;
	}

	public void setID_CLUB(String iDCLUB) {
		ID_CLUB = iDCLUB;
	}

	public String getLANGUAGE() {
		return LANGUAGE;
	}

	public void setLANGUAGE(String lANGUAGE) {
		LANGUAGE = lANGUAGE;
	}

	public String getCREATION_DATE() {
		return CREATION_DATE;
	}

	public void setCREATION_DATE(String cREATIONDATE) {
		CREATION_DATE = cREATIONDATE;
	}

	public String getCREATED_BY() {
		return CREATED_BY;
	}

	public void setCREATED_BY(String cREATEDBY) {
		CREATED_BY = cREATEDBY;
	}

	public String getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}

	public void setLAST_UPDATE_DATE(String lASTUPDATEDATE) {
		LAST_UPDATE_DATE = lASTUPDATEDATE;
	}

	public Integer getLAST_UPDATE_BY() {
		return LAST_UPDATE_BY;
	}

	public void setLAST_UPDATE_BY(Integer lASTUPDATEBY) {
		LAST_UPDATE_BY = lASTUPDATEBY;
	}

	public Integer getIS_REJECTED() {
		return IS_REJECTED;
	}

	public void setIS_REJECTED(Integer iSREJECTED) {
		IS_REJECTED = iSREJECTED;
	}

	public Integer getID_TERMINAL_MANUFACTURER() {
		return ID_TERMINAL_MANUFACTURER;
	}

	public void setID_TERMINAL_MANUFACTURER(Integer iDTERMINALMANUFACTURER) {
		ID_TERMINAL_MANUFACTURER = iDTERMINALMANUFACTURER;
	}

	public Integer getID_DEVICE_TYPE() {
		return ID_DEVICE_TYPE;
	}

	public void setID_DEVICE_TYPE(Integer iDDEVICETYPE) {
		ID_DEVICE_TYPE = iDDEVICETYPE;
	}

	public Integer getID_DEALER() {
		return ID_DEALER;
	}

	public void setID_DEALER(Integer iDDEALER) {
		ID_DEALER = iDDEALER;
	}

	public Integer getID_TERM_OWNER() {
		return ID_TERM_OWNER;
	}

	public void setID_TERM_OWNER(Integer iDTERMOWNER) {
		ID_TERM_OWNER = iDTERMOWNER;
	}

	public Integer getID_FINANCE_ACQUIRER() {
		return ID_FINANCE_ACQUIRER;
	}

	public void setID_FINANCE_ACQUIRER(Integer iDFINANCEACQUIRER) {
		ID_FINANCE_ACQUIRER = iDFINANCEACQUIRER;
	}

	public Integer getID_TECHNICAL_ACQUIRER() {
		return ID_TECHNICAL_ACQUIRER;
	}

	public void setID_TECHNICAL_ACQUIRER(Integer iDTECHNICALACQUIRER) {
		ID_TECHNICAL_ACQUIRER = iDTECHNICALACQUIRER;
	}

	public Integer getID_CARD_STATUS() {
		return ID_CARD_STATUS;
	}

	public void setID_CARD_STATUS(Integer iDCARDSTATUS) {
		ID_CARD_STATUS = iDCARDSTATUS;
	}

	public Integer getID_BON_CATEGORY() {
		return ID_BON_CATEGORY;
	}

	public void setID_BON_CATEGORY(Integer iDBONCATEGORY) {
		ID_BON_CATEGORY = iDBONCATEGORY;
	}

	public Integer getID_DISC_CATEGORY() {
		return ID_DISC_CATEGORY;
	}

	public void setID_DISC_CATEGORY(Integer iDDISCCATEGORY) {
		ID_DISC_CATEGORY = iDDISCCATEGORY;
	}

	public Integer getID_LOYALITY_HISTORY() {
		return ID_LOYALITY_HISTORY;
	}

	public void setID_LOYALITY_HISTORY(Integer iDLOYALITYHISTORY) {
		ID_LOYALITY_HISTORY = iDLOYALITYHISTORY;
	}

	public Integer getID_LOYALITY_BON_LINE() {
		return ID_LOYALITY_BON_LINE;
	}

	public void setID_LOYALITY_BON_LINE(Integer iDLOYALITYBONLINE) {
		ID_LOYALITY_BON_LINE = iDLOYALITYBONLINE;
	}

	public Integer getID_LOYALITY_DISC_LINE() {
		return ID_LOYALITY_DISC_LINE;
	}

	public void setID_LOYALITY_DISC_LINE(Integer iDLOYALITYDISCLINE) {
		ID_LOYALITY_DISC_LINE = iDLOYALITYDISCLINE;
	}

	public String getSTATE_TRANS_TSL() {
		return STATE_TRANS_TSL;
	}

	public void setSTATE_TRANS_TSL(String sTATETRANSTSL) {
		STATE_TRANS_TSL = sTATETRANSTSL;
	}

	public String getSYS_DATE_FULL_FRMT() {
		return SYS_DATE_FULL_FRMT;
	}

	public void setSYS_DATE_FULL_FRMT(String sYSDATEFULLFRMT) {
		SYS_DATE_FULL_FRMT = sYSDATEFULLFRMT;
	}

	public Date getSYS_DATE_FRMT() {
		return SYS_DATE;
	}

	public void setSYS_DATE_FRMT(Date sYSDATE) {
		SYS_DATE = sYSDATE;
	}

	public String getSYS_TIME_FRMT() {
		return SYS_TIME_FRMT;
	}

	public void setSYS_TIME_FRMT(String sYSTIMEFRMT) {
		SYS_TIME_FRMT = sYSTIMEFRMT;
	}

	public String getCLUB_DISC_PRV_FRMT() {
		return CLUB_DISC_PRV_FRMT;
	}

	public void setCLUB_DISC_PRV_FRMT(String cLUBDISCPRVFRMT) {
		CLUB_DISC_PRV_FRMT = cLUBDISCPRVFRMT;
	}

	public String getCLUB_BON_PRV_FRMT() {
		return CLUB_BON_PRV_FRMT;
	}

	public void setCLUB_BON_PRV_FRMT(String cLUBBONPRVFRMT) {
		CLUB_BON_PRV_FRMT = cLUBBONPRVFRMT;
	}

	public String getDATE_ACC_PRV_FRMT() {
		return DATE_ACC_PRV_FRMT;
	}

	public void setDATE_ACC_PRV_FRMT(String dATEACCPRVFRMT) {
		DATE_ACC_PRV_FRMT = dATEACCPRVFRMT;
	}

	public String getDATE_MOV_PRV_FRMT() {
		return DATE_MOV_PRV_FRMT;
	}

	public void setDATE_MOV_PRV_FRMT(String dATEMOVPRVFRMT) {
		DATE_MOV_PRV_FRMT = dATEMOVPRVFRMT;
	}

	public String getDATE_CALC_PRV_FRMT() {
		return DATE_CALC_PRV_FRMT;
	}

	public void setDATE_CALC_PRV_FRMT(String dATECALCPRVFRMT) {
		DATE_CALC_PRV_FRMT = dATECALCPRVFRMT;
	}

	public String getBAL_ACC_PRV_FRMT() {
		return BAL_ACC_PRV_FRMT;
	}

	public void setBAL_ACC_PRV_FRMT(String bALACCPRVFRMT) {
		BAL_ACC_PRV_FRMT = bALACCPRVFRMT;
	}

	public String getBAL_CUR_PRV_FRMT() {
		return BAL_CUR_PRV_FRMT;
	}

	public void setBAL_CUR_PRV_FRMT(String bALCURPRVFRMT) {
		BAL_CUR_PRV_FRMT = bALCURPRVFRMT;
	}

	public String getBAL_BON_PER_PRV_FRMT() {
		return BAL_BON_PER_PRV_FRMT;
	}

	public void setBAL_BON_PER_PRV_FRMT(String bALBONPERPRVFRMT) {
		BAL_BON_PER_PRV_FRMT = bALBONPERPRVFRMT;
	}

	public String getBAL_DISC_PER_PRV_FRMT() {
		return BAL_DISC_PER_PRV_FRMT;
	}

	public void setBAL_DISC_PER_PRV_FRMT(String bALDISCPERPRVFRMT) {
		BAL_DISC_PER_PRV_FRMT = bALDISCPERPRVFRMT;
	}

	public String getOPR_SUM_FRMT() {
		return OPR_SUM_FRMT;
	}

	public void setOPR_SUM_FRMT(String oPRSUMFRMT) {
		OPR_SUM_FRMT = oPRSUMFRMT;
	}

	public String getSUM_PAY_CASH_FRMT() {
		return SUM_PAY_CASH_FRMT;
	}

	public void setSUM_PAY_CASH_FRMT(String sUMPAYCASHFRMT) {
		SUM_PAY_CASH_FRMT = sUMPAYCASHFRMT;
	}

	public String getSUM_PAY_CARD_FRMT() {
		return SUM_PAY_CARD_FRMT;
	}

	public void setSUM_PAY_CARD_FRMT(String sUMPAYCARDFRMT) {
		SUM_PAY_CARD_FRMT = sUMPAYCARDFRMT;
	}

	public String getSUM_PAY_BON_FRMT() {
		return SUM_PAY_BON_FRMT;
	}

	public void setSUM_PAY_BON_FRMT(String sUMPAYBONFRMT) {
		SUM_PAY_BON_FRMT = sUMPAYBONFRMT;
	}

	public Float getFL_EXT_LOYL() {
		return FL_EXT_LOYL;
	}

	public void setFL_EXT_LOYL(Float fLEXTLOYL) {
		FL_EXT_LOYL = fLEXTLOYL;
	}

	public String getCLUB_SUM_FRMT() {
		return CLUB_SUM_FRMT;
	}

	public void setCLUB_SUM_FRMT(String cLUBSUMFRMT) {
		CLUB_SUM_FRMT = cLUBSUMFRMT;
	}

	public String getSUM_BON_FRMT() {
		return SUM_BON_FRMT;
	}

	public void setSUM_BON_FRMT(String sUMBONFRMT) {
		SUM_BON_FRMT = sUMBONFRMT;
	}

	public String getSUM_BON_CASH_FRMT() {
		return SUM_BON_CASH_FRMT;
	}

	public void setSUM_BON_CASH_FRMT(String sUMBONCASHFRMT) {
		SUM_BON_CASH_FRMT = sUMBONCASHFRMT;
	}

	public String getSUM_BON_CARD_FRMT() {
		return SUM_BON_CARD_FRMT;
	}

	public void setSUM_BON_CARD_FRMT(String sUMBONCARDFRMT) {
		SUM_BON_CARD_FRMT = sUMBONCARDFRMT;
	}

	public String getSUM_BON_BON_FRMT() {
		return SUM_BON_BON_FRMT;
	}

	public void setSUM_BON_BON_FRMT(String sUMBONBONFRMT) {
		SUM_BON_BON_FRMT = sUMBONBONFRMT;
	}

	public String getSUM_BON_DISC_FRMT() {
		return SUM_BON_DISC_FRMT;
	}

	public void setSUM_BON_DISC_FRMT(String sUMBONDISCFRMT) {
		SUM_BON_DISC_FRMT = sUMBONDISCFRMT;
	}

	public String getSUM_DISC_FRMT() {
		return SUM_DISC_FRMT;
	}

	public void setSUM_DISC_FRMT(String sUMDISCFRMT) {
		SUM_DISC_FRMT = sUMDISCFRMT;
	}

	public String getCASH_CARD_NR() {
		return CASH_CARD_NR;
	}

	public void setCASH_CARD_NR(String cASHCARDNR) {
		CASH_CARD_NR = cASHCARDNR;
	}

	public String getC_NR() {
		return C_NR;
	}

	public void setC_NR(String cNR) {
		C_NR = cNR;
	}

	public String getC_CHECK_NR() {
		return C_CHECK_NR;
	}

	public void setC_CHECK_NR(String cCHECKNR) {
		C_CHECK_NR = cCHECKNR;
	}

	public String getCLUB_ST() {
		return CLUB_ST;
	}

	public void setCLUB_ST(String cLUBST) {
		CLUB_ST = cLUBST;
	}

	public String getCLUB_DISC_FRMT() {
		return CLUB_DISC_FRMT;
	}

	public void setCLUB_DISC_FRMT(String cLUBDISCFRMT) {
		CLUB_DISC_FRMT = cLUBDISCFRMT;
	}

	public String getCLUB_BON_FRMT() {
		return CLUB_BON_FRMT;
	}

	public void setCLUB_BON_FRMT(String cLUBBONFRMT) {
		CLUB_BON_FRMT = cLUBBONFRMT;
	}

	public String getDATE_ACC_FRMT() {
		return DATE_ACC_FRMT;
	}

	public void setDATE_ACC_FRMT(String dATEACCFRMT) {
		DATE_ACC_FRMT = dATEACCFRMT;
	}

	public String getDATE_MOV_FRMT() {
		return DATE_MOV_FRMT;
	}

	public void setDATE_MOV_FRMT(String dATEMOVFRMT) {
		DATE_MOV_FRMT = dATEMOVFRMT;
	}

	public String getBAL_ACC_FRMT() {
		return BAL_ACC_FRMT;
	}

	public void setBAL_ACC_FRMT(String bALACCFRMT) {
		BAL_ACC_FRMT = bALACCFRMT;
	}

	public String getBAL_CUR_FRMT() {
		return BAL_CUR_FRMT;
	}

	public void setBAL_CUR_FRMT(String bALCURFRMT) {
		BAL_CUR_FRMT = bALCURFRMT;
	}

	public String getBAL_BON_PER_FRMT() {
		return BAL_BON_PER_FRMT;
	}

	public void setBAL_BON_PER_FRMT(String bALBONPERFRMT) {
		BAL_BON_PER_FRMT = bALBONPERFRMT;
	}

	public String getBAL_DISC_PER_FRMT() {
		return BAL_DISC_PER_FRMT;
	}

	public void setBAL_DISC_PER_FRMT(String bALDISCPERFRMT) {
		BAL_DISC_PER_FRMT = bALDISCPERFRMT;
	}

	public String getIS_POSTING_TSL() {
		return IS_POSTING_TSL;
	}

	public void setIS_POSTING_TSL(String iSPOSTINGTSL) {
		IS_POSTING_TSL = iSPOSTINGTSL;
	}

	public String getDATE_LAST_USERS_CHANGES_FRMT() {
		return DATE_LAST_USERS_CHANGES_FRMT;
	}

	public void setDATE_LAST_USERS_CHANGES_FRMT(String dATELASTUSERSCHANGESFRMT) {
		DATE_LAST_USERS_CHANGES_FRMT = dATELASTUSERSCHANGESFRMT;
	}

	public String getSUM_1_1_3_1_FRMT() {
		return SUM_1_1_3_1_FRMT;
	}

	public void setSUM_1_1_3_1_FRMT(String sUM_1_1_3_1FRMT) {
		SUM_1_1_3_1_FRMT = sUM_1_1_3_1FRMT;
	}

	public String getSUM_1_1_3_2_FRMT() {
		return SUM_1_1_3_2_FRMT;
	}

	public void setSUM_1_1_3_2_FRMT(String sUM_1_1_3_2FRMT) {
		SUM_1_1_3_2_FRMT = sUM_1_1_3_2FRMT;
	}

	public String getSUM_1_1_3_3_FRMT() {
		return SUM_1_1_3_3_FRMT;
	}

	public void setSUM_1_1_3_3_FRMT(String sUM_1_1_3_3FRMT) {
		SUM_1_1_3_3_FRMT = sUM_1_1_3_3FRMT;
	}

	public String getSUM_1_1_3_4_FRMT() {
		return SUM_1_1_3_4_FRMT;
	}

	public void setSUM_1_1_3_4_FRMT(String sUM_1_1_3_4FRMT) {
		SUM_1_1_3_4_FRMT = sUM_1_1_3_4FRMT;
	}

	public String getSUM_1_1_8_1_FRMT() {
		return SUM_1_1_8_1_FRMT;
	}

	public void setSUM_1_1_8_1_FRMT(String sUM_1_1_8_1FRMT) {
		SUM_1_1_8_1_FRMT = sUM_1_1_8_1FRMT;
	}

	public String getSUM_1_1_8_2_FRMT() {
		return SUM_1_1_8_2_FRMT;
	}

	public void setSUM_1_1_8_2_FRMT(String sUM_1_1_8_2FRMT) {
		SUM_1_1_8_2_FRMT = sUM_1_1_8_2FRMT;
	}

	public String getSUM_1_1_8_3_FRMT() {
		return SUM_1_1_8_3_FRMT;
	}

	public void setSUM_1_1_8_3_FRMT(String sUM_1_1_8_3FRMT) {
		SUM_1_1_8_3_FRMT = sUM_1_1_8_3FRMT;
	}

	public String getSUM_1_1_8_4_FRMT() {
		return SUM_1_1_8_4_FRMT;
	}

	public void setSUM_1_1_8_4_FRMT(String sUM_1_1_8_4FRMT) {
		SUM_1_1_8_4_FRMT = sUM_1_1_8_4FRMT;
	}
	
}

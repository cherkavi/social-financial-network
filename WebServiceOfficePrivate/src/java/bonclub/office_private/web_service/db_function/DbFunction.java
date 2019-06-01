package bonclub.office_private.web_service.db_function;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import bonclub.office_private.web_service.common_objects.CommodityInformation;
import bonclub.office_private.web_service.common_objects.ParentMessage;
import bonclub.office_private.web_service.common_objects.Purse;
import bonclub.office_private.web_service.common_objects.RatingsUser;
import bonclub.office_private.web_service.db_function.wrap.Wrap;
import bonclub.office_private.web_service.db_function.wrap.WrapPerson;
import bonclub.office_private.web_service.db_function.wrap.WrapPersonParameters;

/** �����, ������� �������� �������, ����������� ��� ������� � ����� ������ */
public class DbFunction {
	private final static String VALID_FUNCTION_RESPONSE = "0";

	/** ������� ����� ������������� */
	public static enum workGroup {
		MANAGER, PRIVATE, EMPLOYEE, WORKER, HOUSEWIFE, PENSIONER, STUDENT, UNEMPLOYED, OTHER_VARIANT
	};

	/** ��� ������������ */
	public static enum sex {
		M, F
	};

	/** ���� */
	public static enum language {
		UA, RU, EN
	};

	private static Logger logger = Logger.getLogger(DbFunction.class);

	/**
	 * �������� ��������� �� ������������
	 * <table border=1>
	 * <tr>
	 * <td>1</td>
	 * <td>p_id_nat_prs</td>
	 * <td>IN</td>
	 * <td>NUMBER</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>bonCardShort</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>p_password</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>p_surname</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>p_name</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>p_patronymic</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>p_date_of_birth</td>
	 * <td>IN</td>
	 * <td>DATE</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>p_sex</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>p_fact_code_country</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>p_fact_adr_zip_code</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>p_fact_adr_id_oblast</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>p_fact_adr_district</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>p_fact_adr_city</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>p_fact_adr_street</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>p_fact_adr_house</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>p_fact_adr_case</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>p_fact_adr_apartment</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>p_phone_home</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>p_phone_mobile</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>p_email</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>p_cd_nat_prs_group</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>p_name_other_variant_group</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>p_language</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>24</td>
	 * <td>p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>25</td>
	 * <td>p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>
	 * <li><b>0</b> �������� ��������</li>
	 * <li><b>1</b> ����� �� �������</li>
	 * <li><b>2</b> ������� �� �������</li>
	 * <li><b>3</b> ������������ ����� ��� ������</li></td>
	 * </tr>
	 * </table>
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param userId
	 *            ���������� ������������� ������������
	 * @param bonCardNumberShort
	 *            - �������� ( 7-������� ) ����� �����
	 * @param password
	 *            - ������
	 * @param surname
	 *            - �������
	 * @param name
	 *            ���
	 * @param fatherName
	 *            ��������
	 * @param dateOfBirth
	 *            ���� ��������
	 * @param sex
	 *            ��� (�������������)
	 * @param codeCountry
	 *            ��� ������
	 * @param zipCode
	 *            �������� ��� ������ ������
	 * @param idOblast
	 *            ������������� �������
	 * @param adrDistrict
	 *            ������������� ������
	 * @param adrCity
	 *            ������������� ������
	 * @param adrStreet
	 *            �����
	 * @param adrHouse
	 *            ���
	 * @param adrCase
	 *            ������ ����
	 * @param adrApartment
	 *            ��������
	 * @param phoneHome
	 *            ������� ���.
	 * @param phoneMobile
	 *            ������� ���.
	 * @param email
	 *            E-mail
	 * @param personGroup
	 *            � ����� ������� ������ �����������
	 * @param otherVariantGroup
	 *            ������ ������� ���. ������
	 * @param language
	 *            ���� (�������������)
	 * @return {@link WrapUpdatePerson}
	 */
	public void updatePerson(Connection connection, int userId,// 2
			String bonCardNumberShort, // 3
			String password,// 4
			String surname,// 5
			String name,// 6
			String fatherName,// 7
			Date dateOfBirth,// 8
			String sex,// 9
			String codeCountry,// 10
			String zipCode, // 11
			String idOblast,// 12
			String adrDistrict,// 13
			String adrCity,// 14
			String adrStreet,// 15
			String adrHouse,// 16
			String adrCase,// 17
			String adrApartment,// 18
			String phoneHome,// 19
			String phoneMobile,// 20
			String email,// 21
			String personGroup,// 22
			String otherVariantGroup,// 23
			String language// 24
	) throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.update_nat_prs(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, userId);
			statement.setString(3, bonCardNumberShort);
			statement.setString(4, password);
			statement.setString(5, surname);
			statement.setString(6, name);
			statement.setString(7, fatherName);
			statement.setDate(8, new java.sql.Date(dateOfBirth.getTime()));
			String baseSex = "U";
			try {
				if (sex.trim().equalsIgnoreCase("�������")) {
					baseSex = "M";
				}
				if (sex.trim().equalsIgnoreCase("�������")) {
					baseSex = "F";
				}
			} catch (Exception ex) {
			}
			;
			statement.setString(9, baseSex);
			statement.setString(10, codeCountry);
			statement.setString(11, zipCode);
			statement.setString(12, idOblast);
			statement.setString(13, adrDistrict);
			statement.setString(14, adrCity);
			statement.setString(15, adrStreet);
			statement.setString(16, adrHouse);
			statement.setString(17, adrCase);
			statement.setString(18, adrApartment);
			statement.setString(19, phoneHome);
			statement.setString(20, phoneMobile);
			statement.setString(21, email);
			statement.setString(22, personGroup);
			statement.setString(23, otherVariantGroup);
			statement.setString(24, language);
			statement.registerOutParameter(25, Types.VARCHAR);
			statement.executeUpdate();

			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(25));
			}
			logger.debug("updatePerson Ok:");
		} finally {
			safeCloseStatement(statement);
		}
	}

	/**
	 * ��������� ������� �� ������������� �� ������ ����� � ������
	 * <table border=1>
	 * <tr>
	 * <td>1</td>
	 * <td>output</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>������� ���������� �������
	 * <ul>
	 * <li><b>0</b>- ��������� �������</li>
	 * <li><b>!=0</b> - ��� ������</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>p_cd_card2</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� ����� �����</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>p_password</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>������</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>p_language</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>���� ������ ������</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>p_id_nat_prs</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>���������� ���</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>p_club_registration_date</td>
	 * <td>OUT</td>
	 * <td>DATE</td>
	 * <td>���� ����������� � �����</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>p_card_serial_number</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� ����� �����</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>p_card_id_issuer</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>�� �������� �����</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>p_card_name_issuer</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� �������� �����</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>p_card_id_payment_system</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>�� ��������� ������� ����� �����</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>p_card_name_payment_system</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� ��������� ������� ����� �����</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>p_cd_card1</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>����� ���-�����</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>p_name_card_state</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ���-�����</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>p_name_card_status</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>����</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>p_name_bon_category</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ���-����� (����)</td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>p_name_disc_category</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ���-����� (������)</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>p_bal_cur</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>�������� ����� �� ����� (�������)</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>p_bal_acc</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>��������� ����� �� ����� (�������)</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>p_bal_bon_per</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ����� �� ������ (�������)</td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>p_bal_disc_per</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ������ (�������)</td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>p_next_date_mov</td>
	 * <td>OUT</td>
	 * <td>DATE</td>
	 * <td>��������� ���� �������� ����� � ���������</td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>p_next_date_calc</td>
	 * <td>OUT</td>
	 * <td>DATE</td>
	 * <td>��������� ���� �������� ���������</td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>p_surname</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������:</td>
	 * </tr>
	 * <tr>
	 * <td>24</td>
	 * <td>p_name</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>���:</td>
	 * </tr>
	 * <tr>
	 * <td>25</td>
	 * <td>p_patronymic</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������:</td>
	 * </tr>
	 * <tr>
	 * <td>26</td>
	 * <td>p_date_of_birth</td>
	 * <td>OUT</td>
	 * <td>DATE</td>
	 * <td>���� ��������:</td>
	 * </tr>
	 * <tr>
	 * <td>27</td>
	 * <td>p_sex</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>���:</td>
	 * </tr>
	 * <tr>
	 * <td>28</td>
	 * <td>p_fact_code_country</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��� ������</td>
	 * </tr>
	 * <tr>
	 * <td>29</td>
	 * <td>p_fact_adr_zip_code</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>������</td>
	 * </tr>
	 * <tr>
	 * <td>30</td>
	 * <td>p_fact_adr_id_oblast</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��� �������</td>
	 * </tr>
	 * <tr>
	 * <td>31</td>
	 * <td>p_fact_adr_name_oblast</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������</td>
	 * </tr>
	 * <tr>
	 * <td>32</td>
	 * <td>p_fact_adr_district</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�����</td>
	 * </tr>
	 * <tr>
	 * <td>33</td>
	 * <td>p_fact_adr_city</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�����</td>
	 * </tr>
	 * <tr>
	 * <td>34</td>
	 * <td>p_fact_adr_street</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�����</td>
	 * </tr>
	 * <tr>
	 * <td>35</td>
	 * <td>p_fact_adr_house</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>���</td>
	 * </tr>
	 * <tr>
	 * <td>36</td>
	 * <td>p_fact_adr_case</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>������</td>
	 * </tr>
	 * <tr>
	 * <td>37</td>
	 * <td>p_fact_adr_apartment</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>����� ��������</td>
	 * </tr>
	 * <tr>
	 * <td>38</td>
	 * <td>p_phone_home</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� �������</td>
	 * </tr>
	 * <tr>
	 * <td>39</td>
	 * <td>p_phone_mobile</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� �������</td>
	 * </tr>
	 * <tr>
	 * <td>40</td>
	 * <td>p_email</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>e-mail</td>
	 * </tr>
	 * <tr>
	 * <td>41</td>
	 * <td>p_cd_nat_prs_group</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� ��������� ������� �� ���� ������</td>
	 * </tr>
	 * <tr>
	 * <td>42</td>
	 * <td>p_name_nat_prs_group</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>��������� - ��������� �������������</td>
	 * </tr>
	 * <tr>
	 * <td>43</td>
	 * <td>p_name_other_variant_group</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>������ ������� ��������� - �� �� ������</td>
	 * </tr>
	 * <tr>
	 * <td>44</td>
	 * <td>,p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>
	 * <li><b>0</b> �������� ��������</li>
	 * <li><b>1</b> ����� �� �������</li>
	 * <li><b>2</b> ������� �� �������</li>
	 * <li><b>3</b> ������������ ����� ��� ������</li></td>
	 * </tr>
	 * </table>
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param cardNumber
	 *            ���������� ����� �����
	 * @param password
	 *            ������, ���������� ��� ����������� ������������ � �����������
	 *            SMS
	 * @param language
	 *            - ���� ( ���������������� ���������� )
	 * */
	public WrapPerson checkPerson(Connection connection, String cardNumber,
			String password, String language) throws DbException, SQLException {
		WrapPerson returnValue = null;
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.check_nat_prs(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			String newNumber = SharedFunction.getShortBoncardNumber(cardNumber);
			statement.setString(2, newNumber);// p_cd_card2 IN VARCHAR2 /**
												// �������� ����� ����� */
			statement.setString(3, password);// p_password IN VARCHAR2 /**
												// ������ */
			statement.setString(4, language);// p_language IN VARCHAR2 /** ����
												// ������ ������ */
			statement.registerOutParameter(5, Types.INTEGER); // p_id_nat_prs
																// OUT NUMBER
																// /**
																// ����������
																// ��� */
			statement.registerOutParameter(6, Types.DATE); // p_club_registration_date
															// OUT DATE /** ����
															// ����������� �
															// ����� */

			statement.registerOutParameter(7, Types.VARCHAR); // p_card_serial_number
																// OUT VARCHAR2
																// /** ��������
																// ����� �����
																// */
			statement.registerOutParameter(8, Types.INTEGER); // p_card_id_issuer
																// OUT NUMBER
																// /** ��
																// ��������
																// ����� */
			statement.registerOutParameter(9, Types.VARCHAR); // p_card_name_issuer
																// OUT VARCHAR2
																// /** ��������
																// ��������
																// ����� */

			statement.registerOutParameter(10, Types.INTEGER); // p_card_id_payment_system
																// OUT NUMBER
																// /** ��
																// ���������
																// ������� �����
																// ����� */
			statement.registerOutParameter(11, Types.VARCHAR); // p_card_name_payment_system
																// OUT NUMBER
																// /** ��������
																// ���������
																// ������� �����
																// */

			statement.registerOutParameter(12, Types.VARCHAR); // p_cd_card1 OUT
																// VARCHAR2 /**
																// �����
																// ���-����� */
			statement.registerOutParameter(13, Types.VARCHAR); // p_name_card_state
																// OUT VARCHAR2
																// /** ���������
																// ���-����� */
			statement.registerOutParameter(14, Types.VARCHAR); // p_name_card_status
																// OUT VARCHAR2
																// /** ���� */
			statement.registerOutParameter(15, Types.VARCHAR); // p_name_bon_category
																// OUT VARCHAR2
																// /** ���������
																// ���-�����
																// (����)*/
			statement.registerOutParameter(16, Types.VARCHAR); // p_name_disc_category
																// OUT VARCHAR2
																// /** ���������
																// ���-�����
																// (������)*/
			statement.registerOutParameter(17, Types.INTEGER); // p_bal_cur OUT
																// NUMBER /**
																// ��������
																// ����� ��
																// �����
																// (�������) */
			statement.registerOutParameter(18, Types.INTEGER); // p_bal_acc OUT
																// NUMBER /**
																// ���������
																// ����� ��
																// �����
																// (�������) */

			statement.registerOutParameter(19, Types.VARCHAR); // p_bal_bon_per
																// OUT VARCHAR2
																// /** ���������
																// ����� ��
																// ������
																// (�������) */
			statement.registerOutParameter(20, Types.VARCHAR); // p_bal_disc_per
																// OUT VARCHAR2
																// /** ���������
																// ������
																// (�������) */
			statement.registerOutParameter(21, Types.DATE); // p_next_date_mov
															// OUT DATE /**
															// ��������� ����
															// �������� ����� �
															// ��������� */
			statement.registerOutParameter(22, Types.DATE); // p_next_date_calc
															// OUT DATE /**
															// ��������� ����
															// ��������
															// ��������� */

			statement.registerOutParameter(23, Types.VARCHAR); // p_surname OUT
																// VARCHAR2 /**
																// �������: */
			statement.registerOutParameter(24, Types.VARCHAR); // p_name OUT
																// VARCHAR2 /**
																// ���: */
			statement.registerOutParameter(25, Types.VARCHAR); // p_patronymic
																// OUT VARCHAR2
																// /** ��������:
																// */
			statement.registerOutParameter(26, Types.DATE); // p_date_of_birth
															// OUT DATE /** ����
															// ��������: */
			statement.registerOutParameter(27, Types.VARCHAR); // p_sex OUT
																// VARCHAR2 /**
																// ���: */
			statement.registerOutParameter(28, Types.VARCHAR); // p_fact_code_country
																// OUT VARCHAR2
																// /** ���
																// ������ */
			statement.registerOutParameter(29, Types.VARCHAR); // p_fact_adr_zip_code
																// OUT VARCHAR2
																// /** ������ */
			statement.registerOutParameter(30, Types.VARCHAR); // p_fact_adr_id_oblast
																// OUT VARCHAR2
																// /** ���
																// ������� */
			statement.registerOutParameter(31, Types.VARCHAR); // p_fact_adr_name_oblast
																// OUT VARCHAR2
																// /** �������
																// */
			statement.registerOutParameter(32, Types.VARCHAR); // p_fact_adr_district
																// OUT VARCHAR2
																// /** ����� */
			statement.registerOutParameter(33, Types.VARCHAR); // p_fact_adr_city
																// OUT VARCHAR2
																// /** ����� */
			statement.registerOutParameter(34, Types.VARCHAR); // p_fact_adr_street
																// OUT VARCHAR2
																// /** ����� */
			statement.registerOutParameter(35, Types.VARCHAR); // p_fact_adr_house
																// OUT VARCHAR2
																// /** ��� */
			statement.registerOutParameter(36, Types.VARCHAR); // p_fact_adr_case
																// OUT VARCHAR2
																// /** ������ */
			statement.registerOutParameter(37, Types.VARCHAR); // p_fact_adr_apartment
																// OUT VARCHAR2
																// /** �����
																// �������� */
			statement.registerOutParameter(38, Types.VARCHAR); // p_phone_home
																// OUT VARCHAR2
																// /** ��������
																// ������� */
			statement.registerOutParameter(39, Types.VARCHAR); // p_phone_mobile
																// OUT VARCHAR2
																// /** ���������
																// ������� */
			statement.registerOutParameter(40, Types.VARCHAR); // p_email OUT
																// VARCHAR2 /**
																// e-mail */
			statement.registerOutParameter(41, Types.VARCHAR); // p_cd_nat_prs_group
																// OUT VARCHAR2
																// /** ���������
																// ���������
																// ������� ��
																// ���� ������
																// */
			statement.registerOutParameter(42, Types.VARCHAR); // p_name_nat_prs_group
																// OUT VARCHAR2
																// /** ���������
																// - ���������
																// �������������
																// */
			statement.registerOutParameter(43, Types.VARCHAR); // p_name_other_variant_group
																// OUT VARCHAR2
																// /** ������
																// �������
																// ��������� -
																// �� �� ������
																// */
			statement.registerOutParameter(44, Types.VARCHAR); // p_result_msg
			statement.executeUpdate();
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(44));
			}
			System.out.println("Function Output: " + statement.getString(1));
			returnValue = new WrapPerson();
			// p_id_nat_prs OUT NUMBER /** ���������� ��� */
			returnValue.setId(statement.getInt(5));
			// p_club_registration_date OUT DATE /** ���� ����������� � ����� */
			returnValue.setRegistrationDate(statement.getDate(6));
			// p_card_serial_number OUT VARCHAR2 /** �������� ����� ����� */
			returnValue.setCardSerialNumber(statement.getString(7));
			// p_card_id_issuer OUT NUMBER /** �� �������� ����� */
			returnValue.setIdEmitentCard(statement.getInt(8));
			// p_card_name_issuer OUT VARCHAR2 /** �������� �������� ����� */
			returnValue.setNameEmitentCard(statement.getString(9));
			// p_card_id_payment_system OUT NUMBER /** �� ��������� �������
			// ����� ����� */
			returnValue.setIdPaymentSystem(statement.getInt(10));
			// p_card_name_payment_system OUT VARCHAR2 /** �������� ���������
			// ������� ����� ����� */
			returnValue.setNamePaymentSystem(statement.getString(11));
			// p_cd_card1 OUT VARCHAR2 /** ����� ���-����� */
			returnValue.setCdCard(statement.getString(12));
			// p_name_card_state OUT VARCHAR2 /** ��������� ���-����� */
			returnValue.setCardState(statement.getString(13));
			// ������ � �����
			returnValue.setPassword(password);
			// p_name_card_status OUT VARCHAR2 /** ���� */
			returnValue.setCardStatus(statement.getString(14));
			// p_name_bon_category OUT VARCHAR2 /** ��������� ���-����� (����)*/
			returnValue.setCardCategory(statement.getString(15));
			// p_name_disc_category OUT VARCHAR2 /** ��������� ���-�����
			// (������)*/
			returnValue.setDiscCategory(statement.getString(16));
			// p_bal_cur OUT NUMBER /** �������� ����� �� ����� (�������) */
			returnValue.setBalCur(statement.getInt(17));
			// p_bal_acc OUT NUMBER /** ��������� ����� �� ����� (�������) */
			returnValue.setBalAcc(statement.getInt(18));
			// p_bal_bon_per OUT VARCHAR2 /** ��������� ����� �� ������
			// (�������) */
			returnValue.setBalBonPer(statement.getString(19));
			// p_bal_disc_per OUT VARCHAR2 /** ��������� ������ (�������) */
			returnValue.setBalDiscPer(statement.getString(20));
			// p_next_date_mov OUT DATE /** ��������� ���� �������� ����� �
			// ��������� */
			returnValue.setNextDateMov(statement.getDate(21));
			// p_next_date_calc OUT DATE /** ��������� ���� �������� ���������
			// */
			returnValue.setNextDateCalc(statement.getDate(22));
			// p_surname OUT VARCHAR2 /** �������: */
			returnValue.setSurname(statement.getString(23));
			// p_name OUT VARCHAR2 /** ���: */
			returnValue.setName(statement.getString(24));
			// p_patronymic OUT VARCHAR2 /** ��������: */
			returnValue.setFatherName(statement.getString(25));
			// p_date_of_birth OUT DATE /** ���� ��������: */
			returnValue.setDateOfBirth(statement.getDate(26));
			// p_sex OUT VARCHAR2 /** ���: */
			returnValue.setSex(statement.getString(27));
			// p_fact_code_country OUT VARCHAR2 /** ��� ������ */
			returnValue.setCodeCountry(statement.getString(28));
			// p_fact_adr_zip_code OUT VARCHAR2 /** ������ */
			returnValue.setZipCode(statement.getString(29));
			// p_fact_adr_id_oblast OUT VARCHAR2 /** ��� ������� */
			returnValue.setAdrOblastId(statement.getString(30));
			// p_fact_adr_name_oblast OUT VARCHAR2 /** ������� */
			returnValue.setAdrOblastName(statement.getString(31));
			// p_fact_adr_district OUT VARCHAR2 /** ����� */
			returnValue.setAdrDiscrict(statement.getString(32));
			// p_fact_adr_city OUT VARCHAR2 /** ����� */
			returnValue.setAdrCity(statement.getString(33));
			// p_fact_adr_street OUT VARCHAR2 /** ����� */
			returnValue.setAdrStreet(statement.getString(34));
			// p_fact_adr_house OUT VARCHAR2 /** ��� */
			returnValue.setAdrHouse(statement.getString(35));
			// p_fact_adr_case OUT VARCHAR2 /** ������ */
			returnValue.setAdrCase(statement.getString(36));
			// p_fact_adr_apartment OUT VARCHAR2 /** ����� �������� */
			returnValue.setAdrApartment(statement.getString(37));
			// p_phone_home OUT VARCHAR2 /** �������� ������� */
			returnValue.setPhoneHome(statement.getString(38));
			// p_phone_mobile OUT VARCHAR2 /** ��������� ������� */
			returnValue.setPhoneMobile(statement.getString(39));
			// p_email OUT VARCHAR2 /** e-mail */
			returnValue.setEmail(statement.getString(40));
			// p_cd_nat_prs_group OUT VARCHAR2 /** ��������� ��������� �������
			// �� ���� ������ */
			returnValue.setProfGroup(statement.getString(41));
			// p_name_nat_prs_group OUT VARCHAR2 /** ��������� - ���������
			// ������������� */
			returnValue.setProfGroupText(statement.getString(42));
			// p_name_other_variant_group OUT VARCHAR2 /** ������ �������
			// ��������� - �� �� ������ */
			returnValue.setProfGroupOtherVariant(statement.getString(43));
			// ��������� ���������� �����
			returnValue.setResultMessage(statement.getString(44));
			returnValue.setReturnValue(statement.getString(1));
			logger.debug("checkPerson Ok: ");
		} finally {
			safeCloseStatement(statement);
		}
		return returnValue;
	}

	/**
	 * ��������� ���-����� �� �������������
	 * <table>
	 * <tr>
	 * <td>1</td>
	 * <td>p_cd_card2</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>�������� ����� �����
	 * <td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>,p_language</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>���� ������ ������
	 * <td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>,p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>
	 * <li><b>0</b> �������� ��������</li>
	 * <li><b>1</b> ����� �� �������</li>
	 * <li><b>2</b> ������� �� �������</li>
	 * <li><b>3</b> ������������ ����� ��� ������</li></td>
	 * </tr>
	 * </table>
	 * 
	 * @throws exception
	 *             , when bonCard is not valid
	 * */
	public void checkBonCard(Connection connection, String cardNumber,
			String language) throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.check_bon_card(?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setString(2,
					SharedFunction.getShortBoncardNumber(cardNumber));
			statement.setString(3, language);
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.executeUpdate();
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(4));
			}
			logger.debug("checkBonCard Ok: ");
		} finally {
			this.safeCloseStatement(statement);
		}
	}

	/**
	 * �������� ��������� �������
	 * <table border=1>
	 * <tr>
	 * <td>1</td>
	 * <td>p_id_nat_prs</td>
	 * <td>IN</td>
	 * <td>NUMBER</td>
	 * <tr>
	 * <td>2</td>
	 * <td>,p_language</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <tr>
	 * <td>3</td>
	 * <td>,p_password</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>,p_club_registration_date</td>
	 * <td>OUT</td>
	 * <td>DATE</td>
	 * <td>���� ����������� � �����</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>,p_fact_code_country</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>,p_fact_adr_zip_code</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>,p_fact_adr_id_oblast</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>,p_fact_adr_district</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>,p_fact_adr_city</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>,p_fact_adr_street</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>,p_fact_adr_house</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>,p_fact_adr_case</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>,p_fact_adr_apartment</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>,p_phone_home</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>,p_phone_mobile</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>,p_email</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>,p_cd_nat_prs_group</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>,p_name_other_variant_group</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>,p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>
	 * <li><b>0</b> �������� ��������</li>
	 * <li><b>1</b> ����� �� �������</li>
	 * <li><b>2</b> ������� �� �������</li>
	 * <li><b>3</b> ������������ ����� ��� ������</li></td>
	 * </tr>
	 * </table>
	 * */
	public WrapPersonParameters personParameters(Connection connection,
			int idPerson, String language) throws SQLException, DbException {
		WrapPersonParameters returnValue = null;
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.get_nat_prs_param(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			/** 2 p_id_nat_prs IN NUMBER */
			statement.setInt(2, idPerson);
			/** 3 p_language IN VARCHAR2 */
			statement.setString(3, language);
			/** 4 p_club_registration_date OUT DATE ���� ����������� � ����� */
			statement.registerOutParameter(4, Types.DATE);// p_club_registration_date
			/** 5 p_surname OUT VARCHAR2 �������: */
			statement.registerOutParameter(5, Types.VARCHAR);
			/** 6 p_name OUT VARCHAR2 ���: */
			statement.registerOutParameter(6, Types.VARCHAR);
			/** 7 p_patronymic OUT VARCHAR2 ��������: */
			statement.registerOutParameter(7, Types.VARCHAR);
			/** 8 p_date_of_birth OUT DATE ���� ��������: */
			statement.registerOutParameter(8, Types.DATE);
			/** 9 p_sex OUT VARCHAR2 ���: */
			statement.registerOutParameter(9, Types.VARCHAR);
			/** 10 p_fact_code_country OUT VARCHAR2 */
			statement.registerOutParameter(10, Types.VARCHAR);
			/** 11 p_fact_adr_zip_code OUT VARCHAR2 */
			statement.registerOutParameter(11, Types.VARCHAR);
			/** 12 p_fact_adr_id_oblast OUT VARCHAR2 */
			statement.registerOutParameter(12, Types.VARCHAR);
			/** 13 p_fact_adr_name_oblast OUT VARCHAR2 ������� */
			statement.registerOutParameter(13, Types.VARCHAR);
			/** 14 p_fact_adr_district OUT VARCHAR2 */
			statement.registerOutParameter(14, Types.VARCHAR);
			/** 15 p_fact_adr_city OUT VARCHAR2 */
			statement.registerOutParameter(15, Types.VARCHAR);
			/** 16 p_fact_adr_street OUT VARCHAR2 */
			statement.registerOutParameter(16, Types.VARCHAR);
			/** 17 p_fact_adr_house OUT VARCHAR2 */
			statement.registerOutParameter(17, Types.VARCHAR);
			/** 18 p_fact_adr_case OUT VARCHAR2 */
			statement.registerOutParameter(18, Types.VARCHAR);
			/** 19 p_fact_adr_apartment OUT VARCHAR2 */
			statement.registerOutParameter(19, Types.VARCHAR);
			/** 20 p_phone_home OUT VARCHAR2 */
			statement.registerOutParameter(20, Types.VARCHAR);
			/** 21 p_phone_mobile OUT VARCHAR2 */
			statement.registerOutParameter(21, Types.VARCHAR);
			/** 22 p_email OUT VARCHAR2 */
			statement.registerOutParameter(22, Types.VARCHAR);
			/** 23 p_cd_nat_prs_group OUT VARCHAR2 */
			statement.registerOutParameter(23, Types.VARCHAR);
			/**
			 * 24 p_name_nat_prs_group OUT VARCHAR2 ��������� - ���������
			 * �������������
			 */
			statement.registerOutParameter(24, Types.VARCHAR);
			/** 25 p_name_other_variant_group OUT VARCHAR2 */
			statement.registerOutParameter(25, Types.VARCHAR);
			/** 26 p_result_msg OUT VARCHAR2 */
			statement.registerOutParameter(26, Types.VARCHAR);
			statement.executeUpdate();
			returnValue = new WrapPersonParameters();
			returnValue.setReturnValue(statement.getString(1));
			/** 26 p_result_msg OUT VARCHAR2 */
			returnValue.setResultMessage(statement.getString(26));
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(26));
			}

			/** 2 p_id_nat_prs IN NUMBER */
			/** 3 p_language IN VARCHAR2 */
			/** 4 p_club_registration_date OUT DATE /** ���� ����������� � ����� */
			returnValue.setRegistrationDate(statement.getDate(4));
			/** 5 p_surname OUT VARCHAR2 /** �������: */
			returnValue.setSurname(statement.getString(5));
			/** 6 p_name OUT VARCHAR2 /** ���: */
			returnValue.setName(statement.getString(6));
			/** 7 p_patronymic OUT VARCHAR2 /** ��������: */
			returnValue.setFatherName(statement.getString(7));
			/** 8 p_date_of_birth OUT DATE /** ���� ��������: */
			returnValue.setBirthDay(statement.getDate(8));
			/** 9 p_sex OUT VARCHAR2 /** ���: */
			returnValue.setSex(statement.getString(9));
			/** 10 p_fact_code_country OUT VARCHAR2 */
			returnValue.setCodeCountry(statement.getString(10));
			/** 11 p_fact_adr_zip_code OUT VARCHAR2 */
			returnValue.setAdrZipCode(statement.getString(11));
			/** 12 p_fact_adr_id_oblast OUT VARCHAR2 */
			returnValue.setAdrIdOblast(statement.getString(12));
			/** 13 p_fact_adr_name_oblast OUT VARCHAR2 /** ������� */
			returnValue.setAdrOblastName(statement.getString(13));
			/** 14 p_fact_adr_district OUT VARCHAR2 */
			returnValue.setAdrDistrict(statement.getString(14));
			/** 15 p_fact_adr_city OUT VARCHAR2 */
			returnValue.setAdrCity(statement.getString(15));
			/** 16 p_fact_adr_street OUT VARCHAR2 */
			returnValue.setAdrStreet(statement.getString(16));
			/** 17 p_fact_adr_house OUT VARCHAR2 */
			returnValue.setAdrHouse(statement.getString(17));
			/** 18 p_fact_adr_case OUT VARCHAR2 */
			returnValue.setAdrCase(statement.getString(18));
			/** 19 p_fact_adr_apartment OUT VARCHAR2 */
			returnValue.setAdrApartment(statement.getString(19));
			/** 20 p_phone_home OUT VARCHAR2 */
			returnValue.setPhoneHome(statement.getString(20));
			/** 21 p_phone_mobile OUT VARCHAR2 */
			returnValue.setPhoneMobile(statement.getString(21));
			/** 22 p_email OUT VARCHAR2 */
			returnValue.setEmail(statement.getString(22));
			/** 23 p_cd_nat_prs_group OUT VARCHAR2 */
			returnValue.setProfGroupId(statement.getString(23));
			/**
			 * 24 p_name_nat_prs_group OUT VARCHAR2 /** ��������� - ���������
			 * �������������
			 */
			returnValue.setProfGroup(statement.getString(24));
			/** 25 p_name_other_variant_group OUT VARCHAR2 */
			returnValue.setProfGroupAnotherVariant(statement.getString(25));

			returnValue.setId(idPerson);
			logger.debug("PersonParameters Ok:");
		} finally {
			safeCloseStatement(statement);
		}
		return returnValue;
	}

	/**
	 * �������� ��������� ������� �� ������������������� E-mail ������
	 * <table border=1>
	 * <td>
	 * <li><b>0</b> �������� ��������</li>
	 * <li><b>1</b> ����� �� �������</li>
	 * <li><b>2</b> ������� �� �������</li>
	 * <li><b>3</b> ������������ ����� ��� ������</li></td>
	 * </tr>
	 * </table>
	 * �������� ���-�� ������������� �� ���������� E-mail
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param email
	 *            - ����� ����������� �����, �� ������� ��������������� ������
	 * @param language
	 *            - ���� ������ ������ (������������� � {@link Wrap})
	 * @return {@link WrapPersonParameters}
	 */
	public int personParametersByEmail(Connection connection, String email,
			String language) throws SQLException, DbException {
		int returnValue = 0;
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.check_nat_prs_for_email(?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			// 2 p_email IN VARCHAR2
			statement.setString(2, email);
			// 3 p_nat_prs_count OUT NUMBER
			statement.registerOutParameter(3, Types.INTEGER);
			// 4 p_id_nat_prs OUT NUMBER
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.executeUpdate();

			String errorMessage = statement.getString(4);
			if ((errorMessage != null) && (!errorMessage.trim().equals(""))) {
				throw new DbException(errorMessage);
			}
			returnValue = statement.getInt(3);
			logger.debug("PersonParametersByEmail Ok:");
		} finally {
			safeCloseStatement(statement);
		}
		return returnValue;
	}

	private void safeCloseStatement(CallableStatement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException ex) {
		}
		;
	}

	/**
	 * �� ��������� ������ ���-����� ������� �����/������ �� ������������������
	 * E-mail �����
	 * <table>
	 * <tr>
	 * <td>1</td>
	 * <td>p_cd_card2</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <tr>
	 * <td>2</td>
	 * <td>,p_language</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td> // RU','UA','EN'
	 * <tr>
	 * <td>3</td>
	 * <td>,p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * </table>
	 * return {@link Wrap} ������� �������� ����� (�������� ������
	 * {@link Wrap#checkReturnValueForZero()} )
	 * @return 
	 * */
	public void sendLoginToEmail(Connection connection, String cardNumber,
			String language) throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.send_login_to_email(?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setString(2, SharedFunction.getShortBoncardNumber(cardNumber));
			statement.setString(3, language);
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.executeUpdate();
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1), statement.getString(4));
			}
			logger.debug("loginToEmail OK:");
		} finally {
			safeCloseStatement(statement);
		}
	}

	/**
	 * FUNCTION add_office_message( <br />
	 * <table border="1">
	 * <tr>
	 * <td>2</td>
	 * <td>p_id_nat_prs</td>
	 * <td>IN</td>
	 * <td>NUMBER</td>
	 * <td>-- �� �������</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>p_title_message</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>-- ��������� ���������</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>p_text_message</td>
	 * <td>IN</td>
	 * <td>VARCHAR2</td>
	 * <td>-- ����� ���������</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>p_id_message</td>
	 * <td>OUT</td>
	 * <td>NUMBER</td>
	 * <td>-- �� ��������������� ���������</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>p_result_msg</td>
	 * <td>OUT</td>
	 * <td>VARCHAR2</td>
	 * <td>-- ��������� ����������</td>
	 * </tr>
	 * </table>
	 * ) RETURN VARCHAR2; <br />
	 * 
	 * ������� ������ �������������� �� ����� ������������
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param idNatPrs
	 *            - ���������� ��� �� ���� ������ ��� ������������ ��
	 *            ������������ ���� ������
	 * @param titleMessage
	 *            - ��������� ��� ���������
	 * @param textMessage
	 *            - ����� ���������
	 * @return <ul>
	 *         <li><b>null</b> - ������ ���������� ���������</li>
	 *         <li><b>value</b> - ���������� ��� ���������</li>
	 *         </ul>
	 */
	public Integer sendLetterToAdmin(Connection connection, int idNatPrs,
			String titleMessage, String textMessage) throws SQLException,
			DbException {
		Integer returnValue = null;
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.add_office_message(?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, idNatPrs); // �� �������
			statement.setString(3, titleMessage); // ��������� ���������
			statement.setString(4, textMessage);
			statement.registerOutParameter(5, Types.INTEGER);
			statement.registerOutParameter(6, Types.VARCHAR);
			statement.executeUpdate();
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(6));
			}
			returnValue = statement.getInt(5);
			if (statement.wasNull()) {
				returnValue = null;
			}
			logger.debug("loginToEmail OK:");
		} finally {
			safeCloseStatement(statement);
		}
		return returnValue;
	}

	/** ������� ����� � ����� ������������ �� ��� �� E-mail */
	public void sendLoginToEmailByMail(Connection connection, String email,
			String language) throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.send_login_to_email_by_email(?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setString(2, email);
			statement.setString(3, language);
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.executeUpdate();
			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(4));
			}
			logger.debug("loginToEmail s OK:");
		} catch (Exception ex) {
			logger.error("loginToEmail Exception: " + ex.getMessage());
		} finally {
			safeCloseStatement(statement);
		}
	}

	/**
	 * ������ ������ ���� ��� ������ � ��������� ��������� �������������
	 * ��������� ����� �����
	 */
	private SimpleDateFormat sdfMessage = new SimpleDateFormat("ddMMyyyyHHmmss");

	/**
	 * ���������� ������ ��� ���������� ��������� ��� ������������
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ��� ���������, ������� ����� �������� ���
	 *            ���������� ��������� �����
	 * */
	public void setMessageForOfficePrivateAsSended(Connection connection,
			Integer messageId) throws SQLException, DbException {
		setMessageForOfficePrivate(connection, messageId, "S", null);
	}

	/**
	 * ���������� ������ ��� ���������� ��������� ��� ���������
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ��� ���������, ������� ����� �������� ���
	 *            ���������� ��������� �����
	 * @param errorMessage
	 *            - ����� ������ ��������
	 * */
	public void setMessageForOfficePrivateAsError(Connection connection,
			Integer messageId, String errorMessage) throws SQLException,
			DbException {
		setMessageForOfficePrivate(connection, messageId, "E", errorMessage);
	}

	/**
	 * ���������� ������ ��� ���������� ��������� ��� �����
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ��� ���������, ������� ����� �������� ���
	 *            ���������� ��������� �����
	 * @param errorMessage
	 *            - ����� ������ ��������
	 * */
	public void setMessageForOfficePrivateAsNew(Connection connection,
			Integer messageId) throws SQLException, DbException {
		setMessageForOfficePrivate(connection, messageId, "N", null);
	}

	/**
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ������������� ���������
	 * @param type
	 *            - ��� ��������� ("S", "E", "N")
	 * @param messageError
	 *            - ����� ������ ��� ��������� "E"
	 * @return <ul>
	 *         <li><b>true</b> - ������������� ��������� ����������</li>
	 *         <li><b>false</b> - ������ �� ����� ����������</li>
	 *         </ul>
	 * <br>
	 *         ������������ �������: PACK_UI_OFFICE_SEND FUNCTION
	 *         <b>write_send_info</b>(
	 *         <table>
	 *         <tr>
	 *         <td>2</td>
	 *         <td>p_id_message IN NUMBER</td>
	 *         <td>-- �� ���������</td>
	 *         </tr>
	 *         <tr>
	 *         <td>3</td>
	 *         <td>,p_send_date IN VARCHAR2</td>
	 *         <td>-- ���� ��������</td>
	 *         </tr>
	 *         <tr>
	 *         <td>4</td>
	 *         <td>,p_send_state IN VARCHAR2</td>
	 *         <td>-- ��������� ��������</td>
	 *         </tr>
	 *         <tr>
	 *         <td></td>
	 *         <td></td>
	 *         <td>-- C ������������</td>
	 *         </tr>
	 *         <tr>
	 *         <td></td>
	 *         <td></td>
	 *         <td>-- E ������</td>
	 *         </tr>
	 *         <tr>
	 *         <td></td>
	 *         <td></td>
	 *         <td>-- N �����</td>
	 *         </tr>
	 *         <tr>
	 *         <td></td>
	 *         <td></td>
	 *         <td>-- S ��������</td>
	 *         </tr>
	 *         <tr>
	 *         <td>5</td>
	 *         <td>,p_error_message IN VARCHAR2</td>
	 *         <td>-- ����� ������</td>
	 *         </tr>
	 *         <tr>
	 *         <td>6</td>
	 *         <td>,p_date_format IN VARCHAR2 DEFAULT 'DD.MM.RRRR'</td>
	 *         <td>-- ������ ����</td>
	 *         </tr>
	 *         <tr>
	 *         <td>7</td>
	 *         <td>,p_id_message_send OUT NUMBER</td>
	 *         <td>-- �� �������� (������ �� ������������)</td>
	 *         </tr>
	 *         <tr>
	 *         <td>8</td>
	 *         <td>,p_result_msg OUT VARCHAR2</td>
	 *         </tr>
	 *         <tr>
	 *         <td>1</td>
	 *         <td>) RETURN VARCHAR2</td>
	 *         </tr>
	 *         </table>
	 */
	private void setMessageForOfficePrivate(Connection connection,
			Integer messageId, String type, String messageError)
			throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE_SEND.write_send_info(?, ?, ?, ?, ?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setInt(2, messageId);
			statement.setString(3, sdfMessage.format(new java.util.Date()));
			statement.setString(4, type);
			if (messageError == null) {
				statement.setNull(5, Types.VARCHAR);
			} else {
				statement.setString(5, messageError);
			}
			statement.setString(6, "DDMMYYYYHH24MISS");
			statement.registerOutParameter(7, Types.INTEGER);
			statement.registerOutParameter(8, Types.VARCHAR);
			statement.executeUpdate();

			if (!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))) {
				throw new DbException(statement.getString(1),
						statement.getString(8));
			}
		} finally {
			safeCloseStatement(statement);
		}
	}

	// private void testWriteMessage(Connection connection, int idNatPrs, String
	// message){
	// Integer returnValue=DbFunction.sendLetterToAdmin(connection, idNatPrs,
	// "", message);
	// System.out.println("ReturnValue="+returnValue);
	// }

	/**
	 * ��������� ��������� ��� ��������� ������� "���������"
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ����� ��������� �� ���� ������
	 * @param logger
	 *            - ������
	 * @return <li>
	 *         <ul>
	 *         <b>true</b> - ������������ ��������
	 *         </ul>
	 *         <ul>
	 *         <b>false</b> - ������ ����������
	 *         </ul>
	 *         </li>
	 */
	// private static boolean setMessageAsSended(Connection connection, int
	// messageId, Logger logger){
	// logger.debug("setMessageAsSended begin");
	// boolean returnValue=false;
	// returnValue=DbFunction.setMessageForOfficePrivateAsSended(connection,
	// messageId);
	// logger.debug("setMessageAsSended -end-");
	// return returnValue;
	// }

	/**
	 * ��������� ��������� ��� ��������� ������� "���������"
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ����� ��������� �� ���� ������
	 * @param logger
	 *            - ������
	 * @param errorMessage
	 *            - ����� ��������� �� ������
	 * @return <li>
	 *         <ul>
	 *         <b>true</b> - ������������ ��������
	 *         </ul>
	 *         <ul>
	 *         <b>false</b> - ������ ����������
	 *         </ul>
	 *         </li>
	 */
	// private static boolean setMessageAsError(Connection connection, int
	// messageId, Logger logger, String errorMessage){
	// logger.debug("setMessageAsSended begin");
	// boolean returnValue=false;
	// returnValue=DbFunction.setMessageForOfficePrivateAsError(connection,
	// messageId, errorMessage);
	// logger.debug("setMessageAsSended -end-");
	// return returnValue;
	// }

	/**
	 * ��������� ��������� ��� ��������� ������� "���������"
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param messageId
	 *            - ���������� ����� ��������� �� ���� ������
	 * @param logger
	 *            - ������
	 * @param errorMessage
	 *            - ����� ��������� �� ������
	 * @return <li>
	 *         <ul>
	 *         <b>true</b> - ������������ ��������
	 *         </ul>
	 *         <ul>
	 *         <b>false</b> - ������ ����������
	 *         </ul>
	 *         </li>
	 */
	// private static boolean setMessageAsNew(Connection connection, int
	// messageId, Logger logger){
	// logger.debug("setMessageAsSended begin");
	// boolean returnValue=false;
	// returnValue=DbFunction.setMessageForOfficePrivateAsNew(connection,
	// messageId);
	// logger.debug("setMessageAsSended -end-");
	// return returnValue;
	// }

	/**
	 * ��������� �������� ������� Connection
	 * 
	 * @param connection
	 *            - ���������� � ����� ������
	 * @param logger
	 *            - ������ private static void checkPerson(Connection
	 *            connection, Logger logger, String cardNumber, String password)
	 *            throws Exception { // String cardNumber="1058221"; // String
	 *            password="1"; DbFunction.checkPerson(connection, cardNumber,
	 *            password, language.RU.name()); // checkPerson // 1058289,
	 *            1058221 WrapPerson
	 *            wrapPerson=DbFunction.checkPerson(connection, cardNumber,
	 *            password, DbFunction.language.RU.toString());
	 *            if(wrapPerson.checkReturnValueForZero()==false){
	 *            logger.warn(wrapPerson.getResultMessage()); }else{
	 *            logger.debug("Surname: "+wrapPerson.getSurname());
	 *            logger.debug("Name: "+wrapPerson.getName());
	 *            logger.debug("Sex: "+wrapPerson.getSex());
	 *            wrapPerson.setSex(DbFunction.sex.M.toString()); //
	 *            wrapPerson.setSurname("C."); // updatePerson // Wrap
	 *            wrapUpdatePerson=function.updatePerson(connection,
	 *            wrapPerson.getId(), password,wrapPerson.getSurname(),
	 *            wrapPerson.getName(), wrapPerson.getFatherName(),
	 *            wrapPerson.getDateOfBirth(), wrapPerson.getSex(),
	 *            wrapPerson.getCodeCountry(), wrapPerson.getZipCode(),
	 *            wrapPerson.getAdrOblastId(), wrapPerson.getAdrDiscrict(),
	 *            wrapPerson.getAdrCity(), wrapPerson.getAdrStreet(),
	 *            wrapPerson.getAdrHouse(), wrapPerson.getAdrCase(),
	 *            wrapPerson.getAdrApartment(), wrapPerson.getPhoneHome(),
	 *            wrapPerson.getPhoneMobile(), wrapPerson.getEmail(),
	 *            wrapPerson.getProfGroup(),
	 *            wrapPerson.getProfGroupOtherVariant(),
	 *            DbFunction.language.RU.toString() ); //
	 *            logger.debug("UpdatePerson: "
	 *            +wrapUpdatePerson.getReturnValue()); //
	 *            if(wrapUpdatePerson.checkReturnValueForZero()==false){ //
	 *            logger
	 *            .warn("Update error:"+wrapUpdatePerson.getResultMessage()); //
	 *            } } //function.checkBonCard(connection, cardNumber, language)
	 *            //function.personParameters(connection, idPerson, language);
	 *            // check Parameter by Email
	 *            //function.personParametersByEmail(connection, email,
	 *            language) int
	 *            count=DbFunction.personParametersByEmail(connection,
	 *            "nmtg_client@mail.ru", DbFunction.language.RU.toString());
	 *            logger.debug("Parameters Count: "+count); //
	 *            logger.debug("Time of registration:"
	 *            +personParameters.getRegistrationDate().getTime()); }
	 */

	/*
	 * private static void initLogger(String packageName){
	 * Logger.getLogger(packageName).setLevel(Level.DEBUG);
	 * Logger.getLogger(packageName).addAppender(new ConsoleAppender(new
	 * PatternLayout("%-5p [%t]: %m%n"))); }
	 */

	/**
	 * ������� ������ �� �������� �� ���������� �������� ������ � ������
	 * 
	 * @param connection
	 * @param phoneNumber
	 * @param language
	 */
	public void sendPasswordByPhone(Connection connection, String phoneNumber, String language) throws SQLException, DbException {
		CallableStatement statement = null;
		try {
			statement = connection
					.prepareCall("{?= call bc_admin.PACK_UI_OFFICE.send_login_to_mobile_phone(?, ?, ?)}");
			statement.registerOutParameter(1, Types.VARCHAR);
			statement.setString(2, phoneNumber);
			statement.setString(3, language);
			statement.registerOutParameter(4, Types.VARCHAR);
			statement.executeUpdate();
			if(!VALID_FUNCTION_RESPONSE.equals(statement.getString(1))){
				throw new DbException(statement.getString(1), statement.getString(4));
			}
		} finally {
			safeCloseStatement(statement);
		}
	}

	public List<Purse> getPurseOfBonCard(Connection connection,
										 String card_serial_number, 
										 Integer id_issuer,
										 Integer id_payment_system) throws SQLException, InstantiationException, IllegalAccessException {
		List<Purse> returnValue = new ArrayList<Purse>();
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id_club_card_purse, number_club_card_purse, \n");
		sql.append("cd_club_card_purse_type, name_club_card_purse_type,\n");
		sql.append("cd_currency, name_currency, sname_currency,\n");
		sql.append("value_club_card_purse\n");
		sql.append("FROM bc_office.vc_club_card_purse_all\n");
		sql.append("WHERE card_serial_number = ?\n");
		sql.append("AND id_issuer = ?\n");
		sql.append("AND id_payment_system = ?\n");
		sql.append("ORDER BY number_club_card_purse\n");

		rs = JdbcHelper.getResultSet(connection, sql.toString(),
				card_serial_number, id_issuer, id_payment_system);
		while (rs.next()) {
			returnValue.add((Purse) JdbcHelper.getPojoFromResultSet(rs,
					Purse.class));
		}
		return returnValue;
	}

	
	/** �� ������� ������ �������� ������-���������  */
	private ParentMessage getParentMessageFromResultSet(ResultSet rs) throws SQLException{
		ParentMessage returnValue=new ParentMessage();
		returnValue.setID_MESSAGE(rs.getInt("ID_MESSAGE"));
		returnValue.setID_NAT_PRS(rs.getInt("ID_NAT_PRS"));
		returnValue.setFULL_NAME_NAT_PRS(rs.getString("FULL_NAME_NAT_PRS"));
		returnValue.setEMAIL(rs.getString("EMAIL"));
		returnValue.setTITLE_MESSAGE(rs.getString("TITLE_MESSAGE"));
		returnValue.setTEXT_MESSAGE(rs.getString("TEXT_MESSAGE"));
		try{
			returnValue.setBEGIN_ACTION_DATE(new Date(rs.getDate("BEGIN_ACTION_DATE").getTime()));
		}catch(Exception dateException){}
		try{
			returnValue.setEND_ACTION_DATE(new Date(rs.getDate("END_ACTION_DATE").getTime()));
		}catch(Exception dateException){}
		returnValue.setID_CL_PATTERN(rs.getInt("ID_CL_PATTERN"));
		returnValue.setSENDINGS_QUANTITY(rs.getInt("SENDINGS_QUANTITY"));
		returnValue.setERROR_SENDINGS_QUANTITY(rs.getInt("ERROR_SENDINGS_QUANTITY"));
		returnValue.setSTATE_RECORD(rs.getString("STATE_RECORD"));
		try{
			returnValue.setCREATION_DATE(new Date(rs.getDate("CREATION_DATE").getTime()));
		}catch(Exception dateException){}
		returnValue.setCREATED_BY(rs.getInt("CREATED_BY"));
		returnValue.setID_CLUB(rs.getInt("ID_CLUB"));
		returnValue.setTYPE_MESSAGE(rs.getString("TYPE_MESSAGE"));
		returnValue.setBASIS_FOR_OPERATION(rs.getString("BASIS_FOR_OPERATION"));
		returnValue.setID_QUEST_RECORD(rs.getInt("ID_QUEST_RECORD"));
		returnValue.setMESSAGE_FILE_NAME(rs.getString("MESSAGE_FILE_NAME"));
		returnValue.setSTORED_MESSAGE_FILE_NAME(rs.getString("STORED_MESSAGE_FILE_NAME"));
		String tempValue=rs.getString("HAS_ATTACHED_FILES");
		if((tempValue!=null)&&(tempValue.trim().equalsIgnoreCase("Y"))){
			returnValue.setAttachedFiles(true);
		}else{
			returnValue.setAttachedFiles(false);
		}
		tempValue=rs.getString("IS_ARCHIVE");
		if((tempValue!=null)&&(tempValue.trim().equalsIgnoreCase("Y"))){
			returnValue.setArchive(true);
		}else{
			returnValue.setArchive(false);
		}
		return returnValue;
	}

	public ParentMessage[] getNewMessages(Connection connection, Integer[] userId) throws SQLException {
		List<ParentMessage> returnValue=new ArrayList<ParentMessage>();
		ResultSet rs=connection.createStatement().executeQuery("select * from bc_admin.VC_NAT_PRS_OFFICE_NEW_ALL where id_nat_prs in ("+this.getListDelimeterComma(userId)+")");
		while(rs.next()){
			returnValue.add(getParentMessageFromResultSet(rs));
		}
		return returnValue.toArray(new ParentMessage[]{});
	}
	
	/** �������� ������ ���������� ����������� ������� */
	private String getListDelimeterComma(Integer[] list){
		StringBuffer returnValue=new StringBuffer();
		for(int counter=0;counter<list.length;counter++){
			if(list[counter]!=null){
				if(returnValue.length()>0){
					returnValue.append(", ");
				}
				returnValue.append(list[counter]);
			}
		}
		return returnValue.toString();
	}

	
	public CommodityInformation[] getInformationByBonCard(Connection connection, String card_serial_number, Integer id_issuer, Integer id_payment_system) throws SQLException, InstantiationException, IllegalAccessException{
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT id_furshet_purchase, card_serial_number, id_issuer,\n");
		sql.append("id_payment_system, id_trans, id_term, s_area_id,\n");
		sql.append("cashid, check_s_num, count_purch, sum_check, purch_num,\n");
		sql.append("purch_datetime, purch_refund, purch_text, purch_group,\n");
		sql.append("purch_barc, purch_price, purch_count, measure,\n");
		sql.append("purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,\n");
		sql.append("sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr\n");
		sql.append("FROM bc_reports.v_fuhshet_purchases_all\n");
		sql.append("WHERE card_serial_number = ?\n");
		sql.append("AND id_issuer = ?\n");
		sql.append("AND id_payment_system = ?\n");
		sql.append("ORDER BY purch_datetime, purch_text \n");

		ResultSet rs=JdbcHelper.getResultSet(connection, sql.toString(), card_serial_number, id_issuer, id_payment_system);
		List<CommodityInformation> returnValue=new ArrayList<CommodityInformation>();
		while(rs.next()){
			returnValue.add((CommodityInformation)JdbcHelper.getPojoFromResultSet(rs, CommodityInformation.class));
		}
		return returnValue.toArray(new CommodityInformation[returnValue.size()]);
	}

	
	public CommodityInformation[] getInformationByIdTransaction(Connection connection, Integer id_trans) throws SQLException, InstantiationException, IllegalAccessException{
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT id_furshet_purchase, card_serial_number, id_issuer,\n");
		sql.append("id_payment_system, id_trans, id_term, s_area_id,\n");
		sql.append("cashid, check_s_num, count_purch, sum_check, purch_num,\n");
		sql.append("purch_datetime, purch_refund, purch_text, purch_group,\n");
		sql.append("purch_barc, purch_price, purch_count, measure,\n");
		sql.append("purch_tax, sum_bon_purch, sum_purch, sum_pay_cash_purch,\n");
		sql.append("sum_pay_bon_purch, sum_pay_card_purch, bon_card_nr\n");
		sql.append("FROM bc_reports.v_fuhshet_purchases_all\n");
		sql.append("WHERE id_trans = ?\n");
		sql.append("ORDER BY purch_datetime, purch_text\n");
		ResultSet rs=JdbcHelper.getResultSet(connection, sql.toString(), id_trans);
		List<CommodityInformation> returnValue=new ArrayList<CommodityInformation>();
		while(rs.next()){
			returnValue.add((CommodityInformation)JdbcHelper.getPojoFromResultSet(rs, CommodityInformation.class));
		}
		return returnValue.toArray(new CommodityInformation[returnValue.size()]);
	}
	
	public RatingsUser[] getUserRatings(Connection connection) throws SQLException{
		List<RatingsUser> returnValue=new ArrayList<RatingsUser>();
		ResultSet rs=connection.createStatement()
					 .executeQuery("SELECT ratind_number, surname||' '||NAME||' '||patronymic full_name, cd_card1, sum_bon/100 sumbon FROM BC_ADMIN.f_club_actioin1 a ORDER BY ratind_number");
		while(rs.next()){
			RatingsUser user=new RatingsUser();
			user.setRatingNumber(rs.getInt(1));
			user.setFullName(rs.getString(2));
			user.setCardNumber(rs.getString(3));
			user.setSumBon(rs.getDouble(4));
			returnValue.add(user);
		}
		rs.close();
		return returnValue.toArray(new RatingsUser[returnValue.size()]);
	}
	
}
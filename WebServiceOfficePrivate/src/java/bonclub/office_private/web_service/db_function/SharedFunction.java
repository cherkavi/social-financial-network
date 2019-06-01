package bonclub.office_private.web_service.db_function;

/** ����� ������� ��� ������ ���������, ����� XFire, �������� */
public class SharedFunction {
	/** �������� �������� ����� �� ������� ������ �������� - ��������� 8 ����, ��� ��������� */
	public static String getShortBoncardNumber(String bonCardNumber){
		if(bonCardNumber!=null){
			if(bonCardNumber.length()>=8){
				try{
					bonCardNumber=bonCardNumber.substring(bonCardNumber.length()-8,bonCardNumber.length()-1);
					return bonCardNumber;
				}catch(Exception ex){
					return bonCardNumber;
				}
			}else{
				return bonCardNumber;
			}
		}else{
			return null;
		}
	}

	/** ��������� ������ {@link User_Cards ����� ������������} �� ��������� ���� ������� �� ��������� ���� 
	 * @param source - ({@link User_Cards} ����� ������������)
	 * @param destination ({@link UsersParent} ������, ��� ������ � �������� )
	public static void fillUserCardsByUsersParent(UsersParent source, User_Cards destination){
		// INFO OfficePrivate UsersParent to User_Cards 
		destination.setPassword(source.getPassword());
		destination.setBoncardNumber(source.getBoncardNumber());
		destination.setCreateTime(source.getRegistrationDate());
		// destination.setLastEnter(dateCreate);
		destination.setClub(source.getClub());
		destination.setCardState(source.getCardState());
		destination.setBonCategory(source.getBonCategory());
		destination.setDiscontCategory(source.getDiscontCategory());
		destination.setBonAviable(source.getBonAviable());
		destination.setBonStorage(source.getBonStorage());
		destination.setBalBonPer(source.getBalBonPer());
		destination.setBalDiscPer(source.getBalDiscPer());
		destination.setNextDateCalc(source.getNextDateCalc());
		destination.setNextDateMov(source.getNextDateMov());
		destination.setCardSerialNumber(source.getCardSerialNumber());
		destination.setIdEmitentCard(source.getIdEmitentCard());
		destination.setNameEmitentCard(source.getNameEmitentCard());
		destination.setIdPaySystemCard(source.getIdPaymentSystemCard());
		destination.setNamePaySystemCard(source.getNamePaymentSystemCard());
		try{
			destination.setTimeUse(source.getTimeUse().getTime());
		}catch(NullPointerException npe){};
	}
	 */
	
	/** ��������� ������ {@link Users ������������} �� ��������� ���� ������� �� ��������� ���� 
	 * @param source - ({@link Users} ������������)
	 * @param destination ({@link UsersParent} ������, ��� ������ � �������� )
	public static void fillUsersByUsersParent(UsersParent source, Users destination){
		// INFO OfficePrivate UsersParent to User 
		destination.setEmail(source.getEmail());
		destination.setFullName(source.getSurname()+" "+source.getName());
		destination.setIdParent(source.getId());
		//source.getName();
		// ��� ������ 
		destination.setCountryKod(source.getCountryKod());
		// ���: 
		destination.setName(source.getName());
		// �������: 
		destination.setSurname(source.getSurname());
		// ��������:
		destination.setFatherName(source.getFatherName());
		// ���� ��������: 
		destination.setBirthDay(source.getBirthDay());
		// ���� �������� � ���� java.util.Date 
		destination.setBirthDate(source.getBirthDate());
		// ���: 
		destination.setSex(source.getSex());
		// ������ 
		destination.setPostIndex(source.getPostIndex()); // FACT_ADR_ZIP_CODE
		//  ������� 
		destination.setOblast(source.getOblast()); // FACT_ADR_NAME_OBLAST
		// ��� ������� 
		destination.setOblastId(source.getOblastId());
		//  ����� 
		destination.setRegion(source.getRegion()); // FACT_ADR_DISTRICT
		//  ����� 
		destination.setCity(source.getCity()); // FACT_ADR_CITY
		//  ����� 
		destination.setStreet(source.getStreet()); // FACT_ADR_STREET
		//  ��� 
		destination.setHouse(source.getHouse()); // FACT_ADR_HOUSE
		//  ������ 
		destination.setHousing(source.getHousing()); // FACT_ADR_CASE
		//  ����� �������� 
		destination.setFlatNumber(source.getFlatNumber()); // FACT_ADR_APARTMENT
		// �������� ������� 
		destination.setPhoneHome(source.getPhoneHome()); // PHONE_HOME 
		//  ��������� ������� 
		destination.setPhoneMobile(source.getPhoneMobile()); // PHONE_MOBILE
		// ��������� - ��������� �������������  
		destination.setProf(source.getProf()); // cd_nat_prs_group
		// ��������� ��������� ������� �� ���� ������ 
		destination.setProfId(source.getProfId());
		// ������ ������� ��������� - �� �� ������ 
		destination.setProfOther(source.getProfOther());
	}
*/
	
	/** ��������� ������ ��� ��������� ���� ������ �� ��������� ��������� ���� ������ 
	 * @param sourceUser - {@link Users ������������}  �� ��������� ���� ������
	 * @param sourceCard - {@link User_Card �������� ������������} �� ��������� ���� ������ 
	 * @param destination - ������������ ������ ��� �������� 
	public static void fillUsersParent(Users sourceUser, User_Cards sourceCard, UsersParent destination){
		if(destination!=null){
			if(sourceCard!=null){
				destination.setBoncardNumber(sourceCard.getBoncardNumber());
				destination.setPassword(sourceCard.getPassword());
			}
			if(sourceUser!=null){
				destination.setId(sourceUser.getIdParent());
				destination.setSurname(sourceUser.getSurname()); 
				destination.setName(sourceUser.getName());
				destination.setFatherName(sourceUser.getFatherName()); 
				destination.setBirthDay(sourceUser.getBirthDay());
				destination.setBirthDate(sourceUser.getBirthDate());
				destination.setSex(sourceUser.getSex());
				destination.setCountryKod(sourceUser.getCountryKod());
				destination.setPostIndex(sourceUser.getPostIndex());
				destination.setOblastId(sourceUser.getOblastId()); 
				destination.setRegion(sourceUser.getRegion());
				destination.setCity(sourceUser.getCity());
				destination.setStreet(sourceUser.getStreet());
				destination.setHouse(sourceUser.getHouse());
				destination.setHousing(sourceUser.getHousing());
				destination.setFlatNumber(sourceUser.getFlatNumber());
				destination.setPhoneHome(sourceUser.getPhoneHome());
				destination.setPhoneMobile(sourceUser.getPhoneMobile());
				destination.setEmail(sourceUser.getEmail());
				destination.setBalBonPer(sourceCard.getBalBonPer());
				destination.setBalDiscPer(sourceCard.getBalDiscPer());
				destination.setNextDateCalc(sourceCard.getNextDateCalc());
				destination.setNextDateMov(sourceCard.getNextDateMov());
				destination.setCardSerialNumber(sourceCard.getCardSerialNumber());
				destination.setIdEmitentCard(sourceCard.getIdEmitentCard());
				destination.setNameEmitentCard(sourceCard.getNameEmitentCard());
				destination.setIdPaymentSystemCard(sourceCard.getIdPaySystemCard());
				destination.setNamePaymentSystemCard(sourceCard.getNamePaySystemCard());
				try{
					destination.setProfId(sourceUser.getProfId().toString());
				}catch(Exception ex){};
				destination.setProfOther(sourceUser.getProfOther());
			}
		}
	}
	 */
		
}

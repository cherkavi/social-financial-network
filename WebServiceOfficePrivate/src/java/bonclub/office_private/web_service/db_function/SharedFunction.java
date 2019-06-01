package bonclub.office_private.web_service.db_function;

/** общие функции для работы удаленных, через XFire, процедур */
public class SharedFunction {
	/** получить короткий номер из полного номера БонКарты - последние 8 цифр, без последней */
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

	/** наполнить объект {@link User_Cards карта пользователя} из локальной базы данными из удаленной базы 
	 * @param source - ({@link User_Cards} карта пользователя)
	 * @param destination ({@link UsersParent} объект, для обмена с сервером )
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
	
	/** наполнить объект {@link Users пользователь} из локальной базы данными из удаленной базы 
	 * @param source - ({@link Users} пользователь)
	 * @param destination ({@link UsersParent} объект, для обмена с сервером )
	public static void fillUsersByUsersParent(UsersParent source, Users destination){
		// INFO OfficePrivate UsersParent to User 
		destination.setEmail(source.getEmail());
		destination.setFullName(source.getSurname()+" "+source.getName());
		destination.setIdParent(source.getId());
		//source.getName();
		// код страны 
		destination.setCountryKod(source.getCountryKod());
		// Имя: 
		destination.setName(source.getName());
		// Фамилия: 
		destination.setSurname(source.getSurname());
		// Отчество:
		destination.setFatherName(source.getFatherName());
		// Дата рождения: 
		destination.setBirthDay(source.getBirthDay());
		// Дата рождения в виде java.util.Date 
		destination.setBirthDate(source.getBirthDate());
		// Пол: 
		destination.setSex(source.getSex());
		// индекс 
		destination.setPostIndex(source.getPostIndex()); // FACT_ADR_ZIP_CODE
		//  область 
		destination.setOblast(source.getOblast()); // FACT_ADR_NAME_OBLAST
		// код области 
		destination.setOblastId(source.getOblastId());
		//  район 
		destination.setRegion(source.getRegion()); // FACT_ADR_DISTRICT
		//  город 
		destination.setCity(source.getCity()); // FACT_ADR_CITY
		//  улица 
		destination.setStreet(source.getStreet()); // FACT_ADR_STREET
		//  дом 
		destination.setHouse(source.getHouse()); // FACT_ADR_HOUSE
		//  корпус 
		destination.setHousing(source.getHousing()); // FACT_ADR_CASE
		//  номер квартиры 
		destination.setFlatNumber(source.getFlatNumber()); // FACT_ADR_APARTMENT
		// домашний телефон 
		destination.setPhoneHome(source.getPhoneHome()); // PHONE_HOME 
		//  мобильный телефон 
		destination.setPhoneMobile(source.getPhoneMobile()); // PHONE_MOBILE
		// профессия - текстовое представление  
		destination.setProf(source.getProf()); // cd_nat_prs_group
		// профессия текстовый акроним из базы данных 
		destination.setProfId(source.getProfId());
		// другой вариант профессии - не из списка 
		destination.setProfOther(source.getProfOther());
	}
*/
	
	/** наполнить объект для удаленной базы данных на основании локальной базы данных 
	 * @param sourceUser - {@link Users пользователь}  из локальной базы данных
	 * @param sourceCard - {@link User_Card карточка пользователя} из локальной базы данных 
	 * @param destination - транспортный объект для передачи 
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

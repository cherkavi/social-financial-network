package bonclub.office_private.web_service.common_objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GiftInformation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private BigDecimal id_nat_prs_gift; 
	private String cd_nat_prs_gift_state; 
	private String name_nat_prs_gift_state;
	private BigDecimal is_club_event_gift; 
    private BigDecimal id_club_event_gift; 
    private BigDecimal id_club_event;
    private String name_club_event; 
    private BigDecimal id_gift; 
    private BigDecimal cd_gift; 
    private String name_gift;
    private BigDecimal cd_currency; 
    private String sname_currency; 
    private BigDecimal cost_gift; 
    private String cost_gift_frmt;
    private Date date_reserve; 
    private String date_reserve_frmt; 
    private BigDecimal id_nat_prs_gift_request;
    private BigDecimal basis_for_gift; 
    private Date date_given; 
    private String date_given_frmt; 
    private BigDecimal id_lg_gift;
    private String desc_lg_gift; 
    private BigDecimal cd_lg_currency; 
    private String sname_lg_currency;
    private BigDecimal cost_lg_gift; 
    private String cost_lg_gift_frmt; 
    private BigDecimal id_gifts_given_place;
    private BigDecimal write_off_goods_action; 
    private String card_serial_number; 
    private BigDecimal card_id_issuer;
    private BigDecimal card_id_payment_system; 
    private BigDecimal id_club_card_purse; 
    private Date date_returned;
    private String date_returned_frmt; 
    private String reason_return; 
    private Date date_canceled;
    private String date_canceled_frmt; 
    private String reason_cancel; 
    private String write_off_amount;
    private String write_off_amount_frmt; 
    private BigDecimal id_club;
	public BigDecimal getId_nat_prs_gift() {
		return id_nat_prs_gift;
	}
	public void setId_nat_prs_gift(BigDecimal idNatPrsGift) {
		id_nat_prs_gift = idNatPrsGift;
	}
	public String getCd_nat_prs_gift_state() {
		return cd_nat_prs_gift_state;
	}
	public void setCd_nat_prs_gift_state(String cdNatPrsGiftState) {
		cd_nat_prs_gift_state = cdNatPrsGiftState;
	}
	public String getName_nat_prs_gift_state() {
		return name_nat_prs_gift_state;
	}
	public void setName_nat_prs_gift_state(String nameNatPrsGiftState) {
		name_nat_prs_gift_state = nameNatPrsGiftState;
	}
	public BigDecimal getIs_club_event_gift() {
		return is_club_event_gift;
	}
	public void setIs_club_event_gift(BigDecimal isClubEventGift) {
		is_club_event_gift = isClubEventGift;
	}
	public BigDecimal getId_club_event_gift() {
		return id_club_event_gift;
	}
	public void setId_club_event_gift(BigDecimal idClubEventGift) {
		id_club_event_gift = idClubEventGift;
	}
	public BigDecimal getId_club_event() {
		return id_club_event;
	}
	public void setId_club_event(BigDecimal idClubEvent) {
		id_club_event = idClubEvent;
	}
	public String getName_club_event() {
		return name_club_event;
	}
	public void setName_club_event(String nameClubEvent) {
		name_club_event = nameClubEvent;
	}
	public BigDecimal getId_gift() {
		return id_gift;
	}
	public void setId_gift(BigDecimal idGift) {
		id_gift = idGift;
	}
	public BigDecimal getCd_gift() {
		return cd_gift;
	}
	public void setCd_gift(BigDecimal cdGift) {
		cd_gift = cdGift;
	}
	public String getName_gift() {
		return name_gift;
	}
	public void setName_gift(String nameGift) {
		name_gift = nameGift;
	}
	public BigDecimal getCd_currency() {
		return cd_currency;
	}
	public void setCd_currency(BigDecimal cdCurrency) {
		cd_currency = cdCurrency;
	}
	public String getSname_currency() {
		return sname_currency;
	}
	public void setSname_currency(String snameCurrency) {
		sname_currency = snameCurrency;
	}
	public BigDecimal getCost_gift() {
		return cost_gift;
	}
	public void setCost_gift(BigDecimal costGift) {
		cost_gift = costGift;
	}
	public String getCost_gift_frmt() {
		return cost_gift_frmt;
	}
	public void setCost_gift_frmt(String costGiftFrmt) {
		cost_gift_frmt = costGiftFrmt;
	}
	public Date getDate_reserve() {
		return date_reserve;
	}
	public void setDate_reserve(Date dateReserve) {
		date_reserve = dateReserve;
	}
	public String getDate_reserve_frmt() {
		return date_reserve_frmt;
	}
	public void setDate_reserve_frmt(String dateReserveFrmt) {
		date_reserve_frmt = dateReserveFrmt;
	}
	public BigDecimal getId_nat_prs_gift_request() {
		return id_nat_prs_gift_request;
	}
	public void setId_nat_prs_gift_request(BigDecimal idNatPrsGiftRequest) {
		id_nat_prs_gift_request = idNatPrsGiftRequest;
	}
	public BigDecimal getBasis_for_gift() {
		return basis_for_gift;
	}
	public void setBasis_for_gift(BigDecimal basisForGift) {
		basis_for_gift = basisForGift;
	}
	public Date getDate_given() {
		return date_given;
	}
	public void setDate_given(Date dateGiven) {
		date_given = dateGiven;
	}
	public String getDate_given_frmt() {
		return date_given_frmt;
	}
	public void setDate_given_frmt(String dateGivenFrmt) {
		date_given_frmt = dateGivenFrmt;
	}
	public BigDecimal getId_lg_gift() {
		return id_lg_gift;
	}
	public void setId_lg_gift(BigDecimal idLgGift) {
		id_lg_gift = idLgGift;
	}
	public String getDesc_lg_gift() {
		return desc_lg_gift;
	}
	public void setDesc_lg_gift(String descLgGift) {
		desc_lg_gift = descLgGift;
	}
	public BigDecimal getCd_lg_currency() {
		return cd_lg_currency;
	}
	public void setCd_lg_currency(BigDecimal cdLgCurrency) {
		cd_lg_currency = cdLgCurrency;
	}
	public String getSname_lg_currency() {
		return sname_lg_currency;
	}
	public void setSname_lg_currency(String snameLgCurrency) {
		sname_lg_currency = snameLgCurrency;
	}
	public BigDecimal getCost_lg_gift() {
		return cost_lg_gift;
	}
	public void setCost_lg_gift(BigDecimal costLgGift) {
		cost_lg_gift = costLgGift;
	}
	public String getCost_lg_gift_frmt() {
		return cost_lg_gift_frmt;
	}
	public void setCost_lg_gift_frmt(String costLgGiftFrmt) {
		cost_lg_gift_frmt = costLgGiftFrmt;
	}
	public BigDecimal getId_gifts_given_place() {
		return id_gifts_given_place;
	}
	public void setId_gifts_given_place(BigDecimal idGiftsGivenPlace) {
		id_gifts_given_place = idGiftsGivenPlace;
	}
	public BigDecimal getWrite_off_goods_action() {
		return write_off_goods_action;
	}
	public void setWrite_off_goods_action(BigDecimal writeOffGoodsAction) {
		write_off_goods_action = writeOffGoodsAction;
	}
	public String getCard_serial_number() {
		return card_serial_number;
	}
	public void setCard_serial_number(String cardSerialNumber) {
		card_serial_number = cardSerialNumber;
	}
	public BigDecimal getCard_id_issuer() {
		return card_id_issuer;
	}
	public void setCard_id_issuer(BigDecimal cardIdIssuer) {
		card_id_issuer = cardIdIssuer;
	}
	public BigDecimal getCard_id_payment_system() {
		return card_id_payment_system;
	}
	public void setCard_id_payment_system(BigDecimal cardIdPaymentSystem) {
		card_id_payment_system = cardIdPaymentSystem;
	}
	public BigDecimal getId_club_card_purse() {
		return id_club_card_purse;
	}
	public void setId_club_card_purse(BigDecimal idClubCardPurse) {
		id_club_card_purse = idClubCardPurse;
	}
	public Date getDate_returned() {
		return date_returned;
	}
	public void setDate_returned(Date dateReturned) {
		date_returned = dateReturned;
	}
	public String getDate_returned_frmt() {
		return date_returned_frmt;
	}
	public void setDate_returned_frmt(String dateReturnedFrmt) {
		date_returned_frmt = dateReturnedFrmt;
	}
	public String getReason_return() {
		return reason_return;
	}
	public void setReason_return(String reasonReturn) {
		reason_return = reasonReturn;
	}
	public Date getDate_canceled() {
		return date_canceled;
	}
	public void setDate_canceled(Date dateCanceled) {
		date_canceled = dateCanceled;
	}
	public String getDate_canceled_frmt() {
		return date_canceled_frmt;
	}
	public void setDate_canceled_frmt(String dateCanceledFrmt) {
		date_canceled_frmt = dateCanceledFrmt;
	}
	public String getReason_cancel() {
		return reason_cancel;
	}
	public void setReason_cancel(String reasonCancel) {
		reason_cancel = reasonCancel;
	}
	public String getWrite_off_amount() {
		return write_off_amount;
	}
	public void setWrite_off_amount(String writeOffAmount) {
		write_off_amount = writeOffAmount;
	}
	public String getWrite_off_amount_frmt() {
		return write_off_amount_frmt;
	}
	public void setWrite_off_amount_frmt(String writeOffAmountFrmt) {
		write_off_amount_frmt = writeOffAmountFrmt;
	}
	public BigDecimal getId_club() {
		return id_club;
	}
	public void setId_club(BigDecimal idClub) {
		id_club = idClub;
	}
	

}

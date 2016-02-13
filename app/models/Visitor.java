package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import util.Utils;
import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import enumeration.GenderEnum;
import enumeration.MaritalStatusEnum;

@Entity
public class Visitor extends Model {
	@ManyToOne
	@JoinColumn(name = "eventId")
	Event eventDay;

	@Required
	public String name;

	@Required
	public String lastName;

	@Enumerated(EnumType.STRING)
	public GenderEnum gender = GenderEnum.M;

	@Enumerated(EnumType.STRING)
	public MaritalStatusEnum maritalStatus = MaritalStatusEnum.Solteiro;

	@MaxSize(3)
	public int age;

	@Temporal(TemporalType.DATE)
	@As("dd/MM/yyyy")
	public Date birthDate;

	@Min(1)
	public long countryId;

	@Min(1)
	public long stateId;

	@Min(1)
	public long cityId;

	public String cep;

	public String address;

	public String complement;

	@Email
	@Unique
	@Required
	public String email;

	@Phone
	@Required
	public String cellphone;
	
	@Phone
	public String phone;

	@Temporal(TemporalType.DATE)
	@As("dd/MM/yyyy")
	public Date visitDay;

	public String whoInvited;

	public boolean isFromSomeChurch;
	
	public String church;
	
	public boolean isReceiveVisitation;
	
	public boolean isKnowChurch;

	@MaxSize(10000)
	public String prayOrders;

	public boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date postedAt = new Date();

	@Hidden
	public long publishedBy;

	@Hidden
	public long institutionId;
	
	public String toString() {
		return name + " " + lastName;
	}

	public String getNameEmail() {
		return name + " " + lastName + " - " + email;
	}

	public String getAddress() {
		return address;
	}

		public void setAddress(String address) {
		this.address = address;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getPostedAt() {
		if (postedAt == null) {
			postedAt = new Date();
		}
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWhoInvited() {
		return whoInvited;
	}

	public void setWhoInvited(String whoInvited) {
		this.whoInvited = whoInvited;
	}

	public boolean isFromSomeChurch() {
		return isFromSomeChurch;
	}

	public void setFromSomeChurch(boolean isFromSomeChurch) {
		this.isFromSomeChurch = isFromSomeChurch;
	}

	public String getChurch() {
		return church;
	}

	public void setChurch(String church) {
		this.church = church;
	}

	public boolean isReceiveVisitation() {
		return isReceiveVisitation;
	}

	public void setReceiveVisitation(boolean isReceiveVisitation) {
		this.isReceiveVisitation = isReceiveVisitation;
	}

	public boolean isKnowChurch() {
		return isKnowChurch;
	}

	public void setKnowChurch(boolean isKnowChurch) {
		this.isKnowChurch = isKnowChurch;
	}

	public String getPrayOrders() {
		return prayOrders;
	}

	public void setPrayOrders(String prayOrders) {
		this.prayOrders = prayOrders;
	}

	public Event getEventDay() {
		return eventDay;
	}

	public void setEventDay(Event eventDay) {
		this.eventDay = eventDay;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public Date getVisitDay() {
		return visitDay;
	}

	public void setVisitDay(Date visitDay) {
		this.visitDay = visitDay;
	}

	public String getNameAndLastNameLimited() {
		return Utils.substringText(getName() + " " + getLastName(), 0, 27);
	}

	public String getEmailLimited() {
		return Utils.substringText(getEmail(), 0, 37);
	}

	public String getEventLimited() {
		return Utils.substringText(getEventDay().toString(), 0, 20);
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public MaritalStatusEnum getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatusEnum maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public long getPublishedId() {
		return Admin.getLoggedUserInstitution().getUser().getId();
	}

	public void setPublishedId(long publishedBy) {
		this.publishedBy = publishedBy;
	}

	public long getInstitutionId() {
		return Admin.getLoggedUserInstitution().getInstitution().getId();
	}

	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}

}

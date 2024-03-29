package models;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

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
public class Member extends Model {
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

	public String birthDate;

	@Temporal(TemporalType.DATE)
	public Date memberSince;

	public String address;

	public String complement;

	public String cep;

	public long countryId;

	public long stateId;

	public long cityId;

	@Email
	@Unique
	@Required
	public String email;

	@Required
	public String cellphone;

	public String phone;

	public boolean isActive;

	@Hidden
	public String postedAt;

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

	public String getPostedAt() throws ParseException {
		if (this.postedAt == null) {
			setPostedAt(Utils.getCurrentDateTimeByFormat("dd/MM/yyyy HH:mm:ss"));
		}
		return postedAt;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getNameAndLastNameLimited() {
		return Utils.substringText(getName() + " " + getLastName(), 0, 27);
	}

	public String getEmailLimited() {
		return Utils.substringText(getEmail(), 0, 37);
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

	public Date getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(Date memberSince) {
		this.memberSince = memberSince;
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

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}

}

package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Min;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import util.Utils;
import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;

@Entity
public class Institution extends Model {
	
	@Required
	public String institution;

	@Required
	public Blob logo;

	public String website;

	public String slogan;

	@Required
	@Min(1)
	public long countryId;

	@Required
	@Min(1)
	public long stateId;

	@Required
	@Min(1)
	public long cityId;

	public String address;
	public String complement;
	public String neighborhood;
	public String cep;

	@Required
	public String cnpj;
	@Required
	@Email
	@Unique
	public String email;
	@Required
	@Phone
	public String phone1;
	@Phone
	public String phone2;
	@Phone
	public String phone3;

	@As("-16.570043, -49.313314")
	public String localizationGPS;
	public String googleApiKey;
	public String googleMapsAddress;

	@Hidden
	public long userId;

	@Hidden
	public long publishedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date licenseDate = new Date();

	public String institutionKey;

	public Date getLicenseDate() {
		return licenseDate;
	}

	public void setLicenseDate(Date licenseDate) {
		this.licenseDate = licenseDate;
	}

	public String toString() {
		return institution;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date postedAt = new Date();

	@Transient
	public String formattedDate = Utils.formatDate(postedAt);

	@PrePersist
	public void prePersist() {
		postedAt = new Date();
	}

	@PostLoad
	public void postLoad() {
		Institution institution = Institution.find("userId = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		User user = User.findById(institution.getUserId());
		user.setInstitutionId(institution.getId());
		user.save();
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

	public String getLocalizationGPS() {
		return localizationGPS;
	}

	public void setLocalizationGPS(String localizationGPS) {
		this.localizationGPS = localizationGPS;
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

	public String getGoogleApiKey() {
		return googleApiKey;
	}

	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}

	public String getGoogleMapsAddress() {
		return googleMapsAddress;
	}

	public void setGoogleMapsAddress(String googleMapsAddress) {
		this.googleMapsAddress = googleMapsAddress;
	}

	public String getIframeCode() {
		return getGoogleMapsAddress() + "&key=" + getGoogleApiKey();
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Blob getLogo() {
		return logo;
	}

	public void setLogo(Blob logo) {
		this.logo = logo;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public long getUserId() {
		return Admin.getLoggedUserInstitution().getUser().getId();
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getCountryName() {
		Country country = Country.verifyById(getCountryId());
		return country.getName();
	}

	public String getStateName() {
		State state = State.verifyById(getStateId());
		return state.getName();
	}

	public String getCityName() {
		City city = City.verifyById(getCityId());
		return city.getName();
	}

	public long getPublishedId() {
		return Admin.getLoggedUserInstitution().getUser().getId();
	}

	public void setPublishedId(long publishedBy) {
		this.publishedBy = publishedBy;
	}

	public static Institution verifyByCnpj(String cnpj) {
		return find("byCnpj", cnpj).first();
	}

	public static Institution verifyByEmail(String email) {
		return find("byEmail", email).first();
	}

	public String getInstitutionKey() {
		if (this.institutionKey == null) {
			setInstitutionKey(Utils.randomKey());
		}
		return institutionKey;
	}

	public void setInstitutionKey(String institutionKey) {
		this.institutionKey = institutionKey;
	}

	public long getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(long publishedBy) {
		this.publishedBy = publishedBy;
	}

}

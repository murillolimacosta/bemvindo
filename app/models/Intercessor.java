package models;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;
import util.Utils;
import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;

@Entity
public class Intercessor extends Model {
	@Required
	public String name;

	public String lastName;

	@Email
	@Required
	@Unique
	public String email;

	public boolean isActive;

	public boolean isReveiceSMS;
	
	public boolean isReceiveMail;

	@Hidden
	public long publishedBy;

	@Hidden
	public long institutionId;

	@Hidden
	public String postedAt;

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostedAt() throws ParseException {
		if (this.postedAt == null) {
			setPostedAt(Utils.getCurrentDateTimeByFormat("dd/MM/yyyy HH:mm:ss"));
		}
		return postedAt;
	}

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public boolean isReveiceSMS() {
		return isReveiceSMS;
	}

	public void setReveiceSMS(boolean isReveiceSMS) {
		this.isReveiceSMS = isReveiceSMS;
	}

	public boolean isReceiveMail() {
		return isReceiveMail;
	}

	public void setReceiveMail(boolean isReceiveMail) {
		this.isReceiveMail = isReceiveMail;
	}

	public long getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(long publishedBy) {
		this.publishedBy = publishedBy;
	}

}
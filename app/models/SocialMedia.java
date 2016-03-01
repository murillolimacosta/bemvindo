package models;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import util.Utils;

@Entity
public class SocialMedia extends Model {
	public String facebook;
	public String twitter;
	public String instagram;
	public String goooglePlus;
	public String pinterest;
	public String youtube;

	public boolean isActive;

	@Hidden
	public String postedAt;

	@Hidden
	public long publishedBy;

	@Hidden
	public long institutionId;

	public String toString() {
		return facebook;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getGoooglePlus() {
		return goooglePlus;
	}

	public void setGoooglePlus(String goooglePlus) {
		this.goooglePlus = goooglePlus;
	}

	public String getPinterest() {
		return pinterest;
	}

	public void setPinterest(String pinterest) {
		this.pinterest = pinterest;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
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

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
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

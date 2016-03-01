package models;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.binding.As;
import play.data.validation.Required;
import play.db.jpa.Model;
import util.Utils;
import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;

@Entity
public class MemberGroup extends Model {

	@Required
	public String title;

	@Hidden
	public long publishedBy;

	public boolean isActive;

	@Hidden
	public long institutionId;

	@Required
	@ManyToMany
	public Set<Member> members;

	public String toString() {
		return title;
	}

	@Hidden
	public String postedAt;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPublishedBy() {
		if (publishedBy == 0) {
			return Admin.getLoggedUserInstitution().getUser().getId();
		}
		return publishedBy;
	}

	public void setPublishedBy(long publishedBy) {
		this.publishedBy = publishedBy;
	}

	public long getInstitutionId() {
		return Admin.getLoggedUserInstitution().getInstitution().getId();
	}

	public void setInstitutionId(long institutionId) {
		this.institutionId = institutionId;
	}

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}

}

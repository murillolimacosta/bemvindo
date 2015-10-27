package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import play.data.binding.As;
import play.db.jpa.Model;

@Entity
public class EmailMarketing extends Model {
	public String name;
	public String email;
	public String template;

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date postedAt = new Date();

	@Hidden
	public long institutionId;

	@Hidden
	public long publishedBy;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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

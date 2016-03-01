package models;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import play.data.binding.As;
import play.db.jpa.Model;
import util.Utils;

@Entity
public class EmailMarketing extends Model {
	public String name;
	public String email;
	public String template;

	@Hidden
	public String postedAt;

	@Hidden
	public long institutionId;

	@Hidden
	public long publishedBy;

	public String getPostedAt() throws ParseException {
		if (this.postedAt == null) {
			setPostedAt(Utils.getCurrentDateTimeByFormat("dd/MM/yyyy HH:mm:ss"));
		}
		return postedAt;
	}

	public void setPostedAt(String postedAt) {
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

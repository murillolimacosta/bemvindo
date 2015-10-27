package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import net.sf.oval.constraint.Max;
import play.data.binding.As;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class SMS extends Model {
	public String name;

	@Required
	public String subject;

	@Required
	public String destination;

	@Required
	@Lob
	@Max(160)
	public String description;

	public boolean isActive;
	public boolean isReuse;

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date sentAt = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date receivedAt = new Date();

	@Hidden
	public long publishedBy;

	@Hidden
	public long institutionId;

	public String toString() {
		return subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

	public Date getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(Date receivedAt) {
		this.receivedAt = receivedAt;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isReuse() {
		return isReuse;
	}

	public void setReuse(boolean isReuse) {
		this.isReuse = isReuse;
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

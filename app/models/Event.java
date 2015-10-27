package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.binding.As;
import play.data.validation.Required;
import play.db.jpa.Model;
import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import enumeration.PeriodDayEnum;
import enumeration.WeekDayEnum;

@Entity
public class Event extends Model {

	@Required
	public String title;

	@Required
	@Enumerated(EnumType.STRING)
	public WeekDayEnum weekDays = WeekDayEnum.Domingo;

	@Required
	@Enumerated(EnumType.STRING)
	public PeriodDayEnum periodDays = PeriodDayEnum.Manhã;

	@Hidden
	public long publishedBy;

	public boolean isActive;

	@Hidden
	public long institutionId;
	
	public String toString() {
		return title + " - " + weekDays + " - " + periodDays;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@As("yyyy-MM-dd HH:mm:ss")
	public Date postedAt = new Date();

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public WeekDayEnum getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(WeekDayEnum weekDays) {
		this.weekDays = weekDays;
	}

	public PeriodDayEnum getPeriodDays() {
		return periodDays;
	}

	public void setPeriodDays(PeriodDayEnum periodDays) {
		this.periodDays = periodDays;
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

}

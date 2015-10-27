package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import controllers.Admin;
import controllers.Application;
import controllers.CRUD.Hidden;
import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;
import util.Utils;
import enumeration.TemplateEnum;

@Entity
public class ConfigureTemplate extends Model {
	@Required
	@Enumerated(EnumType.STRING)
	public TemplateEnum template = TemplateEnum.Template1;

	@Required
	public String title1;

	@Required
	@MaxSize(10000)
	public String subTitle1;

	@Required
	@MaxSize(10000)
	public String title2;

	@Required
	@MaxSize(10000)
	@Lob
	public String paragraph1;

	@Required
	@MaxSize(10000)
	@Lob
	public String paragraph2;

	@MaxSize(10000)
	@Lob
	public String paragraph3;

	@MaxSize(10000)
	public String title3;

	@MaxSize(10000)
	@Lob
	public String paragraph4;

	@MaxSize(10000)
	@Lob
	public String paragraph5;
	
	@MaxSize(10000)
	@Lob
	public String paragraph6;
	
	@MaxSize(10000)
	@Lob
	public String paragraph7;
	
	@MaxSize(10000)
	@Lob
	public String paragraph8;
	
	@MaxSize(10000)
	@Lob
	public String paragraph9;
	
	@MaxSize(10000)
	@Lob
	public String paragraph10;

	@Required
	@MaxSize(10000)
	public String footer1;

	@MaxSize(100)
	public String footer2;
	
	@MaxSize(100)
	public String footer3;

	@Hidden
	public long publishedBy;

	@Hidden
	public long institutionId;

	public Blob attachment;
	
	public Blob image1;
	public Blob image2;
	public Blob image3;
	public Blob image4;

	public String toString() {
		return title1;
	}

	public boolean isActive;

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

	public String getTitle1() {
		return title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getSubTitle1() {
		return subTitle1;
	}

	public void setSubTitle1(String subTitle1) {
		this.subTitle1 = subTitle1;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getParagraph1() {
		return paragraph1;
	}

	public void setParagraph1(String paragraph1) {
		this.paragraph1 = paragraph1;
	}

	public String getParagraph2() {
		return paragraph2;
	}

	public void setParagraph2(String paragraph2) {
		this.paragraph2 = paragraph2;
	}

	public String getParagraph3() {
		return paragraph3;
	}

	public void setParagraph3(String paragraph3) {
		this.paragraph3 = paragraph3;
	}

	public String getTitle3() {
		return title3;
	}

	public void setTitle3(String title3) {
		this.title3 = title3;
	}

	public String getParagraph4() {
		return paragraph4;
	}

	public void setParagraph4(String paragraph4) {
		this.paragraph4 = paragraph4;
	}

	public String getParagraph5() {
		return paragraph5;
	}

	public void setParagraph5(String paragraph5) {
		this.paragraph5 = paragraph5;
	}

	public TemplateEnum getTemplate() {
		return template;
	}

	public void setTemplate(TemplateEnum template) {
		this.template = template;
	}

	public String getFooter1() {
		return footer1;
	}

	public void setFooter1(String footer1) {
		this.footer1 = footer1;
	}

	public String getFooter2() {
		return footer2;
	}

	public void setFooter2(String footer2) {
		this.footer2 = footer2;
	}

	public Blob getAttachment() {
		return attachment;
	}

	public void setAttachment(Blob attachment) {
		this.attachment = attachment;
	}

	public String getTitle1Limited() {
		return Utils.substringText(getTitle1(), 0, 28);
	}

	public String getSubTitle1Limited() {
		return Utils.substringText(getSubTitle1(), 0, 32);
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

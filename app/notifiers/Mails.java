package notifiers;

import models.ConfigureTemplate;
import models.Institution;
import models.Member;
import models.User;
import models.Visitor;

import org.apache.commons.mail.EmailAttachment;

import play.Play;
import play.mvc.Mailer;
import util.Utils;
import controllers.Admin;
import controllers.Application;

public class Mails extends Mailer {
	public static void welcome(User user) {
		setSubject("Bem vindo %s", user.getName());
		addRecipient(user.email);
		setFrom("BemVindo <naoreponda@ebemvindo.com>");
//		EmailAttachment attachment = new EmailAttachment();
//		attachment.setDescription("A pdf document");
//		attachment.setPath(Play.getFile("rules.pdf").getPath());
//		addAttachment(attachment);
		send(user);
	}

	public static void lostPassword(User user) {
		String newpassword = user.password;
		setFrom("BemVindo <naoreponda@ebemvindo.com>");
		setSubject("Ops! Esqueceu sua senha?");
		addRecipient(user.email);
		send(user, newpassword);
	}

	public static void main(String[] args) {
		String str = ", 1";
		String f[] = str.split(",");
		String finalStr = "";
		for (String string : f) {
			if (!Utils.isNullOrEmpty(string)) {
				finalStr = finalStr + string;
			}
		}
		System.out.println(finalStr.trim());
	}

	public static void template1(Visitor visitor, ConfigureTemplate template) {
		Institution institution = Institution.find("userId = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		setSubject("Olá, " + visitor.getName() + "! " + template.getTitle1());
		addRecipient(visitor.getEmail());
		setFrom("Eu <" + institution.getEmail() + ">");
		// if (configureTemplate.getAttachment().getFile() != null) {
		// EmailAttachment attachment = new EmailAttachment();
		// attachment.setDescription(Utils.splitSpacesAndLimitSubstring(configureTemplate.getTitle1(),
		// 12) + Utils.timeHourNow());
		// attachment.setPath(Play.getFile(configureTemplate.getAttachment().toString()).getPath());
		// addAttachment(attachment);
		// }
		send(template, institution);
	}
	
	public static void template2(Visitor visitor, ConfigureTemplate template) {
		Institution institution = Institution.find("userId = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		setSubject("Olá, " + visitor.getName() + "! " + template.getTitle1());
		addRecipient(visitor.getEmail());
		setFrom("Eu <" + institution.getEmail() + ">");
		// if (configureTemplate.getAttachment().getFile() != null) {
		// EmailAttachment attachment = new EmailAttachment();
		// attachment.setDescription(Utils.splitSpacesAndLimitSubstring(configureTemplate.getTitle1(),
		// 12) + Utils.timeHourNow());
		// attachment.setPath(Play.getFile(configureTemplate.getAttachment().toString()).getPath());
		// addAttachment(attachment);
		// }
		send(template, institution);
	}

	public static void template1(Member member, ConfigureTemplate template) {
		Institution institution = Institution.find("userId = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		setSubject("Olá, " + member.getName() + "! " + template.getTitle1());
		addRecipient(member.getEmail());
		setFrom("Eu <" + institution.getEmail() + ">");
		// if (configureTemplate.getAttachment().getFile() != null) {
		// EmailAttachment attachment = new EmailAttachment();
		// attachment.setDescription(Utils.splitSpacesAndLimitSubstring(configureTemplate.getTitle1(),
		// 12) + Utils.timeHourNow());
		// attachment.setPath(Play.getFile(configureTemplate.getAttachment().toString()).getPath());
		// addAttachment(attachment);
		// }
		send(template, institution);
	}
	
	public static void template2(Member member, ConfigureTemplate template) {
		Institution institution = Institution.find("userId = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		setSubject("Olá, " + member.getName() + "! " + template.getTitle1());
		addRecipient(member.getEmail());
		setFrom("Eu <" + institution.getEmail() + ">");
		// if (configureTemplate.getAttachment().getFile() != null) {
		// EmailAttachment attachment = new EmailAttachment();
		// attachment.setDescription(Utils.splitSpacesAndLimitSubstring(configureTemplate.getTitle1(),
		// 12) + Utils.timeHourNow());
		// attachment.setPath(Play.getFile(configureTemplate.getAttachment().toString()).getPath());
		// addAttachment(attachment);
		// }
		send(template, institution);
	}
}

package notifiers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.ConfigureTemplate;
import models.Institution;
import models.Member;
import models.User;
import models.Visitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.mail.EmailAttachment;

import bemvindo.service.model.BodyMail;
import bemvindo.service.model.JsonBody;
import bemvindo.service.model.JsonBodyMail;
import bemvindo.service.model.SendTo;
import bemvindo.service.model.Sender;
import play.Play;
import play.mvc.Mailer;
import util.Utils;
import ws.ConnectionService;
import controllers.Admin;
import controllers.Application;

public class MailNotifier extends Mailer {
	public static String STR_URL_SEND_MAIL = Play.configuration.getProperty("send.mail.url");
	public static String STR_FAIL_MESSAGE = "Ops! :( Algo estranho aconteceu! Por favor, tente novamente!";

	public static void welcome(User user) {
		setSubject("Bem vindo %s", user.getName());
		addRecipient(user.email);
		setFrom("BemVindo <naoreponda@ebemvindo.com>");
		// EmailAttachment attachment = new EmailAttachment();
		// attachment.setDescription("A pdf document");
		// attachment.setPath(Play.getFile("rules.pdf").getPath());
		// addAttachment(attachment);
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

	public static String sendMailMembers(List<Member> members, ConfigureTemplate template) {
		String output = "";
		JsonBody jsonBody = new JsonBody();
		JsonBodyMail jsonBodyMail = jsonBody.jsonBodyMail;
		jsonBodyMail.sender = fillSender();
		jsonBodyMail.bodyMail = fillBodyMail(template);
		jsonBodyMail.sendTo = fillSendToMembers(members);
		output = connectAndSend(jsonBodyMail.toString());
		if ("SUCCESS".equals(output)) {
			return "E-mails enviados a " + members.size() + " destinatários.";
		} else {
			return STR_FAIL_MESSAGE;
		}
	}

	public static String sendMailVisitors(List<Visitor> visitors, ConfigureTemplate template) {
		String output = "";
		JsonBody jsonBody = new JsonBody();
		JsonBodyMail jsonBodyMail = jsonBody.jsonBodyMail;
		jsonBodyMail.sender = fillSender();
		jsonBodyMail.bodyMail = fillBodyMail(template);
		jsonBodyMail.sendTo = fillSendToVisitors(visitors);
		output = connectAndSend(jsonBodyMail.toString());
		if ("SUCCESS".equals(output)) {
			return "E-mails enviados a " + visitors.size() + " destinatários.";
		} else {
			return STR_FAIL_MESSAGE;
		}
	}
	
	private static String connectAndSend(String jsonContent) {
		String output = "";
		try {
			output = ConnectionService.openConnection(jsonContent, STR_URL_SEND_MAIL);
		} catch (IOException e) {
			e.printStackTrace();
			return "Ops! Algo errado aconteceu! Tente novamente!";
		}
		return output;

	}


	private static BodyMail fillBodyMail(ConfigureTemplate template) {
		BodyMail bodyMail = new BodyMail();
		bodyMail.template = String.valueOf(template.template.getValue());
		bodyMail.title1 = template.title1;
		bodyMail.subTitle1 = template.subTitle1;
		bodyMail.title2 = template.title2;
		bodyMail.paragraph1 = template.paragraph1;
		bodyMail.paragraph2 = template.paragraph2;
		bodyMail.paragraph3 = template.paragraph3;
		bodyMail.paragraph4 = template.paragraph4;
		bodyMail.paragraph5 = template.paragraph5;
		bodyMail.title3 = template.title3;
		bodyMail.paragraph6 = template.paragraph6;
		bodyMail.paragraph7 = template.paragraph7;
		bodyMail.paragraph8 = template.paragraph8;
		bodyMail.paragraph9 = template.paragraph9;
		bodyMail.paragraph10 = template.paragraph10;
		bodyMail.footer1 = template.footer1;
		bodyMail.footer2 = template.footer2;
		bodyMail.footer3 = template.footer3;
		bodyMail.image1 = template.image1.toString();
		bodyMail.image2 = template.image2.toString();
		bodyMail.image3 = template.image3.toString();
		bodyMail.image4 = template.image4.toString();
		bodyMail.attachment = template.attachment.toString();
		return bodyMail;
	}

	private static List<SendTo> fillSendToMembers(List<Member> membersList) {
		List<SendTo> sentoList = new ArrayList<SendTo>();
		for (Member member : membersList) {
			SendTo sendTo = new SendTo();
			sendTo.name = member.name;
			sendTo.sex = member.gender.getLabel();
			sendTo.destination = member.email;
			sentoList.add(sendTo);
		}
		return sentoList;
	}

	private static List<SendTo> fillSendToVisitors(List<Visitor> visitorsList) {
		List<SendTo> sentoList = new ArrayList<SendTo>();
		for (Visitor visitor : visitorsList) {
			SendTo sendTo = new SendTo();
			sendTo.name = visitor.name;
			sendTo.sex = visitor.gender.getLabel();
			sendTo.destination = visitor.email;
			sentoList.add(sendTo);
		}
		return sentoList;
	}

	private static Sender fillSender() {
		Sender sender = new Sender();
		sender.key = Admin.getLoggedUserInstitution().getInstitution().getInstitutionKey();
		sender.company = Admin.getLoggedUserInstitution().getInstitution().getInstitution();
		sender.from = Admin.getLoggedUserInstitution().getInstitution().getEmail();
		sender.postedAt = Utils.getCurrentDateTime();
		return sender;
	}
}

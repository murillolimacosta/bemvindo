package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import models.ConfigureTemplate;
import models.EmailMarketing;
import models.Event;
import models.Member;
import models.Visitor;
import notifiers.MailNotifier;

import org.apache.commons.collections.CollectionUtils;

import play.mvc.With;
import util.Utils;
import enumeration.GenderEnum;
import enumeration.MaritalStatusEnum;

@CRUD.For(models.EmailMarketing.class)
@With(Secure.class)
public class EmailMarketingCRUD extends CRUD {

	public static String STR_BLANK_MESSAGE = "Não há nada a enviar! Selecione ao menos um destinatário!";

	public static void emailMarketingVisitors() {
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true order by name, lastname").fetch();
		List<ConfigureTemplate> templates = ConfigureTemplate.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true").fetch();
		List<Event> events = Event.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true").fetch();
		GenderEnum[] genderEnum = GenderEnum.values();
		MaritalStatusEnum[] maritalStatusEnum = MaritalStatusEnum.values();
		render(visitors, templates, genderEnum, events, maritalStatusEnum);
	}

	public static void emailMarketingMembers() {
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true order by name, lastname").fetch();
		List<ConfigureTemplate> templates = ConfigureTemplate.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true").fetch();
		GenderEnum[] genderEnum = GenderEnum.values();
		MaritalStatusEnum[] maritalStatusEnum = MaritalStatusEnum.values();
		render(members, templates, genderEnum, maritalStatusEnum);
	}

	public static void sendEmailVisitors(String idTemplate, boolean all, String visitors, String genderEnum, String ageRange, String events, String maritalStatusEnum) {
		String output = "";
		/* Enviar para todos os visitantes */
		if (all) {
			output = emailToAllVisitors(idTemplate);
			render(output);
			return;
			/* Enviar para visitantes selecionados */
		} else if (!Utils.isNullOrEmpty(visitors)) {
			output = emailToSelectedVisitors(idTemplate, visitors);
			render(output);
			return;
			/* Enviar por sexo e faixa etária */
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				output = emailByVisitorGenderAndAgeRange(idTemplate, genderEnum, ageRange.trim());
				render(output);
				return;
			}
			/* Enviar por sexo apenas */
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			output = emailByVisitorGender(idTemplate, genderEnum);
			render(output);
			return;
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			/* Enviar por faixa etária apenas */
			if (validateAgeRange(ageRange.trim())) {
				output = emailByVisitorAgeRange(idTemplate, ageRange.trim());
				render(output);
				return;
			}
			/* Enviar por evento apenas */
		} else if (!Utils.isNullOrEmpty(events) && Utils.isNullOrEmpty(ageRange)) {
			output = emailByVisitorEvent(idTemplate, ageRange.trim());
			render(output);
			return;
			/* Enviar por evento e faixa etária */
		} else if (!Utils.isNullOrEmpty(events) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				output = emailByVisitorEventAndAgeRange(idTemplate, events, ageRange.trim());
				render(output);
				return;
			}
			/* Enviar por estado civil apenas apenas */
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			output = emailByVisitorMaritalStatus(idTemplate, maritalStatusEnum);
			render(output);
			return;
			/* Enviar por estado civil e faixa etária */
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				output = emailByVisitorMaritalStatusAndAgeRange(idTemplate, maritalStatusEnum, ageRange.trim());
				render(output);
				return;
			}
		}
		emailMarketingVisitors();
	}

	public static void sendEmailMembers(String idTemplate, boolean all, String members, String genderEnum, String ageRange, String maritalStatusEnum) {
		String output = null;
		/* Enviar para todos os membros */
		if (all) {
			output = emailToAllMembers(idTemplate);
			render(output);
			return;
			/* Enviar para membros selecionados */
		} else if (!Utils.isNullOrEmpty(members)) {
			output = emailToSelectedMembers(idTemplate, members);
			render(output);
			return;
			/* Enviar por sexo e faixa etária */
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				output = emailByMemberGenderAndAgeRange(idTemplate, genderEnum, ageRange.trim());
				render(output);
				return;
			}
			/* Enviar por sexo apenas */
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			output = emailByMemberGender(idTemplate, genderEnum);
			render(output);
			return;
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			/* Enviar por faixa etária apenas */
			if (validateAgeRange(ageRange.trim())) {
				output = emailByMemberAgeRange(idTemplate, ageRange.trim());
				render(output);
				return;
			}
			/* Enviar por estado civil apenas apenas */
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			output = emailByMemberMaritalStatus(idTemplate, maritalStatusEnum);
					render(output);
			return;
			/* Enviar por estado civil e faixa etária */
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				output = emailByMemberMaritalStatusAndAgeRange(idTemplate, maritalStatusEnum, ageRange.trim());
				render(output);
				return;
			}
		}
		emailMarketingMembers();
	}

	private static String emailToAllVisitors(String idTemplate) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailToAllMembers(String idTemplate) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailToSelectedVisitors(String idTemplate, String visitors) {
		List<Visitor> visitorsList = new ArrayList<Visitor>();
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String v[] = visitors.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Visitor visitor = Visitor.findById(Long.valueOf(id));
				visitorsList.add(visitor);
			}
		}
		if (CollectionUtils.isNotEmpty(visitorsList)) {
			return MailNotifier.sendMailVisitors(visitorsList, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailToSelectedMembers(String idTemplate, String members) {
		List<Member> membersList = new ArrayList<Member>();
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String v[] = members.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Member member = Member.findById(Long.valueOf(id));
				membersList.add(member);
			}
		}
		if (CollectionUtils.isNotEmpty(membersList)) {
			return MailNotifier.sendMailMembers(membersList, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorGenderAndAgeRange(String idTemplate, String genderEnum, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find(
				"institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByMemberGenderAndAgeRange(String idTemplate, String genderEnum, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find(
				"institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorGender(String idTemplate, String genderEnum) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByMemberGender(String idTemplate, String genderEnum) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorAgeRange(String idTemplate, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByMemberAgeRange(String idTemplate, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorEvent(String idTemplate, String events) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorEventAndAgeRange(String idTemplate, String events, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find(
				"institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorMaritalStatus(String idTemplate, String maritalStatusEnum) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByMemberMaritalStatus(String idTemplate, String maritalStatusEnum) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByVisitorMaritalStatusAndAgeRange(String idTemplate, String maritalStatusEnum, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find(
				"institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(visitors)) {
			return MailNotifier.sendMailVisitors(visitors, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static String emailByMemberMaritalStatusAndAgeRange(String idTemplate, String maritalStatusEnum, String ageRange) {
		Long idTemp = Long.valueOf(Utils.split(",", idTemplate));
		ConfigureTemplate template = ConfigureTemplate.findById(idTemp);
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find(
				"institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		if (CollectionUtils.isNotEmpty(members)) {
			return MailNotifier.sendMailMembers(members, template);
		}
		return STR_BLANK_MESSAGE;
	}

	private static boolean validateAgeRange(String ageRange) {
		validation.match(ageRange, "\\d{1,2}-\\d{2,3}").message("validation.matchAge.erro");
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			return false;
		}
		return true;
	}

	private static EmailMarketing setEmailVisitor(Visitor visitor, String templateId) {
		String id = Utils.split(",", templateId);
		ConfigureTemplate template = ConfigureTemplate.findById(Long.valueOf(id));
		EmailMarketing emailM = new EmailMarketing();
		emailM = new EmailMarketing();
		emailM.setName(visitor.getName());
		emailM.setEmail(visitor.getEmail());
		emailM.setTemplate(template.getTitle1());
		emailM.setPostedAt(new Date());
		return emailM;
	}

	private static EmailMarketing setEmailMember(Member member, String templateId) {
		String id = Utils.split(",", templateId);
		ConfigureTemplate template = ConfigureTemplate.findById(Long.valueOf(id));
		EmailMarketing emailM = new EmailMarketing();
		emailM = new EmailMarketing();
		emailM.setName(member.getName());
		emailM.setEmail(member.getEmail());
		emailM.setTemplate(template.getTitle1());
		emailM.setPostedAt(new Date());
		return emailM;
	}
}

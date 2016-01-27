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
import play.mvc.With;
import util.Utils;
import enumeration.GenderEnum;
import enumeration.MaritalStatusEnum;

@CRUD.For(models.EmailMarketing.class)
@With(Secure.class)
public class EmailMarketingCRUD extends CRUD {
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

	public static void sendEmailVisitors(String template, boolean all, String visitors, String genderEnum, String ageRange, String events, String maritalStatusEnum) {
		List<EmailMarketing> sentEmails = null;
		// ~~~~ Enviar para todos os visitantes
		if (all) {
			sentEmails = emailToAllVisitors(template);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar para visitantes selecionados
		} else if (!Utils.isNullOrEmpty(visitors)) {
			sentEmails = emailToSelectedVisitors(template, visitors);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar por sexo e faixa etária
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByVisitorGenderAndAgeRange(template, genderEnum, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
			// ~~~~ Enviar por sexo apenas
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentEmails = emailByVisitorGender(template, genderEnum);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			// ~~~~ Enviar por faixa etária apenas
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByVisitorAgeRange(template, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
			// ~~~~ Enviar por evento apenas
		} else if (!Utils.isNullOrEmpty(events) && Utils.isNullOrEmpty(ageRange)) {
			sentEmails = emailByVisitorEvent(template, ageRange.trim());
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar por evento e faixa etária
		} else if (!Utils.isNullOrEmpty(events) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByVisitorEventAndAgeRange(template, events, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
			// ~~~~ Enviar por estado civil apenas apenas
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentEmails = emailByVisitorMaritalStatus(template, maritalStatusEnum);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar por estado civil e faixa etária
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByVisitorMaritalStatusAndAgeRange(template, maritalStatusEnum, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
		}
		emailMarketingVisitors();
	}

	public static void sendEmailMembers(String template, boolean all, String members, String genderEnum, String ageRange, String maritalStatusEnum) {
		List<EmailMarketing> sentEmails = null;
		// ~~~~ Enviar para todos os membros
		if (all) {
			sentEmails = emailToAllMembers(template);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar para membros selecionados
		} else if (!Utils.isNullOrEmpty(members)) {
			sentEmails = emailToSelectedMembers(template, members);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar por sexo e faixa etária
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByMemberGenderAndAgeRange(template, genderEnum, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
			// ~~~~ Enviar por sexo apenas
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentEmails = emailByMemberGender(template, genderEnum);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			// ~~~~ Enviar por faixa etária apenas
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByMemberAgeRange(template, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
			// ~~~~ Enviar por estado civil apenas apenas
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentEmails = emailByMemberMaritalStatus(template, maritalStatusEnum);
			if (sentEmails != null) {
				render(sentEmails);
				return;
			}
			// ~~~~ Enviar por estado civil e faixa etária
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentEmails = emailByMemberMaritalStatusAndAgeRange(template, maritalStatusEnum, ageRange.trim());
				if (sentEmails != null) {
					render(sentEmails);
					return;
				}
			}
		}
		emailMarketingMembers();
	}

	private static List<EmailMarketing> emailToAllVisitors(String template) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailToAllMembers(String template) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and email is not null and isActive = true").fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailToSelectedVisitors(String template, String visitors) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String v[] = visitors.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Visitor visitor = Visitor.findById(Long.valueOf(id));
				if (!Utils.isNullOrEmpty(visitor.getEmail())) {
					if (emailToVisitors(visitor, template)) {
						emailM = setEmailVisitor(visitor, template);
						listEmail.add(emailM);
					}
				}
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailToSelectedMembers(String template, String members) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String v[] = members.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Member member = Member.findById(Long.valueOf(id));
				if (!Utils.isNullOrEmpty(member.getEmail())) {
					if (emailToMembers(member, template)) {
						emailM = setEmailMember(member, template);
						listEmail.add(emailM);
					}
				}
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorGenderAndAgeRange(String template, String genderEnum, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByMemberGenderAndAgeRange(String template, String genderEnum, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorGender(String template, String genderEnum) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByMemberGender(String template, String genderEnum) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and email is not null and isActive = true").fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorAgeRange(String template, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByMemberAgeRange(String template, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorEvent(String template, String events) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorEventAndAgeRange(String template, String events, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorMaritalStatus(String template, String maritalStatusEnum) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and email is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByMemberMaritalStatus(String template, String maritalStatusEnum) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and email is not null and isActive = true").fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByVisitorMaritalStatusAndAgeRange(String template, String maritalStatusEnum, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true")
				.fetch();
		for (Visitor visitor : visitors) {
			if (emailToVisitors(visitor, template)) {
				emailM = setEmailVisitor(visitor, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static List<EmailMarketing> emailByMemberMaritalStatusAndAgeRange(String template, String maritalStatusEnum, String ageRange) {
		List<EmailMarketing> listEmail = new ArrayList<EmailMarketing>();
		EmailMarketing emailM;
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and email is not null and isActive = true")
				.fetch();
		for (Member member : members) {
			if (emailToMembers(member, template)) {
				emailM = setEmailMember(member, template);
				listEmail.add(emailM);
			}
		}
		return listEmail;
	}

	private static boolean emailToVisitors(Visitor visitor, String idConfigureTemplate) {
		try {
			String id = Utils.split(",", idConfigureTemplate);
			ConfigureTemplate template = ConfigureTemplate.findById(Long.valueOf(id));
			if (template.getTemplate().toString().equals("Template1")) {
				MailNotifier.template1(visitor, template);
			} else if (template.getTemplate().toString().equals("Template2")) {
				MailNotifier.template2(visitor, template);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
			return false;
		}
	}

	private static boolean emailToMembers(Member member, String idConfigureTemplate) {
		try {
			String id = Utils.split(",", idConfigureTemplate);
			ConfigureTemplate template = ConfigureTemplate.findById(Long.valueOf(id));
			if (template.getTemplate().toString().equals("Template1")) {
				MailNotifier.template1(member, template);
			} else if (template.getTemplate().toString().equals("Template2")) {
				MailNotifier.template2(member, template);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
			return false;
		}
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

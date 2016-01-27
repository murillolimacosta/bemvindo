package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import models.Event;
import models.Member;
import models.SMS;
import models.Visitor;
import notifiers.SMSNotifier;
import play.Play;
import play.mvc.Controller;
import util.Utils;
import enumeration.GenderEnum;
import enumeration.MaritalStatusEnum;

public class SendSMS extends Controller {
	public static void smsVisitors() {
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and cellphone is not null and isActive = true order by name, lastname").fetch();
		List<SMS> smss = SMS.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isReuse = true and isActive = true").fetch();
		List<Event> events = Event.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true").fetch();
		GenderEnum[] genderEnum = GenderEnum.values();
		MaritalStatusEnum[] maritalStatusEnum = MaritalStatusEnum.values();
		render(visitors, smss, genderEnum, events, maritalStatusEnum);
	}
	
	public static void smsMembers() {
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and cellphone is not null and isActive = true order by name, lastname").fetch();
		List<SMS> smss = SMS.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isReuse = true and isActive = true").fetch();
		GenderEnum[] genderEnum = GenderEnum.values();
		MaritalStatusEnum[] maritalStatusEnum = MaritalStatusEnum.values();
		render(members, smss, genderEnum, maritalStatusEnum);
	}
	
	public static void sendSMSVisitors(SMS sms, boolean all, String visitors, String genderEnum, String ageRange, String events, String maritalStatusEnum) {
		List<SMS> sentSMSs = null;
		// ~~~~ Enviar para todos os visitantes
		if (all) {
			sentSMSs = smsToAllVisitors(sms);
			if (sentSMSs != null) {
				render(sentSMSs);
				return;
			}
			// ~~~~ Enviar para visitantes selecionados
		} else if (!Utils.isNullOrEmpty(visitors)) {
			sentSMSs = smsToSelectedVisitors(sms, visitors);
			if (sentSMSs != null) {
				render(sentSMSs);
				return;
			}
			// ~~~~ Enviar por sexo e faixa etária
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentSMSs = smsByVisitorGenderAndAgeRange(sms, genderEnum, ageRange.trim());
				if (sentSMSs != null) {
					render(sentSMSs);
					return;
				}
			}
			// ~~~~ Enviar por sexo apenas
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentSMSs = smsByVisitorGender(sms, genderEnum);
			if (sentSMSs != null) {
				render(sentSMSs);
				return;
			}
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			// ~~~~ Enviar por faixa etária apenas
			if (validateAgeRange(ageRange.trim())) {
				sentSMSs = smsByVisitorAgeRange(sms, ageRange.trim());
				if (sentSMSs != null) {
					render(sentSMSs);
					return;
				}
			}
			// ~~~~ Enviar por evento apenas
		} else if (!Utils.isNullOrEmpty(events) && Utils.isNullOrEmpty(ageRange)) {
			sentSMSs = smsByVisitorEvent(sms, ageRange.trim());
			if (sentSMSs != null) {
				render(sentSMSs);
				return;
			}
			// ~~~~ Enviar por evento e faixa etária
		} else if (!Utils.isNullOrEmpty(events) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentSMSs = smsByVisitorEventAndAgeRange(sms, events, ageRange.trim());
				if (sentSMSs != null) {
					render(sentSMSs);
					return;
				}
			}
			// ~~~~ Enviar por estado civil apenas apenas
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentSMSs = smsByVisitorMaritalStatus(sms, maritalStatusEnum);
			if (sentSMSs != null) {
				render(sentSMSs);
				return;
			}
			// ~~~~ Enviar por estado civil e faixa etária
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentSMSs = smsByVisitorMaritalStatusAndAgeRange(sms, maritalStatusEnum, ageRange.trim());
				if (sentSMSs != null) {
					render(sentSMSs);
					return;
				}
			}
		}
		smsVisitors();
	}

	public static void sendSMSMembers(SMS sms, boolean all, String members, String genderEnum, String ageRange, String maritalStatusEnum) {
		List<SMS> sentSMS = null;
		// ~~~~ Enviar para todos os membros
		if (all) {
			sentSMS = smsToAllMembers(sms);
			if (sentSMS != null) {
				render(sentSMS);
				return;
			}
			// ~~~~ Enviar para membros selecionados
		} else if (!Utils.isNullOrEmpty(members)) {
			sentSMS = smsToSelectedMembers(sms, members);
			if (sentSMS != null) {
				render(sentSMS);
				return;
			}
			// ~~~~ Enviar por sexo e faixa etária
		} else if (!Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentSMS = smsByMemberGenderAndAgeRange(sms, genderEnum, ageRange.trim());
				if (sentSMS != null) {
					render(sentSMS);
					return;
				}
			}
			// ~~~~ Enviar por sexo apenas
		} else if (!Utils.isNullOrEmpty(genderEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentSMS = smsByMemberGender(sms, genderEnum);
			if (sentSMS != null) {
				render(sentSMS);
				return;
			}
		} else if (Utils.isNullOrEmpty(genderEnum) && !Utils.isNullOrEmpty(ageRange)) {
			// ~~~~ Enviar por faixa etária apenas
			if (validateAgeRange(ageRange.trim())) {
				sentSMS = smsByMemberAgeRange(sms, ageRange.trim());
				if (sentSMS != null) {
					render(sentSMS);
					return;
				}
			}
			// ~~~~ Enviar por estado civil apenas apenas
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && Utils.isNullOrEmpty(ageRange)) {
			sentSMS = smsByMemberMaritalStatus(sms, maritalStatusEnum);
			if (sentSMS != null) {
				render(sentSMS);
				return;
			}
			// ~~~~ Enviar por estado civil e faixa etária
		} else if (!Utils.isNullOrEmpty(maritalStatusEnum) && !Utils.isNullOrEmpty(ageRange)) {
			if (validateAgeRange(ageRange.trim())) {
				sentSMS = smsByMemberMaritalStatusAndAgeRange(sms, maritalStatusEnum, ageRange.trim());
				if (sentSMS != null) {
					render(sentSMS);
					return;
				}
			}
		}
		smsMembers();
	}

	private static List<SMS> smsToAllVisitors(SMS sms) {
		List<SMS> listSMS = new ArrayList<SMS>();
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsToAllMembers(SMS sms) {
		List<SMS> listSMS = new ArrayList<SMS>();
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and cellphone is not null and isActive = true").fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsToSelectedVisitors(SMS sms, String visitors) {
		List<SMS> listSMS = new ArrayList<SMS>();
		String v[] = visitors.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Visitor visitor = Visitor.findById(Long.valueOf(id));
				if (!Utils.isNullOrEmpty(visitor.getCellphone())) {
					if (smsToVisitors(visitor, sms)) {
						sms = setSMSVisitor(visitor, sms.getDescription());
						listSMS.add(sms);
					}
				}
			}
		}
		return listSMS;
	}

	private static List<SMS> smsToSelectedMembers(SMS sms, String members) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String v[] = members.split(Pattern.quote(","));
		for (String str : v) {
			if (!Utils.isNullOrEmpty(str)) {
				String id = str.trim();
				Member member = Member.findById(Long.valueOf(id));
				if (!Utils.isNullOrEmpty(member.getCellphone())) {
					if (smsToMembers(member, sms)) {
						sms = setSMSMember(member, sms.getDescription());
						listSMS.add(sms);
					}
				}
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorGenderAndAgeRange(SMS sms, String genderEnum, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByMemberGenderAndAgeRange(SMS sms, String genderEnum, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true").fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorGender(SMS sms, String genderEnum) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByMemberGender(SMS sms, String genderEnum) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] gender = genderEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and gender = '" + gender[0] + "' and cellphone is not null and isActive = true").fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorAgeRange(SMS sms, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByMemberAgeRange(SMS sms, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true").fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorEvent(SMS sms, String events) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorEventAndAgeRange(SMS sms, String events, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and eventDay_id = '" + events + "' and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorMaritalStatus(SMS sms, String maritalStatusEnum) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and cellphone is not null and isActive = true").fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByMemberMaritalStatus(SMS sms, String maritalStatusEnum) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and cellphone is not null and isActive = true").fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByVisitorMaritalStatusAndAgeRange(SMS sms, String maritalStatusEnum, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Visitor> visitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true")
				.fetch();
		for (Visitor visitor : visitors) {
			if (smsToVisitors(visitor, sms)) {
				sms = setSMSVisitor(visitor, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static List<SMS> smsByMemberMaritalStatusAndAgeRange(SMS sms, String maritalStatusEnum, String ageRange) {
		List<SMS> listSMS = new ArrayList<SMS>();
		String[] age = ageRange.trim().split("-");
		String[] maritalStatus = maritalStatusEnum.split(Pattern.quote(","));
		List<Member> members = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and maritalStatus = '" + maritalStatus[0] + "' and age >= " + age[0] + " and age <= " + age[1] + " and cellphone is not null and isActive = true")
				.fetch();
		for (Member member : members) {
			if (smsToMembers(member, sms)) {
				sms = setSMSMember(member, sms.getDescription());
				listSMS.add(sms);
			}
		}
		return listSMS;
	}

	private static boolean smsToVisitors(Visitor visitor, SMS sms) {
		try {
			SMSNotifier.send(visitor.getCellphone(), sms.getSubject(), sms.getDescription());
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
			return false;
		}
	}

	private static boolean smsToMembers(Member member, SMS sms) {
		try {
			SMSNotifier.send(member.getCellphone(), sms.getSubject(), sms.getDescription());
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

	private static SMS setSMSVisitor(Visitor visitor, String message) {
		SMS sms = new SMS();
		sms = new SMS();
		sms.setName(visitor.getName());
		sms.setDestination(visitor.getCellphone());
		sms.setDescription(message);
		sms.setSentAt(new Date());
		return sms;
	}

	private static SMS setSMSMember(Member member, String message) {
		SMS sms = new SMS();
		sms = new SMS();
		sms.setName(member.getName());
		sms.setDestination(member.getCellphone());
		sms.setDescription(message);
		sms.setSentAt(new Date());
		return sms;
	}


}

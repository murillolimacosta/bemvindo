package controllers;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

import controllers.CRUD.ObjectType;
import models.City;
import models.ConfigureTemplate;
import models.Country;
import models.Institution;
import models.Intercessor;
import models.Member;
import models.State;
import models.User;
import models.Visitor;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import util.UserInstitutionParameter;

@With(Secure.class)
public class Admin extends Controller {
	public static UserInstitutionParameter userInstitutionParameter = null;

	static boolean enableMenu() {
		if (userBelongsToInstitution() && validateLicenseDate()) {
			return true;
		}
		return false;
	}

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user.name);
		}
	}

	@Before
	static void globals() {
		renderArgs.put("connected", getLoggedUserInstitution().getUser());
	}

	public static void firstStep() {
		List<Country> listCountries = Country.findAll();
		render(listCountries);
	}

	public static void save(User user) {
		validation.valid(user);
		if (validation.hasErrors()) {
			render("@form", user);
		}
		user.save();
		index();
	}

	public static void index() {
		User connectedUser = getLoggedUserInstitution().getUser();
		if (connectedUser == null || connectedUser.getInstitutionId() == 0) {
			Admin.firstStep();
		} else {
			/* Verify expiration license */
			if (validateLicenseDate()) {
				int contVisitors = Visitor.find("institutionId = " + connectedUser.getInstitutionId()).fetch().size();
				int contTemplates = ConfigureTemplate.find("institutionId = " + connectedUser.getInstitutionId()).fetch().size();
				int contMembers = Member.find("institutionId = " + connectedUser.getInstitutionId()).fetch().size();
				int contIntercessors = Intercessor.find("institutionId = " + connectedUser.getInstitutionId()).fetch().size();
				List<Visitor> listVisitors = Visitor.find("institutionId = " + connectedUser.getInstitutionId() + " and isActive = true order by postedAt desc").fetch(5);
				List<ConfigureTemplate> listTemplates = ConfigureTemplate.find("institutionId = " + connectedUser.getInstitutionId() + " and isActive = true order by postedAt desc").fetch(5);
				List<Member> listMembers = Member.find("institutionId = " + connectedUser.getInstitutionId() + " and isActive = true order by postedAt desc").fetch(5);
				Admin.enableUserConditions(connectedUser);
				render(listVisitors, listTemplates, contVisitors, contTemplates, contMembers, listMembers, contIntercessors, connectedUser);
			} else {
				/* Redirect to page of information about expired license */
				render("@admin.expiredLicense", connectedUser);
			}
		}
	}

	private static boolean validateLicenseDate() {
		if (getLoggedUserInstitution().getInstitution().getLicenseDate().after(new Date()))
			return true;
		else
			return false;
	}

	public static void create() throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Model object = (Model) constructor.newInstance();
		Binder.bindBean(params.getRootParamNode(), "object", object);
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/blank.html", type, object);
			} catch (TemplateNotFoundException e) {
				render("CRUD/blank.html", type, object);
			}
		}
		object._save();
		flash.success(play.i18n.Messages.get("crud.created", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		if (params.get("_saveAndAddAnother") != null) {
			redirect(request.controller + ".blank");
		}
		redirect(request.controller + ".show", object._key());
	}

	protected static void enableUserConditions(User user) {
		if (enableMenu()) {
			session.put("enableUser", "true");
		} else {
			session.put("enableUser", "false");
		}
		return;
	}

	public static User getLoggedUser() {
		String userId = session.get("logged");
		return userId == null ? null : (User) User.findById(Long.parseLong(userId));
	}

	public static Institution getLoggedInstitution() {
		long institutionId = getLoggedUser().getInstitutionId();
		return institutionId == 0 ? null : (Institution) Institution.findById(institutionId);
	}

	private static boolean userBelongsToInstitution() {
		if (getLoggedInstitution() == null) {
			return false;
		}
		return true;
	}

	public static UserInstitutionParameter getLoggedUserInstitution() {
		if (userInstitutionParameter == null)
			userInstitutionParameter = new UserInstitutionParameter();
		if (userInstitutionParameter.getUser() == null || userInstitutionParameter.getInstitution() == null) {
			userInstitutionParameter.setUser(getLoggedUser());
			if (getLoggedUser().getInstitutionId() > 0) {
				userInstitutionParameter.setInstitution(getLoggedInstitution());
			}
		}
		return userInstitutionParameter;
	}

}

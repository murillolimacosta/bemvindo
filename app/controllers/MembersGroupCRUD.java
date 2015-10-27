package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.sun.xml.internal.fastinfoset.stax.events.Util;

import controllers.CRUD.ObjectType;
import models.Country;
import models.Member;
import models.MemberGroup;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;
import play.templates.Template;
import play.templates.TemplateLoader;

@CRUD.For(models.MemberGroup.class)
@With(Secure.class)
public class MembersGroupCRUD extends CRUD {
	public static int AUTOCOMPLETE_MAX = 10;

	static Set auxList = null;

	public static void list(int page, String search, String searchFields, String orderBy, String order) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		if (page < 1) {
			page = 1;
		}
		String where = "institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId();
		List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
		Long count = type.count(search, searchFields, where);
		Long totalCount = type.count(null, null, where);
		try {
			render(type, objects, count, totalCount, page, orderBy, order);
		} catch (TemplateNotFoundException e) {
			render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
		}
	}

	public static void blank() throws Exception {
		auxList = new HashSet();
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		Constructor<?> constructor = type.entityClass.getDeclaredConstructor();
		constructor.setAccessible(true);
		Model object = (Model) constructor.newInstance();
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("MembersGroupCRUD/blank.html", type, object);
		}
	}
	
	public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        MemberGroup object = MemberGroup.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
        Set members = object.members;
        auxList = object.members;
        notFoundIfNull(object);
        try {
            render(type, object, members);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object, members);
        }
    }

	public static void save(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		MemberGroup object = MemberGroup.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
		notFoundIfNull(object);
		Binder.bindBean(params.getRootParamNode(), "object", object);
		object.members = null;
		object.members = auxList;
		Set members = object.members;
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/show.html", type, object, members);
			} catch (TemplateNotFoundException e) {
				render("CRUD/show.html", type, object, members);
			}
		}
		object._save();
		flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		redirect(request.controller + ".show", object._key());
	}

	public static void getMembersJSON() {
		List<Member> listMembers = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true order by postedAt desc").fetch();
		renderJSON(listMembers);
	}

	public static void listMembersAutoComplete(final String[] term) {
		List<Member> listMembers = Member.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true order by postedAt desc").fetch();
		List<Member> filteredMembers = new ArrayList<Member>();
		String aux = term[0];
		if (aux != null) {
			for (Member member : listMembers) {
				if (member.name.toLowerCase().startsWith(aux.toLowerCase())) {
					filteredMembers.add(member);
				}
				if (filteredMembers.size() == AUTOCOMPLETE_MAX) {
					break;
				}
			}
		}
		renderJSON(filteredMembers);
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

	public static void addMember(final String memberId) {
		if (!Util.isEmptyString(memberId)) {
			Set members = new HashSet();
			Member member = Member.findById(Long.valueOf(memberId));
			if (member != null) {
				if (!auxList.contains(member))
					auxList.add(member);
			}
			members = auxList;
			render("@includes.gridgroupmembers", members);
		}
	}

	public static void deleteMember(final String memberId) {
		if (memberId != null) {
			Member member = Member.findById(Long.valueOf(memberId));
			Set members = new HashSet();
			if (auxList.contains(member)) {
				auxList.remove(member);
			}
			members = auxList;
			render("@includes.gridgroupmembers", members);
		}
	}

}

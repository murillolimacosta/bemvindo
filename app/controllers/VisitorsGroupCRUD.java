package controllers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.xml.internal.fastinfoset.stax.events.Util;

import controllers.CRUD.ObjectType;
import models.Visitor;
import models.VisitorGroup;
import models.VisitorGroup;
import play.data.binding.Binder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@CRUD.For(models.VisitorGroup.class)
@With(Secure.class)
public class VisitorsGroupCRUD extends CRUD {
	
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
			render("VisitorsGroupCRUD/blank.html", type, object);
		}
	}
	
	public static void show(String id) throws Exception {
        ObjectType type = ObjectType.get(getControllerClass());
        notFoundIfNull(type);
        VisitorGroup object = VisitorGroup.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
        Set visitors = object.visitors;
        auxList = object.visitors;
        notFoundIfNull(object);
        try {
            render(type, object, visitors);
        } catch (TemplateNotFoundException e) {
            render("CRUD/show.html", type, object, visitors);
        }
    }

	public static void save(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		VisitorGroup object = VisitorGroup.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
		notFoundIfNull(object);
		Binder.bindBean(params.getRootParamNode(), "object", object);
		object.visitors = null;
		object.visitors = auxList;
		Set visitors = object.visitors;
		validation.valid(object);
		if (validation.hasErrors()) {
			renderArgs.put("error", play.i18n.Messages.get("crud.hasErrors"));
			try {
				render(request.controller.replace(".", "/") + "/show.html", type, object, visitors);
			} catch (TemplateNotFoundException e) {
				render("CRUD/show.html", type, object, visitors);
			}
		}
		object._save();
		flash.success(play.i18n.Messages.get("crud.saved", type.modelName));
		if (params.get("_save") != null) {
			redirect(request.controller + ".list");
		}
		redirect(request.controller + ".show", object._key());
	}

	public static void getVisitorsJSON() {
		List<Visitor> listVisitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true order by postedAt desc").fetch();
		renderJSON(listVisitors);
	}

	public static void listVisitorsAutoComplete(final String[] term) {
		List<Visitor> listVisitors = Visitor.find("institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " and isActive = true order by postedAt desc").fetch();
		List<Visitor> filteredVisitors = new ArrayList<Visitor>();
		String aux = term[0];
		if (aux != null) {
			for (Visitor visitor : listVisitors) {
				if (visitor.name.toLowerCase().startsWith(aux.toLowerCase())) {
					filteredVisitors.add(visitor);
				}
				if (filteredVisitors.size() == AUTOCOMPLETE_MAX) {
					break;
				}
			}
		}
		renderJSON(filteredVisitors);
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

	public static void addVisitor(final String visitorId) {
		if (!Util.isEmptyString(visitorId)) {
			Set visitors = new HashSet();
			Visitor visitor = Visitor.findById(Long.valueOf(visitorId));
			if (visitor != null) {
				if (!auxList.contains(visitor))
					auxList.add(visitor);
			}
			visitors = auxList;
			render("@includes.gridgroupvisitors", visitors);
		}
	}

	public static void deleteVisitor(final String visitorId) {
		if (visitorId != null) {
			Visitor visitor = Visitor.findById(Long.valueOf(visitorId));
			Set visitors = new HashSet();
			if (auxList.contains(visitor)) {
				auxList.remove(visitor);
			}
			visitors = auxList;
			render("@includes.gridgroupvisitors", visitors);
		}
	}
}

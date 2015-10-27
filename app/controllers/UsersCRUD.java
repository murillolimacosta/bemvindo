package controllers;

import java.util.List;

import controllers.CRUD.ObjectType;
import models.City;
import models.Country;
import models.Member;
import models.State;
import models.User;
import models.Visitor;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;

@CRUD.For(models.User.class)
@With(Secure.class)
public class UsersCRUD extends CRUD {
	public static void list(int page, String search, String searchFields, String orderBy, String order) {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		if (page < 1) {
			page = 1;
		}
		String where = "id = " + Admin.getLoggedUserInstitution().getUser().getId();
		List<Model> objects = type.findPage(page, search, searchFields, orderBy, order, where);
		Long count = type.count(search, searchFields, where);
		Long totalCount = type.count(null, null, where);
		try {
			render(type, objects, count, totalCount, page, orderBy, order);
		} catch (TemplateNotFoundException e) {
			render("CRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
		}
	}
	
	public static void show(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		// Filtro pelo usuário conectado para proteger os dados dos demais
		// usuários
		User object = User.find("id = " + Admin.getLoggedUserInstitution().getUser().getId()).first();
		notFoundIfNull(object);
		Country country = Country.find("byId", object.getCountryId()).first();
		State state = State.find("byId", object.getStateId()).first();
		City city = City.find("byId", object.getCityId()).first();
		try {
			render(type, object, country, state, city);
		} catch (TemplateNotFoundException e) {
			render("MembersCRUD/show.html", type, object, country, state, city);
		}
	}
}

package controllers;

import java.io.File;
import java.util.List;

import controllers.CRUD.ObjectType;
import models.ConfigureTemplate;
import models.Institution;
import models.Visitor;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;
import play.vfs.VirtualFile;

@CRUD.For(models.ConfigureTemplate.class)
@With(Secure.class)
public class ConfigureTemplateCRUD extends CRUD {
	public static void templateExample1() {
		render();
	}

	public static void templateExample2() {
		render();
	}

	public static void renderTemplate1(Institution institution, ConfigureTemplate template) {
		render(institution, template);
	}

	public static void renderTemplate2(Institution institution, ConfigureTemplate template) {
		render(institution, template);
	}

	public static void viewTemplate(long id) {
		Institution institution = Institution.findById(Admin.getLoggedUserInstitution().getInstitution().getId());
		ConfigureTemplate template = ConfigureTemplate.findById(id);
		if (template.getTemplate().name().equals("Template1")) {
			renderTemplate1(institution, template);
		}
		if (template.getTemplate().name().equals("Template2")) {
			renderTemplate2(institution, template);
		}
	}

	public static void getImage(long id) {
		Institution institution = Institution.findById(Admin.getLoggedUserInstitution().getInstitution().getId());
		notFoundIfNull(institution);
		if (institution.getLogo() != null) {
			renderBinary(institution.getLogo().get());
			return;
		} else {
			VirtualFile vf = VirtualFile.fromRelativePath("/public/images/logo-271x149.png");
			File f = vf.getRealFile();
			renderBinary(f);
		}
	}

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

	public static void show(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		// Filtro pelo usuário conectado para proteger os dados dos demais usuários
		ConfigureTemplate object = ConfigureTemplate.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
		notFoundIfNull(object);
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("CRUD/show.html", type, object);
		}
	}
	
	public static void simple() {
		render();
	}
}

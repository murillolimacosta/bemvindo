package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import models.Visitor;
import models.VisitorPrayOrder;
import play.db.Model;
import play.exceptions.TemplateNotFoundException;
import play.mvc.With;
import util.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.xml.internal.fastinfoset.stax.events.Util;

@CRUD.For(models.VisitorPrayOrder.class)
@With(Secure.class)
public class VisitorPrayOrderCRUD extends CRUD {

	public static Set<VisitorPrayOrder> auxList = new HashSet<VisitorPrayOrder>();
	public static String visitorId = null;
	public static List<VisitorPrayOrder> prayOrders = null;

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
			render("IntercessorsCRUD/list.html", type, objects, count, totalCount, page, orderBy, order);
		}
	}

	public static void show(String id) throws Exception {
		ObjectType type = ObjectType.get(getControllerClass());
		notFoundIfNull(type);
		// Filtro pelo usuário conectado para proteger os dados dos demais
		// usuários
		VisitorPrayOrder object = VisitorPrayOrder.find("id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId()).first();
		notFoundIfNull(object);
		try {
			render(type, object);
		} catch (TemplateNotFoundException e) {
			render("CRUD/show.html", type, object);
		}
	}

	public static void prayOrders(final String id) {
		List<VisitorPrayOrder> prayOrders = getPrayOrdersByIdVisitor(id);
		if (!Utils.isNullOrEmpty(id))
			visitorId = id;
		render(id, prayOrders);
	}

	public static String addPrayOrders(String values) {
		String[] fields = request.params.data.get("body");
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(fields[0]).getAsJsonObject();
		int visitorId = obj.get("visitorId").getAsInt();
		Visitor visitor = Visitor.findById(Long.valueOf(visitorId));
		if (visitor != null) {
			JsonElement descriptionElement = obj.get("description");
			removeAllPrayOrdersByIdVisitor(String.valueOf(visitorId));
			if (descriptionElement.isJsonArray()) {
				JsonArray jsonArrayDescription = (JsonArray) descriptionElement;
				for (JsonElement prayOrder : jsonArrayDescription) {
					if (prayOrder != null) {
						savePrayOrder(visitor, prayOrder.getAsString());
					}
				}
			} else if (descriptionElement.isJsonPrimitive()) {
				if (descriptionElement != null) {
					savePrayOrder(visitor, descriptionElement.getAsString());
				}
			}
			return "{\"msg\":\"success\"}";
		}
		return "{\"msg\":\"error\"}";
	}

	private static void savePrayOrder(Visitor visitor, String description) {
		VisitorPrayOrder visitorPrayOrder = new VisitorPrayOrder();
		visitorPrayOrder.setVisitor(visitor);
		visitorPrayOrder.setDescription(description);
		visitorPrayOrder.setPostedAt(Utils.getCurrentDateTimeByFormat("dd/MM/yyyy HH:mm:ss"));
		visitorPrayOrder.setInstitutionId(Admin.getLoggedUserInstitution().getInstitution().getId());
		visitorPrayOrder.willBeSaved = true;
		validation.valid(visitorPrayOrder);
		if (!validation.hasErrors()) {
			visitorPrayOrder.save();
		}
	}

	public static List<String> getItemsDescription(String visitorId) {
		List<String> listDescriptions = new ArrayList<String>();
		List<VisitorPrayOrder> prayOrdersList = getPrayOrdersByIdVisitor(visitorId);
		for (VisitorPrayOrder prayOrder : prayOrdersList) {
			if (StringUtils.isNotBlank(prayOrder.description)) {
				listDescriptions.add(prayOrder.description);
			}
		}
		return listDescriptions;
	}

	public static List<VisitorPrayOrder> getPrayOrdersByIdVisitor(String id) {
		return VisitorPrayOrder.find("visitor.id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " order by description asc").fetch();
	}

	public static int removeAllPrayOrdersByIdVisitor(String id) {
		return VisitorPrayOrder.delete("visitor.id = " + id + " and institutionId = " + Admin.getLoggedUserInstitution().getInstitution().getId() + " order by description asc");
	}

}

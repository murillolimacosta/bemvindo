package controllers;

import java.sql.Connection;
import java.sql.Statement;

import javassist.compiler.ast.Stmnt;
import models.City;
import models.Country;
import models.Event;
import models.Institution;
import models.State;
import models.User;
import play.Play;
import play.mvc.Controller;
import util.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class PopulateDB extends Controller {
	public static final String STR_EVENTS_JSON_FILE_PATH = "\\conf\\populate\\events.json";
	public static final String STR_COUNTRIES_JSON_FILE_PATH = "\\conf\\populate\\countries.json";
	public static final String STR_STATES_JSON_FILE_PATH = "\\conf\\populate\\states.json";
	public static final String STR_CITIES_JSON_FILE_PATH = "\\conf\\populate\\cities.json";
	public static final String STR_USERS_JSON_FILE_PATH = "\\conf\\populate\\users.json";
	public static final String STR_INSTITUTIONS_JSON_FILE_PATH = "\\conf\\populate\\institutions.json";

	public static void populate() {

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String jsonContent = null;
		/* Populate events */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_EVENTS_JSON_FILE_PATH));
		// new PopulateDB().configureAutoIncrementValue();
		/* Populate events */
		new PopulateDB().populateEvents(jsonContent, gson);
		/* Populate countries */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_COUNTRIES_JSON_FILE_PATH));
		new PopulateDB().populateCountries(jsonContent, gson);
		/* Populate states */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_STATES_JSON_FILE_PATH));
		new PopulateDB().populateStates(jsonContent, gson);
		/* Populate cities */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_CITIES_JSON_FILE_PATH));
		new PopulateDB().populateCities(jsonContent, gson);
		/* Populate users */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_USERS_JSON_FILE_PATH));
		new PopulateDB().populateUsers(jsonContent, gson);
		/* Populate institutions */
		jsonContent = Utils.getJsonFileContent(Play.getFile(STR_INSTITUTIONS_JSON_FILE_PATH));
		// new PopulateDB().populateInstitutions(jsonContent, gson);
		render();
	}

	private void configureAutoIncrementValue() {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE Event AUTO_INCREMENT = 1 ; ");
		sb.append("ALTER TABLE Country AUTO_INCREMENT = 1 ; ");
		sb.append("ALTER TABLE State AUTO_INCREMENT = 1 ; ");
		sb.append("ALTER TABLE City AUTO_INCREMENT = 1 ; ");
		sb.append("ALTER TABLE User AUTO_INCREMENT = 1 ; ");
		Connection conn = play.db.DB.getConnection();
		try {
			Statement stm = conn.createStatement();
			try {
				stm.execute(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			} finally {
				stm.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void populateEvents(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "events");
		try {
			Event event = new Event();
			if (event.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					event = gson.fromJson(elem, Event.class);
					event.id = 0l;
					event.setActive(true);
					event.willBeSaved = true;
					validation.valid(event);
					if (!validation.hasErrors()) {
						event.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateCountries(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "countries");
		try {
			Country country = new Country();
			if (country.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					country = gson.fromJson(elem, Country.class);
					country.willBeSaved = true;
					validation.valid(country);
					if (!validation.hasErrors()) {
						country.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateStates(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "states");
		try {
			State state = new State();
			if (state.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					state = gson.fromJson(elem, State.class);
					state.willBeSaved = true;
					validation.valid(state);
					if (!validation.hasErrors()) {
						state.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateCities(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "cities");
		try {
			City city = new City();
			if (city.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					city = gson.fromJson(elem, City.class);
					city.willBeSaved = true;
					validation.valid(city);
					if (!validation.hasErrors()) {
						city.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateUsers(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "users");
		try {
			User user = new User();
			if (user.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					user = gson.fromJson(elem, User.class);
					user.id = 0l;
					user.setActive(true);
					user.willBeSaved = true;
					validation.valid(user);
					if (!validation.hasErrors()) {
						user.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateInstitutions(String jsonContent, Gson gson) {
		JsonArray jsonArray = Utils.getJsonArray(jsonContent, "institutions");
		try {
			Institution institution = new Institution();
			if (institution.findAll().isEmpty()) {
				for (JsonElement elem : jsonArray) {
					institution = gson.fromJson(elem, Institution.class);
					institution.id = 0l;
					institution.willBeSaved = true;
					validation.valid(institution);
					if (!validation.hasErrors()) {
						institution.merge();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.jpa.Model;

@Entity
public class Country extends Model {
	@Id
	public Long id;
	
	public String name;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public static Country verifyById(long id) {
		return find("byId", id).first();
	}

}

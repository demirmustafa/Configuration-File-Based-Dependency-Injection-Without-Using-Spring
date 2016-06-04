package org.javalog.model;

public class Company {

	private String name;
	private Person ceo;
	private Person cto;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getCeo() {
		return ceo;
	}

	public void setCeo(Person ceo) {
		this.ceo = ceo;
	}

	public Person getCto() {
		return cto;
	}

	public void setCto(Person cto) {
		this.cto = cto;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", ceo=" + ceo + ", cto=" + cto + "]";
	}

}

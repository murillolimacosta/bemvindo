package enumeration;

public enum TemplateEnum {
	T1("Template1", 1), T2("Template2", 2);

	String label;
	int value;

	TemplateEnum(String label, int value) {
		this.value = value;
		this.label = label;
	}

	@Override
	public String toString() {
		return this.label;
	}

	public int getValue() {
		return this.value;
	}

	public String getLabel() {
		return label;
	}

}

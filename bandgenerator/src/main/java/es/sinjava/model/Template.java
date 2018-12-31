package es.sinjava.model;

import java.util.HashMap;

public class Template extends HashMap<String, String> {

	private static final long serialVersionUID = 1L;
	private String templateName;
	private FieldContainer fieldContainer;

	public static final String BODY = "body";
	public static final String HEADER = "header";
	public static final String FOOTER = "footer";

	public FieldContainer getFieldContainer() {
		return fieldContainer;
	}

	public void setFieldContainer(FieldContainer fieldContainer) {
		this.fieldContainer = fieldContainer;
	}

	public Template(String templName) {
		super();
		fieldContainer = new FieldContainer();
		fieldContainer.setContainer(new HashMap<String, String>());
		setTemplateName(templName);
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String put(String key, String value) {
		// Buscamos si tiene algún parametro
		if (value.contains(">>>")) {
			String[] inputToken = value.split(">>");
			for (String token : inputToken) {
				if (token.startsWith(">")) {
					String field = token.substring(1, token.lastIndexOf(">"));
					fieldContainer.getContainer().put(field, "");
				}
			}
		}
		return super.put(key, value);
	}

}

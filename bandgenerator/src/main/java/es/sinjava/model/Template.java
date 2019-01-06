/*
 * 
 */
package es.sinjava.model;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Template.
 */
public class Template extends HashMap<String, String> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The template name. */
	private String templateName;
	
	/** The field container. */
	private FieldContainer fieldContainer;

	/** The Constant BODY. */
	public static final String BODY = "body";
	
	/** The Constant HEADER. */
	public static final String HEADER = "header";
	
	/** The Constant FOOTER. */
	public static final String FOOTER = "footer";
	
	/** The Constant QR. */
	public static final String QR = "qr";

	/**
	 * Gets the field container.
	 *
	 * @return the field container
	 */
	public FieldContainer getFieldContainer() {
		return fieldContainer;
	}

	/**
	 * Sets the field container.
	 *
	 * @param fieldContainer the new field container
	 */
	public void setFieldContainer(FieldContainer fieldContainer) {
		this.fieldContainer = fieldContainer;
	}

	/**
	 * Instantiates a new template.
	 *
	 * @param templName the templ name
	 */
	public Template(String templName) {
		super();
		fieldContainer = new FieldContainer();
		fieldContainer.setContainer(new HashMap<String, String>());
		setTemplateName(templName);
	}

	/**
	 * Gets the template name.
	 *
	 * @return the template name
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * Sets the template name.
	 *
	 * @param templateName the new template name
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}

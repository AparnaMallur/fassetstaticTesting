package com.fasset.entities.abstracts;

import java.beans.Transient;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractEntityID extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long id;

	// Este metodo es abstracto porque las anotaciones no son heredables
	public abstract Long getId();

	// Este metodo es protegido para evitar que un programador pueda poner un
	// idenfificador en la instancia, ya que los identitificadores deben ser
	// gestionados por la capa de persistencia
	protected void setId(final Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntityID other = (AbstractEntityID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Transient
	public String getFormatedNumber() {
		return StringUtils.leftPad(String.valueOf(getId()), 4, '0');
	}
}
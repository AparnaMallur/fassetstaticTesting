package com.fasset.entities.abstracts;

public abstract class AbstractEntityIDStr extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String id;

	// Este metodo es abstracto porque las anotaciones no son heredables
	public abstract String getId();

	// Este metodo es protegido para evitar que un programador pueda poner un
	// idenfificador en la instancia, ya que los identitificadores deben ser
	// gestionados por la capa de persistencia
	protected void setId(final String id) {
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
		AbstractEntityIDStr other = (AbstractEntityIDStr) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {

	private static final long serialVersionUID = 1L; // Serializable em Java, serve para o objeto poder ser gravador em arquivo, trafegado em rede, sempre bom colocar;
	private Integer id;
	private String name;
	
	public Department() {
	}

	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override 
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) { // hashCode e equals para os objetos possam ser comparados pelo CONTEÚDO e não pela ref. de ponteiros;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "DepartmentId: " + id + ", DepName: " + name;
	}
}
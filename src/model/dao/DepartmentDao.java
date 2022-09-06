package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj); 
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
}

/* Padr�o DAO - Objeto respons�vel por fazer acesso a dados relacionados a esta entidade (Department);
 * DepartmentDao - Objeto que ir� mexer nos dados dos departamentos, inser��o, atualiza��o, dele��o, sele��o, etc;
 * Cada DAO ser� uma interface;
*/ 
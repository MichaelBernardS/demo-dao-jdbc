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

/* Padrão DAO - Objeto responsável por fazer acesso a dados relacionados a esta entidade (Department);
 * DepartmentDao - Objeto que irá mexer nos dados dos departamentos, inserção, atualização, deleção, seleção, etc;
 * Cada DAO será uma interface;
*/ 
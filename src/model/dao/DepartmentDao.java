package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj); // Operação responsável por inserir no BD este objeto que enviar como parâmetro de entrada;
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); // Operaçao retornando um department, recebendo um integer id como argumento; Operação responsável por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se não existir, vai retornar nulo;
	List<Department> findAll(); // Operação para retornar todos os departamentos;
}
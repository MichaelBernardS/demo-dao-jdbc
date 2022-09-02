package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	void insert(Department obj); // Opera��o respons�vel por inserir no BD este objeto que enviar como par�metro de entrada;
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id); // Opera�ao retornando um department, recebendo um integer id como argumento; Opera��o respons�vel por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se n�o existir, vai retornar nulo;
	List<Department> findAll(); // Opera��o para retornar todos os departamentos;
}

/* Padr�o DAO - Objeto respons�vel por fazer acesso a dados relacionados a esta entidade (Department);
 * DepartmentDao - Objeto que ir� mexer nos dados dos departamentos, inser��o, atualiza��o, dele��o, sele��o, etc;
 * Cada DAO ser� uma interface;
*/ 
package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj); // Função para inserir um novo vendedor; Obj será o objeto que passar como argumento p isso;
	void update(Seller obj); // Atualização dos dados de um vendedor;
	void deleteById(Integer id); // Operação de deletar um vendedor;
	Seller findById(Integer id); // Operaçao retornando um Seller, recebendo um integer id como argumento; Operação responsável por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se não existir, vai retornar nulo;
	List<Seller> findAll(); // Operação para retornar todos os vendedores;
	List<Seller> findByDepartment(Department department); // Operação responsável por buscar os vendedores dado um departamento;
	
}

/* Padrão DAO - Objeto responsável por fazer acesso a dados relacionados a esta entidade (Seller);
 * SellerDao - Objeto que irá mexer nos dados dos sellers, inserção, atualização, deleção, seleção, etc;
 * Cada DAO será uma interface; Objeto Factory que será responsável por instanciar os objetos DAO (Através da implementação);
*/ 
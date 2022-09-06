package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj); // Fun��o para inserir um novo vendedor; Obj ser� o objeto que passar como argumento p isso;
	void update(Seller obj); // Atualiza��o dos dados de um vendedor;
	void deleteById(Integer id); // Opera��o de deletar um vendedor;
	Seller findById(Integer id); // Opera�ao retornando um Seller, recebendo um integer id como argumento; Opera��o respons�vel por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se n�o existir, vai retornar nulo;
	List<Seller> findAll(); // Opera��o para retornar todos os vendedores;
	List<Seller> findByDepartment(Department department); // Opera��o respons�vel por buscar os vendedores dado um departamento;
	
}

/* Padr�o DAO - Objeto respons�vel por fazer acesso a dados relacionados a esta entidade (Seller);
 * SellerDao - Objeto que ir� mexer nos dados dos sellers, inser��o, atualiza��o, dele��o, sele��o, etc;
 * Cada DAO ser� uma interface; Objeto Factory que ser� respons�vel por instanciar os objetos DAO (Atrav�s da implementa��o);
*/ 
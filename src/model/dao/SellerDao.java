package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj); // Opera��o respons�vel por inserir no BD este objeto que enviar como par�metro de entrada;
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id); // Opera�ao retornando um Seller, recebendo um integer id como argumento; Opera��o respons�vel por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se n�o existir, vai retornar nulo;
	List<Seller> findAll(); // Opera��o para retornar todos os vendedores;
}

/* Padr�o DAO - Objeto respons�vel por fazer acesso a dados relacionados a esta entidade (Seller);
 * SellerDao - Objeto que ir� mexer nos dados dos sellers, inser��o, atualiza��o, dele��o, sele��o, etc;
 * Cada DAO ser� uma interface; Objeto Factory que ser� respons�vel por instanciar os objetos DAO (Atrav�s da implementa��o);
*/ 
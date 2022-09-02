package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	void insert(Seller obj); // Operação responsável por inserir no BD este objeto que enviar como parâmetro de entrada;
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id); // Operaçao retornando um Seller, recebendo um integer id como argumento; Operação responsável por pegar esse id, e consultar no BD um objeto com esse id, se existir, vai retornar, e se não existir, vai retornar nulo;
	List<Seller> findAll(); // Operação para retornar todos os vendedores;
}

/* Padrão DAO - Objeto responsável por fazer acesso a dados relacionados a esta entidade (Seller);
 * SellerDao - Objeto que irá mexer nos dados dos sellers, inserção, atualização, deleção, seleção, etc;
 * Cada DAO será uma interface; Objeto Factory que será responsável por instanciar os objetos DAO (Através da implementação);
*/ 
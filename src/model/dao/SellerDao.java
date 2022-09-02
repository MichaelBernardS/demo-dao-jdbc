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
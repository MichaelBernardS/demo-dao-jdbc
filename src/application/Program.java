package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa não conhece a implementação, só conhece a interface, é também uma forma de fzer injeção de dependência sem explicitar a implementação;

		System.out.println("==== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("==== TEST 2: seller findByDepartment ====");
		Department department = new Department(2, null); // Instanciando um departamento p passar como argumento p função abaixo de buscar por departamento;
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("==== TEST 3: seller findAll ====");
		list = sellerDao.findAll(); // Reaproveitando a lista instanciada acima, descartando os dados acima;
		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("==== TEST 4: seller insert  ====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department); // Inserindo os dados do novo vendedor que será inserido; null pro novo id, ele gerará automático; Reaproveitando o department lá acima, com o id, não precisando do nome do mesmo;
		sellerDao.insert(newSeller); // Toda vez que rodar, vai inserir um novo vendedor;
		System.out.println("Inserted! New id = " + newSeller.getId());
	}
}
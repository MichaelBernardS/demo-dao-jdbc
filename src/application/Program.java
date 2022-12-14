package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa n?o conhece a implementa??o, s? conhece a interface, ? tamb?m uma forma de fzer inje??o de depend?ncia sem explicitar a implementa??o;

		System.out.println("==== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("==== TEST 2: seller findByDepartment ====");
		Department department = new Department(2, null); // Instanciando um departamento p passar como argumento p fun??o abaixo de buscar por departamento;
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
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department); // Inserindo os dados do novo vendedor que ser? inserido; null pro novo id, ele gerar? autom?tico; Reaproveitando o department l? acima, com o id, n?o precisando do nome do mesmo;
		sellerDao.insert(newSeller); // Toda vez que rodar, vai inserir um novo vendedor;
		System.out.println("Inserted! New id = " + newSeller.getId());
		
		System.out.println("==== TEST 5: seller update  ====");
		seller = sellerDao.findById(1); // Reaproveitando o seller acima, e carregar os dados do vendedor 1;
		seller.setName("Martha Wayne"); // Setando um novo nome pra esse vendedor;
		sellerDao.update(seller);
		System.out.println("Update completed");
		
		System.out.println("==== TEST 5: seller update  ====");
		System.out.print("Enter id for delete test: ");
		int id = sc.nextInt();
		sellerDao.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
	}
}
package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa n�o conhece a implementa��o, s� conhece a interface, � tamb�m uma forma de fzer inje��o de depend�ncia sem explicitar a implementa��o;

		System.out.println("==== TEST 1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("==== TEST 2: seller findByDepartment ====");
		Department department = new Department(2, null); // Instanciando um departamento p passar como argumento p fun��o abaixo de buscar por departamento;
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println("==== TEST 3: seller findByDepartment ====");
		list = sellerDao.findAll(); // Reaproveitando a lista instanciada acima, descartando os dados acima;
		for (Seller obj : list) {
			System.out.println(obj);
		}
	}
}
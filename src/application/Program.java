package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department obj = new Department(1, "Books");
		
		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, obj);
		
		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa não conhece a implementação, só conhece a interface, é também uma forma de fzer injeção de dependência sem explicitar a implementação;

		System.out.println(obj);
		
		System.out.println(seller);
	}
}
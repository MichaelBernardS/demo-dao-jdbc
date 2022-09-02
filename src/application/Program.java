package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa não conhece a implementação, só conhece a interface, é também uma forma de fzer injeção de dependência sem explicitar a implementação;

		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
	}
}
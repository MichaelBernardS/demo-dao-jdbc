package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao(); // Desta forma, o programa n�o conhece a implementa��o, s� conhece a interface, � tamb�m uma forma de fzer inje��o de depend�ncia sem explicitar a implementa��o;

		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
	}
}
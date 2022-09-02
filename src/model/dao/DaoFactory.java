package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); // Macete usado para não precisar expor a implementação, e sim a interface;
	}
}

// Fábrica de DAOs; Operações estáticas para instanciar os DAOs; Classe auxiliar, responsável por instanciar os DAOs;
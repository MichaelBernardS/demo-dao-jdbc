package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(); // Macete usado para n�o precisar expor a implementa��o, e sim a interface;
	}
}

// F�brica de DAOs; Opera��es est�ticas para instanciar os DAOs; Classe auxiliar, respons�vel por instanciar os DAOs;
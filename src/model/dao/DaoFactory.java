package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); // Macete usado para n?o precisar expor a implementa??o, e sim a interface;
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}

// F?brica de DAOs; Opera??es est?ticas para instanciar os DAOs; Classe auxiliar, respons?vel por instanciar os DAOs;
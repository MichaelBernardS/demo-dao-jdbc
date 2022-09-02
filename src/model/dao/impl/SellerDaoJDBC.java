package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn; // Objeto conn criado p ter ele a disposição em qqer lugar da classe SellerDaoJDBC, p n precisar ficar instanciando p tds os métodos abaixo;
	
	public SellerDaoJDBC(Connection conn) { // Construtor manual, de conexão c BD, injetando dependência do conn p classe SellerDaoJDBC;
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
	}

	@Override
	public void update(Seller obj) {
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Seller findById(Integer id) { // Método para achar um vendedor por id;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " // Buscando tds os campos do vendedor + o nome do departamento (dando um apelido a ele como DepName)
					+ "FROM seller INNER JOIN department " // Dando um JOIN para buscar os dados das 2 tabelas;
					+ "ON seller.DepartmentId = department.Id " // Tanto de vendedor qnt departamento;
					+ "WHERE seller.Id = ? "); // Onde o id será o informado abaixo (depois);
			st.setInt(1, id); // id que recebeu como parâmetro no método;
			rs = st.executeQuery(); // Comando SQL vai ser executado, e o resultado vai cair dentro do resultSet;
			// É preciso entender aqui, que ao buscar no BD uma tabela, ele n puxa as infos em forma de tabela, em linhas e colunas, 
			// e sim OBJETOS, pois estamos programando em orientação a objetos; Portanto, precisamos associar o Department com Seller;
			// Associação dos objetos;
			if (rs.next()) { // Testar se veio algum resultado, pois o rs começa no 0;
				Department dep = new Department(); // Instanciando um departamento, e associando abaixo os valores a ele;
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));
				Seller obj = new Seller(); // Agora, instanciando um vendedor, e associando abaixo os valores a ele;
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				obj.setDepartment(dep); // É necessário aqui instanciar o departamento todo, logo, o objeto criado acima;
				return obj;
			}
			return null; // Se n achar nd, retorna nulo, pois quer dizer q n tem nenhum vendedor, c esse id informado;
		}
		catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally { // finally para fechar todos os recursos;
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// Não fecha a conexão, pois pode ter mais operações dentro desta classe, a conexão se fecha apenas no programa;
		}
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}
}
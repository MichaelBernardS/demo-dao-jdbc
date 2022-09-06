package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO department "
					+ "(Name) "
					+ "VALUES "
					+ "(?)", 
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) { // Testando se as linhas alteradas foram maior que 0;
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {// if pois estamos inserindo apenas um dado;
					int id = rs.getInt(1); // Se existir, pegar o id gerado;
					obj.setId(id); // E atribuir dentro do objeto obj
					DB.closeResultSet(rs); // Fechando dentro do escopo do if, pois ele foi gerado aqui, e não dá p ser fechado no finally lá em baixo;
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!"); // Lançando exceção p caso n tenha inserido nd;
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
					
			st.setString(1, obj.getName());  
			st.setInt(2, obj.getId());
			
			st.executeUpdate(); 
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?"); // Apagar o Id informado; Caso não tenha, ele executa msm assim, e não dá erro, mas não apaga nenhum;
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch 
			(SQLException e) {
				throw new DbException(e.getMessage());
			}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department WHERE Id = ? ");
			st.setInt(1, id); // id que recebeu como parâmetro no método;
			rs = st.executeQuery(); // Comando SQL vai ser executado, e o resultado vai cair dentro do resultSet;

			if (rs.next()) { // Testar se veio algum resultado, pois o rs começa no 0;
				Department dep = instantiateDepartment(rs); // Chamando a função de instanciação de departamentos feito abaixo;
				return dep;
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
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException { // Função que colocamos a parte para reaproveitá-la, p fzer o reuso da instanciação do departamento, e não ficar um código verboso toda vez que for instanciá-lo; E iremos propagar a exceção e não fazer outra, pois já fizemos lá em cima;
		Department dep = new Department(); // Criando uma variável temporária local e instanciando o departamento c os dados abaixo;
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department ORDER BY Id");
			rs = st.executeQuery(); 
			
			List<Department> list = new ArrayList<>();
			
			while (rs.next()) { // Toda vez que passar pela linha do resultSet, vai testar, se o departamento já existir, vai reaproveitar ele;
				
				Department obj = instantiateDepartment(rs);
				list.add(obj);
				}
			return list;
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
}
package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn; // Objeto conn criado p ter ele a disposi��o em qqer lugar da classe SellerDaoJDBC, p n precisar ficar instanciando p tds os m�todos abaixo;
	
	public SellerDaoJDBC(Connection conn) { // Construtor manual, de conex�o c BD, injetando depend�ncia do conn p classe SellerDaoJDBC;
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) { // Fun��o para inserir um novo vendedor;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName()); // Setando os valores, primeiro nome; 1 de primeira interroga��o, e obj.getName() desse objeto (obj) q veio como par�metro de entrada; 
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); // Navegar no objeto, a partir do departamento, achar o ID dele;
			
			int rowsAffected = st.executeUpdate(); // Vari�vel p guardar o tanto de linhas afetadas;
			
			if (rowsAffected > 0) { // Testando se as linhas alteradas foram maior que 0;
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {// if pois estamos inserindo apenas um dado;
					int id = rs.getInt(1); // Se existir, pegar o id gerado;
					obj.setId(id); // E atribuir dentro do objeto obj
					DB.closeResultSet(rs); // Fechando dentro do escopo do if, pois ele foi gerado aqui, e n�o d� p ser fechado no finally l� em baixo;
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!"); // Lan�ando exce��o p caso n tenha inserido nd;
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
	public void update(Seller obj) {
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Seller findById(Integer id) { // M�todo para achar um vendedor por id;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " // Buscando tds os campos do vendedor + o nome do departamento (dando um apelido a ele como DepName)
					+ "FROM seller INNER JOIN department " // Dando um JOIN para buscar os dados das 2 tabelas;
					+ "ON seller.DepartmentId = department.Id " // Tanto de vendedor qnt departamento;
					+ "WHERE seller.Id = ? "); // Onde o id ser� o: (informado abaixo);
			st.setInt(1, id); // id que recebeu como par�metro no m�todo;
			rs = st.executeQuery(); // Comando SQL vai ser executado, e o resultado vai cair dentro do resultSet;
			// � preciso entender aqui, que ao buscar no BD uma tabela, ele n puxa as infos em forma de tabela, em linhas e colunas, 
			// e sim OBJETOS, pois estamos programando em orienta��o a objetos; Portanto, precisamos associar o Department com Seller;
			// Associa��o dos objetos;
			if (rs.next()) { // Testar se veio algum resultado, pois o rs come�a no 0;
				Department dep = instantiateDepartment(rs); // Chamando a fun��o de instancia��o de departamentos feito abaixo;
				Seller obj = instantiateSeller(rs, dep); // Agora, chamando a instancia��o de um vendedor, feito abaixo tb;
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
			// N�o fecha a conex�o, pois pode ter mais opera��es dentro desta classe, a conex�o se fecha apenas no programa;
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException { // Fun��o que colocamos a parte para reaproveit�-la, p fazer o reuso da instancia��o do vendedor, e n�o ficar um c�digo verboso toda vez que for instanci�-lo; E iremos propagar a exce��o e n�o fazer outra, pois j� fizemos l� em cima;
		Seller obj = new Seller(); // Criando uma vari�vel tempor�ria local e instanciando o vendedor c os dados abaixo;
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); // � necess�rio aqui instanciar o departamento todo, logo, o objeto criado abaixo;
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException { // Fun��o que colocamos a parte para reaproveit�-la, p fzer o reuso da instancia��o do departamento, e n�o ficar um c�digo verboso toda vez que for instanci�-lo; E iremos propagar a exce��o e n�o fazer outra, pois j� fizemos l� em cima;
		Department dep = new Department(); // Criando uma vari�vel tempor�ria local e instanciando o departamento c os dados abaixo;
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " // Buscando tds os campos do vendedor + o nome do departamento (dando um apelido a ele como DepName)
					+ "FROM seller INNER JOIN department " // Dando um JOIN para buscar os dados das 2 tabelas;
					+ "ON seller.DepartmentId = department.Id " // Tanto de vendedor qnt departamento;
					+ "ORDER BY Name"); // Ordenado por nome, ou seja, ordem alfab�tica;
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // Integer = id do departamento; Department o valor de cada departamento; Estrutura de dados map criado vazio, p guardar qqer departamento que for instanciado;
			
			while (rs.next()) { // Toda vez que passar pela linha do resultSet, vai testar, se o departamento j� existir, vai reaproveitar ele;
				
				Department dep = map.get(rs.getInt("DepartmentId")); // Map criado para buscar dentro do map se j� existe o dep; Pois n�o pode repetir o departamento, tem que ser o mesmo pros vendedores que achar, n�o o repetindo; N�o dando nulo, � pq o dep j� existe, e vai puxar esse j� existente p utilizar e n�o criar outro;
				
				if (dep == null) { // Se for nulo;
					dep = instantiateDepartment(rs); // Ai sim vai instanciar o departamento; 
					map.put(rs.getInt("DepartmentId"), dep); // Aqui vai guardar o departamento e identificar pela key dele (getInt department) e o departamento � o que tiver na vari�vel dep;
				}
				
				Seller obj = instantiateSeller(rs, dep); // Instanciando todos os vendedores, sem repeti��o de departamento;
				list.add(obj); // Adicionar esse vendedor na lista;
			}
			return list;
		}
		catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally { // finally para fechar todos os recursos;
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// N�o fecha a conex�o, pois pode ter mais opera��es dentro desta classe, a conex�o se fecha apenas no programa;
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " // Buscando tds os campos do vendedor + o nome do departamento (dando um apelido a ele como DepName)
					+ "FROM seller INNER JOIN department " // Dando um JOIN para buscar os dados das 2 tabelas;
					+ "ON seller.DepartmentId = department.Id " // Tanto de vendedor qnt departamento;
					+ "WHERE DepartmentId = ? " // Onde o id ser� o: (informado abaixo);
					+ "ORDER BY Name"); // Ordenado por nome, ou seja, ordem alfab�tica;
			st.setInt(1, department.getId());
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // Integer = id do departamento; Department o valor de cada departamento; Estrutura de dados map criado vazio, p guardar qqer departamento que for instanciado;
			
			while (rs.next()) { // Neste caso � while, pois vai percorrer ENQNT tiver um pr�ximo, pois pode n�o ter somente 1;
				
				Department dep = map.get(rs.getInt("DepartmentId")); // Map criado para buscar dentro do map se j� existe o dep; Pois n�o pode repetir o departamento, tem que ser o mesmo pros vendedores que achar, n�o o repetindo; N�o dando nulo, � pq o dep j� existe, e vai puxar esse j� existente p utilizar e n�o criar outro;
				
				if (dep == null) { // Se for nulo;
					dep = instantiateDepartment(rs); // Ai sim vai instanciar o departamento; 
					map.put(rs.getInt("DepartmentId"), dep); // Aqui vai guardar o departamento e identificar pela key dele (getInt department) e o departamento � o que tiver na vari�vel dep;
				}
				
				Seller obj = instantiateSeller(rs, dep); 
				list.add(obj); // Adicionar esse vendedor na lista;
			}
			return list;
		}
		catch (SQLException e){
			throw new DbException(e.getMessage());
		}
		finally { // finally para fechar todos os recursos;
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// N�o fecha a conex�o, pois pode ter mais opera��es dentro desta classe, a conex�o se fecha apenas no programa;
		}
	}
}
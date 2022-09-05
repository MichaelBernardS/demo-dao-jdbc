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
	
	private Connection conn; // Objeto conn criado p ter ele a disposição em qqer lugar da classe SellerDaoJDBC, p n precisar ficar instanciando p tds os métodos abaixo;
	
	public SellerDaoJDBC(Connection conn) { // Construtor manual, de conexão c BD, injetando dependência do conn p classe SellerDaoJDBC;
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) { // Função para inserir um novo vendedor;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName()); // Setando os valores, primeiro nome; 1 de primeira interrogação, e obj.getName() desse objeto (obj) q veio como parâmetro de entrada; 
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId()); // Navegar no objeto, a partir do departamento, achar o ID dele;
			
			int rowsAffected = st.executeUpdate(); // Variável p guardar o tanto de linhas afetadas;
			
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
					+ "WHERE seller.Id = ? "); // Onde o id será o: (informado abaixo);
			st.setInt(1, id); // id que recebeu como parâmetro no método;
			rs = st.executeQuery(); // Comando SQL vai ser executado, e o resultado vai cair dentro do resultSet;
			// É preciso entender aqui, que ao buscar no BD uma tabela, ele n puxa as infos em forma de tabela, em linhas e colunas, 
			// e sim OBJETOS, pois estamos programando em orientação a objetos; Portanto, precisamos associar o Department com Seller;
			// Associação dos objetos;
			if (rs.next()) { // Testar se veio algum resultado, pois o rs começa no 0;
				Department dep = instantiateDepartment(rs); // Chamando a função de instanciação de departamentos feito abaixo;
				Seller obj = instantiateSeller(rs, dep); // Agora, chamando a instanciação de um vendedor, feito abaixo tb;
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

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException { // Função que colocamos a parte para reaproveitá-la, p fazer o reuso da instanciação do vendedor, e não ficar um código verboso toda vez que for instanciá-lo; E iremos propagar a exceção e não fazer outra, pois já fizemos lá em cima;
		Seller obj = new Seller(); // Criando uma variável temporária local e instanciando o vendedor c os dados abaixo;
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep); // É necessário aqui instanciar o departamento todo, logo, o objeto criado abaixo;
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException { // Função que colocamos a parte para reaproveitá-la, p fzer o reuso da instanciação do departamento, e não ficar um código verboso toda vez que for instanciá-lo; E iremos propagar a exceção e não fazer outra, pois já fizemos lá em cima;
		Department dep = new Department(); // Criando uma variável temporária local e instanciando o departamento c os dados abaixo;
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
					+ "ORDER BY Name"); // Ordenado por nome, ou seja, ordem alfabética;
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // Integer = id do departamento; Department o valor de cada departamento; Estrutura de dados map criado vazio, p guardar qqer departamento que for instanciado;
			
			while (rs.next()) { // Toda vez que passar pela linha do resultSet, vai testar, se o departamento já existir, vai reaproveitar ele;
				
				Department dep = map.get(rs.getInt("DepartmentId")); // Map criado para buscar dentro do map se já existe o dep; Pois não pode repetir o departamento, tem que ser o mesmo pros vendedores que achar, não o repetindo; Não dando nulo, é pq o dep já existe, e vai puxar esse já existente p utilizar e não criar outro;
				
				if (dep == null) { // Se for nulo;
					dep = instantiateDepartment(rs); // Ai sim vai instanciar o departamento; 
					map.put(rs.getInt("DepartmentId"), dep); // Aqui vai guardar o departamento e identificar pela key dele (getInt department) e o departamento é o que tiver na variável dep;
				}
				
				Seller obj = instantiateSeller(rs, dep); // Instanciando todos os vendedores, sem repetição de departamento;
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
			// Não fecha a conexão, pois pode ter mais operações dentro desta classe, a conexão se fecha apenas no programa;
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
					+ "WHERE DepartmentId = ? " // Onde o id será o: (informado abaixo);
					+ "ORDER BY Name"); // Ordenado por nome, ou seja, ordem alfabética;
			st.setInt(1, department.getId());
			rs = st.executeQuery(); 
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>(); // Integer = id do departamento; Department o valor de cada departamento; Estrutura de dados map criado vazio, p guardar qqer departamento que for instanciado;
			
			while (rs.next()) { // Neste caso é while, pois vai percorrer ENQNT tiver um próximo, pois pode não ter somente 1;
				
				Department dep = map.get(rs.getInt("DepartmentId")); // Map criado para buscar dentro do map se já existe o dep; Pois não pode repetir o departamento, tem que ser o mesmo pros vendedores que achar, não o repetindo; Não dando nulo, é pq o dep já existe, e vai puxar esse já existente p utilizar e não criar outro;
				
				if (dep == null) { // Se for nulo;
					dep = instantiateDepartment(rs); // Ai sim vai instanciar o departamento; 
					map.put(rs.getInt("DepartmentId"), dep); // Aqui vai guardar o departamento e identificar pela key dele (getInt department) e o departamento é o que tiver na variável dep;
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
			// Não fecha a conexão, pois pode ter mais operações dentro desta classe, a conexão se fecha apenas no programa;
		}
	}
}
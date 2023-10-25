package DAO;

import model.Jogo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class JogoDAO extends Dao {	
	public JogoDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Jogo jogo) {
		boolean status = false;
		try {
			
			Integer[] categ = Arrays.stream( jogo.getCategorias() ).boxed().toArray( Integer[]::new );
			Array array = conexao.createArrayOf("INTEGER", categ);
			
			String sql = "INSERT INTO jogo (nome, categorias, empresa, descricao, avaliacao, links, especificacoes) "
		            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement st = conexao.prepareStatement(sql);
		st.setString(1, jogo.getNome());
		st.setArray(2, array);
		st.setString(3, jogo.getEmpresa());
		st.setString(4, jogo.getDescricao());
		st.setDouble(5, jogo.getAvaliacao());
		st.setArray(6, conexao.createArrayOf("varchar", jogo.getLinks()));
		st.setString(7, jogo.getEspecificacoes());

		st.executeUpdate();
		st.close();
		status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Jogo get(int id) {
		Jogo jogo = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogo WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){
	        	Array a = rs.getArray("categorias");
	        	Array b = rs.getArray("links");
	        	jogo = new Jogo(rs.getInt("id"), rs.getString("nome"), (int[])a.getArray(), 
     				   rs.getString("empresa"), 
     				   rs.getString("descricao"),
			               rs.getDouble("avaliacao"),
			               (String[])b.getArray(),
			               rs.getString("especificacoes"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogo;
	}
	
	
	public List<Jogo> get() {
		return get("id");
	}

	
	public List<Jogo> getOrderById() {
		return get("id");		
	}
	
	
	public List<Jogo> getOrderByNome() {
		return get("nome");		
	}
	
	private List<Jogo> get(String orderBy) {
		List<Jogo> jogos = new ArrayList<Jogo>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM jogo" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	
	        	Array b = rs.getArray("links");
	        	
	        	int[] categ;
	        	Array arrcateg = rs.getArray("categorias");
	            Short[] s = (Short[])arrcateg.getArray();
	            categ = new int[s.length];
	            for(int i=0; i<s.length; i++)
	                categ[i] = (int) s[i];
	        	
	        	Jogo p = new Jogo(rs.getInt("id"), rs.getString("nome"), categ, 
     				   rs.getString("empresa"), 
     				   rs.getString("descricao"),
			               rs.getDouble("avaliacao"),
			               (String[])b.getArray(),
			               rs.getString("especificacoes"));
	            jogos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogos;
	}
	
	
	public boolean update(Jogo jogo) {
		boolean status = false;
		try {
			
			Integer[] categ = Arrays.stream( jogo.getCategorias() ).boxed().toArray( Integer[]::new );
			Array array = conexao.createArrayOf("INTEGER", categ);
			
			
			String sql = "UPDATE jogo (nome, categorias, empresa, descricao, avaliacao, links, especificacoes) "
		            + "VALUES (?, ?, ?, ?, ?, ?, ?) "
					   + "WHERE id = " + jogo.getId();
			PreparedStatement st = conexao.prepareStatement(sql);
			
			st.setString(1, jogo.getNome());
			st.setArray(2, array);
			st.setString(3, jogo.getEmpresa());
			st.setString(4, jogo.getDescricao());
			st.setDouble(5, jogo.getAvaliacao());
			st.setArray(6, conexao.createArrayOf("varchar", jogo.getLinks()));
			st.setString(7, jogo.getEspecificacoes());
			
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM jogo WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}
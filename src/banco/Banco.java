package banco;

import java.sql.*;
import java.util.ArrayList;

import dados.Pessoa;
import erro.PessoaException;

public class Banco {
	
	// Cria um objeto de conexão com banco de dados
	public static Connection objConexao;
	
	/**
	 * Abre conexão com o banco de dados.
	 * @throws SQLException
	 */
	public static void abrirConexaoBD() throws SQLException {
		
		// Registra um novo driver para conexão com banco de dados
		DriverManager.registerDriver(new org.firebirdsql.jdbc.FBDriver());
		
		String banco = "jdbc:firebirdsql:Notebook-PC/3050:C:/Users/Notebook/Desktop/Ltp4/Cadastro de Pessoas/BDPESSOAS/BDPESSOAS.GDB";
		String usuario = "SYSDBA";
		String senha = "masterkey";
		
		// Estabelece conexão com banco de dados a partir do objeto de conexão
		objConexao = DriverManager.getConnection(banco, usuario, senha);
		
	}
	
	/**
	 * Fecha conexão com banco de dados.
	 * @throws SQLException
	 */
	public static void fecharConexaoBD() throws SQLException {
		objConexao.close();
	}
	
	/**
	 * Inclui nova pessoa buscando se não já possui alguém com mesmo nome.
	 * e data de nascimento.
	 * @param objPessoa
	 * @throws SQLException
	 * @throws PessoaException
	 */
	public static void incluirPessoa(Pessoa objPessoa) throws SQLException, PessoaException {
		
		// Query que busca se existe pessoa com esse nome no banco de dados
		String selectQuerySQL = "SELECT * FROM AGENDA WHERE UPPER(NOME) = ? AND NASCIMENTO = ?";
		PreparedStatement objSQLSelect = objConexao.prepareStatement(selectQuerySQL);
		
		// Executa a query com os nomes parametrizados
		objSQLSelect.setString(1, objPessoa.getNome().toUpperCase());
		objSQLSelect.setDate(2, objPessoa.getNascimento());
		ResultSet resposta = objSQLSelect.executeQuery();
		
		// Verifica se existe pessoa cadastrada com o nome e nascimento informados
		if (resposta.next())
			throw new SQLException("Essa pessoa já foi cadastrada antes. Nome repetido e data de nascimento.");
		
		// Se não houver erro, executa-se o próximo passo
		String insertQuerySQL = "INSERT INTO AGENDA (NOME, TELEFONE, NASCIMENTO, EMAIL) VALUES (?,?,?,?)";
		PreparedStatement objSQLInsert = objConexao.prepareStatement(insertQuerySQL);
		
		// Inserindo o conteúdo nos campos
		objSQLInsert.setString(1, objPessoa.getNome());
		objSQLInsert.setString(2, objPessoa.getTelefone());
		objSQLInsert.setDate(3, objPessoa.getNascimento());
		objSQLInsert.setString(4, objPessoa.getEmail());
		objSQLInsert.executeUpdate();
		
	}
	
	/**
	 * Altera uma pessoa que já possui cadastro no banco de dados.
	 * @param objPessoa
	 * @throws SQLException
	 * @throws PessoaException
	 */
	public static void alterarPessoa(Pessoa objPessoa) throws SQLException {
		
		// Query de atualização
		String updateQuerySQL = "UPDATE AGENDA SET NOME = ?, TELEFONE = ?, NASCIMENTO = ?, EMAIL = ? WHERE CODIGO = ?";
		PreparedStatement objSQLUpdate = objConexao.prepareStatement(updateQuerySQL);
		
		// Executa a query
		objSQLUpdate.setString(1, objPessoa.getNome());
		objSQLUpdate.setString(2, objPessoa.getTelefone());
		objSQLUpdate.setDate(3, objPessoa.getNascimento());
		objSQLUpdate.setString(4, objPessoa.getEmail());
		objSQLUpdate.setInt(5, objPessoa.getCodigo());
		objSQLUpdate.executeUpdate();
		
	}
	
	/**
	 * Exclui uma pessoa registrada na tabela.
	 * @param codigo
	 * @throws SQLException
	 */
	public static void excluirPessoa(int codigo) throws SQLException {
		
		// Query de remoção
		String deleteQuerySQL = "DELETE FROM AGENDA WHERE CODIGO = ?";
		PreparedStatement objSQLDelete = objConexao.prepareStatement(deleteQuerySQL);
		
		// Executa a query
		objSQLDelete.setInt(1, codigo);
		objSQLDelete.executeUpdate();
		
	}
	
	/**
	 * Consulta no banco de dados se existe pessoa para o código parametrizado.
	 * @param codigo
	 * @return Pessoa
	 * @throws SQLException
	 */
	public static Pessoa consultarPessoa(int codigo) throws SQLException {
		
		// Query de busca
		String selectQuerySQL = "SELECT * FROM AGENDA WHERE CODIGO = ?";
		PreparedStatement objSQLSelect = objConexao.prepareStatement(selectQuerySQL);
		
		// Executa a query
		objSQLSelect.setInt(1, codigo);
		ResultSet resposta = objSQLSelect.executeQuery();
		
		if ( resposta.next() )
			return new Pessoa(
				resposta.getInt("CODIGO"),
				resposta.getString("NOME"),
				resposta.getString("TELEFONE"),
				resposta.getDate("NASCIMENTO"),
				resposta.getString("EMAIL")
			);
		
		else
			throw new SQLException("Não existe pessoa para o código informado.");
		
	}
	
	/**
	 * Consulta nome parametrizado
	 * @param nome
	 * @return Lista de pessoas ordenadas pelo nome
	 * @throws SQLException
	 */
	public static ArrayList<Pessoa> consultarPessoaNome(String nome) throws SQLException {
		
		// Query de busca
		String selectQuerySQL = "SELECT * FROM AGENDA WHERE NOME LIKE ? ORDER BY NOME";
		PreparedStatement objSQLSelect = objConexao.prepareStatement(selectQuerySQL);
		
		// Executa a query
		objSQLSelect.setString(1, "%" + nome.toUpperCase() + "%");
		ResultSet resposta = objSQLSelect.executeQuery();
		
		// Cria uma lista para armazenar os nomes encontrados
		ArrayList<Pessoa> listaPessoas = new ArrayList<Pessoa>(); 
		int totalPessoas = 0;
		
		while ( resposta.next() ) {
			listaPessoas.add(new Pessoa(
				resposta.getInt("CODIGO"),
				resposta.getString("NOME"),
				resposta.getString("TELEFONE"),
				resposta.getDate("NASCIMENTO"),
				resposta.getString("EMAIL")
			));
			totalPessoas++;
		}
		
		if ( totalPessoas > 0 )
			return listaPessoas;
		else
			throw new SQLException("Não existe nenhuma pessoa para o nome informado.");
		
	}

	/**
	 * Consulta pessoas pelo mês de aniversário
	 * @param mes
	 * @return Lista de pessoas ordenadas pelo nome
	 * @throws SQLException
	 */
	public static ArrayList<Pessoa> consultarPessoaData(int mes) throws SQLException {
		
		// Query de busca
		String selectQuerySQL = "SELECT * FROM AGENDA WHERE EXTRACT(MONTH FROM NASCIMENTO) = ? ORDER BY NOME";
		PreparedStatement objSQLSelect = objConexao.prepareStatement(selectQuerySQL);
		
		// Executa a query
		objSQLSelect.setInt(1, mes);
		ResultSet resposta = objSQLSelect.executeQuery();
		
		// Cria uma lista para armazenar os nomes encontrados
		ArrayList<Pessoa> listaPessoas = new ArrayList<Pessoa>(); 
		int totalPessoas = 0;
		
		while ( resposta.next() ) {
			listaPessoas.add(new Pessoa(
				resposta.getInt("CODIGO"),
				resposta.getString("NOME"),
				resposta.getString("TELEFONE"),
				resposta.getDate("NASCIMENTO"),
				resposta.getString("EMAIL")
			));
			totalPessoas++;
		}
		
		if ( totalPessoas > 0 )
			return listaPessoas;
		else
			throw new SQLException("Não existe nenhuma pessoa para o mês de nascimento informado.");
		
	}
	
}

package usuario;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import banco.Banco;
import dados.Pessoa;
import erro.PessoaException;
import utilitarios.Console;
import utilitarios.LtpUtil;

public class Usuario {
	
	public static void main(String[] args) throws PessoaException {
		
		/**
		 * Abre conex�o com banco de dados
		 * e se ocorrer um erro, cai na exce��o
		 */
		try {
			
			Banco.abrirConexaoBD();
			menu();
			Banco.fecharConexaoBD();
			System.out.println("Aplicativo finalizado");
			System.exit(0);
			
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			System.exit(0);
			
		}

	}
	
	/**
	 * Inclui pessoa e valida os campos informados pelo usu�rio
	 * @throws SQLException
	 * @throws PessoaException
	 */
	public static void incluirPessoa() throws PessoaException {
		System.out.println("==========\n1. Incluir pessoa");
		
		// Declara��o de vari�veis
		String nome = "";
		String telefone = "";
		Date nascimento = new Date(System.currentTimeMillis());
		String email = "";
		
		// Valida��o do nome
		while (true) {
			nome = Console.readLine("Nome: ").trim();
			if ( nome.isEmpty() ) System.out.println("Nome inv�lido.");
			else break;
		}
		
		// Atribui��o do telefone
		telefone = Console.readLine("Telefone: ").trim();
		
		// Valida��o da data de nascimento
		while (true) {
			String nascimentoString = Console.readLine("Data de nascimento (dd/mm/aaaa): ");
			if ( LtpUtil.validarData(nascimentoString, nascimento) && nascimento.before(new Date(System.currentTimeMillis())) )
				break;
			else
				System.out.println("Data de nascimento inv�lida.");
		}
		
		// Valida��o do e-mail
		while (true) {
			email = Console.readLine("Email: ");
			if ( LtpUtil.validarEmail(email) ) break;
			else System.out.println("Email inv�lido.");
		}
		
		// Cria um objeto Pessoa
		Pessoa objPessoa = new Pessoa(0, nome, telefone, nascimento, email);
		
		// Inserindo os dados no banco de dados
		try {
			
			Banco.incluirPessoa(objPessoa);
			System.out.println("Pessoa cadastrada.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Altera os dados de uma pessoa j� cadastrada no banco de dados
	 * @throws SQLException
	 * @throws PessoaException
	 */
	private static void alterarPessoa() {
		System.out.println("==========\n2. Alterar pessoa");
		
		int codigo = 0;
		String nome = "";
		String telefone = "";
		Date nascimento = new Date( System.currentTimeMillis() );
		String email = "";
		
		try {
			
			codigo = Console.readInt("C�digo: ");
			Pessoa objPessoa = Banco.consultarPessoa(codigo);
			System.out.println( objPessoa.toString() );
			
			// Altera��o do nome
			String opcaoNome = Console.readLine("Deseja alterar o nome? s/n");
			if ( opcaoNome.equalsIgnoreCase("s") ) {
				while(true) {
					nome = Console.readLine("Novo nome: ").trim();
					if ( nome.isEmpty() ) System.out.println("Nome inv�lido.");
					else break;
				}
				
				// Alterando direto no banco
				objPessoa.setNome(nome);
			}
			
			// Altera��o do telefone
			String opcaoTelefone = Console.readLine("Deseja alterar o telefone? s/n");
			if ( opcaoTelefone.equalsIgnoreCase("s") ) {
				telefone = Console.readLine("Novo telefone: ").trim();
				
				// Alterando direto no banco
				objPessoa.setTelefone(telefone);
			}
			
			// Altera��o do nascimento
			String opcaoNascimento = Console.readLine("Deseja alterar o nascimento? s/n");
			if ( opcaoNascimento.equalsIgnoreCase("s") ) {
				while (true) {
					String nascimentoString = Console.readLine("Nova data de nascimento (dd/mm/aaaa): ");
					if ( LtpUtil.validarData(nascimentoString, nascimento) && nascimento.before(new Date(System.currentTimeMillis())) )
						break;
					else
						System.out.println("Data de nascimento inv�lida.");
				}
				
				// Alterando direto no banco
				objPessoa.setNascimento(nascimento);
			}
			
			// Altera��o do e-mail
			String opcaoEmail = Console.readLine("Deseja alterar o e-mail? s/n");
			if ( opcaoEmail.equalsIgnoreCase("s") ) {
				while (true) {
					email = Console.readLine("Novo e-mail: ");
					if ( LtpUtil.validarEmail(email) ) break;
					else System.out.println("E-mail inv�lido.");
				}
				
				// Alterando direto no banco
				objPessoa.setEmail(email);
			}
			
			// Altera pessoa no banco de dados
			Banco.alterarPessoa(objPessoa);
			System.out.println("�Dados da pessoa foram alterados.");
				
			
		} catch (SQLException e) {
			System.out.println( e.getMessage() );
		}
		
	}
	
	/**
	 * Exclui uma pessoa no banco de dados pelo c�digo digitado
	 * @throws SQLException
	 * @throws PessoaException
	 */
	private static void excluirPessoa() throws SQLException, PessoaException {
		System.out.println("==========\n3. Excluir pessoa");
		
		try {
			
			int codigo = 0;
			
			// Valida��o do c�digo digitado
			while(true) {
				codigo = Console.readInt("Digite o c�digo: ");
				if ( codigo < 0 ) System.out.println("C�digo inv�lido.");
				else break;
			}
			
			// Busca no banco de dados
			Pessoa objPessoa = Banco.consultarPessoa(codigo);
			System.out.println( objPessoa.toString() );
			
			// Confirma��o de exclus�o
			String opcao = Console.readLine("Confirmar exclus�o? s/n ");
			if ( opcao.equalsIgnoreCase("s") )
				Banco.excluirPessoa(codigo);
			
		} catch ( SQLException e ) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Consulta uma pessoa no banco de dados pelo c�digo digitado
	 * @throws SQLException
	 */
	private static void consultarPessoaCodigo() throws SQLException {
		System.out.println("==========\n4. Consultar pessoa pelo C�digo");
		
		try {
			
			int codigo = 0;
			
			// Valida��o do c�digo digitado
			while(true) {
				codigo = Console.readInt("Digite o c�digo: ");
				if ( codigo < 0 ) System.out.println("C�digo inv�lido.");
				else break;
			}
			
			// Busca no banco de dados
			Pessoa objPessoa = Banco.consultarPessoa(codigo);
			System.out.println( objPessoa.toString() );
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Consulta o nome digitado no banco de dados e imprime os nomes encontrados
	 * @throws SQLException
	 */
	private static void consultarPessoaNome() throws SQLException {
		System.out.println("==========\n5. Consultar pessoa pelo Nome");
		
		try {
			
			String nome = "";
			
			// Valida��o do c�digo digitado
			while(true) {
				nome = Console.readLine("Digite o nome: ");
				if ( nome.isEmpty() ) System.out.println("Nome inv�lido.");
				else break;
			}
			
			// Busca no banco de dados por pessoas com trechos do nome
			ArrayList<Pessoa> objPessoas = Banco.consultarPessoaNome(nome);
			
			// Imprime na tela as pessoas encontradas
			if ( !objPessoas.isEmpty() )
				for ( Pessoa obj : objPessoas )
					System.out.println( obj.toString() );
			else
				System.out.println("Nenhuma pessoa encontrada para o nome digitado.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Consulta pessoas no banco de dados que fa�am anivers�rio no m�s digitado
	 * @throws SQLException
	 */
	private static void consultarPessoaMes() throws SQLException {
		System.out.println("==========\n6. Consultar pessoa pelo M�s de Nascimento");
		
		try {
			
			int mes = 0;
			
			// Valida��o do m�s digitado
			while(true) {
				mes = Console.readInt("Digite o m�s: ");
				if ( mes < 1 || mes  > 12 ) System.out.println("Digite um m�s v�lido.");
				else break;
			}
			
			// Busca no banco de dados
			ArrayList<Pessoa> objPessoas = Banco.consultarPessoaData(mes);
			
			// Imprime na tela as pessoas encontradas
			if ( !objPessoas.isEmpty() )
				for ( Pessoa obj : objPessoas )
					System.out.println( obj.toString() );
			else
				System.out.println("Nenhuma pessoa encontrada para o m�s digitado.");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Menu
	 * @throws PessoaException 
	 * @throws SQLException 
	 */
	private static void menu() throws PessoaException, SQLException {
		
		int opcao = 0;
		do {
			System.out.println("=== MENU ===");
			System.out.println("1. Incluir pessoa");
			System.out.println("2. Alterar pessoa");
			System.out.println("3. Excluir pessoa");
			System.out.println("4. Consultar pessoa pelo C�digo");
			System.out.println("5. Consultar pessoa pelo Nome");
			System.out.println("6. Consultar pessoa pelo M�s de Nascimento");
			System.out.println("0. Sair");
			
			opcao = Console.readInt("Escolha uma op��o: ");
			
			switch ( opcao ) {
				case 1:
					incluirPessoa();
					break;
					
				case 2:
					alterarPessoa();
					break;
					
				case 3:
					excluirPessoa();
					break;
					
				case 4:
					consultarPessoaCodigo();
					break;
					
				case 5:
					consultarPessoaNome();
					break;
					
				case 6:
					consultarPessoaMes();
					break;
					
				case 0:
					break;
					
				default:
					System.out.println("Op��o inv�lida");
					break;
			}
		} while ( opcao != 0 );
		
	}
}

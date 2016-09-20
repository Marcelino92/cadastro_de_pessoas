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
		 * Abre conexão com banco de dados
		 * e se ocorrer um erro, cai na exceção
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
	 * Inclui pessoa e valida os campos informados pelo usuário
	 * @throws SQLException
	 * @throws PessoaException
	 */
	public static void incluirPessoa() throws PessoaException {
		System.out.println("==========\n1. Incluir pessoa");
		
		// Declaração de variáveis
		String nome = "";
		String telefone = "";
		Date nascimento = new Date(System.currentTimeMillis());
		String email = "";
		
		// Validação do nome
		while (true) {
			nome = Console.readLine("Nome: ").trim();
			if ( nome.isEmpty() ) System.out.println("Nome inválido.");
			else break;
		}
		
		// Atribuição do telefone
		telefone = Console.readLine("Telefone: ").trim();
		
		// Validação da data de nascimento
		while (true) {
			String nascimentoString = Console.readLine("Data de nascimento (dd/mm/aaaa): ");
			if ( LtpUtil.validarData(nascimentoString, nascimento) && nascimento.before(new Date(System.currentTimeMillis())) )
				break;
			else
				System.out.println("Data de nascimento inválida.");
		}
		
		// Validação do e-mail
		while (true) {
			email = Console.readLine("Email: ");
			if ( LtpUtil.validarEmail(email) ) break;
			else System.out.println("Email inválido.");
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
	 * Altera os dados de uma pessoa já cadastrada no banco de dados
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
			
			codigo = Console.readInt("Código: ");
			Pessoa objPessoa = Banco.consultarPessoa(codigo);
			System.out.println( objPessoa.toString() );
			
			// Alteração do nome
			String opcaoNome = Console.readLine("Deseja alterar o nome? s/n");
			if ( opcaoNome.equalsIgnoreCase("s") ) {
				while(true) {
					nome = Console.readLine("Novo nome: ").trim();
					if ( nome.isEmpty() ) System.out.println("Nome inválido.");
					else break;
				}
				
				// Alterando direto no banco
				objPessoa.setNome(nome);
			}
			
			// Alteração do telefone
			String opcaoTelefone = Console.readLine("Deseja alterar o telefone? s/n");
			if ( opcaoTelefone.equalsIgnoreCase("s") ) {
				telefone = Console.readLine("Novo telefone: ").trim();
				
				// Alterando direto no banco
				objPessoa.setTelefone(telefone);
			}
			
			// Alteração do nascimento
			String opcaoNascimento = Console.readLine("Deseja alterar o nascimento? s/n");
			if ( opcaoNascimento.equalsIgnoreCase("s") ) {
				while (true) {
					String nascimentoString = Console.readLine("Nova data de nascimento (dd/mm/aaaa): ");
					if ( LtpUtil.validarData(nascimentoString, nascimento) && nascimento.before(new Date(System.currentTimeMillis())) )
						break;
					else
						System.out.println("Data de nascimento inválida.");
				}
				
				// Alterando direto no banco
				objPessoa.setNascimento(nascimento);
			}
			
			// Alteração do e-mail
			String opcaoEmail = Console.readLine("Deseja alterar o e-mail? s/n");
			if ( opcaoEmail.equalsIgnoreCase("s") ) {
				while (true) {
					email = Console.readLine("Novo e-mail: ");
					if ( LtpUtil.validarEmail(email) ) break;
					else System.out.println("E-mail inválido.");
				}
				
				// Alterando direto no banco
				objPessoa.setEmail(email);
			}
			
			// Altera pessoa no banco de dados
			Banco.alterarPessoa(objPessoa);
			System.out.println("“Dados da pessoa foram alterados.");
				
			
		} catch (SQLException e) {
			System.out.println( e.getMessage() );
		}
		
	}
	
	/**
	 * Exclui uma pessoa no banco de dados pelo código digitado
	 * @throws SQLException
	 * @throws PessoaException
	 */
	private static void excluirPessoa() throws SQLException, PessoaException {
		System.out.println("==========\n3. Excluir pessoa");
		
		try {
			
			int codigo = 0;
			
			// Validação do código digitado
			while(true) {
				codigo = Console.readInt("Digite o código: ");
				if ( codigo < 0 ) System.out.println("Código inválido.");
				else break;
			}
			
			// Busca no banco de dados
			Pessoa objPessoa = Banco.consultarPessoa(codigo);
			System.out.println( objPessoa.toString() );
			
			// Confirmação de exclusão
			String opcao = Console.readLine("Confirmar exclusão? s/n ");
			if ( opcao.equalsIgnoreCase("s") )
				Banco.excluirPessoa(codigo);
			
		} catch ( SQLException e ) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Consulta uma pessoa no banco de dados pelo código digitado
	 * @throws SQLException
	 */
	private static void consultarPessoaCodigo() throws SQLException {
		System.out.println("==========\n4. Consultar pessoa pelo Código");
		
		try {
			
			int codigo = 0;
			
			// Validação do código digitado
			while(true) {
				codigo = Console.readInt("Digite o código: ");
				if ( codigo < 0 ) System.out.println("Código inválido.");
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
			
			// Validação do código digitado
			while(true) {
				nome = Console.readLine("Digite o nome: ");
				if ( nome.isEmpty() ) System.out.println("Nome inválido.");
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
	 * Consulta pessoas no banco de dados que façam aniversário no mês digitado
	 * @throws SQLException
	 */
	private static void consultarPessoaMes() throws SQLException {
		System.out.println("==========\n6. Consultar pessoa pelo Mês de Nascimento");
		
		try {
			
			int mes = 0;
			
			// Validação do mês digitado
			while(true) {
				mes = Console.readInt("Digite o mês: ");
				if ( mes < 1 || mes  > 12 ) System.out.println("Digite um mês válido.");
				else break;
			}
			
			// Busca no banco de dados
			ArrayList<Pessoa> objPessoas = Banco.consultarPessoaData(mes);
			
			// Imprime na tela as pessoas encontradas
			if ( !objPessoas.isEmpty() )
				for ( Pessoa obj : objPessoas )
					System.out.println( obj.toString() );
			else
				System.out.println("Nenhuma pessoa encontrada para o mês digitado.");
			
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
			System.out.println("4. Consultar pessoa pelo Código");
			System.out.println("5. Consultar pessoa pelo Nome");
			System.out.println("6. Consultar pessoa pelo Mês de Nascimento");
			System.out.println("0. Sair");
			
			opcao = Console.readInt("Escolha uma opção: ");
			
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
					System.out.println("Opção inválida");
					break;
			}
		} while ( opcao != 0 );
		
	}
}

package dados;

import java.sql.Date;

import utilitarios.LtpUtil;

public class Pessoa {
	
	// Atributos privados
	private int codigo;
	private String nome;
	private String telefone;
	private Date nascimento;
	private String email;
	
	// Método construtor padrão
	public Pessoa(int codigo, String nome, String telefone, Date nascimento, String email) {
		this.codigo = codigo;
		this.nome = nome;
		this.telefone = telefone;
		this.nascimento = nascimento;
		this.email = email;
	}

	// Métodos Get e Set
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	// Método ToString
	@Override
	public String toString() {
		return "Nome: " + nome + "\n"
				+ "Telefone: " + telefone + "\n"
				+ "Nascimento: " + LtpUtil.formatarData(nascimento, "dd/MM/yyyy") + "\n"
				+ "E-mail: " + email;
	}
	
}
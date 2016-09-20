package erro;

public class PessoaException extends Exception {
	
	/**
	 * Informa o erro de exceção
	 * @param motivo
	 */
	public PessoaException(String motivo) {
		super(motivo);
	}

}

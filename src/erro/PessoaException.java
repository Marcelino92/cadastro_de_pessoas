package erro;

public class PessoaException extends Exception {
	
	/**
	 * Informa o erro de exce��o
	 * @param motivo
	 */
	public PessoaException(String motivo) {
		super(motivo);
	}

}

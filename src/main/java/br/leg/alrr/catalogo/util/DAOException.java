package br.leg.alrr.catalogo.util;

/**
 * Classe que representa uma exceção específica. Usada em métodos que fazem comunicação
 * com o banco de dados.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 */
public class DAOException extends Exception {

    public DAOException() {
    }

    /**
     * Construtor
     *
     * @param message representa a mensagem que deve ser exibida quando a execeção for lançada.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Construtor
     *
     * @param cause
     */
    public DAOException(Throwable cause) {
        super(cause);
    }

    /**
     * Construtor
     *
     * @param message
     * @param cause
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}

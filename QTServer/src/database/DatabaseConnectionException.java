package database;

/**
 * Eccezione controllata (checked exception) utilizzata per segnalare errori specifici relativi alla connessione al database.
 * <p>
 * Questa è una {@link Exception} controllata (checked exception), il che significa che i metodi
 * che potrebbero lanciarla devono dichiararla nella loro clausola {@code throws} o gestirla
 * con un blocco {@code try-catch}. Viene utilizzata per incapsulare vari problemi
 * che possono verificarsi durante il tentativo di stabilire o utilizzare una connessione
 * al database, come problemi con il caricamento del driver JDBC, errori di autenticazione,
 * o problemi di rete che impediscono la comunicazione con il server del database.
 * </p>
 */
public class DatabaseConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Costruisce una nuova {@code DatabaseConnectionException} con un messaggio di default.
     * Il messaggio di default è "Errore di connessione al database".
     */
    public DatabaseConnectionException() {
        super("Errore di connessione al database");
    }

    /**
     * Costruisce una nuova {@code DatabaseConnectionException} con il messaggio di dettaglio specificato.
     *
     * @param message Il messaggio di dettaglio. Il messaggio di dettaglio viene salvato per
     * un successivo recupero tramite il metodo {@link Throwable#getMessage()}.
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova {@code DatabaseConnectionException} con il messaggio di dettaglio
     * e la causa specificati.
     * <p>
     * Nota che il messaggio di dettaglio associato a {@code cause} non viene
     * automaticamente incorporato nel messaggio di dettaglio di questa eccezione.
     * </p>
     *
     * @param message Il messaggio di dettaglio (salvato per recupero successivo tramite {@link Throwable#getMessage()}).
     * @param cause   La causa (salvata per recupero successivo tramite {@link Throwable#getCause()}).
     * Un valore {@code null} è permesso, e indica che la causa è inesistente o sconosciuta.
     */
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruisce una nuova {@code DatabaseConnectionException} con la causa specificata e un
     * messaggio di dettaglio di {@code (cause==null ? null : cause.toString())}
     * (che tipicamente contiene la classe e il messaggio di dettaglio di {@code cause}).
     * Questo costruttore è utile per le eccezioni che sono essenzialmente wrapper
     * per altre {@code Throwable} (ad esempio, {@link java.sql.SQLException}).
     *
     * @param cause La causa (salvata per recupero successivo tramite {@link Throwable#getCause()}).
     * Un valore {@code null} è permesso, e indica che la causa è inesistente o sconosciuta.
     */
    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }
}

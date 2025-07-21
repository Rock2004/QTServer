package database;

/**
 * Eccezione controllata (checked exception) utilizzata per segnalare
 * che un'operazione di database ha restituito un {@link java.sql.ResultSet} vuoto
 * in un contesto in cui ci si aspettava che contenesse almeno una riga di dati.
 */
public class EmptySetException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore predefinito.
     * Inizializza l'eccezione con un messaggio di default.
     */
    public EmptySetException() {
        super("Il ResultSet restituito è vuoto, ma erano attesi dei dati.");
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico.
     *
     * @param message Il messaggio di dettaglio che descrive la causa dell'eccezione.
     */
    public EmptySetException(String message) {
        super(message);
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico e una causa scatenante.
     *
     * @param message Il messaggio di dettaglio che descrive la causa dell'eccezione.
     * @param cause   L'eccezione originale (causa) che ha portato a questa EmptySetException.
     * Utile per il concatenamento delle eccezioni.
     */
    public EmptySetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore che accetta una causa scatenante.
     * Il messaggio di dettaglio sarà quello della causa (se presente) o null.
     *
     * @param cause L'eccezione originale (causa) che ha portato a questa EmptySetException.
     */
    public EmptySetException(Throwable cause) {
        super(cause);
    }
}

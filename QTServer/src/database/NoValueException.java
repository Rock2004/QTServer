package database; // O nel tuo package dedicato alle eccezioni, se preferisci

/**
 * Eccezione controllata (checked exception) utilizzata per segnalare
 * l'assenza di un valore atteso all'interno di un {@link java.sql.ResultSet}.
 * <p>
 * Ad esempio, può essere lanciata se si tenta di recuperare un valore da un
 * ResultSet che è vuoto, o da una colonna specifica che non contiene dati
 * quando ci si aspetta che ne contenga.
 * </p>
 */
public class NoValueException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore predefinito.
     * Inizializza l'eccezione con un messaggio di default.
     */
    public NoValueException() {
        super("Nessun valore trovato nel ResultSet dove era atteso.");
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico.
     *
     * @param message Il messaggio di dettaglio che descrive la causa dell'eccezione.
     */
    public NoValueException(String message) {
        super(message);
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico e una causa scatenante.
     *
     * @param message Il messaggio di dettaglio che descrive la causa dell'eccezione.
     * @param cause   L'eccezione originale (causa) che ha portato a questa NoValueException.
     * Utile per il concatenamento delle eccezioni (exception chaining).
     */
    public NoValueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore che accetta una causa scatenante.
     * Il messaggio di dettaglio sarà quello della causa (se presente) o null.
     *
     * @param cause L'eccezione originale (causa) che ha portato a questa NoValueException.
     */
    public NoValueException(Throwable cause) {
        super(cause);
    }
}

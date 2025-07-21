package data;

/**
 * Eccezione controllata (checked exception) utilizzata per segnalare che un'operazione
 * è stata tentata su un dataset che risulta essere vuoto, in un contesto in cui
 * tale operazione non è permessa o non avrebbe senso.
 * <p>
 * Ad esempio, può essere lanciata prima di eseguire un algoritmo di clustering
 * se il dataset di input non contiene alcun esempio.
 * </p>
 */
public class EmptyDatasetException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore predefinito.
     * Inizializza l'eccezione con un messaggio standard che descrive la condizione.
     */
    public EmptyDatasetException() {
        super("Il dataset fornito è vuoto. Impossibile procedere con l'operazione.");
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico.
     *
     * @param message Il messaggio di dettaglio che descrive la causa specifica dell'eccezione.
     */
    public EmptyDatasetException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova {@code EmptyDatasetException} con il messaggio di dettaglio
     * e la causa specificati.
     * <p>
     * Utile per wrappare un'eccezione di livello inferiore mantenendo il contesto originale
     * dell'errore e aggiungendo un messaggio specifico per questa condizione.
     * </p>
     *
     * @param message Il messaggio di dettaglio (salvato per recupero successivo tramite {@link Throwable#getMessage()}).
     * @param cause   La causa (salvata per recupero successivo tramite {@link Throwable#getCause()}).
     * Un valore {@code null} è permesso, e indica che la causa è inesistente o sconosciuta.
     */
    public EmptyDatasetException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruisce una nuova {@code EmptyDatasetException} con la causa specificata e un
     * messaggio di dettaglio derivato dalla causa (se presente).
     * <p>
     * Questo costruttore è utile per le eccezioni che sono essenzialmente wrapper
     * per altre {@code Throwable} quando non è necessario un messaggio di dettaglio aggiuntivo.
     * </p>
     *
     * @param cause La causa (salvata per recupero successivo tramite {@link Throwable#getCause()}).
     * Un valore {@code null} è permesso, e indica che la causa è inesistente o sconosciuta.
     */
    public EmptyDatasetException(Throwable cause) {
        super(cause);
    }
}

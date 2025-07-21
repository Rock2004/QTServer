package mining;

/**
 * Eccezione controllata (checked exception) per segnalare che l'algoritmo di clustering
 * ha generato un unico cluster. Questo può indicare che il raggio utilizzato
 * è troppo grande per il dataset fornito o che i dati presentano una particolare configurazione
 * che porta a un singolo raggruppamento.
 */
public class ClusteringRadiusException extends Exception {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore predefinito.
     * Inizializza l'eccezione con un messaggio standard che descrive la condizione.
     */
    public ClusteringRadiusException() {
        super("L'algoritmo di clustering ha prodotto un solo cluster. " +
              "Questo potrebbe indicare che il raggio e' troppo ampio per il dataset fornito.");
    }

    /**
     * Costruttore che accetta un messaggio di dettaglio specifico.
     *
     * @param message Il messaggio di dettaglio che descrive la causa specifica dell'eccezione.
     */
    public ClusteringRadiusException(String message) {
        super(message);
    }

    /**
     * Costruisce una nuova {@code ClusteringRadiusException} con il messaggio di dettaglio
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
    public ClusteringRadiusException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruisce una nuova {@code ClusteringRadiusException} con la causa specificata e un
     * messaggio di dettaglio derivato dalla causa (se presente).
     * <p>
     * Questo costruttore è utile per le eccezioni che sono essenzialmente wrapper
     * per altre {@code Throwable} quando non è necessario un messaggio di dettaglio aggiuntivo.
     * </p>
     *
     * @param cause La causa (salvata per recupero successivo tramite {@link Throwable#getCause()}).
     * Un valore {@code null} è permesso, e indica che la causa è inesistente o sconosciuta.
     */
    public ClusteringRadiusException(Throwable cause) {
        super(cause);
    }
}

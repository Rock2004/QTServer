package mining;

/**
 * Segnala che l'algoritmo di clustering ha prodotto un unico grande cluster.
 * <p>
 * Questa è un'eccezione controllata (checked exception) che indica una condizione
 * anomala ma prevedibile: il raggio fornito per il clustering potrebbe essere
 * troppo ampio per il dataset, causando l'inclusione di tutti i punti in un
 * solo gruppo.
 * </p>
 * <p>
 * Un gestore di questa eccezione potrebbe tentare di rieseguire l'algoritmo
 * con un raggio più piccolo.
 * </p>
 *
 * @see QTMiner
 */
public class ClusteringRadiusException extends Exception {

    /**
     * Identificativo univoco per la versione della classe, usato durante la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce una {@code ClusteringRadiusException} con un messaggio di default.
     */
    public ClusteringRadiusException() {
        super("L'algoritmo di clustering ha prodotto un solo cluster. " +
              "Questo potrebbe indicare che il raggio e' troppo ampio per il dataset fornito.");
    }

    /**
     * Costruisce una {@code ClusteringRadiusException} con un messaggio di dettaglio specifico.
     *
     * @param message Il messaggio di dettaglio che descrive la causa dell'eccezione.
     */
    public ClusteringRadiusException(String message) {
        super(message);
    }

    /**
     * Costruisce una {@code ClusteringRadiusException} con un messaggio di dettaglio
     * e una causa specificati.
     * <p>
     * Questo costruttore è utile per incapsulare (wrappare) un'eccezione di livello
     * inferiore, mantenendo il contesto originale dell'errore.
     * </p>
     *
     * @param message Il messaggio di dettaglio (accessibile tramite {@link #getMessage()}).
     * @param cause   La causa dell'errore (accessibile tramite {@link #getCause()}).
     * Un valore {@code null} indica che la causa è inesistente o sconosciuta.
     */
    public ClusteringRadiusException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruisce una {@code ClusteringRadiusException} con una causa specificata.
     * Il messaggio di dettaglio sarà quello della causa (se presente).
     *
     * @param cause La causa dell'errore (accessibile tramite {@link #getCause()}).
     * Un valore {@code null} indica che la causa è inesistente o sconosciuta.
     */
    public ClusteringRadiusException(Throwable cause) {
        super(cause);
    }
}
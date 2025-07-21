package data;

/**
 * Classe pubblica che rappresenta un item (coppia attributo-valore) di tipo continuo.
 * Un item continuo è definito da un attributo di tipo {@link ContinuousAttribute}
 * e un valore numerico (Double).
 * <p>
 * Estende la classe astratta {@link Item}.
 * </p>
 * 
 * @see Item
 */
public class ContinuousItem extends Item {

    private static final long serialVersionUID = 1L;

	/**
     * Costruttore per un ContinuousItem.
     * Richiama il costruttore della superclasse {@link Item}.
     *
     * @param attribute L'attributo continuo. Anche se il tipo dichiarato è {@link Attribute},
     * ci si aspetta che sia un'istanza di {@link ContinuousAttribute}
     * affinché il calcolo della distanza funzioni correttamente.
     * @param value     Il valore numerico ({@link Double}) associato all'attributo.
     */
    public ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value); // Chiama il costruttore della superclasse Item
    }

    /**
     * Calcola la distanza tra il valore scalato di questo item e un valore scalato in input.
     * La distanza è la differenza assoluta tra il valore scalato dell'attributo
     * di questo item (ottenuto da this.getValue()) e il valore scalato dell'input 'a'.
     * Per ottenere i valori scalati, utilizza il metodo getScaledValue()
     * della classe {@link ContinuousAttribute}.
     *
     * @param a L'oggetto che rappresenta l'altro valore con cui confrontarsi.
     * Ci si aspetta che sia un {@link Double}.
     * @return La differenza assoluta tra i due valori scalati.
     * @throws IllegalStateException se l'attributo memorizzato non è un {@link ContinuousAttribute}
     * o se il valore memorizzato non è un {@link Double}.
     * @throws IllegalArgumentException se l'oggetto 'a' in input non è un {@link Double}.
     */
    @Override
    public double distance(Object a) {
        // Verifica che l'attributo sia effettivamente un ContinuousAttribute
        // per poter utilizzare il metodo getScaledValue().
        if (!(this.getAttribute() instanceof ContinuousAttribute)) {
            throw new IllegalStateException("L'attributo in ContinuousItem deve essere un'istanza di ContinuousAttribute.");
        }
        ContinuousAttribute continuousAttribute = (ContinuousAttribute) this.getAttribute();

        // Recupera il valore grezzo (non scalato) di questo item.
        // Il metodo getValue() della superclasse Item restituisce Object, ma qui ci aspettiamo un Double.
        if (!(this.getValue() instanceof Double)) {
            throw new IllegalStateException("Il valore in ContinuousItem deve essere di tipo Double.");
        }
        double currentValue = (Double) this.getValue();

        // Scala il valore grezzo di questo item.
        double scaledCurrentValue = continuousAttribute.getScaledValue(currentValue);

        // Il parametro 'a' dovrebbe essere anch'esso un valore grezzo (Double) da scalare.
        if (!(a instanceof Double)) {
            throw new IllegalArgumentException("Il valore 'a' in input per il calcolo della distanza deve essere di tipo Double.");
        }
        double inputValue = (Double) a;

        // Scala il valore 'a' fornito in input, usando la stessa logica di scaling dell'attributo.
        double scaledInputValue = continuousAttribute.getScaledValue(inputValue);

        // Calcola e restituisce la differenza assoluta dei due valori scalati.
        return Math.abs(scaledCurrentValue - scaledInputValue);
    }
}
package data;

/**
 * Rappresenta un item (coppia attributo-valore) di tipo continuo.
 * <p>
 * Questa classe concreta estende {@link Item} per gestire valori numerici
 * (es. {@code (Temperatura, 25.5)}). La sua funzione principale è fornire
 * un'implementazione per il calcolo della distanza tra valori continui.
 * </p>
 *
 * @see Item
 * @see ContinuousAttribute
 */
public class ContinuousItem extends Item {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce un nuovo item con un attributo e un valore continui.
     *
     * @param attribute L'attributo associato. Per un corretto funzionamento, deve essere
     * un'istanza di {@link ContinuousAttribute}.
     * @param value     Il valore numerico di tipo {@link Double} associato all'attributo.
     */
    public ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza normalizzata tra il valore di questo item e un altro valore.
     * <p>
     * Il metodo esegue i seguenti passaggi:
     * <ol>
     * <li>Verifica che l'attributo interno sia di tipo {@link ContinuousAttribute} e il valore interno sia un {@link Double}. Se non lo sono, solleva {@link IllegalStateException}.</li>
     * <li>Verifica che il parametro in input {@code a} sia di tipo {@link Double}. Se non lo è, solleva {@link IllegalArgumentException}.</li>
     * <li>Normalizza (scala in [0,1]) sia il valore dell'item sia il valore in input usando il metodo {@link ContinuousAttribute#getScaledValue(double)}.</li>
     * <li>Restituisce la differenza assoluta tra i due valori normalizzati.</li>
     * </ol>
     * La distanza risultante è un valore adimensionale, utile per algoritmi di clustering.
     *
     * @param a L'oggetto (che deve essere un {@link Double}) rispetto al quale calcolare la distanza.
     * @return La distanza normalizzata (un valore {@code double}).
     * @throws IllegalStateException se lo stato interno dell'item (attributo o valore) non è del tipo corretto.
     * @throws IllegalArgumentException se il parametro {@code a} non è di tipo {@link Double}.
     */
    @Override
    public double distance(Object a) {
        if (!(this.getAttribute() instanceof ContinuousAttribute)) {
            throw new IllegalStateException("L'attributo in ContinuousItem deve essere un'istanza di ContinuousAttribute.");
        }
        ContinuousAttribute continuousAttribute = (ContinuousAttribute) this.getAttribute();

        if (!(this.getValue() instanceof Double)) {
            throw new IllegalStateException("Il valore in ContinuousItem deve essere di tipo Double.");
        }
        double currentValue = (Double) this.getValue();
        double scaledCurrentValue = continuousAttribute.getScaledValue(currentValue);

        if (!(a instanceof Double)) {
            throw new IllegalArgumentException("Il valore 'a' in input per il calcolo della distanza deve essere di tipo Double.");
        }
        double inputValue = (Double) a;
        double scaledInputValue = continuousAttribute.getScaledValue(inputValue);

        return Math.abs(scaledCurrentValue - scaledInputValue);
    }
}
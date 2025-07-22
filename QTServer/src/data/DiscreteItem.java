package data;

/**
 * Rappresenta un item (coppia attributo-valore) di tipo discreto.
 * <p>
 * Questa classe concreta estende {@link Item} per gestire valori categorici
 * (es. la coppia ({@code Outlook}, {@code "Sunny"})). La sua funzione
 * principale è fornire un'implementazione per il calcolo della distanza
 * tra valori discreti.
 * </p>
 *
 * @see Item
 * @see DiscreteAttribute
 */
public class DiscreteItem extends Item {

    /**
     * Identificativo di versione per la serializzazione.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Costruisce un nuovo item con un attributo e un valore discreti.
     * <p>
     * La segnatura di questo costruttore garantisce la coerenza dei tipi,
     * richiedendo specificamente un {@link DiscreteAttribute} e un valore {@link String}.
     * </p>
     *
     * @param attribute L'attributo discreto a cui l'item si riferisce.
     * @param value     Il valore (stringa) assunto dall'attributo.
     */
    public DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore di questo item e un altro oggetto.
     * <p>
     * La metrica di distanza è binaria:
     * <ul>
     * <li><b>0.0</b> se i due valori sono uguali (secondo il metodo {@code .equals()}).</li>
     * <li><b>1.0</b> se i due valori sono diversi, se l'oggetto {@code a} è nullo, o se è di un tipo non compatibile.</li>
     * </ul>
     * Questo approccio è robusto e non lancia eccezioni per tipi non compatibili.
     *
     * @param a L'oggetto da confrontare con il valore di questo item.
     * @return 0.0 se i valori sono uguali, 1.0 altrimenti.
     */
    @Override
    public double distance(Object a) {
        if (this.getValue().equals(a)) {
            return 0.0;
        } else {
            return 1.0;
        }
    }
}
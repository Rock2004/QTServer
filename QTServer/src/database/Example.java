package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un singolo esempio o record di dati, concettualmente simile a una riga di una tabella.
 * <p>
 * Un'istanza di {@code Example} contiene una lista di oggetti, dove ogni oggetto
 * rappresenta il valore di un attributo per quell'esempio.
 * La classe implementa {@link Comparable<Example>} per consentire l'ordinamento
 * o il confronto tra diversi esempi.
 * </p>
 * 
 * @see Comparable
 * @see java.util.ArrayList
 * @see java.util.List
 */
public class Example implements Comparable<Example> {

    /**
     * Lista interna che memorizza i valori dell'esempio.
     * Ogni elemento della lista è un oggetto che rappresenta il valore di un attributo.
     * Utilizza un {@link ArrayList} per la memorizzazione.
     */
    private List<Object> example = new ArrayList<Object>();

    /**
     * Aggiunge un oggetto (valore di un attributo) alla fine della lista di valori
     * che costituiscono questo esempio.
     *
     * @param o L'oggetto da aggiungere all'esempio.
     */
    public void add(Object o) {
        example.add(o);
    }
    
    /**
     * Restituisce l'oggetto (valore di un attributo) presente alla posizione (indice) specificata
     * all'interno di questo esempio.
     *
     * @param i L'indice (basato su zero) dell'oggetto da recuperare.
     * @return L'oggetto presente alla posizione {@code i} nella lista dei valori dell'esempio.
     * @throws IndexOutOfBoundsException se l'indice {@code i} è negativo o maggiore o uguale
     * al numero di valori nell'esempio.
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta questo esempio con un altro oggetto {@code Example} per l'ordinamento.
     * <p>
     * Il confronto avviene elemento per elemento, dal primo all'ultimo.
     * Non appena si trova una coppia di elementi non uguali nelle rispettive posizioni,
     * il risultato del confronto di questi due elementi (assumendo che siano {@link Comparable})
     * determina il risultato del confronto tra i due esempi.
     * Se tutti gli elementi corrispondenti sono uguali fino alla fine della tupla più corta
     * (se le lunghezze fossero diverse, ma qui si assume implicitamente che siano uguali
     * per un confronto completo), o se tutti gli elementi sono uguali e le lunghezze sono le stesse,
     * il metodo restituisce 0, indicando che gli esempi sono considerati uguali ai fini dell'ordinamento.
     * </p>
     * <p>
     * **Attenzione:** Questo metodo assume che gli oggetti contenuti nella lista {@code example}
     * siano istanze di {@link Comparable} per poter invocare {@code compareTo}.
     * Se un oggetto non è {@code Comparable} e si arriva al punto di doverlo confrontare,
     * verrà lanciata una {@link ClassCastException} a runtime.
     * Inoltre, il confronto avviene utilizzando gli elementi dell'esempio {@code ex} (parametro)
     * per chiamare {@code compareTo} sull'elemento corrispondente di {@code this.example}.
     * Questo potrebbe essere invertito a seconda dell'ordinamento desiderato (es. {@code this.example.get(i).compareTo(o)}).
     * </p>
     *
     * @param ex L'altro oggetto {@code Example} con cui confrontare questo esempio.
     * @return Un valore negativo se questo esempio è "minore" di {@code ex},
     * zero se sono considerati "uguali",
     * un valore positivo se questo esempio è "maggiore" di {@code ex}.
     * @throws ClassCastException se gli oggetti confrontati non implementano {@link Comparable}
     * o se non sono mutuamente comparabili.
     * @throws IndexOutOfBoundsException se gli esempi hanno lunghezze diverse e si tenta
     * di accedere a un indice non valido.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public int compareTo(Example ex) {
        int i = 0;
        for (Object o : ex.example) {
            if (i >= this.example.size()) {
                return -1; 
            }
            
            Object currentObject = this.example.get(i);
            if (!o.equals(currentObject)) {
                if (o instanceof Comparable && currentObject instanceof Comparable && o.getClass().equals(currentObject.getClass())) {
                    return ((Comparable)o).compareTo(currentObject);
                } else {
                    return ((Comparable)o).compareTo(currentObject);
                }
            }
            i++;
        }
        if (i < this.example.size()) {
            return 1;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale di questo esempio.
     * I valori degli attributi nell'esempio vengono convertiti in stringa
     * e concatenati, separati da uno spazio. Un ulteriore spazio viene aggiunto alla fine.
     *
     * @return Una stringa che elenca i valori dell'esempio separati da spazi.
     */
    @Override
    public String toString() {
        String str = "";
        for (Object o : example) {
            str += o.toString() + " ";
        }
        return str;
    }
}

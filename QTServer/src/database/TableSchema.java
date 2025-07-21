package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe pubblica che rappresenta lo schema di una tabella di un database.
 * <p>
 * Questa classe si connette al database tramite un'istanza di {@link DbAccess}
 * per recuperare i metadati delle colonne di una tabella specificata,
 * come il nome e il tipo di ciascuna colonna. I tipi SQL vengono mappati
 * a categorie più generiche ("string" o "number").
 * Utilizza la classe interna {@link Column} per modellare le colonne della tabella
 * </p>
 *
 * @see DbAccess
 * @see Column
 * @see java.sql.Connection
 */
public class TableSchema{

    /**
     * Riferimento all'oggetto {@link DbAccess} utilizzato per ottenere la connessione al database.
     * Questo campo ha visibilità di package.
     */
    DbAccess db;

    /**
     * Classe interna pubblica che modella una colonna della tabella.
     * <p>
     * Ogni colonna è definita da un nome e da un tipo generico ("string" o "number")
     * derivato dal tipo SQL originale.
     * </p>
     */
    public class Column {
    	
    	/**
    	 * 
    	 */
        private String name;
        
        /**
         * 
         */
        private String type;

        /**
         * Costruisce un nuovo oggetto {@code Column} con il nome e il tipo specificati.
         *
         * @param name Il nome della colonna.
         * @param type Il tipo generico della colonna ("string" o "number").
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna.
         *
         * @return Il nome della colonna.
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Verifica se il tipo generico della colonna è "number".
         *
         * @return {@code true} se il tipo della colonna è "number", {@code false} altrimenti.
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione testuale della colonna nel formato "nome:tipo".
         *
         * @return Una stringa che rappresenta il nome e il tipo della colonna.
         */
        @Override
        public String toString() {
            return name + ":" + type;
        }
    }

    /**
     * Lista contenente gli oggetti {@link Column} che rappresentano lo schema della tabella.
     * Ogni elemento della lista descrive una colonna della tabella.
     * Questo campo ha visibilità di package.
     */
    List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Costruisce un'istanza di {@code TableSchema} per una tabella specifica.
     * <p>
     * Si connette al database utilizzando l'oggetto {@link DbAccess} fornito,
     * recupera i metadati delle colonne per la tabella specificata e popola
     * la lista interna {@code tableSchema}. I tipi di colonna SQL vengono mappati
     * ai tipi generici "string" o "number".
     * </p>
     *
     * @param db L'oggetto {@link DbAccess} configurato per la connessione al database.
     * @param tableName Il nome della tabella di cui si vuole ottenere lo schema.
     * @throws SQLException Se si verifica un errore durante l'accesso ai metadati del database
     * o durante la comunicazione con il database.
     * @throws NullPointerException se {@code db} o {@code tableName} sono nulli, o se la connessione
     * ottenuta da {@code db.getConnection()} è nulla.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        this.db = db;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();
        
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number"); // JDBC SMALLINT
        mapSQL_JAVATypes.put("INT", "number");   // JDBC INTEGER
        mapSQL_JAVATypes.put("LONG", "number");  // JDBC BIGINT
        mapSQL_JAVATypes.put("FLOAT", "number"); // JDBC REAL or FLOAT
        mapSQL_JAVATypes.put("DOUBLE", "number");// JDBC DOUBLE

        Connection con = db.getConnection();
        if (con == null) {
            throw new SQLException("Connessione al database non disponibile tramite DbAccess.");
        }
        DatabaseMetaData meta = con.getMetaData();
        // Recupera le informazioni sulle colonne per la tabella specificata.
        // I parametri null indicano che non si filtrano catalog o schema specifici.
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            // Per ogni colonna trovata, se il suo TYPE_NAME è presente nella mappa,
            // crea un nuovo oggetto Column e lo aggiunge alla lista tableSchema.
            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME"))) {
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
                );
            }
            // Colonne con tipi SQL non mappati esplicitamente in mapSQL_JAVATypes verranno ignorate.
        }
        res.close(); // Chiude il ResultSet per rilasciare le risorse del database.
        // La connessione 'con' non viene chiusa qui, poiché è gestita dall'oggetto DbAccess.
    }

    /**
     * Restituisce il numero di colonne (attributi) definite nello schema della tabella.
     *
     * @return Il numero totale di colonne mappate nello schema.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce l'oggetto {@link Column} alla posizione (indice) specificata nello schema della tabella.
     *
     * @param index L'indice (basato su zero) della colonna da recuperare.
     * @return L'oggetto {@link Column} che descrive la colonna all'indice specificato.
     * @throws IndexOutOfBoundsException se l'indice è fuori dai limiti
     * (minore di 0 o >= getNumberOfAttributes()).
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }
}

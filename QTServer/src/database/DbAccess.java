package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe pubblica che gestisce l'accesso a una base di dati MySQL.
 * 
 * <p>Si occupa di caricare il driver, aprire e chiudere la connessione
 * e fornire l'oggetto Connection per l'interazione con il database.</p>
 * 
 * <p>Richiede che il connettore JDBC di MySQL sia presente nel classpath.</p>
 * 
 */
public class DbAccess {

    /** Nome completo della classe del driver JDBC di MySQL. */
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /** Tipo di DBMS (MySQL) con prefisso JDBC. */
    private final String DBMS = "jdbc:mysql";

    /** Indirizzo del server che ospita il database. */
    private final String SERVER = "localhost";

    /** Nome del database. */
    private final String DATABASE = "MapDB";

    /** Porta sulla quale MySQL è in ascolto. */
    private final String PORT = "3306";

    /** Nome utente per accedere al database. */
    private final String USER_ID = "MapUser";

    /** Password per accedere al database. */
    private final String PASSWORD = "map";

    /** Oggetto Connection che rappresenta la connessione al database. */
    private Connection conn;

    /**
     * Inizializza la connessione al database.
     * 
     * <p>Carica dinamicamente il driver MySQL e stabilisce una connessione
     * utilizzando i parametri configurati.</p>
     * 
     * @throws DatabaseConnectionException se il caricamento del driver o la connessione falliscono
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            // Caricamento del driver
            Class.forName(DRIVER_CLASS_NAME);

            // Costruzione della stringa di connessione
            String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                    + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

            // Creazione della connessione
            conn = DriverManager.getConnection(connectionString);

        } catch (ClassNotFoundException | SQLException e) {
            // In caso di errore, viene lanciata l'eccezione personalizzata
            throw new DatabaseConnectionException("Errore durante la connessione al database.", e);
        }
    }

    /**
     * Restituisce l'oggetto {@link Connection} attualmente attivo.
     * 
     * @return la connessione attiva al database, o {@code null} se non inizializzata
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database se attiva.
     * 
     * <p>Non lancia eccezioni, ma stampa eventuali errori su standard error.</p>
     */
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Errore nella chiusura della connessione: " + e.getMessage());
            }
        }
    }
}

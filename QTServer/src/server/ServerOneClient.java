package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import data.Data;
import mining.QTMiner;

public class ServerOneClient extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Data data;
	private QTMiner kmeans;

	public ServerOneClient(Socket s) throws IOException {
		this.socket = s;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.out.flush();
		this.in = new ObjectInputStream(socket.getInputStream());
		System.out.println("[SERVER-THREAD] Stream per il client " + s.getInetAddress() + " inizializzati.");
		this.start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				int requestCode = (Integer) in.readObject();
				System.out.println("Server: Ricevuta richiesta [" + requestCode + "]");

				switch (requestCode) {
					
					case 0: // storeTableFromDb()
						try {
							String tableName = (String) in.readObject();
							System.out.println("Server: Caricamento dati da tabella '" + tableName + "'...");
							this.data = new Data(tableName);
							out.writeObject("OK");
							System.out.println("Server: Dati caricati con successo.");
						} catch (Exception e) {
							out.writeObject("ERROR: " + e.getMessage());
						}
						break;

					case 1: // learningFromDbTable()
						try {
							if (this.data == null) throw new Exception("Dati non caricati.");
							double radius = (Double) in.readObject();
							System.out.println("Server: Esecuzione clustering con raggio " + radius + "...");
							this.kmeans = new QTMiner(radius);
							int numClusters = kmeans.compute(this.data);
							
							out.writeObject("OK");
							out.writeObject(numClusters);
							out.writeObject(kmeans.getC().toString(this.data));
							System.out.println("Server: Clustering completato.");
						} catch (Exception e) {
							out.writeObject("ERROR: " + e.getMessage());
						}
						break;

					case 2: // storeClusterInFile()
						try {
							if (this.kmeans == null) throw new Exception("Nessun cluster da salvare.");
							String fileName = (String) in.readObject();
							System.out.println("Server: Salvataggio cluster su file '" + fileName + "'...");
							this.kmeans.salva(fileName);
							out.writeObject("OK");
							System.out.println("Server: Salvataggio completato.");
						} catch (Exception e) {
							out.writeObject("ERROR: " + e.getMessage());
						}
						break;
						
					case 3: // learningFromFile()
						try {
							// MODIFICA QUI: Ora legge solo il nome del file come atteso dal nuovo MainTest
							String fileNameToLoad = (String) in.readObject();
							System.out.println("Server: Caricamento cluster da file '" + fileNameToLoad + "'...");
							
							this.kmeans = new QTMiner(fileNameToLoad);
							
							out.writeObject("OK");
							out.writeObject(this.kmeans.getC().toString());
							 System.out.println("Server: Caricamento da file completato.");
						} catch (FileNotFoundException e) {
							out.writeObject("ERROR: File non trovato sul server. Dettagli: " + e.getMessage());
						} catch (Exception e) {
							out.writeObject("ERROR: " + e.getMessage());
						}
						break;

					default:
						out.writeObject("ERROR: Codice richiesta non valido.");
						break;
				}
			}
		} catch (SocketException e) {
			System.out.println("Server: Client " + socket.getInetAddress() + " si è disconnesso.");
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Server: Errore di comunicazione con il client " + socket.getInetAddress() + ": " + e.getMessage());
		} finally {
			try {
				if (socket != null && !socket.isClosed()) socket.close();
			} catch (IOException e) { /* ignora */ }
		}
	}
}
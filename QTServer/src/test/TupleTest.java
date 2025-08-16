package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import data.*;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    private Tuple tuple1;
    private Tuple tuple2;
    private ContinuousAttribute tempAttribute;
    private DiscreteAttribute outlookAttribute;

    @BeforeEach
    void setUp() {
        // Setup attributi
        tempAttribute = new ContinuousAttribute("temperature", 0, 10.0, 40.0);
        outlookAttribute = new DiscreteAttribute("outlook", 1, new String[]{"sunny", "rainy", "overcast"});

        // Setup tupla 1
        tuple1 = new Tuple(2);
        tuple1.add(new ContinuousItem(tempAttribute, 25.0), 0); // Valore scalato: (25-10)/(40-10) = 0.5
        tuple1.add(new DiscreteItem(outlookAttribute, "sunny"), 1);

        // Setup tupla 2
        tuple2 = new Tuple(2);
        tuple2.add(new ContinuousItem(tempAttribute, 13.0), 0); // Valore scalato: (13-10)/(40-10) = 0.1
        tuple2.add(new DiscreteItem(outlookAttribute, "rainy"), 1);
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la distanza tra due tuple")
    void testGetDistance() {
        // Distanza per attributo continuo: |0.5 - 0.1| = 0.4
        // Distanza per attributo discreto: "sunny" != "rainy" -> 1.0
        // Distanza totale = 0.4 + 1.0 = 1.4
        double expectedDistance = 1.4;
        
        // Usiamo un delta per la comparazione di double
        assertEquals(expectedDistance, tuple1.getDistance(tuple2), 0.001,
                "La distanza calcolata non è corretta.");
    }
    
    @Test
    @DisplayName("Dovrebbe lanciare IllegalArgumentException per tuple di diversa lunghezza")
    void testGetDistanceWithDifferentLength() {
        Tuple shortTuple = new Tuple(1);
        shortTuple.add(new DiscreteItem(outlookAttribute, "sunny"), 0);

        assertThrows(IllegalArgumentException.class, () -> {
            tuple1.getDistance(shortTuple);
        }, "Dovrebbe essere lanciata un'eccezione per tuple di lunghezza diversa.");
    }
}
package test;

import data.*;
import mining.*;
import database.Example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QTMinerTest {

    private Data testData;
    private List<Attribute> attributes;

    @BeforeEach
    void setUp() {
        // 1. Creare lo schema degli attributi
        attributes = new ArrayList<>();
        attributes.add(new ContinuousAttribute("X", 0, 0.0, 10.0));
        attributes.add(new ContinuousAttribute("Y", 1, 0.0, 10.0));

        // 2. Creare i dati di esempio (due gruppi distinti)
        List<Example> examples = new ArrayList<>();
        // Gruppo 1
        examples.add(createExample(1.0, 1.0));
        examples.add(createExample(1.5, 2.0));
        examples.add(createExample(3.0, 4.0));
        // Gruppo 2
        examples.add(createExample(8.0, 7.0));
        examples.add(createExample(7.0, 8.0));
        
        // 3. Istanziare Data con il costruttore da test
        testData = new Data(examples, attributes);
    }
    
    private Example createExample(double x, double y) {
        Example ex = new Example();
        ex.add(x);
        ex.add(y);
        return ex;
    }

    @Test
    @DisplayName("compute dovrebbe trovare due cluster con un raggio appropriato")
    void testComputeShouldFindTwoClusters() throws EmptyDatasetException, ClusteringRadiusException {
        // Raggio abbastanza piccolo da separare i due gruppi
        QTMiner miner = new QTMiner(0.5);
        int numClusters = miner.compute(testData);

        assertEquals(2, numClusters, "Dovrebbe trovare esattamente 2 cluster.");
    }

    @Test
    @DisplayName("compute dovrebbe lanciare ClusteringRadiusException con un raggio troppo grande")
    void testComputeShouldThrowExceptionForLargeRadius() {
        // Raggio così grande da includere tutti i punti in un unico cluster
        QTMiner miner = new QTMiner(10.0);
        
        assertThrows(ClusteringRadiusException.class, () -> {
            miner.compute(testData);
        }, "Dovrebbe lanciare ClusteringRadiusException se si forma un solo cluster.");
    }
}
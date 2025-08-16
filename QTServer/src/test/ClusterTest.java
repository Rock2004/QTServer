package test;

import data.Tuple;
import mining.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClusterTest {

    private Cluster clusterA;
    private Cluster clusterB;

    @BeforeEach
    void setUp() {
        clusterA = new Cluster(new Tuple(0)); // Centroide fittizio
        clusterB = new Cluster(new Tuple(0)); // Centroide fittizio
    }

    @Test
    @DisplayName("compareTo dovrebbe restituire -1 se il cluster ha meno elementi")
    void compareToShouldReturnMinusOneForSmallerSize() {
        clusterA.addData(1); // Dimensione 1
        clusterB.addData(10); // Dimensione 2
        clusterB.addData(11);
        
        assertEquals(-1, clusterA.compareTo(clusterB), "Dovrebbe restituire -1 per dimensione minore.");
    }

    @Test
    @DisplayName("compareTo dovrebbe restituire 1 se il cluster ha più elementi")
    void compareToShouldReturnOneForLargerSize() {
        clusterA.addData(1);
        clusterA.addData(2); // Dimensione 2
        clusterB.addData(10); // Dimensione 1
        
        assertEquals(1, clusterA.compareTo(clusterB), "Dovrebbe restituire 1 per dimensione maggiore.");
    }

    @Test
    @DisplayName("compareTo dovrebbe restituire 1 se i cluster hanno la stessa dimensione")
    void compareToShouldReturnOneForEqualSize() {
        clusterA.addData(1);
        clusterA.addData(2); // Dimensione 2
        clusterB.addData(10);
        clusterB.addData(11); // Dimensione 2
        
        assertEquals(1, clusterA.compareTo(clusterB),
                "Dovrebbe restituire 1 per dimensioni uguali, come da implementazione.");
    }
}
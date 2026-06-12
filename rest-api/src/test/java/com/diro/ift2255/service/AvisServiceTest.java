package com.diro.ift2255.service;

import com.diro.ift2255.model.Avis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AvisServiceTest {

    private AvisService service;
    private static final String TEST_FILE = "data/testavis.json";

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
        service = new AvisService(TEST_FILE);
    }

    /**
     * Teste la méthode :
     * AvisService.getByCourse(String)
     * Vérifie que seuls les avis du cours demandé sont retournés
     */
    @Test
    void getByCourseReturnsOnlyMatchingCourse() {
        service.ajouterAvis(new Avis("IFT2255", 4, 4, "Bon cours", "Alice"));
        service.ajouterAvis(new Avis("IFT2255", 5, 5, "Difficile", "Bob"));
        service.ajouterAvis(new Avis("IFT1025", 3, 2, "Correct", "Charlie"));

        var avis = service.getByCourse("IFT2255");

        assertEquals(2, avis.size());
    }
    /**
 * Teste la méthode :
 * AvisService.getByCourse(String)
 * Cas où aucun avis n'existe pour le cours
 */
@Test
void getByCourseReturnsEmptyListWhenNoAvis() {
    service.ajouterAvis(new Avis("IFT2255", 4, 4, "Bon cours", "Alice"));

    var avis = service.getByCourse("IFT9999");

    assertTrue(avis.isEmpty());
}
/**
 * Teste la méthode :
 * AvisService.ajouterAvis(Avis)
 * Vérifie que l'avis est bien enregistré
 */
@Test
void ajouterAvisPersistsAvis() {
    service.ajouterAvis(new Avis("IFT2255", 5, 5, "Excellent", "Bob"));

    var avis = service.getByCourse("IFT2255");

    assertEquals(1, avis.size());
    assertEquals("Bob", avis.get(0).getAuteur());
}

}
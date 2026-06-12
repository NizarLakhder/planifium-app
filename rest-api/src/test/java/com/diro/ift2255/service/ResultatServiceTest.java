package com.diro.ift2255.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ResultatService
 */
class ResultatServiceTest {

    /**
     * Test : retourne Optional.empty si le cours n'existe pas
     */
    @Test
    void returnsEmptyWhenCourseNotFound() {
        ResultatService service = new ResultatService();

        assertTrue(service.getBySigle("COURSE_INEXISTANT").isEmpty());
    }

    /**
     * Test : retourne un résultat lorsque le cours existe
     * (le fichier CSV doit contenir IFT2255)
     */
    @Test
    void returnsResultWhenCourseExists() {
        ResultatService service = new ResultatService();

        assertTrue(service.getBySigle("IFT2255").isPresent());
    }

    /**
 * Test : retourne Optional.empty si le sigle est null
 */
@Test
void returnsEmptyWhenSigleIsNull() {
    ResultatService service = new ResultatService();

    assertTrue(service.getBySigle(null).isEmpty());
}
}
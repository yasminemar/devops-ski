package tn.esprit.spring;
import static org.mockito.Mockito.*; // Assurez-vous d'importer Mockito
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PisteServicesImplTest {
    @Mock
    private IPisteRepository pisteRepository;
    @InjectMocks
    private PisteServicesImpl pisteServices;

    private Piste piste1;
    private Piste piste2;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        piste1 = new Piste();
        piste2 = new Piste();
        piste1.setNamePiste("Piste Verte");
    }
    @Test
    public void testRetrieveAllPistes() {
        when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste1, piste2));

        List<Piste> pistes = pisteServices.retrieveAllPistes();

        assertThat(pistes).hasSize(2);
        verify(pisteRepository, times(1)).findAll();
    }
    @Test
    public void testAddPiste() {
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste1);

        Piste savedPiste = pisteServices.addPiste(piste1);

        assertThat(savedPiste).isEqualTo(piste1);
        verify(pisteRepository, times(1)).save(piste1);
    }
    @Test
    public void testRemovePiste() {
        Long numPiste = 1L;

        pisteServices.removePiste(numPiste);

        verify(pisteRepository, times(1)).deleteById(numPiste);
    }

    @Test
    public void testRetrievePiste() {
        Long numPiste = 1L;
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.of(piste1));

        Piste retrievedPiste = pisteServices.retrievePiste(numPiste);

        assertThat(retrievedPiste).isEqualTo(piste1);
        verify(pisteRepository, times(1)).findById(numPiste);
    }
    @Test
    public void testRetrievePisteNotFound() {
        Long numPiste = 1L;
        when(pisteRepository.findById(numPiste)).thenReturn(Optional.empty());

        Piste retrievedPiste = pisteServices.retrievePiste(numPiste);

        assertThat(retrievedPiste).isNull();
        verify(pisteRepository, times(1)).findById(numPiste);
    }
}

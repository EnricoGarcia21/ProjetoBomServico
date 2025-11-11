package unoeste.fipp.bomservico;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.services.AnuncioService;

import java.util.Optional;

@SpringBootTest
public class AnuncioServiceTest {
    @Autowired
    AnuncioService anuncioService;

    @Test
    void getAnuncioTeste(){
        Optional<Anuncio> anuncio = anuncioService.getAnuncioById(1L);
        anuncio.ifPresent(a -> System.out.println(a.getDescr()));
    }
}

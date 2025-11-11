package unoeste.fipp.bomservico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Interesse;
import unoeste.fipp.bomservico.repositories.AnuncioRepository;
import unoeste.fipp.bomservico.repositories.InteresseRepository;

import java.util.List;

@Service
public class InteresseService {

    @Autowired
    private InteresseRepository interesseRepository;

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Interesse createInteresse(Long anuncioId, Interesse interesse) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        interesse.setAnuncio(anuncio);
        return interesseRepository.save(interesse);
    }

    public List<Interesse> getInteressesByPrestadorLogin(String login) {
        return interesseRepository.findByPrestadorLogin(login);
    }

    public List<Interesse> getInteressesByAnuncioId(Long anuncioId) {
        return interesseRepository.findByAnuncioId(anuncioId);
    }

    public void deleteInteresse(Long id) {
        interesseRepository.deleteById(id);
    }
}

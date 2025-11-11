package unoeste.fipp.bomservico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Usuario;
import unoeste.fipp.bomservico.repositories.AnuncioRepository;
import unoeste.fipp.bomservico.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {
    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Anuncio> getAllAnuncios() {
        return anuncioRepository.findAll();
    }

    public Optional<Anuncio> getAnuncioById(Long id) {
        return anuncioRepository.findById(id);
    }

    public List<Anuncio> getAnunciosByPrestadorLogin(String login) {
        return anuncioRepository.findAll().stream()
                .filter(a -> a.getUsuario() != null && a.getUsuario().getLogin().equals(login))
                .toList();
    }

    public Anuncio createAnuncio(Anuncio anuncio, String prestadorLogin) {
        Usuario usuario = usuarioRepository.findByLogin(prestadorLogin)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        anuncio.setUsuario(usuario);
        return anuncioRepository.save(anuncio);
    }

    public Anuncio updateAnuncio(Long id, Anuncio anuncioDetails) {
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        anuncio.setTitulo(anuncioDetails.getTitulo());
        anuncio.setDescr(anuncioDetails.getDescr());
        anuncio.setDiasTrab(anuncioDetails.getDiasTrab());
        anuncio.setHorarioInicioDia(anuncioDetails.getHorarioInicioDia());
        anuncio.setHorarioFimDia(anuncioDetails.getHorarioFimDia());
        anuncio.setCategoriaList(anuncioDetails.getCategoriaList());

        return anuncioRepository.save(anuncio);
    }

    public void deleteAnuncio(Long id) {
        anuncioRepository.deleteById(id);
    }

    public List<Anuncio> searchAnuncios(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return anuncioRepository.findAll();
        }

        return anuncioRepository.findAll().stream()
                .filter(a -> a.getTitulo().toLowerCase().contains(keyword.toLowerCase()) ||
                             a.getDescr().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public boolean isPrestadorOwner(Long anuncioId, String prestadorLogin) {
        Optional<Anuncio> anuncio = anuncioRepository.findById(anuncioId);
        return anuncio.isPresent() &&
               anuncio.get().getUsuario() != null &&
               anuncio.get().getUsuario().getLogin().equals(prestadorLogin);
    }
}

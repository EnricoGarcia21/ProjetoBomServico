package unoeste.fipp.bomservico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Usuario;
import unoeste.fipp.bomservico.repositories.AnuncioRepository;
import unoeste.fipp.bomservico.repositories.FotoRepository;
import unoeste.fipp.bomservico.repositories.InteresseRepository;
import unoeste.fipp.bomservico.repositories.UsuarioRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AnuncioService {
    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private InteresseRepository interesseRepository;

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

    @Transactional
    public void deleteAnuncio(Long id) {
        // Find the anuncio first to get related data
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        // Delete all fotos and their files
        if (anuncio.getFotoList() != null && !anuncio.getFotoList().isEmpty()) {
            anuncio.getFotoList().forEach(foto -> {
                // Delete physical file
                try {
                    Path filePath = Paths.get(foto.getNomeArq());
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Erro ao deletar arquivo de foto: " + e.getMessage());
                }
            });
            // Delete foto records from database
            fotoRepository.deleteAll(anuncio.getFotoList());
        }

        // Delete all interesses related to this anuncio
        interesseRepository.deleteAll(interesseRepository.findByAnuncioId(id));

        // Clear many-to-many relationship with categorias
        anuncio.setCategoriaList(null);
        anuncioRepository.save(anuncio);

        // Finally, delete the anuncio
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

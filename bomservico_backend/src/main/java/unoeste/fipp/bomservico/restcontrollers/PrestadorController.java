package unoeste.fipp.bomservico.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.bomservico.dto.ErrorResponse;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Categoria;
import unoeste.fipp.bomservico.entities.Foto;
import unoeste.fipp.bomservico.entities.Interesse;
import unoeste.fipp.bomservico.entities.Usuario;
import unoeste.fipp.bomservico.services.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/apis/prestador")
public class PrestadorController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private InteresseService interesseService;

    @Autowired
    private FotoService fotoService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * GET /apis/prestador/get-all-cat - Get all categories
     */
    @GetMapping("/get-all-cat")
    public ResponseEntity<?> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/get-all-cat"));
        }
    }

    /**
     * GET /apis/prestador/get-anuncios - Get prestador's ads
     */
    @GetMapping("/get-anuncios")
    public ResponseEntity<?> getMyAnuncios() {
        try {
            String login = getCurrentUsername();
            List<Anuncio> anuncios = anuncioService.getAnunciosByPrestadorLogin(login);
            return ResponseEntity.ok(anuncios);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/get-anuncios"));
        }
    }

    /**
     * GET /apis/prestador/get-anuncio/{id} - Get one ad
     */
    @GetMapping("/get-anuncio/{id}")
    public ResponseEntity<?> getAnuncio(@PathVariable Long id) {
        try {
            String login = getCurrentUsername();
            Anuncio anuncio = anuncioService.getAnuncioById(id)
                    .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

            // Check if prestador owns this ad
            if (!anuncio.getUsuario().getLogin().equals(login)) {
                return ResponseEntity.status(403)
                        .body(new ErrorResponse(403, "Forbidden", "Acesso negado", "/apis/prestador/get-anuncio/" + id));
            }

            return ResponseEntity.ok(anuncio);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/get-anuncio/" + id));
        }
    }

    /**
     * POST /apis/prestador - Create new ad
     */
    @PostMapping
    public ResponseEntity<?> createAnuncio(@RequestBody Anuncio anuncio) {
        try {
            String login = getCurrentUsername();
            Anuncio created = anuncioService.createAnuncio(anuncio, login);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador"));
        }
    }

    /**
     * PUT /apis/prestador - Update ad
     */
    @PutMapping
    public ResponseEntity<?> updateAnuncio(@RequestBody Anuncio anuncio) {
        try {
            String login = getCurrentUsername();

            // Check if prestador owns this ad
            if (!anuncioService.isPrestadorOwner(anuncio.getId(), login)) {
                return ResponseEntity.status(403)
                        .body(new ErrorResponse(403, "Forbidden", "Acesso negado", "/apis/prestador"));
            }

            Anuncio updated = anuncioService.updateAnuncio(anuncio.getId(), anuncio);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador"));
        }
    }

    /**
     * DELETE /apis/prestador/{id} - Delete ad
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnuncio(@PathVariable Long id) {
        try {
            String login = getCurrentUsername();

            // Check if prestador owns this ad
            if (!anuncioService.isPrestadorOwner(id, login)) {
                return ResponseEntity.status(403)
                        .body(new ErrorResponse(403, "Forbidden", "Acesso negado", "/apis/prestador/" + id));
            }

            anuncioService.deleteAnuncio(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/" + id));
        }
    }

    /**
     * GET /apis/prestador/dados - Get prestador profile
     */
    @GetMapping("/dados")
    public ResponseEntity<?> getProfile() {
        try {
            String login = getCurrentUsername();
            Usuario usuario = usuarioService.getUsuarioByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/dados"));
        }
    }

    /**
     * PUT /apis/prestador/dados - Update prestador profile
     */
    @PutMapping("/dados")
    public ResponseEntity<?> updateProfile(@RequestBody Usuario usuario) {
        try {
            String login = getCurrentUsername();
            Usuario updated = usuarioService.updateUsuario(login, usuario);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/dados"));
        }
    }

    /**
     * GET /apis/prestador/mensagens - Get received messages
     */
    @GetMapping("/mensagens")
    public ResponseEntity<?> getMensagens() {
        try {
            String login = getCurrentUsername();
            List<Interesse> mensagens = interesseService.getInteressesByPrestadorLogin(login);
            return ResponseEntity.ok(mensagens);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/mensagens"));
        }
    }

    /**
     * DELETE /apis/prestador/mensagem/{id} - Delete message
     */
    @DeleteMapping("/mensagem/{id}")
    public ResponseEntity<?> deleteMensagem(@PathVariable Long id) {
        try {
            interesseService.deleteInteresse(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/mensagem/" + id));
        }
    }

    /**
     * POST /apis/prestador/anuncio/{id}/foto - Upload photo
     */
    @PostMapping("/anuncio/{id}/foto")
    public ResponseEntity<?> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String login = getCurrentUsername();

            // Check if prestador owns this ad
            if (!anuncioService.isPrestadorOwner(id, login)) {
                return ResponseEntity.status(403)
                        .body(new ErrorResponse(403, "Forbidden", "Acesso negado", "/apis/prestador/anuncio/" + id + "/foto"));
            }

            Foto foto = fotoService.uploadFoto(id, file);
            return ResponseEntity.ok(foto);
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", "Erro ao fazer upload: " + e.getMessage(), "/apis/prestador/anuncio/" + id + "/foto"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/anuncio/" + id + "/foto"));
        }
    }

    /**
     * DELETE /apis/prestador/foto/{id} - Delete photo
     */
    @DeleteMapping("/foto/{id}")
    public ResponseEntity<?> deleteFoto(@PathVariable Long id) {
        try {
            fotoService.deleteFoto(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", "Erro ao deletar foto: " + e.getMessage(), "/apis/prestador/foto/" + id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/prestador/foto/" + id));
        }
    }
}

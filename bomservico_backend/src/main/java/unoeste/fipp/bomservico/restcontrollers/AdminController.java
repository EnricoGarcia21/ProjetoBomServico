package unoeste.fipp.bomservico.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.bomservico.dto.ErrorResponse;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Categoria;
import unoeste.fipp.bomservico.services.AnuncioService;
import unoeste.fipp.bomservico.services.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/apis/admin")
public class AdminController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AnuncioService anuncioService;

    /**
     * GET /apis/admin/get-all-cat - Get all categories
     */
    @GetMapping("/get-all-cat")
    public ResponseEntity<?> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.getAllCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin/get-all-cat"));
        }
    }

    /**
     * GET /apis/admin/get-cat/{id} - Get one category
     */
    @GetMapping("/get-cat/{id}")
    public ResponseEntity<?> getCategoria(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.getCategoriaById(id)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin/get-cat/" + id));
        }
    }

    /**
     * POST /apis/admin - Create new category
     */
    @PostMapping
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria created = categoriaService.createCategoria(categoria);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin"));
        }
    }

    /**
     * PUT /apis/admin - Update category
     */
    @PutMapping
    public ResponseEntity<?> updateCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria updated = categoriaService.updateCategoria(categoria.getId(), categoria);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin"));
        }
    }

    /**
     * DELETE /apis/admin/cat/{id} - Delete category
     */
    @DeleteMapping("/cat/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin/cat/" + id));
        }
    }

    /**
     * GET /apis/admin/anuncios - Get all ads
     */
    @GetMapping("/anuncios")
    public ResponseEntity<?> getAllAnuncios() {
        try {
            List<Anuncio> anuncios = anuncioService.getAllAnuncios();
            return ResponseEntity.ok(anuncios);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin/anuncios"));
        }
    }

    /**
     * DELETE /apis/admin/anuncio/{id} - Delete inappropriate ad
     */
    @DeleteMapping("/anuncio/{id}")
    public ResponseEntity<?> deleteAnuncio(@PathVariable Long id) {
        try {
            anuncioService.deleteAnuncio(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/admin/anuncio/" + id));
        }
    }
}

package unoeste.fipp.bomservico.restcontrollers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.bomservico.dto.ErrorResponse;
import unoeste.fipp.bomservico.dto.InteresseRequest;
import unoeste.fipp.bomservico.dto.LoginRequest;
import unoeste.fipp.bomservico.dto.LoginResponse;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Interesse;
import unoeste.fipp.bomservico.entities.Usuario;
import unoeste.fipp.bomservico.security.CustomUserDetailsService;
import unoeste.fipp.bomservico.security.JwtUtil;
import unoeste.fipp.bomservico.services.AnuncioService;
import unoeste.fipp.bomservico.services.InteresseService;
import unoeste.fipp.bomservico.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/public")
public class PublicController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private InteresseService interesseService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * POST /apis/public/login - Login and get JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getSenha())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", "Login ou senha inválidos", "/apis/public/login"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getLogin());
        Usuario usuario = usuarioService.getUsuarioByLogin(loginRequest.getLogin())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        final String jwt = jwtUtil.generateToken(usuario.getLogin(), usuario.getNivel());

        LoginResponse response = new LoginResponse(jwt, usuario.getLogin(), usuario.getNome(), usuario.getNivel());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /apis/public/add-user - Register new service provider
     */
    @PostMapping("/add-user")
    public ResponseEntity<?> registerPrestador(@RequestBody Usuario usuario) {
        try {
            // Set nivel to 0 for prestador
            usuario.setNivel(0);
            Usuario created = usuarioService.createUsuario(usuario);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/add-user"));
        }
    }

    /**
     * GET /apis/public/check-user/{login} - Check user nivel (for debugging)
     */
    @GetMapping("/check-user/{login}")
    public ResponseEntity<?> checkUserNivel(@PathVariable String login) {
        try {
            Usuario usuario = usuarioService.getUsuarioByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return ResponseEntity.ok(Map.of(
                    "login", usuario.getLogin(),
                    "nome", usuario.getNome(),
                    "nivel", usuario.getNivel(),
                    "role", usuario.getNivel() == 1 ? "ADMIN" : "PRESTADOR"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/check-user/" + login));
        }
    }

    /**
     * PUT /apis/public/fix-user-nivel/{login} - Fix user nivel to prestador (for debugging)
     */
    @PutMapping("/fix-user-nivel/{login}")
    public ResponseEntity<?> fixUserNivel(@PathVariable String login) {
        try {
            Usuario usuario = usuarioService.getUsuarioByLogin(login)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setNivel(0); // Set to PRESTADOR
            usuarioService.updateUsuarioNivel(login, 0);
            return ResponseEntity.ok(Map.of(
                    "message", "User nivel updated successfully",
                    "login", usuario.getLogin(),
                    "newNivel", 0,
                    "role", "PRESTADOR"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/fix-user-nivel/" + login));
        }
    }

    /**
     * POST /apis/public/hash-password - Generate BCrypt hash for password (for debugging)
     */
    @PostMapping("/hash-password")
    public ResponseEntity<?> hashPassword(@RequestBody Map<String, String> request) {
        try {
            String password = request.get("password");
            if (password == null || password.isEmpty()) {
                throw new RuntimeException("Password is required");
            }
            String hash = usuarioService.hashPassword(password);
            return ResponseEntity.ok(Map.of(
                    "password", password,
                    "hash", hash,
                    "length", hash.length()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/hash-password"));
        }
    }

    /**
     * POST /apis/public/fix-all-passwords - Fix all user passwords (for debugging)
     */
    @PostMapping("/fix-all-passwords")
    public ResponseEntity<?> fixAllPasswords() {
        try {
            Map<String, String> defaultPasswords = Map.of(
                    "jorge", "123",
                    "admin", "admin",
                    "eddy", "eddy123",
                    "evandro", "evandro"
            );

            List<String> updated = new ArrayList<>();
            for (Map.Entry<String, String> entry : defaultPasswords.entrySet()) {
                String login = entry.getKey();
                String password = entry.getValue();
                try {
                    usuarioService.updatePassword(login, password);
                    updated.add(login);
                } catch (Exception e) {
                    // User might not exist, skip
                }
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Passwords updated successfully",
                    "updated", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/fix-all-passwords"));
        }
    }

    /**
     * GET /apis/public/anuncio/get-filter - Search/filter ads
     */
    @GetMapping("/anuncio/get-filter")
    public ResponseEntity<?> searchAnuncios(@RequestParam(required = false) String keyword) {
        try {
            List<Anuncio> anuncios = anuncioService.searchAnuncios(keyword);
            return ResponseEntity.ok(anuncios);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/anuncio/get-filter"));
        }
    }

    /**
     * GET /apis/public/anuncio/get-one/{id} - Get one ad details
     */
    @GetMapping("/anuncio/get-one/{id}")
    public ResponseEntity<?> getAnuncio(@PathVariable Long id) {
        try {
            Anuncio anuncio = anuncioService.getAnuncioById(id)
                    .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
            return ResponseEntity.ok(anuncio);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/anuncio/get-one/" + id));
        }
    }

    /**
     * POST /apis/public/mensagem/{anuncioId} - Send interest message
     */
    @PostMapping("/mensagem/{anuncioId}")
    public ResponseEntity<?> sendInteresse(@PathVariable Long anuncioId,
                                           @Valid @RequestBody InteresseRequest interesseRequest) {
        try {
            Interesse interesse = new Interesse();
            interesse.setNome(interesseRequest.getNome());
            interesse.setFone(interesseRequest.getFone());
            interesse.setEmail(interesseRequest.getEmail());
            interesse.setMensagem(interesseRequest.getMensagem());

            Interesse created = interesseService.createInteresse(anuncioId, interesse);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "Bad Request", e.getMessage(), "/apis/public/mensagem/" + anuncioId));
        }
    }
}

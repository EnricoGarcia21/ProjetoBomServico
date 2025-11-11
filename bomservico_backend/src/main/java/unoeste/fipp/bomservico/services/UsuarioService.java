package unoeste.fipp.bomservico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unoeste.fipp.bomservico.entities.Usuario;
import unoeste.fipp.bomservico.repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public Usuario createUsuario(Usuario usuario) {
        // Encrypt password before saving
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(String login, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Update fields (except login which is the PK and senha)
        usuario.setNome(usuarioDetails.getNome());
        usuario.setCpf(usuarioDetails.getCpf());
        usuario.setDtNasc(usuarioDetails.getDtNasc());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setTelefone(usuarioDetails.getTelefone());
        usuario.setEndereco(usuarioDetails.getEndereco());

        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(String login) {
        usuarioRepository.deleteById(login);
    }

    public boolean authenticateUsuario(String login, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByLogin(login);
        if (usuario.isPresent()) {
            return passwordEncoder.matches(senha, usuario.get().getSenha());
        }
        return false;
    }
}

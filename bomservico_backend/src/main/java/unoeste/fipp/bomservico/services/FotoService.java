package unoeste.fipp.bomservico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.bomservico.entities.Anuncio;
import unoeste.fipp.bomservico.entities.Foto;
import unoeste.fipp.bomservico.repositories.AnuncioRepository;
import unoeste.fipp.bomservico.repositories.FotoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Foto uploadFoto(Long anuncioId, MultipartFile file) throws IOException {
        // Check if anuncio has less than 3 photos
        long fotoCount = fotoRepository.countByAnuncioId(anuncioId);
        if (fotoCount >= 3) {
            throw new RuntimeException("Anúncio já possui 3 fotos. Máximo permitido.");
        }

        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create Foto entity
        Foto foto = new Foto();
        foto.setNomeArq(uploadDir + "/" + filename);
        foto.setAnuncio(anuncio);

        return fotoRepository.save(foto);
    }

    public List<Foto> getFotosByAnuncioId(Long anuncioId) {
        return fotoRepository.findByAnuncioId(anuncioId);
    }

    public void deleteFoto(Long fotoId) throws IOException {
        Foto foto = fotoRepository.findById(fotoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        // Delete file from filesystem
        Path filePath = Paths.get(uploadDir).resolve(foto.getNomeArq());
        Files.deleteIfExists(filePath);

        // Delete from database
        fotoRepository.deleteById(fotoId);
    }
}

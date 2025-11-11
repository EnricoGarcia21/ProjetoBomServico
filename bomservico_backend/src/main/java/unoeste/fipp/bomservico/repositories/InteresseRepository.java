package unoeste.fipp.bomservico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unoeste.fipp.bomservico.entities.Interesse;

import java.util.List;

@Repository
public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    List<Interesse> findByAnuncioId(Long anuncioId);

    @Query("SELECT i FROM Interesse i WHERE i.anuncio.usuario.login = :login ORDER BY i.id DESC")
    List<Interesse> findByPrestadorLogin(@Param("login") String login);
}

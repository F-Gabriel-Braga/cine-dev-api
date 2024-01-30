package io.github.fgabrielbraga.CineDev.repository;

import io.github.fgabrielbraga.CineDev.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilmRepository extends JpaRepository<Film, UUID> {

    @Query(value = "SELECT * FROM films f WHERE " +
            "f.title LIKE CONCAT('%', IFNULL(?, ''), '%') AND " +
            "f.genres LIKE CONCAT('%', IFNULL(?, ''), '%')", nativeQuery = true)
    List<Film> findAllWithFilter(String title, String genres);

    @Query(value = "SELECT * FROM films ORDER BY published_in DESC LIMIT 120", nativeQuery = true)
    List<Film> findForClient();

    @Query(value = "SELECT * FROM films WHERE genres LIKE CONCAT('%', IFNULL(?, ''), '%') " +
            "ORDER BY published_in DESC LIMIT 120", nativeQuery = true)
    List<Film> findByGenresForClient(String genres);
}

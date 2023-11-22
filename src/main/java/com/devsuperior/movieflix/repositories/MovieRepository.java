package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query(nativeQuery = true, value = """
            SELECT * FROM (
                SELECT DISTINCT m.id, m.title, m.sub_title, m.movie_year, m.img_url
                FROM tb_movie m
                INNER JOIN tb_genre g ON m.genre_id = g.id
                WHERE (:genreId = 0 OR m.genre_id = :genreId)
                ORDER BY m.title
            ) AS tb_result
            """,
            countQuery = """
            SELECT COUNT(*) FROM(
                SELECT DISTINCT m.id, m.title, m.sub_title, m.movie_year, m.img_url
                FROM tb_movie m
                INNER JOIN tb_genre g ON m.genre_id = g.id
                WHERE (:genreId = 0 OR m.genre_id = :genreId)
                ORDER BY m.title
            ) AS tb_result
            """)
    Page<MovieProjection> searchMovies(Long genreId, Pageable pageable);

    @Query("SELECT obj " +
            "FROM Movie obj " +
            "JOIN FETCH obj.genre " +
            "WHERE obj.id IN :movieIds")
    List<Movie> searchMoviesWithGenre(List<Long> movieIds);
}

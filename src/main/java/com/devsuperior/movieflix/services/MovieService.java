package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAll(String genre, Pageable pageable) {
        Long genreId = 0L;
        if(!"0".equals(genre)) {
            genreId = Long.parseLong(genre);
        }

        Page<MovieProjection> page = repository.searchMovies(genreId, pageable);
        List<Long> movieIds = page.map(m -> m.getId()).toList();

        List<Movie> entities = repository.searchMoviesWithGenre(movieIds);
        entities = (List<Movie>) Utils.replace(page.getContent(), entities);

        List<MovieCardDTO> dtos = entities.stream().map(m -> new MovieCardDTO(m)).toList();

        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    public MovieDetailsDTO findById(Long id) {
        Optional<Movie> obj = repository.findById(id);
        Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new MovieDetailsDTO(entity);
    }
}

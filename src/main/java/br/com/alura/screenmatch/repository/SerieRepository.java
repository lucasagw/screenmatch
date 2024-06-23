package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Category;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {


    Optional<Serie> findByTitleContainingIgnoreCase(String title);


    List<Serie> findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(String actor, double rating);


    List<Serie> findByTotalSeasonsLessThanEqualAndImdbRatingGreaterThanEqual(int totalSeasons, double rating);


    List<Serie> findTop5ByOrderByImdbRatingDesc();


    List<Serie> findByGenre(Category category);


    @Query("SELECT s FROM Serie s WHERE s.totalSeasons <= :totalSeasons AND s.imdbRating >= :rating")
    List<Serie> getSerieBySeasonAndRating(int totalSeasons, double rating);


    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE e.title ILIKE %:episodeTitle%")
    List<Episode> findEpisodesByTitle(String episodeTitle);


    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie " +
            "ORDER BY e.imdbRating DESC LIMIT 5")
    List<Episode> findTop5EpisodesBySerie(Serie serie);


    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s = :serie " +
            "AND YEAR(e.released) >= :anoLancamento " +
            "ORDER BY s.title")
    List<Episode> findBySerieAndReleasedYear(Serie serie, int anoLancamento);


    List<Serie> findTop5ByOrderByEpisodesReleasedDesc();


    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodes e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.released) DESC LIMIT 5")
    List<Serie> findLimit5ByOrderByEpisodesReleasedDesc();


    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id AND e.season = :season")
    List<Episode> getEpisodesBySerieAndSeason(Long id, Integer season);
}

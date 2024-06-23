package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Category;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SerieService {

    @Autowired
    private SerieRepository serieRepository;


    public List<SerieDTO> findAll() {
        return convertToDTO(serieRepository.findAll());
    }

    public List<SerieDTO> seriesTop5() {

        return convertToDTO(serieRepository.findTop5ByOrderByImdbRatingDesc());
    }

    public List<SerieDTO> seriesReleased() {

        return convertToDTO(serieRepository.findLimit5ByOrderByEpisodesReleasedDesc());
    }

    public SerieDTO serieDetail(Long id) {

        Serie serie = serieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Serie not found"));

        return new SerieDTO(serie.getId(), serie.getTitle(),
                serie.getTotalSeasons(), serie.getImdbRating(),
                serie.getGenre(), serie.getActors(),
                serie.getPoster(), serie.getPlot());
    }

    public List<EpisodeDTO> getTotalSeasons(Long id) {

        Serie serie = serieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Serie not found"));

        return serie.getEpisodes().stream()
                .map(e -> new EpisodeDTO(e.getTitle(),
                        e.getEpisode(), e.getSeason()))
                .toList();
    }

    public List<EpisodeDTO> getTSeason(Long id, Integer season) {

        return serieRepository.getEpisodesBySerieAndSeason(id, season)
                .stream()
                .map(e -> new EpisodeDTO(e.getTitle(),
                        e.getEpisode(), e.getSeason()))
                .toList();
    }

    private List<SerieDTO> convertToDTO(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitle(),
                        s.getTotalSeasons(), s.getImdbRating(),
                        s.getGenre(), s.getActors(),
                        s.getPoster(), s.getPlot()))
                .toList();
    }

    public List<SerieDTO> seriesByCategory(String category) {

        return convertToDTO(serieRepository.findByGenre(Category.fromPtBrString(category.toUpperCase())));
    }

    public List<EpisodeDTO> seriesEpisodesTop5(Long id) {

        Serie serie = serieRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Serie not found"));

        return serieRepository.findTop5EpisodesBySerie(serie)
                .stream()
                .map(e -> new EpisodeDTO(e.getTitle(),
                        e.getEpisode(), e.getSeason()))
                .toList();
    }
}


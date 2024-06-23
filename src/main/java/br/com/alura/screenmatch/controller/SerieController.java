package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;


    @GetMapping
    public List<SerieDTO> series() {
        return serieService.findAll();
    }

    @GetMapping("/top5")
    public List<SerieDTO> seriesTop5() {
        return serieService.seriesTop5();
    }

    @GetMapping("/released")
    public List<SerieDTO> seriesReleased() {
        return serieService.seriesReleased();
    }

    @GetMapping("/{id}")
    public SerieDTO serieDetail(@PathVariable Long id) {
        return serieService.serieDetail(id);
    }

    @GetMapping("/{id}/seasons/all")
    public List<EpisodeDTO> getTotalSeasons(@PathVariable Long id) {
        return serieService.getTotalSeasons(id);
    }

    @GetMapping("/{id}/seasons/{season}")
    public List<EpisodeDTO> getTSeason(@PathVariable Long id, @PathVariable Integer season) {
        return serieService.getTSeason(id, season);
    }

    @GetMapping("/category/{category}")
    public List<SerieDTO> seriesByCategory(@PathVariable String category) {
        return serieService.seriesByCategory(category);
    }

    @GetMapping("{id}/seasons/top")
    public List<EpisodeDTO> seriesEpisodesTop5(@PathVariable Long id) {
        return serieService.seriesEpisodesTop5(id);
    }
}

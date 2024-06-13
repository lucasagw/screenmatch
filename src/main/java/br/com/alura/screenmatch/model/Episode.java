package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "episode")
    private Integer episode;

    @Column(name = "season")
    private Integer season;

    @Column(name = "imdb_rating")
    private Double imdbRating;

    @Column(name = "released")
    private LocalDate released;

    @ManyToOne
    @JoinColumn(name = "serie_id", referencedColumnName = "id")
    private Serie serie;

    public Episode() {
    }

    public Episode(String season, EpisodeData episodeData, Serie serie) {
        this.title = episodeData.title();
        this.episode = Integer.parseInt(episodeData.episode());
        this.season = Integer.parseInt(season);
        try {
            this.imdbRating = Double.parseDouble(episodeData.imdbRating());
        } catch (NumberFormatException e) {
            this.imdbRating = 0.0;
        }
        try {
            this.released = LocalDate.parse(episodeData.released());
        } catch (DateTimeParseException e) {
            this.released = null;
        }
        this.serie = serie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", episode=" + episode +
                ", season=" + season +
                ", imdbRating=" + imdbRating +
                ", released=" + released +
                '}';
    }
}

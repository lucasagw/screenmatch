package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episode {

    private String title;
    private Integer episode;
    private Integer season;
    private Double imdbRating;
    private LocalDate released;

    public Episode() {
    }

    public Episode(String season, EpisodeData episodeData) {
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

    @Override
    public String toString() {
        return "Episode{" +
                "title='" + title + '\'' +
                ", episode=" + episode +
                ", season=" + season +
                ", imdbRating=" + imdbRating +
                ", released=" + released +
                '}';
    }
}

package br.com.alura.screenmatch.model;


import br.com.alura.screenmatch.service.LibreTranslateConsult;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "total_seasons")
    private Integer totalSeasons;

    @Column(name = "imdb_rating")
    private Double imdbRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Category genre;

    @Column(name = "actors")
    private String actors;

    @Column(name = "poster")
    private String poster;

    @Column(name = "plot")
    private String plot;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();


    public Serie() {
    }

    public Serie(SerieData serieData) {
        this.title = serieData.title();
        this.totalSeasons = serieData.totalSeasons();
        this.imdbRating = OptionalDouble.of(Double.parseDouble(serieData.imdbRating())).orElse(0.0);
        this.genre = Category.fromString(serieData.genre().split(",")[0].trim());
        this.actors = serieData.actors();
        this.poster = serieData.poster();
        this.plot = LibreTranslateConsult.getTranslate(serieData.plot()).trim();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public Category getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public String getPlot() {
        return plot;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        //episodes.forEach(e -> e.setSerie(this));
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Serie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", imdbRating=" + imdbRating +
                ", genre=" + genre +
                ", actors='" + actors + '\'' +
                ", poster='" + poster + '\'' +
                ", plot='" + plot + '\'' +
                ", episodes=" + episodes +
                '}';
    }
}

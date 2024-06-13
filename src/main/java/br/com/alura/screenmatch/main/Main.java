package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.*;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private final String ADDRESS = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apiKey=77e71fdc";

    private ApiConsume apiConsume = new ApiConsume();

    private ConvertsData convertsData = new ConvertsData();

    private List<Serie> series = new ArrayList<>();

    private SerieRepository serieRepository;

    private Optional<Serie> serie;


    public Main(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void displayMenu() {
        var option = -1;
        while (option != 0) {

            var menu = """
                    1 - Search series
                    2 - Search episodes by series
                    3 - List series searched  
                    4 - Search serie by title
                    5 - Search series by actor
                    6 - Top 5 series            
                    7 - Search series by genre
                    8 - Filter series by total seasons and rating
                    9 - Search episode by title     
                    10 - Top 5 episodes by series
                    11 - Search episodes by date                
                                        
                    0 - Exit
                    """;

            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchSeriesWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listSeriesSearched();
                    break;
                case 4:
                    searchSerieByTitle();
                    break;
                case 5:
                    searchSerieByActor();
                    break;
                case 6:
                    searchTop5Series();
                    break;
                case 7:
                    searchSerieByGenre();
                    break;
                case 8:
                    filterSerieByTotalSeasonsAndRating();
                    break;
                case 9:
                    searchEpisodeByTitle();
                    break;
                case 10:
                    searchTop5EpisodesBySeries();
                    break;
                case 11:
                    searchEpisodesByDate();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchSeriesWeb() {
        SerieData dados = getSerieData();
        //seriesData.add(dados);
        Serie serie = new Serie(dados);
        serieRepository.save(serie);

        System.out.println(dados);
    }

    private SerieData getSerieData() {


        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = apiConsume.getData(ADDRESS + nomeSerie.replace(" ", "+") + API_KEY);
        SerieData dados = convertsData.getData(json, SerieData.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {

        listSeriesSearched();

        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = serieRepository.findByTitleContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();

            List<SeasonData> seasonDataList = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalSeasons(); i++) {
                var json = apiConsume.getData(ADDRESS + serieEncontrada.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
                SeasonData seasonData = convertsData.getData(json, SeasonData.class);
                seasonDataList.add(seasonData);
            }
            seasonDataList.forEach(System.out::println);

            List<Episode> episodes = seasonDataList.stream()
                    .flatMap(s -> s.episodes().stream()
                            .map(e -> new Episode(s.season(), e, serieEncontrada)))
                    .toList();

            serieEncontrada.setEpisodes(episodes);

            serieRepository.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listSeriesSearched() {

        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }

    private void searchSerieByTitle() {

        System.out.println("Escolha uma série pelo nome: ");

        var nomeSerie = scanner.nextLine();

        serie = serieRepository.findByTitleContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            System.out.println("Dados da série: " + serie.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void searchSerieByActor() {

        System.out.println("Escolha um ator: ");
        var actor = scanner.nextLine();

        System.out.println("Avaliação mínima: ");
        var rating = scanner.nextDouble();

        List<Serie> series = serieRepository.findByActorsContainingIgnoreCaseAndImdbRatingGreaterThanEqual(actor, rating);

        if (series.isEmpty()) {
            System.out.println("Série não encontrada");
        } else {
            System.out.println("Séries encontradas em que o ator " + actor + " participa: ");
            series.forEach(serie ->
                    System.out.println(serie.getTitle() + " avaliação: " + serie.getImdbRating()));
        }

    }

    private void searchSerieByActorWithStream() {

        System.out.println("Escolha um ator: ");
        var actor = scanner.nextLine();

        System.out.println("Avaliação mínima: ");
        var rating = scanner.nextDouble();

        List<Serie> series = serieRepository.findAll().stream()
                .filter(serie -> serie.getActors().toUpperCase().contains(actor.toUpperCase()) && serie.getImdbRating() >= rating)
                .toList();

        if (series.isEmpty()) {
            System.out.println("Série não encontrada");
        } else {
            System.out.println("Séries encontradas em que o ator " + actor + " participa: ");
            series.forEach(serie ->
                    System.out.println(serie.getTitle() + " avaliação: " + serie.getImdbRating()));
        }

    }

    private void searchTop5Series() {

        List<Serie> top5Series = serieRepository.findTop5ByOrderByImdbRatingDesc();
        System.out.println("Top 5 séries: ");
        top5Series.forEach(serie ->
                System.out.println(serie.getTitle() + " avaliação: " + serie.getImdbRating()));

    }

    private void searchSerieByGenre() {

        System.out.println("Escolha um gênero: ");
        var genre = scanner.nextLine();

        List<Serie> series = serieRepository.findByGenre(Category.fromPtBrString(genre));

        if (series.isEmpty()) {
            System.out.println("Série não encontrada");
        } else {
            System.out.println("Séries encontradas no gênero " + genre + ": ");
            series.forEach(serie ->
                    System.out.println(serie.getTitle() + " avaliação: " + serie.getImdbRating()));
        }
    }

    private void filterSerieByTotalSeasonsAndRating() {

        System.out.println("Digite o número máximo de temporadas: ");
        var totalSeasons = scanner.nextInt();

        System.out.println("Digite a avaliação mínima: ");
        var rating = scanner.nextDouble();

        List<Serie> series = serieRepository.getSerieBySeasonAndRating(totalSeasons, rating);

        if (series.isEmpty()) {
            System.out.println("Série não encontrada");
        } else {
            System.out.println("Séries encontradas com até " + totalSeasons + " temporadas e avaliação maior ou igual a " + rating + ": ");
            series.forEach(serie ->
                    System.out.println(serie.getTitle() + " avaliação: " + serie.getImdbRating()));
        }
    }

    private void searchEpisodeByTitle() {

        System.out.println("Digite o nome do episódio: ");
        var episodeTitle = scanner.nextLine();

        List<Episode> episodes = serieRepository.findEpisodesByTitle(episodeTitle);

        if (episodes.isEmpty()) {
            System.out.println("Episódio não encontrado");
        } else {
            System.out.println("Episódios encontrados com o nome " + episodeTitle + ": ");
            episodes.forEach(episode ->
                    System.out.printf("Serie: %s Season: %d Episode %d - %s\n",
                            episode.getSerie().getTitle(), episode.getSeason(),
                            episode.getEpisode(), episode.getTitle()));
        }
    }

    private void searchTop5EpisodesBySeries() {

        searchSerieByTitle();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            List<Episode> top5Episodes = serieRepository.findTop5EpisodesBySerie(serieEncontrada);

            System.out.println("Top 5 episódios da série " + serieEncontrada.getTitle() + ": ");
            top5Episodes.forEach(episode ->
                    System.out.printf("Season: %d Episode %d - %s avaliação: %.1f\n",
                            episode.getSeason(), episode.getEpisode(), episode.getTitle(), episode.getImdbRating()));
        }
    }

    private void searchEpisodesByDate() {

        searchSerieByTitle();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();

            System.out.println("Digite o ano de lançamento: ");
            var anoLancamento = scanner.nextInt();
            scanner.nextLine();

            List<Episode> episodes = serieRepository.findBySerieAndReleasedYear(serieEncontrada, anoLancamento);

            if (episodes.isEmpty()) {
                System.out.println("Episódio não encontrado");
            } else {
                System.out.println("Episódios encontrados no ano:" + anoLancamento + " da série " + serieEncontrada.getTitle());
                episodes.forEach(episode ->
                        System.out.printf("Season: %d Episode %d - %s - Released: %s\n",
                                episode.getSeason(), episode.getEpisode(), episode.getTitle(), episode.getReleased()));
            }
        }

    }

}
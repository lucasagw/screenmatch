package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private final String ADDRESS = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apiKey=77e71fdc";

    private ApiConsume apiConsume = new ApiConsume();

    private ConvertsData convertsData = new ConvertsData();

    public void displayMenu() {
        System.out.println("1. Search for a serie");
        var serieName = scanner.nextLine();
        var json = apiConsume.getData(ADDRESS + serieName.replace(" ", "+") + API_KEY);

        SerieData serieData = convertsData.getData(json, SerieData.class);
        System.out.println(serieData);

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= serieData.totalSeasons(); i++) {
            json = apiConsume.getData(ADDRESS + serieName.replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonData seasonData = convertsData.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        /*for (int i = 0; i < serieData.totalSeasons(); i++) {
            List<EpisodeData> episodesData = seasons.get(i).episodes();
            System.out.println("Season " + (i + 1));
            for (int j = 0; j < episodesData.size(); j++) {
                System.out.println(episodesData.get(j).title());
            }
        }*/

        /*seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));
        seasons.forEach(s -> System.out.println(s));
        seasons.forEach(System.out::println);*/

        /*List<EpisodeData> episodeDataList = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                //.collect(Collectors.toList());
                .toList();

        System.out.println("\nTop 5 episodes");
        episodeDataList.stream()
                .filter(e -> !e.imdbRating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(EpisodeData::imdbRating).reversed())
                .peek(e -> System.out.println("Ordenação " + e))
                .limit(5)
                .peek(e -> System.out.println("Limite " + e))
                .map(e -> e.title().toUpperCase() + " - " + e.imdbRating())
                .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);*/

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(e -> new Episode(s.season(), e)))
                .toList();

        episodes.forEach(System.out::println);


        /*System.out.println("\nQual episódio você deseja ver?");
        var partOfTitle = scanner.nextLine();
        Optional<Episode> first = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(partOfTitle.toUpperCase()))
                .findFirst();

        if (first.isPresent()) {
            System.out.println("Season found!");
            System.out.println("Season: " + first.get().getSeason());
        } else {
            System.out.println("Episode not found!");
        }*/

        /*System.out.println("\nA partir de que ano você deseja ver os episódios?");
        var year = scanner.nextInt();
        scanner.nextLine();

        LocalDate date = LocalDate.of(year, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e -> e.getReleased() != null && e.getReleased().isAfter(date))
                .forEach(e -> System.out.println("Season: " + e.getSeason() +
                        " Episode: " + e.getTitle() +
                        " Data de lançamento: " + e.getReleased().format(formatter)));*/


        Map<Integer, Double> seasonRatings = episodes.stream()
                .filter(e -> e.getImdbRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getImdbRating)));

        System.out.println("\nSeason ratings");
        System.out.println(seasonRatings);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getImdbRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getImdbRating));

        System.out.println("\nEpisode ratings");
        //System.out.println(est);
        System.out.println("\nAverage: " + est.getAverage());
        System.out.println("Episode with the highest rating: " + est.getMax());
        System.out.println("Episode with the low rating: " + est.getMin());
        System.out.println("Total of episodes: " + est.getCount());

    }

}

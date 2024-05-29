package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.SeasonData;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertsData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {

    private Scanner scanner = new Scanner(System.in);

    private final String ADDRESS = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apiKey=77e71fdc";

    private ApiConsume apiConsume = new ApiConsume();

    private ConvertsData convertsData = new ConvertsData();

    private List<SerieData> seriesData = new ArrayList<>();

    private SerieRepository serieRepository;


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
        SerieData dadosSerie = getSerieData();
        List<SeasonData> seasonDataList = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalSeasons(); i++) {
            var json = apiConsume.getData(ADDRESS + dadosSerie.title().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonData seasonData = convertsData.getData(json, SeasonData.class);
            seasonDataList.add(seasonData);
        }
        seasonDataList.forEach(System.out::println);
    }

    private void listSeriesSearched() {

        List<Serie> series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
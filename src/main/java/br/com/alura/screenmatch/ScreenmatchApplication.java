package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.SerieData;
import br.com.alura.screenmatch.service.ApiConsume;
import br.com.alura.screenmatch.service.ConvertsData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //search.replace(" ", "+") +
        var address = "https://www.omdbapi.com/?t=gilmore+girls&apiKey=77e71fdc";
        var apiConsume = new ApiConsume();
        var json = apiConsume.getData(address);
        System.out.println(json);
		/*json = apiConsume.getData("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);*/
        ConvertsData convertsData = new ConvertsData();
        SerieData data = convertsData.getData(json, SerieData.class);
		System.out.println(data);

    }
}

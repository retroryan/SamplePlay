package services;

import models.Airport;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class AirportDB {

    private final ApplicationLifecycle appLifecycle;

    List<Airport> airports;


    public List<Airport> getAirports() {
        return airports;
    }

    @Inject
    public AirportDB(ApplicationLifecycle appLifecycle, Configuration conf) {
        this.appLifecycle = appLifecycle;

        //String datafile = "data/airline-flights/airports.csv";
        String datafile = conf.getString("airport.datafile");

        this.airports = readAirports(datafile);
        Logger.info("AirportDB loaded airports.  Total count:" + airports.size());

        appLifecycle.addStopHook(() -> {
            // Do something on stop
            airports = null;
            return CompletableFuture.completedFuture(null);
        });
    }

    public static List<Airport> readAirports(String datafile) {
        try {

            Stream<String> lines = Files.lines(Paths.get(datafile));

            List<Airport> airportList = lines
                    .skip(1)
                    //.peek(stringConsumer)
                    .map(line -> Airport.createAirport(line))
                    .collect(Collectors.toList());

            //airportList.forEach(System.out::println);

            return airportList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAirportFromCode(String iata) {
        return getAirports()
                .stream()
                .filter(airport -> airport.getAirportCode().contains(iata))
                .findFirst()
                .map(Airport::toString)
                .orElse("Airport Not Found");
    }

    public String DelayGetAirportFromCode(String iata) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getAirportFromCode(iata);

    }
}

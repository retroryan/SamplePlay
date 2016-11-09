package controllers;

import akka.actor.ActorRef;
import play.libs.concurrent.Futures;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import services.AirportDB;
import services.StockActor;
import services.StockWS;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static akka.pattern.Patterns.ask;

public class WSDemoController extends Controller {

    private final WSClient ws;
    private final AirportDB airportDB;
    private final StockWS stockWS;
    private ActorRef stockActor;

    @Inject
    public WSDemoController(WSClient ws, AirportDB airportDB,
                            StockWS stockWS,
                            @Named("stock-actor") ActorRef stockActor) {
        this.ws = ws;
        this.airportDB = airportDB;
        this.stockWS = stockWS;
        this.stockActor = stockActor;
    }

    /**
     *  Exercise One - Return a Future of an Async Response
     * @return
     */
    public CompletionStage<Result> getAirport() {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() ->
                airportDB.getAirportFromCode("SAZ"));

        return stringCompletableFuture.thenApply(results -> ok("FOUND AIRPORT: " + results));
    }

    /**
     * Exercise Two - Return a Future of a list of Async Response
     *
     * @return
     */
    public CompletionStage<Result> getAirports() {
        CompletableFuture<String> SLC = CompletableFuture.supplyAsync(() ->
                airportDB.getAirportFromCode("SLC"));
        CompletableFuture<String> MKC = CompletableFuture.supplyAsync(() ->
                airportDB.getAirportFromCode("MKC"));
        CompletableFuture<String> ORG = CompletableFuture.supplyAsync(() ->
                airportDB.getAirportFromCode("ORG"));
        CompletableFuture<String> SAZ = CompletableFuture.supplyAsync(() ->
                airportDB.getAirportFromCode("SAZ"));

        CompletionStage<List<String>> sequence = Futures.sequence(SLC, MKC, ORG, SAZ);

        return Futures.sequence(SLC, MKC, ORG, SAZ).thenApply(foundAirports -> {
            String allAirports = foundAirports.stream()
                    .collect(Collectors.joining(", "));
            return ok("Got result: " + allAirports);
        });
    }

    /**
     * Exercise Three - Call a web service to retrieve a url
     *
     * @return
     */
    public CompletionStage<Result> getWebsite() {
        WSRequest url = ws.url("https://www.playframework.com/documentation/2.4.x/JavaSessionFlash");
        CompletionStage<WSResponse> wsResponseCompletionStage = url.get();
        return wsResponseCompletionStage.thenApply(results -> ok(results.getBody()));
    }

    /**
     *   Exercise Four - Use the Stock Web Service to fetch a list of tweets
     * @return
     */
    public CompletionStage<Result> fetchTweets() {
        CompletionStage<List<String>> tsla = stockWS.fetchTweets("TSLA");
        return tsla.thenApply(results -> {
            String finalString = results.stream().reduce("", (s, s2) -> s + "\n " + s2);
            return ok(finalString);
        });
    }

    /**
     *   Exercise Seven - Call the Actor and return the future of the results:
     * @return
     */
    public CompletionStage<Result> callActor() {
        return FutureConverters.toJava(ask(stockActor,
                new StockActor.SayHi("TSLA"), 1000)
        ).thenApply(response -> ok((String) response));
    }

    public CompletionStage<Result> fetchTweetsFromActor() {
        return FutureConverters.toJava(ask(stockActor,
                new StockActor.GetTweet("TSLA"), 10000)
        ).thenApply(response -> ok((String) response));
    }

}

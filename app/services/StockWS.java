package services;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class StockWS {

    public static String TWEETS_URL = "http://twitter-search-proxy.herokuapp.com/search/tweets";
    private WSClient ws;


    @Inject
    public StockWS(WSClient ws) {
        this.ws = ws;
    }

    public CompletionStage<List<String>> fetchTweets(String symbol) {

        WSRequest request = ws.url(TWEETS_URL)
                .setQueryParameter("q", symbol);

        CompletionStage<List<String>> stringCompletionStage = request.get().thenApply(results -> {
            if (results.getStatus() == Http.Status.OK) {
                JsonNode statuses = results.asJson().findPath("statuses");
                List<JsonNode> text = statuses.findValues("text");

                List<String> statusTextList = text.stream()
                        .map(JsonNode::toString)
                        .filter(tweet -> !tweet.substring(1,tweet.length()-1).equalsIgnoreCase(symbol))
                        .collect(Collectors.toList());

                return statusTextList;
            } else {
                return Collections.singletonList("NOT FOUND");
            }
        });

        return stringCompletionStage;
    }

    public CompletionStage<String> fetchFirstTweet(String symbol) {

        WSRequest request = ws.url(TWEETS_URL)
                .setQueryParameter("q", symbol);

        CompletionStage<String> stringCompletionStage = request.get().thenApply(results -> {
            if (results.getStatus() == Http.Status.OK) {
                JsonNode statuses = results.asJson().findPath("statuses");
                return statuses.findValue("text").asText();
            } else {
                return "NOT FOUND";
            }
        });

        return stringCompletionStage;
    }
}

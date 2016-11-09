package services;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class StockActor extends UntypedActor {

    public static String TWEETS_URL = "http://twitter-search-proxy.herokuapp.com/search/tweets";
    private final WSClient ws;
    private final ActorSystem actorSystem;
    private final StockWS stockWS;

    @Inject
    public StockActor(WSClient ws, ActorSystem actorSystem, StockWS stockWS) {
        this.ws = ws;
        this.actorSystem = actorSystem;
        this.stockWS = stockWS;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SayHi) {
            String senderMessage = ((SayHi) message).message;
            sender().tell("echo ..." + senderMessage, self());
        }
        else if (message instanceof GetTweet) {
            String symbol = ((GetTweet) message).symbol;
            CompletionStage<String> futureFetchTweets = stockWS.fetchFirstTweet(symbol);
            akka.pattern.PatternsCS.pipe(futureFetchTweets, actorSystem.dispatcher()).to(sender());


        }
    }

    public static final class GetTweet {
        public final String symbol;

        public GetTweet(String symbol) {
            this.symbol = symbol;
        }
    }

    public static final class SayHi {

        private final String message;

        public SayHi(String message) {
            this.message = message;
        }
    }
}

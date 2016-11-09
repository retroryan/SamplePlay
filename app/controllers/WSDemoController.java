package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

public class WSDemoController extends Controller {

    @Inject
    public WSDemoController() {
    }

    /**
     *  Exercise One - Return a Future of an Async Response
     * @return
     */
    public CompletionStage<Result> getAirport() {
        return null;
    }

    /**
     * Exercise Two - Return a Future of a list of Async Response
     *
     * @return
     */
    public CompletionStage<Result> getAirports() {
        return null;
    }

    /**
     * Exercise Three - Call a web service to retrieve a url
     *
     * @return
     */
    public CompletionStage<Result> getWebsite() {
        return null;
    }

    /**
     *   Exercise Four - Use the Stock Web Service to fetch a list of tweets
     * @return
     */
    public CompletionStage<Result> fetchTweets() {
        return null;    }

    /**
     *   Exercise Seven - Call the Actor and return the future of the results:
     * @return
     */
    public CompletionStage<Result> callActor() {
        return null;
    }

    public CompletionStage<Result> fetchTweetsFromActor() {
        return null;
    }

}

package controllers;

import play.Logger;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class VerboseAction extends play.mvc.Action.Simple {
    public CompletionStage<Result> call(Http.Context ctx) {

        Logger.info("Calling action for {}", ctx);

        CompletionStage<Result> call = delegate.call(ctx);

        return call.thenApply(result -> {
            String toString = result.body().toString();
            System.out.println("toString = " + toString);
            return ok("MODIFIED RESULTS");
        });
    }
}
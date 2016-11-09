import play.Configuration;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.http.HttpErrorHandler;
import play.libs.F;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static play.mvc.Results.badRequest;

@Singleton
public class NOT_USED_ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public NOT_USED_ErrorHandler(Configuration configuration, Environment environment, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    @Override
    protected  CompletionStage<Result> onNotFound(Http.RequestHeader requestHeader, String s) {
        return CompletableFuture.completedFuture(badRequest(views.html.notFound.render(requestHeader.uri())));
    }
}
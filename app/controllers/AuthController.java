package controllers;

import models.LoginData;
import play.Configuration;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class AuthController extends Controller {


    private static String AUTH_SERVICE_URL;
    private WSClient ws;

    public class SessionKeys {
        public static final String USERNAME = "username";
        public static final String AUTH_TOKEN = "token";
    }

    public enum AuthResult {
        OK, ERROR;
    }

    @Inject
    public AuthController(Configuration conf, WSClient ws) {
        this.AUTH_SERVICE_URL = conf.getString("service.auth.url");
        this.ws = ws;
    }

    public Result index() {
        return ok(views.html.authindex.render(session(), "Welcome"));
    }

    public CompletionStage<Result> authlogin() {
        LoginData loginData = new LoginData("user-5","pass");
        return authenticate(loginData, session());
    }

    public CompletionStage<Result> badlogin() {
        LoginData loginData = new LoginData("user-05","bad");
        return authenticate(loginData, session());
    }

    public CompletionStage<Result> badlogin2() {
        LoginData loginData = new LoginData("user-5","bad");
        return authenticate(loginData, session());
    }

    public Result authlogout() {
        session().clear();
        return ok(views.html.authindex.render(session(), "Logged Out"));
    }

    private CompletionStage<Result> authenticate(LoginData loginData, Http.Session localSession) {

        System.out.println("AuthController.authenticate");
        System.out.println("loginData = " + loginData.getUsername());

        WSRequest wsRequest = ws.url(AUTH_SERVICE_URL + "/auth");
        CompletionStage<WSResponse> post = wsRequest.post(Json.toJson(loginData));
        System.out.println("wsResponseCompletionStage post= " + post);

        CompletionStage<Map<AuthResult, String>> mapCompletionStage = post.thenApply(wsResponse -> {
            Map<AuthResult, String> result = new HashMap();
            if (wsResponse.getStatus() == OK) {
                result.put(AuthResult.OK, wsResponse.asJson().findPath("token").textValue());
            } else {
                result.put(AuthResult.ERROR, wsResponse.asJson().findPath("error").textValue());
            }
            System.out.println("wsResponse.asJson() = " + wsResponse.asJson());
            return result;
        });



        CompletionStage<Result> result = mapCompletionStage.thenApply(authResultStringMap -> {
            System.out.println("authResultStringMap = " + authResultStringMap);
            if (authResultStringMap.containsKey(AuthResult.OK)) {
                localSession.put(SessionKeys.USERNAME, loginData.getUsername());
                localSession.put(SessionKeys.AUTH_TOKEN, authResultStringMap.get(AuthResult.OK));
                return redirect(routes.AuthController.index());
            } else {
                localSession.clear();
                return badRequest(views.html.authindex.render(localSession, authResultStringMap.get(AuthResult.ERROR)));
            }
        });

        System.out.println("result = " + result);

        return result;

    }
}

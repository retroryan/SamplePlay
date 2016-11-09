package controllers;


import models.Airport;
import play.Logger;
import play.cache.Cached;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.AirportDB;

import javax.inject.Inject;

@With(VerboseAction.class)
public class DemosController extends Controller {

    private final AirportDB airportDB;
    private String FAVORITE_AIRPORT = "favorite_airport";

    @Inject
    public DemosController(AirportDB airportDB) {
        this.airportDB = airportDB;
    }

    @With(VerboseAction.class)
    @Cached(key = "index.result", duration=1000)
    public Result index() {
        if (session().containsKey(FAVORITE_AIRPORT)) {
            String iata = session(FAVORITE_AIRPORT);
            String foundAirport = airportDB.getAirportFromCode(iata);
            return ok(views.html.demo.render(foundAirport));
        }
        else {
            return ok(views.html.demo.render("Call findAirport First"));
        }
    }

    public Result findAirport(String iata) {
        String foundAirport = airportDB.getAirportFromCode(iata);
        session("favorite_airport", iata);
        return ok(views.html.airport.render(foundAirport));
    }

    public Result clear() {
        session().remove(FAVORITE_AIRPORT);
        return redirect(routes.DemosController.index());
    }
}

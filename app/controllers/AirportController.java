package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Airport;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AirportDB;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AirportController extends Controller {

    private final AirportDB airportDB;

    @Inject
    public AirportController(AirportDB airportDB) {
        this.airportDB = airportDB;
    }

    /**
     * ROUTES EXAMPLES
     **/
    public Result airports() {
        List<String> listOfAirports = getListOfAirports(10);
        return ok(views.html.airport.render(listOfAirports.toString()));
    }

    public Result getAirports(String str) {
        Integer count = Integer.parseInt(str);
        Logger.info("getting " + count + " airports");
        List<String> listOfAirports = getListOfAirports(count);
        return ok(views.html.airport.render(listOfAirports.toString()));
    }

    public List<String> getListOfAirports(int count) {
        return airportDB.getAirports()
                .stream()
                .limit(count)
                .map(Airport::toString)
                .collect(Collectors.toList());
    }

    public Result findAirport(String iata) {
        Logger.info("searching for iata = " + iata);
        String foundAirport = airportDB.getAirportFromCode(iata);
        return ok(views.html.airport.render(foundAirport));
    }

    public Result findAirportCodes(String iatas) {
        String[] iatasList = iatas.split(",");
        Logger.info("searching for iatas = " + iatasList);
        return ok(views.html.airport.render("searching for iatas = " + Arrays.toString(iatasList)));
    }

    /** TEMPLATE EXAMPLES **/
    public Result getAP2(Integer count) {
        List<Airport> listOfAirports = getAirportsAsAirports(count);
        return ok(views.html.ap2.render(listOfAirports));
    }

    public Result getAP3(Integer count) {
        List<Airport> listOfAirports = getAirportsAsAirports(count);
        return ok(views.html.ap3.render(listOfAirports));
    }

    public Result tagtest() {
        return ok(views.html.tagtest.render());
    }


    private List<Airport> getAirportsAsAirports(Integer count) {
        return airportDB.getAirports()
                    .stream()
                    .limit(count)
                    .collect(Collectors.toList());
    }

    /**  FORM EXAMPLES with JSON BINDING **/

    public static class Ingredient {
        public String id;
        public String type;

        @Override
        public String toString() {
            return "Ingredient{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result jsonTest() {
        Http.RequestBody body = request().body();
        JsonNode jsonNode = body.asJson();

        JsonNode batter = jsonNode.get("batter");
        Iterator<JsonNode> elements = batter.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            Ingredient ingredient = Json.fromJson(next, Ingredient.class);
            System.out.println("ingredient = " + ingredient);
        }
        return ok("Got json: " + jsonNode);
    }
    /***
     *   curl --header "Content-type: application/json" --request POST --data '{"batter":[{ "id": "1001", "type": "Regular" },{ "id": "1002", "type": "Chocolate" },{ "id": "1003", "type": "Blueberry" },{ "id": "1004", "type": "Devil's Food" }]}' http://localhost:8080/jsonTest
     */

    @BodyParser.Of(BodyParser.Json.class)
    public Result jsonPerson() {
        JsonNode json = request().body().asJson();
        String name = json.findPath("name").textValue();
        if(name == null) {
            return badRequest("Missing parameter [name]");
        } else {
            ObjectNode result = Json.newObject();
            result.put("exampleField1", "foobar");
            result.put("exampleGreeting", "Hello " + name);
            return ok(result);
        }
    }

    public Result getAirportsJson() {
        List<Airport> airportsAsAirports = getAirportsAsAirports(10);
        return ok(Json.toJson(airportsAsAirports));
    }

}

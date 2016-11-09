package models;

public class Airport {

    //iata
    private String airportCode;
    private String airportName;
    private String city;
    private String state;
    private String country;

    //lat and long
    private Double lat;
    private Double lng;


    public Airport(String airportCode, String airportName, String city, String state, String country, Double lat, Double lng) {
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
    }

    public static Airport createAirport(String rawString) {
        String[] split = rawString.split(",");
        return new Airport(getRawString(split[0]), getRawString(split[1]), getRawString(split[2]), getRawString(split[3]), getRawString(split[4]), Double.parseDouble(split[5]), Double.parseDouble(split[6]));
    }

    public static String getRawString(String strWithParens) {
        if (strWithParens.length() > 0) return strWithParens.replaceAll("\"", "");
        else return strWithParens;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportCode='" + airportCode + '\'' +
                ", airportName='" + airportName + '\'' +
                ", city=" + city +
                ", state=" + state +
                ", country='" + country + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }
}

public class Stations {
    private final String station;
    private final String line;

    public Stations(String station, String line) {
        this.station = station;
        this.line = line;
    }

    public String getStation() {
        return station;
    }

    public String getLine() {
        return line;
    }
}

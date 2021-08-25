import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String URL = "https://www.moscowmap.ru/metro.html#lines";
    private static final String DATA_FILE = "src/main/resources/new.json";

    public static void main(String[] args) throws Exception {
        try {
            Document document = Jsoup.connect(URL).maxBodySize(0).get();

            Elements lineElements = document.getElementsByAttributeValueStarting("class",
                    "js-metro-line t-metrostation-list-header t-icon-metroln ln");
            //С сайта код - <span class="js-metro-line t-metrostation-list-header t-icon-metroln
            // ln-2" data-line="2">Замоскворецкая линия</span>

            Elements stationElements = document.select("div.js-metro-stations.t-metrostation-list-table");
            //С сайта код - <div class="js-metro-stations t-metrostation-list-table" data-line="1" style="" +
            //"grid-template-rows: repeat(13,36px);"><p><a data-metrost="1,32,1,10,43,0" data-id="1"


            JSONObject objectForJson = new JSONObject();
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
            objectForJson.put("stations", getObjectStation(stationElements));
            objectForJson.put("lines", getArrayLine(lineElements));
            writer.write(objectForJson.toJSONString());
            writer.close();


            getNumberOfStations();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static JSONArray getArrayLine(Elements lineElements) {
        JSONArray lines = new JSONArray();
        ArrayList<Lines> linesList = new ArrayList<>();

        for (Element elementLine : lineElements) {
            String nameLine = elementLine.text();
            String numberLine = elementLine.attr("data-line");
            linesList.add(new Lines(numberLine, nameLine));
        }

        for (Lines h : linesList) {
            JSONObject lines1 = new JSONObject();
            lines1.put("number", h.getNumber());
            lines1.put("name", h.getName());
            lines.add(lines1);
        }
        return lines;
    }


    private static JSONObject getObjectStation(Elements stationElements) {
        ArrayList<Stations> stList = new ArrayList<>();

        for (Element element : stationElements) {
            String numberLine = element.attr("data-line");
            Elements nameStationElement = element.select("span.name");
            //System.out.println("Линия " + numberLine + " Количество станций на данной линии: " + nameStationElement.size());
            for (Element elementStation : nameStationElement) {
                String nameStation = elementStation.text();
                stList.add(new Stations(nameStation, numberLine));
            }
        }

        HashMap<String, List<String>> stationMap = stList.stream()
                .collect(Collectors.groupingBy(Stations::getLine
                        , HashMap::new
                        , Collectors.mapping(
                                Stations::getStation,
                                Collectors.toList())));
        return new JSONObject(stationMap);
    }


    private static void getNumberOfStations() throws Exception {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(DATA_FILE));
        JSONObject metroJsonObject = (JSONObject) object;
        JSONObject stationsObj = (JSONObject) metroJsonObject.get("stations");
        stationsObj.keySet().forEach(o -> {
            JSONArray stationsArray = (JSONArray) stationsObj.get(o);
            System.out.println("Линия " + o + " Количество станций на данной линии: " + stationsArray.size());
        });
    }


    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}



//Elements connectionsElements = document.select("div.js-metro-stations.t-metrostation-list-table");
//objectForJson.put("connections", getArrayConnections(connectionsElements));
package basavets.connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class Connection {

    private final HashMap<String, String> currency;

    public Connection(HashMap<String, String> currency) {
        this.currency = currency;
    }


    public void setCurrency() {
        Document document = null;
        try {
            String URL = "https://www.nbrb.by/statistics/rates/ratesdaily.asp";
            document = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements1 = null;
        Elements elements2 = null;
        if (document != null) {
            elements1 = document.getElementsByAttributeValueStarting("class", "curName");
            elements2 = document.getElementsByAttributeValueStarting("class", "curCours");
        }

        ArrayList<String> nameCurrency = new ArrayList<>();
        if (elements1 != null) {
            elements1.forEach(element -> nameCurrency.add(element.text()));
        }
        ArrayList<String> numberCurrency = new ArrayList<>();
        if (elements2 != null) {
            elements2.forEach(element -> numberCurrency.add(element.text()));
        }

        for (int i = 0; i < nameCurrency.size(); i++) {
            for (int j = i; ; ) {
                currency.put(nameCurrency.get(i), numberCurrency.get(j));
                break;
            }
        }
    }

    public HashMap<String, String> getCurrency() {
        return currency;
    }
}

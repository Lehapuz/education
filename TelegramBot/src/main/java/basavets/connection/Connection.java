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
    private final HashMap<String, String> hashCurrency;

    public Connection(HashMap<String, String> currency, HashMap<String, String> hashCurrency) {
        this.currency = currency;
        this.hashCurrency = hashCurrency;
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
        Elements elements3 = null;
        if (document != null) {
            elements1 = document.getElementsByAttributeValueStarting("class", "curName");
            elements2 = document.getElementsByAttributeValueStarting("class", "curCours");
            elements3 = document.getElementsByAttributeValueStarting("class", "curAmount");
        }

        ArrayList<String> nameCurrency = new ArrayList<>();
        if (elements1 != null) {
            elements1.forEach(element -> nameCurrency.add(element.text()));
        }
        ArrayList<String> numberCurrency = new ArrayList<>();
        if (elements2 != null) {
            elements2.forEach(element -> numberCurrency.add(element.text()));
        }
        ArrayList<String> amountCurrency = new ArrayList<>();
        if (elements3 != null) {
            elements3.forEach(element -> amountCurrency.add(element.text()));
        }

        for (int i = 0; i < amountCurrency.size(); i++) {
            currency.put(nameCurrency.get(i), numberCurrency.get(i));
            hashCurrency.put(numberCurrency.get(i), amountCurrency.get(i));
        }
    }

    public HashMap<String, String> getCurrency() {
        return currency;
    }

    public HashMap<String, String> getHashCurrency() {
        return hashCurrency;
    }
}

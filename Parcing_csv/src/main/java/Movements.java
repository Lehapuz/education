import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movements {
    private double income;
    private double outcome;
    private final Map<String, Double> outcomeList = new HashMap<>();

    public Movements(String pathMovementsCsv) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(pathMovementsCsv));
            lines.remove(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : lines) {
            String[] fragments;
            fragments = line.split(",");
            if (fragments.length == 8) {
                if (fragments[7].equals("0")) {
                    double incomeTmp = Double.parseDouble(fragments[6]);
                    income = incomeTmp + income;
                }
                if (fragments[6].equals("0")) {
                    double outcomeTmp = Double.parseDouble(fragments[7]);
                    outcome = outcomeTmp + outcome;
                    outcomeList.put(fragments[5], Double.parseDouble(fragments[7]));
                }
            }

            if (fragments.length != 8) {
                if (fragments[8].equals("0")) {
                    String text = fragments[6].replaceAll("\"", "") + "." + fragments[7].replaceAll("\"", "");
                    double incomeTmp = Double.parseDouble(text);
                    income = incomeTmp + income;
                }
                if (fragments[6].equals("0")) {
                    String text = fragments[7].replaceAll("\"", "") + "." + fragments[8].replaceAll("\"", "");
                    double outcomeTmp = Double.parseDouble(text);
                    outcome = outcomeTmp + outcome;
                    outcomeList.put(fragments[5], Double.parseDouble(text));
                }
            }
        }
    }

    public double getExpenseSum() {
        return outcome;
    }

    public double getIncomeSum() {
        return income;
    }

    @Override
    public String toString() {

        return "Сумма расходов: " + getExpenseSum() + " руб" + "\n"
                + "Сумма доходов: " + getIncomeSum() + " руб";
    }

    public void Print() {

        System.out.println("Суммы расходов по организациям:");
        for (String outcome : outcomeList.keySet()) {
            Pattern pattern = Pattern.compile("[A-Za-z].+?\\s{3}");
            Matcher matcher = pattern.matcher(outcome);
            while (matcher.find()) {
                System.out.println(outcome.substring(matcher.start(), matcher.end()) + "\t" + outcomeList.get(outcome) + " руб.");
            }
        }
    }
}


//fragments = line.split("[,]", 8);
//if (fragments[6].equals("0")) {
//income += 0;
//} else {
//double incomeTmp = Double.parseDouble(fragments[6].replaceAll("\"", "").replace(',','.').replaceAll("\"", ""));
//income += incomeTmp;
//}
//if (fragments[7].equals("0")) {
//outcome += 0;
//} else {
//double outcomeTmp = Double.parseDouble(fragments[7].replaceAll("\"", "").replace(',','.'));
//outcome += outcomeTmp;
//outcomeList.put(fragments[5],Double.parseDouble(fragments[7].replaceAll("\"", "").replace(',','.')));
//}

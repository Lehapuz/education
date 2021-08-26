public class Main {
    public static void main(String[] args) {
        String FILE = "src/test/resources/movementList.csv";
        Movements movements = new Movements(FILE);
        System.out.println(movements.toString());
        movements.Print();
    }
}

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Game
{
    private final static String JSON_FILE_NAME = "src/plants.json";

    private final Scanner scanner;

    private final List<Plant> plants;

    private int cash;
    public MySQLCache cache;

    public static void main(String[] args) throws FileNotFoundException
    {
        List<Plant> plantList = jsonToPlants(JSON_FILE_NAME);
        for (int i = 0; i < plantList.size(); i++) {
            System.out.println((i + 1) + " " + plantList.get(i));
        }

        new Game(jsonToPlants(JSON_FILE_NAME), 100, 8).start();
    }

    private static List<Plant> jsonToPlants(String fileName) throws FileNotFoundException {
        return Arrays.asList(new Gson().fromJson(new FileReader(fileName), Plant[].class));
    }

    public Game(List<Plant> plants, int cash, int fieldSize) {

        this.plants = plants;
        this.scanner = new Scanner(System.in);
        cache = new MySQLCache(cash, fieldSize);
        this.cash = cache.getCash();
    }

    public void start() {
        while (true) {
//            cash = cache.getCash();
            System.out.println(cache.getFields().values());
            System.out.println("YOUR CASH : " + cash);
            System.out.println("Enter cell for your plant (1-8)");
            validField(scanner.nextLine()).ifPresent(fieldNumber -> {
                Field field = cache.getFields().get(fieldNumber);
                System.out.println("Enter plants number (1-" + plants.size() + ")");
                if (field.isEmpty()) {
                    validPlant(scanner.nextLine()).ifPresent(plantNumber -> {
                        Plant plant = plants.get(plantNumber);
                        execute(() -> getHarvest(fieldNumber), plant.getTime());
                        cash -= plant.getSeedPrice();
                        cache.getFields().put(fieldNumber, field.updateField(plant));
                        cache.addPlant(fieldNumber, field);
                        cache.addCash(cash);
                    });
                } else {
                    cash += field.getHarvestPrice();
                    cache.getFields().put(fieldNumber, field.updateField());
                    cache.addPlant(fieldNumber, field);
                }
            });
        }
    }

    private Optional<Integer> validPlant(String userInput) {
        try{
            Integer number = Integer.valueOf(userInput)-1;
            if (number < 0 || number >= plants.size()) return printAndReturnOptional("OLOLO! YOU WERE ABROAD");
            if (cash < plants.get(number).getSeedPrice()) return printAndReturnOptional("YOU HAVEN'T ENOUGH MONEY");
            return Optional.of(number);
        }catch(NumberFormatException e){
            return printAndReturnOptional("OLOLO! INCORRECT INPUT");
        }
    }

    private Optional<Integer> validField(String userInput) {
        try{
            Integer number = Integer.valueOf(userInput)-1;
            if (number < 0 || number >= cache.getFields().size()) return printAndReturnOptional("OLOLO! YOU WERE ABROAD");
            if (cache.getFields().get(number).isFull()) return printAndReturnOptional("OLOLO! FIELD NOT EMPTY");
            return Optional.of(number);
        }catch(NumberFormatException e){
            return printAndReturnOptional("OLOLO! INCORRECT INPUT");
        }
    }

    private Optional printAndReturnOptional(String arg) {
        System.out.println(arg);
        return Optional.empty();
    }

    public void getHarvest(int fieldNumber) {
        cache.getFields().put(fieldNumber, cache.getFields().get(fieldNumber).updateField());
        cache.addPlant(fieldNumber, cache.getFields().get(fieldNumber));

        System.out.println(cache.getFields().values());
    }

    private void execute(Runnable task, long delaySec) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
                timer.cancel();
            }
        };
        timer.schedule(timerTask, delaySec * 1000);
    }
}

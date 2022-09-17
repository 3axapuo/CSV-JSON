import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String CSV_FILE_PATH = "CSV-XML-JSON/src/main/resources/log.csv";
    private static final String JSON_FILE_PATH = "CSV-XML-JSON/src/main/resources/JSON/basket.json";
    static Basket basket;
    static ClientLog clientLog;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        clientLog = new ClientLog();
        clientLog.importAsCSV(new File(CSV_FILE_PATH)); // импортируем сохраненный журнал
        basket = new Basket(setProduct(), setPrices()); // создаем продукты и цены по умолчанию

        // ЗАДАЧА №1, Также вместо вызова метода saveTxt в методе main сериализуйте корзину
        // в json-формате в файл basket.json
        JSONObject objBasket = new JSONObject();
        objBasket.put("products", "Продукты");
        objBasket.put("amount", "Количество");

        JSONArray products = new JSONArray(); // JSON, массив приобретенных продуктов
        JSONArray amount = new JSONArray(); // JSON, массив количество приобретенных продуктов

        File textFile = new File(JSON_FILE_PATH);
        if (textFile.exists()) { // если файл существуют, считываем их
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(JSON_FILE_PATH));
                JSONObject jsonObject = (JSONObject) obj;
                products = (JSONArray) jsonObject.get("products");
                amount = (JSONArray) jsonObject.get("amount");
                System.out.println("Ваша корзина загружена и составляет: ");
                for (int i = 0; i < products.size(); i++) { // заполняем корзину
                    basket.addToCart(Integer.parseInt(products.get(i).toString()),
                            Integer.parseInt(amount.get(i).toString()));
                }
            } catch (IOException e) {
                System.out.println("⊠ ОШИБКА: Файл " + JSON_FILE_PATH + " не найден!");
            } catch (ParseException e) {
                System.out.println("⊠ ОШИБКА: Во время чтения данных из " + JSON_FILE_PATH + " произошла ошибка!");
            }
        }
        System.out.println("Список возможных товаров для покупки: ");
        for (int i = 1; i < basket.products.length + 1; i++) { // Выводим список товаров и их цены.
            System.out.println(i + ". " + basket.products[i - 1] + " " + basket.prices[i - 1] + "руб/шт.");
        }
        while (true) {
            System.out.println("\n☐ Выберите товар и количество или введите `end` ");
            try {
                String input = scanner.nextLine();
                if ("end".equals(input)) break; // проверка на выход
                String[] addCart = input.split(" "); // создаем массив, кладем туда строки раздельно до и после пробела
                int selectProduct = Integer.parseInt(addCart[0].trim());
                int selectCount = Integer.parseInt(addCart[1].trim());
                if ((selectProduct != 0) && (selectCount != 0))
                    basket.addToCart((selectProduct) - 1, selectCount);
                products.add((selectProduct) - 1); // JSON, добавляем строку продукт
                amount.add(selectCount); // JSON, добавляем строку количество
                clientLog.log((selectProduct) - 1, selectCount);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("⊠ ОШИБКА: Вы не выбрали товар или количество!");
            }
        }
        basket.printCart(); // метод вывода на экран покупательской корзины.
        objBasket.put("products", products); // JSON, добавляем массив продуктов
        objBasket.put("amount", amount); // JSON, добавляем массив количества продуктов

        try (FileWriter file = new FileWriter(JSON_FILE_PATH)) {
            file.write(objBasket.toJSONString());
            objBasket.clear();
        } catch (IOException e) {
            System.out.println("⊠ ОШИБКА: Сохранить текущую корзину в файл " + JSON_FILE_PATH + " не удалось!");
        }
        clientLog.exportAsCSV(new File(CSV_FILE_PATH)); // CSV, создаем файл журнала
        scanner.close();
    }

    public static String[] setProduct() {
        String[] products = new String[3];
        products[0] = "Хлеб";
        products[1] = "Яблоки";
        products[2] = "Молоко";
        return products;
    }

    public static int[] setPrices() {
        int[] prices = new int[3];
        prices[0] = 100;
        prices[1] = 200;
        prices[2] = 300;
        return prices;
    }
} // class Main

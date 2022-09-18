import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String XML_FILE_PATH = "shop.xml"; // ЗАДАЧА №2, В корне вашего проекта разместите: shop.xml.

    static Basket basket;
    static ClientLog clientLog;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        Scanner scanner = new Scanner(System.in);
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); // создаем строителя документа
        Document docXML = documentBuilder.parse(XML_FILE_PATH); // создаем дерево DOM документа из файла

        Settings settings = new Settings(3, 3, 2);
        settings.read(docXML.getDocumentElement()); // передаем в класс считывание настроек (значений)

        clientLog = new ClientLog();
        clientLog.importAsCSV(new File(settings.log[1])); // импортируем сохраненный журнал
        basket = new Basket(setProduct(), setPrices()); // создаем продукты и цены по умолчанию

        // ЗАДАЧА №1, Также вместо вызова метода saveTxt в методе main сериализуйте корзину
        // в json-формате в файл basket.json
        JSONObject objBasket = new JSONObject();
        objBasket.put("products", "Продукты");
        objBasket.put("amount", "Количество");

        JSONArray products = new JSONArray(); // JSON, массив приобретенных продуктов
        JSONArray amount = new JSONArray(); // JSON, массив количество приобретенных продуктов

        File textFile = new File(settings.load[1]);
        if (textFile.exists() && settings.load[0].equals("true")) { // если файл существуют И верна настройка, считываем корзину
            if (settings.load[2].equals("json")) {
                JSON json = new JSON();
                amount = json.createJSON(new JSONParser(), settings, products, amount, basket);
            }
            if (settings.load[2].equals("text")) {
                basket = basket.loadFromTxtFile(textFile);
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

        if (settings.save[2].equals("json")) {
            try (FileWriter file = new FileWriter(settings.save[1])) {
                file.write(objBasket.toJSONString());
                objBasket.clear();
            } catch (IOException e) {
                System.out.println("⊠ ОШИБКА: Сохранить текущую корзину в файл " + settings.save[1] + " не удалось!");
            }
        }
        if (settings.save[2].equals("text")) {
            basket.saveTxt(textFile);
        }
        if (settings.log[0].equals("true"))
            clientLog.exportAsCSV(new File(settings.log[1])); // CSV, создаем файл журнала
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

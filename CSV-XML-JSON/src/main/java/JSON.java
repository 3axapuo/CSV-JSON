import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSON {

    public JSONArray createJSON(JSONParser parser, Settings settings, JSONArray products, JSONArray amount, Basket basket) {
        try {
            Object obj = parser.parse(new FileReader(settings.load[1]));
            JSONObject jsonObject = (JSONObject) obj;
            products = (JSONArray) jsonObject.get("products");
            amount = (JSONArray) jsonObject.get("amount");
            System.out.println("Ваша корзина загружена и составляет: ");
            for (int i = 0; i < products.size(); i++) { // заполняем корзину
                basket.addToCart(Integer.parseInt(products.get(i).toString()),
                        Integer.parseInt(amount.get(i).toString()));
            }
        } catch (IOException e) {
            System.out.println("⊠ ОШИБКА: Файл " + settings.load[1] + " не найден!");
        } catch (ParseException e) {
            System.out.println("⊠ ОШИБКА: Во время чтения данных из " + settings.load[1] + " произошла ошибка!");
        }
        return amount;
    }
}

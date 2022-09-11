import java.io.File;
import java.util.Scanner;

public class Main {
    static Basket basket;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        File textFile = new File("basket.txt");
        if (textFile.exists()) {
            basket = Basket.loadFromTxtFile(textFile);
        } else {
            basket = new Basket(setProduct(), setPrices());
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
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("⊠ ОШИБКА: Вы не выбрали товар или количество!");
                continue;
            }
        }
        basket.printCart();
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

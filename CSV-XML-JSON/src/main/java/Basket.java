import java.io.*;
import java.util.Arrays;

// Создайте класс Basket, объект которого будет представлять себе покупательскую корзину.
public class Basket implements Serializable { // Класс Basket реализует (implement) интерфейс Serializable, который необходим для записи в ObjectOutputStream.
    protected String[] products; // массив продуктов
    protected int[] prices; // массив цен продуктов
    protected int[] cart;


    // конструктор, принимающий массив цен и названий продуктов
    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length]; // количество продуктов = список продуктов
    }

    public void addToCart(int productNum, int amount) { // метод добавления amount штук продукта номер productNum в корзину;
        if (productNum > (products.length)) { // если выбранный товар больше списка товаров то
            System.out.println("⊠ ОШИБКА: Такого товара или количества не существует! Ваши вводные данные " + Arrays.toString(cart));
        } else {
            cart[productNum] += amount; // записываем в массив выбранный товар и его количество
            System.out.println("☑ Товар '" + products[productNum] + "' в количестве " + amount + " добавлен в корзину!");
        }

        //try {
        //saveTxt(new File("basket.txt"));
        saveBin(new File("basket.bin"));
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
    }

    public void printCart() { // метод вывода на экран покупательской корзины.
        int sumProducts = 0; // итоговая суммы корзины
        System.out.println("Список покупок:");
        System.out.println("\n☑ Ваша корзина составляет: ");
        for (int i = 1; i < products.length + 1; i++) { // Выводим список выбранных товаров и их цены.
            if (cart[i - 1] != 0) {
                System.out.println(i + ". "
                        + products[i - 1] + " - "
                        + cart[i - 1] + "шт., по "
                        + prices[i - 1] + "руб/шт., на общую сумму: "
                        + (cart[i - 1] * prices[i - 1]));
            }
            sumProducts += cart[i - 1] * prices[i - 1]; //общая сумма, выбранный товар умножаем на цену
        }
        System.out.println("Итого: " + sumProducts);
    }

    // Добавьте метод saveBin(File file) для сохранения в файл в бинарном формате.
    public void saveBin(File file) {
        try (OutputStream fileOut = new FileOutputStream(file)) {
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(Basket.this);
        } catch (IOException e) {
            System.out.println("Сохранить текущую корзину в файл \"basket.bin\" не удалось!");
        }
    }

    // Добавьте метод static loadFromBinFile(File file) для загрузки корзины из бинарного файла.
    public static Basket loadFromBinFile(File file) {
        try (InputStream fileIn = new FileInputStream(file)) {
            ObjectInputStream ObjectIn = new ObjectInputStream(fileIn);
            return (Basket) ObjectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Загрузить сохраненную корзину из файла \"basket.bin\" не удалось!");
        }
        return null;
    }

    // метод сохранения корзины в текстовый файл;
    // использовать встроенные сериализаторы нельзя;
    public void saveTxt(File textFile) {
        try (PrintWriter out = new PrintWriter(textFile)) {
            out.print("Продукт, количество, цена \n");
            for (int i = 0; i < cart.length; i++) {
                out.print(products[i] + "," + cart[i] + "," + prices[i] + "\n");
            }

        } catch (
                IOException e) {
            System.out.println("Сохранить текущую корзину в файл \"basket.txt\" не удалось!");
        }
    }

    // статический(!) метод восстановления объекта корзины из текстового файла,
    // в который ранее была она сохранена;
    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line = reader.readLine();
            line = reader.readLine(); // пропуск первой строки (информационной)  // reader.skip(25);
            String[] product = new String[0]; // новый массив для продуктов
            int[] cart = new int[0]; // новый массив для количества продуктов
            int[] price = new int[0]; // новый массив для цен продуктов
            while (line != null) { // обход всех (целых) строк
                String[] lineValues = line.split(","); // разделяем строку на значения
                for (int value = 0; value < lineValues.length; value++) { // обходим значения строки
                    if (value == 0)
                        product = addArrayString(product.length, product, String.valueOf(lineValues[value]));
                    if (value == 1) cart = addArrayInt(cart.length, cart, Integer.parseInt(lineValues[value]));
                    if (value == 2) price = addArrayInt(price.length, price, Integer.parseInt(lineValues[value]));
                }
                line = reader.readLine(); // переход на следующую строку
            }
            Basket basket = new Basket(product, price); // создаем корзину из полученных массивов
            basket.cart = cart;
            return basket;
        } catch (
                IOException e) {
            System.out.println("Загрузить сохраненную корзину из файла \"basket.txt\" не удалось!");
        }
        return null;
    }

    public static int[] addArrayInt(int n, int[] arr, int x) // + метод добавления элемента в числовой массив
    {
        int i;
        int[] newarr = new int[n + 1];
        for (i = 0; i < n; i++)
            newarr[i] = arr[i];
        newarr[n] = x;
        return newarr;
    }

    public static String[] addArrayString(int n, String[] arr, String x) // + метод добавления элемента в строковый массив
    {
        int i;
        String[] newarr = new String[n + 1];
        for (i = 0; i < n; i++)
            newarr[i] = arr[i];
        newarr[n] = x;
        return newarr;
    }

    public int[] getCart() {
        return cart;
    }

}

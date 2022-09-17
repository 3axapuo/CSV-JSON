import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog { // Создайте класс ClientLog для сохранения всех операций, которые ввёл пользователь.
    String[] log = new String[0];

    public String[] log(int productNum, int amount) {
        String strValues = productNum + "," + amount; // соединяем значения в одну строку.
        log = Basket.addArrayString(log.length, log, strValues);
        return log;
    }

    // Также у объекта этого класса должен быть метод exportAsCSV(File txtFile) для сохранения
    // всего журнала действия в файл в формате csv.
    public void exportAsCSV(File txtFile) {
        try {
            FileWriter outFileWriter = new FileWriter(txtFile);
            CSVWriter outCSV = new CSVWriter(outFileWriter, ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            String[] header = {"productNum", "amount"}; // строка шапки
            outCSV.writeNext(header);
            for (int iValue = 0; iValue < log.length; iValue++) { // обходим значения строки
                String[] value = log[iValue].split(","); // разделяем на значения
                outCSV.writeNext(value);
            }
            outCSV.close();
            System.out.println("Журнал \"log.csv\"успешно сохранен.");
        } catch (IOException e) {
            System.out.println("⊠ ОШИБКА: Сохранить текущий файл журнала \"log.csv\" не удалось!");
        }
    } //exportAsCSV

    public void importAsCSV(File txtFile) { // + загружаем сохраненный журнал
        try {
            CSVReader reader = new CSVReader(new FileReader(txtFile));
            String[] nextLine;
            reader.readNext(); // пропускаем шапку (header)
            while ((nextLine = reader.readNext()) != null) {
                String[] values = String.join(",", nextLine).split(";");
                log(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }
            System.out.println("Журнал \"log.csv\"успешно загружен.");
        } catch (IOException e) {
            System.out.println("⊠ ОШИБКА: Загрузить сохраненный журнал из файла \"log.csv\" не удалось!");
        }
    }//importAsCSV
}
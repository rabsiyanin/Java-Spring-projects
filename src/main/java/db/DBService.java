package ru.kozlov.Lab.db;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kozlov.Lab.Service.HashTable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class DBService {

    private static final String TITANIC = "src/main/resources/titanic.csv";
    private static final String DB = "src/main/resources/db.csv";

    private static final String ID_ROW = "src/main/resources/id_row.csv";

    private final HashTable hashTable;


    public void createDB() throws IOException {
        FileWriter fileWriter = new FileWriter(DB);
        fileWriter.close();
    }

    public void deleteDB() throws IOException {
        Files.delete(Paths.get(DB));
    }

    public void cleanDB() {
        try {
            FileWriter fw = new FileWriter(DB, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
        }

    } //clean data

    public void saveDB() {
    }

    public void exportDB() {
    }

    public void importDB() {
        try {
            deleteDB();
            Files.copy(Paths.get(TITANIC), Paths.get(DB));
            fillIdRowTable();

            hashTable.fillIdRowHashMap();
            hashTable.fillIdBytesHashmap();
            hashTable.fillIdAgeHashMap();

        } catch (IOException e) {
        }
    }

    private void cleanIdRowTable() {
        try {
            FileWriter fw = new FileWriter(ID_ROW, false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
        }

    }

    private void createIdRowTable() throws IOException {
        FileWriter fw = new FileWriter(ID_ROW, false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
    }

    private void fillIdRowTable() throws IOException {
        cleanIdRowTable();
        createIdRowTable();
        try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReader(fileReader);
            FileWriter fileWriter = new FileWriter(ID_ROW, StandardCharsets.UTF_8);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            List<String[]> rows = csvReader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = {String.valueOf(i), rows.get(i - 1)[0].equals("") ? "" : rows.get(i - 1)[0]};
                csvWriter.writeNext(row);
            }
            /*fileReader.close();
            fileWriter.close();
            csvWriter.close();
            csvReader.close();*/

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

    }
}

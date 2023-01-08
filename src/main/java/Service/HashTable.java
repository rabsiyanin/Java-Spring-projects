package ru.kozlov.Lab.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Component
public class HashTable {

    private static final String DB = "src/main/resources/db.csv";

    HashMap<Integer, Integer> idRowHashMap = new HashMap<>();

    HashMap<Integer, Integer> idAgeHashMap = new HashMap<>();

    HashMap<Integer, Integer> idBytesHashMap = new HashMap<>();


    public int getRowOfPassengerById(int id) {
        System.out.println("this is get row in hash");
        return idRowHashMap.get(id);
    }

    public void fillIdAgeHashMap() {
        try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> rows = csvReader.readAll();

            for (int i = 1; i <= rows.size(); i++) {
                idAgeHashMap.put(i, rows.get(i - 1)[5].equals("") ? -1 : Integer.parseInt(rows.get(i - 1)[5]));
            }
            System.out.println(idAgeHashMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }


    public void fillIdRowHashMap() {
        try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> rows = csvReader.readAll();

            for (int i = 1; i <= rows.size(); i++) {
                idRowHashMap.put(i, rows.get(i - 1)[0].equals("") ? -1 : Integer.parseInt(rows.get(i - 1)[0]));
            }
            System.out.println(idRowHashMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }

    }

    public void refreshMapsOnInsert(int id, int bytes, int age) {
        idRowHashMap.put(id, idRowHashMap.get(idRowHashMap.size()));
        idBytesHashMap.put(id, bytes + idBytesHashMap.get(idBytesHashMap.size()));
        idAgeHashMap.put(id, age);
    }

    public void fillIdBytesHashmap() {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DB, "rw")) {

            int currentBytes = 0;

            for (int key : idRowHashMap.keySet()) {
                String line = randomAccessFile.readLine();
                currentBytes = currentBytes + line.getBytes().length;

                idBytesHashMap.put(key, currentBytes);

            }


            System.out.println(idBytesHashMap);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLastByteForInserting() {
        return idBytesHashMap.get(idBytesHashMap.size());
    }

    public int getBytesById(int id){
        return idBytesHashMap.get(id);
    }

    public boolean isPassengerAlreadyExists(int id) {
        return idRowHashMap.containsKey(id);
    }

    public List<Integer> getIdsOfAge(int age) {
        List<Integer> list = idAgeHashMap.entrySet().stream().filter(e -> e.getValue() == age).map(Map.Entry::getKey).collect(Collectors.toList());
        return list;
    }

    public void setAgeById(int id, int age){
        idAgeHashMap.put(id, age);
    }

}

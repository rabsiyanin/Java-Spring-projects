package ru.kozlov.Lab.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kozlov.Lab.model.Passenger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;


import java.util.List;


@Service
@AllArgsConstructor
public class PassengerService {

    private static final String DB = "src/main/resources/db.csv";

    private final HashTable hashTable;


    public List<Passenger> findAll() {
        List<Passenger> passengerList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReader(fileReader);

            List<String[]> passengerListString = csvReader.readAll();
            //System.out.println(passengerListString.get(0));

            for (String[] passenger : passengerListString) {
                if (passenger.length != 1)
                    passengerList.add(new Passenger(passenger));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }


        return passengerList;
    }

    public List<Passenger> findAll(String age) {
        if (age == null || age.isEmpty()) {
            return findAll();
        } else {

            List<Passenger> passengerList = new ArrayList<>();

            List<String[]> passengerListString = new ArrayList<>();

            List<Integer> rowsToRead = new ArrayList<>();


            for (int row : hashTable.getIdsOfAge(Integer.parseInt(age))) {
                rowsToRead.add(row);
            }

            try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
                CSVReader csvReader = new CSVReader(fileReader);

                int currentRow = 0;

                for (int row : rowsToRead) {
                    csvReader.skip(row - 1 - currentRow);
                    currentRow = row;
                    passengerListString.add(csvReader.readNext());
                }

                for (String[] passenger : passengerListString) {
                    if (passenger.length != 1)
                        passengerList.add(new Passenger(passenger));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
            return passengerList;
        }
    }

    public List<Passenger> findById(int id) {
        System.out.println("this is findById");
        int rowInDB = hashTable.getRowOfPassengerById(id);
        List<Passenger> passengerList = new ArrayList<>();
        Passenger passenger = findByRow(rowInDB);
        passengerList.add(passenger);
        return passengerList;
    }

    private Passenger findByRow(int row) {
        System.out.println("this is findByRow");

        Passenger passenger;
        try (FileReader fileReader = new FileReader(DB, StandardCharsets.UTF_8)) {
            CSVReader csvReader = new CSVReader(fileReader);

            csvReader.skip(row - 1);
            passenger = new Passenger(csvReader.readNext());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return passenger;
    }

    public void insert(Passenger passenger) {

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DB, "rw")) {

            if (hashTable.isPassengerAlreadyExists(passenger.getPassengerId()))
                throw new RuntimeException();

            randomAccessFile.seek(hashTable.getLastByteForInserting());
            randomAccessFile.readLine();
            randomAccessFile.writeBytes(passenger + "\n");


            hashTable.refreshMapsOnInsert(passenger.getPassengerId(), passenger.toString().getBytes().length, (int) passenger.getAge());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Passenger passenger) {
        System.out.println("this is update");

        int previousPassengerBytes = passenger.getPassengerId() == 1 ? 0 : hashTable.getBytesById(passenger.getPassengerId() - 1);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DB, "rw")) {

            randomAccessFile.seek(previousPassengerBytes);//hashTable.getBytesById(passenger.getPassengerId()));
            randomAccessFile.readLine();

            int targetBytes = hashTable.getBytesById(passenger.getPassengerId()) - previousPassengerBytes;
            int currentBytes = passenger.toString().getBytes().length;

            if (targetBytes == currentBytes){
                //String blank = " ";
                //blank = blank.repeat(targetBytes - currentBytes > 0 ? targetBytes - currentBytes - 1 : 0);
                randomAccessFile.writeBytes(passenger.toString()+ "\n");
            }


            //System.out.println(randomAccessFile.readLine());

            hashTable.fillIdBytesHashmap();
            hashTable.setAgeById(passenger.getPassengerId(), (int) passenger.getAge());


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void delete(int id) {

        System.out.println("this is delete");

        int passengerBytes = id == 1 ? 0 : hashTable.getBytesById(id - 1);

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(DB, "rw")) {

            randomAccessFile.seek(passengerBytes);
            randomAccessFile.readLine();

            String blank = " ";
            blank = blank.repeat(hashTable.getBytesById(id) - passengerBytes);
            randomAccessFile.writeBytes(blank + "\n");

            System.out.println(randomAccessFile.readLine());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteByAge(String age) {
        System.out.println("this is delete by age");
        List<Integer> idsToDelete = new ArrayList<>();

        for (int id : hashTable.getIdsOfAge(Integer.parseInt(age))) {
            idsToDelete.add(id);
        }

        System.out.println("ids to delete: " + idsToDelete);

        for (int id : idsToDelete) {
            delete(id);
        }

    }

}

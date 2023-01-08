package ru.kozlov.Lab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

    int passengerId;
    boolean survived;
    int pClass;
    String name;
    String sex;
    int age;
    int sibSP;
    int parch;
    String ticket;
    double fare;
    String cabin;
    String embarked;

    public Passenger(String[] stringPassenger) {
        this.passengerId = stringPassenger[0].equals("") ? null : Integer.parseInt(stringPassenger[0]);
        this.survived = stringPassenger[1].equals("1") ? true : false;
        this.pClass = stringPassenger[2].equals("") ? -1 : Integer.parseInt(stringPassenger[2]);
        this.name = stringPassenger[3].equals("") ? null : stringPassenger[3];
        this.sex = stringPassenger[4].equals("") ? null : stringPassenger[4];
        this.age = stringPassenger[5].equals("") ? -1 : Integer.parseInt(stringPassenger[5]);
        this.sibSP = stringPassenger[6].equals("") ? null : Integer.parseInt(stringPassenger[6]);
        this.parch = stringPassenger[7].equals("") ? null : Integer.parseInt(stringPassenger[7]);
        this.ticket = stringPassenger[8].equals("") ? null : stringPassenger[8];
        this.fare = stringPassenger[9].equals("") ? -1 : Double.parseDouble(stringPassenger[9]);
        this.cabin = stringPassenger[10].equals("") ? null : stringPassenger[10];
        this.embarked = stringPassenger[11].equals("") ? null : stringPassenger[11];
    }

    public String[] toStringArray() {
        String[] passenger = {
                String.valueOf(passengerId),
                String.valueOf(survived),
                String.valueOf(pClass),
                name,
                sex,
                String.valueOf(age),
                String.valueOf(sibSP),
                String.valueOf(parch),
                ticket,
                String.valueOf(fare),
                cabin,
                embarked};
        return passenger;
    }

    public String toString() {
        return new String(String.valueOf(passengerId) + "," +
                String.valueOf(survived == true ? 1 : 0) + "," +
                String.valueOf(pClass) + "," +
                "\"" + name + "\"" + "," +
                sex + "," +
                String.valueOf(age) + "," +
                String.valueOf(sibSP) + "," +
                String.valueOf(parch) + "," +
                ticket + "," +
                String.valueOf(fare) + "," +
                cabin + "," +
                embarked);
    }

}

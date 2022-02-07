package passenger;

import flight.Destination;
import flight.Flight;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PassengerDao {
    private final String fileName;
    private final File file;

    public PassengerDao() {
        String dirName = System.getProperty("user.dir") + "/src/main/java/passenger";
        this.fileName = "PassengerDB.txt";
        this.file = new File(dirName, fileName);
    }

    public List<Passenger> getAllPassengers() {
        // make sure file exists
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Empty DB created!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create empty list
        List<Passenger> allPassengers;
        allPassengers = new ArrayList<>();
        // read each line and parse
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] info = scanner.nextLine().split(",");
                // parse arguments to new passenger and add
                Passenger p = new Passenger(info[0], info[1], info[2], info[3], info[4]);
                allPassengers.add(p);
            }
        // any lines that don't conform produce error and are not loaded
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": when attempting to read " + fileName);
        }
        return allPassengers;
    }

//    public List<Passenger> getAllPassengers() {
//        return allPassengers;
//    }

    public void updateAllPassengers(List<Passenger> allPassengers) {
        //make sure file exists
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Empty DB created!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // create fileWriter with our txt file as an argument
            FileWriter fileWriter = new FileWriter(file);
            // create printWriter with fileWriter as argument
            PrintWriter myWriter = new PrintWriter(fileWriter);

            for (Passenger p : allPassengers) {
                if (p == null) {
                    myWriter.println("null");
                } else {
                    myWriter.println(p.toStringCSV());
                }
            }
            // end of writing
            myWriter.flush();
            myWriter.close();

        } catch (Exception e) {
            System.out.println("Couldn't write to file.");
        }
    }
}

package flight;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlightDao {
    private final String fileName;
    private final File file;

    public FlightDao() {
        String dirName = System.getProperty("user.dir") + "/src/main/java/flight";
        this.fileName = "FlightDB.txt";
        this.file = new File(dirName, fileName);
    }

    public List<Flight> getAllFlights() {
        // make sure file exists
        try {
            if (file.createNewFile()) {
                System.out.println("Empty DB created!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create empty list
        List<Flight> allFlights;
        allFlights = new ArrayList<>();
        // read each line and parse
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                // set up each loop to use the next line and reset counts
                String info = scanner.nextLine();
                int count = 0;
                int index = 0;

                // find third comma to split info around
                for (int i = 0; i < info.length(); i++) {
                    char c = info.charAt(i);
                    if (c == ',') {
                        count++;
                    }
                    if (count == 3) {
                        index = i;
                        break;
                    }
                }

                //split info into flight details and passenger array
                String[] infoPart1 = info.substring(0, index).split(",");
                String[] infoPart2 = info.substring(index + 1).replaceAll("\\[", "").replaceAll("]", "").split(",");

                //parsing time into localDateTime
                String[] time = infoPart1[2].split("-");

                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);
                int hour = Integer.parseInt(time[3]);
                int minute = Integer.parseInt(time[4]);

                LocalDateTime localDateTime = LocalDateTime.of(year, month, day, hour, minute);

                //parsing passengers into an array
                String[] passengers = new String[infoPart2.length];

                for (int i = 0; i < passengers.length; i++) {
                    String p = infoPart2[i].trim();
                    if (p.equalsIgnoreCase("null")) {
                        passengers[i] = null;
                    } else {
                        passengers[i] = p;
                    }
                }

                // create flight with all extracted arguments and add
                Flight f = new Flight(infoPart1[0], Destination.valueOf(infoPart1[1]), localDateTime, passengers);
                allFlights.add(f);
            }
        // any lines that don't conform produce error and are not loaded
        } catch (Exception e) {
            System.out.println(e.getMessage() + ": when attempting to read " + fileName);
        }
        return allFlights;
    }

    public void updateAllFlights(List<Flight> allFlights){
        // make sure file exists
        try {
            if (file.createNewFile()) {
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

            for (Flight f : allFlights) {
                if (f == null) {
                    myWriter.println("null");
                } else {
                    myWriter.println(f.toStringCSV());
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








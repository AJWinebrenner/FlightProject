import util.IdGenerator;
import util.Interface;
import flight.FlightService;
import flight.FlightDao;
import passenger.PassengerDao;
import passenger.PassengerService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        FlightDao flightDao = new FlightDao();
        PassengerDao passengerDao = new PassengerDao();
        FlightService flightService = new FlightService(flightDao);
        IdGenerator idGenerator = new IdGenerator();
        PassengerService passengerService = new PassengerService(passengerDao, flightService, idGenerator);

        ////////////////////////////////////////////////////////

        System.out.println("Welcome to the Flight Booking CLI!");

        String[] options = {
                "Add a flight",
                "Display all flights",
                "Display fully-booked flights",
                "Create a new user",
                "Display flight or book flight for a specific user",
                "Cancel flight",
                "Quit the program"
        };

        while (true) {

            int option = Interface.getOption("Please select your option using the numbers:",
                    options);

            // Switch statements here
            switch (option) {
                case 1 -> {
                    System.out.println("'Add flight' selected.");
                    flightService.addFlight();
                }
                case 2 -> {
                    System.out.println("'Display all flights' selected.");
                    flightService.displayAllFlights();
                }
                case 3 -> {
                    System.out.println("'Display fully-booked flights' selected.");
                    flightService.displayFullyBooked();
                }
                case 4 -> {
                    System.out.println("'Create a new user' selected.");
                    passengerService.promptCreateNewUser();
                }
                case 5 -> {
                    System.out.println("'Display flight or book a flight for a specific user' selected.");
                    passengerService.chooseIdOrName();
                }
                case 6 -> {
                    System.out.println("'Cancel a flight' selected.");
                    flightService.PromptCancelFlight();
                }
                case 7 -> {
                    System.out.println("Thanks for using our management system!");
                    System.exit(0);
                }
            }
            System.out.println("Press enter to continue.");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        }
    }

    //menus


}








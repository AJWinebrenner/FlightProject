import util.IdGenerator;
import util.Interface;
import flight.FlightService;
import flight.FlightDao;
import passenger.PassengerDao;
import passenger.PassengerService;

public class Main {

    static FlightDao flightDao = new FlightDao();
    static PassengerDao passengerDao = new PassengerDao();
    static FlightService flightService = new FlightService(flightDao);
    static IdGenerator idGenerator = new IdGenerator();
    static PassengerService passengerService = new PassengerService(passengerDao, flightService, idGenerator);


    public static void main(String[] args) {

        System.out.println("Welcome to the Flight Management CLI!");

        String[] options = {
                "Manage Flights",
                "Manage Passengers",
                "Quit the program"
        };

        while (true) {

            System.out.println("/////Main Menu/////");

            int option = Interface.getOption("Enter a number:", options);

            // Switch statements here
            switch (option) {
                case 1 -> manageFlights();
                case 2 -> managePassengers();
                case 3 -> {
                    System.out.println("Thanks for using our management system!");
                    System.exit(0);
                }
            }
        }
    }

    private static void manageFlights() {
        String[] options = {
                "Add a flight",
                "Display all flights",
                "Display fully-booked flights",
                "Cancel flight",
                "Back"
        };

        while (true) {
            int option = Interface.getOption("/////Manage Flights/////", options);
            boolean back = false;
            switch (option) {
                case 1 -> flightService.promptAddFlight();
                case 2 -> flightService.displayAllFlights();
                case 3 -> flightService.displayFullyBooked();
                case 4 -> flightService.PromptCancelFlight();
                case 5 -> back = true;
            }
            if (back) {
                break;
            }
        }
    }

    private static void managePassengers() {
        String[] options = {
                "Create a new user",
                "Manage specific user",
                "Back"
        };

        while (true) {
            int option = Interface.getOption("/////Manage Passengers/////", options);
            boolean back = false;
            switch (option) {
                case 1 -> passengerService.promptCreateNewUser();
                case 2 -> passengerService.chooseIdOrName();
                case 3 -> back = true;
            }
            if (back) {
                break;
            }
        }
    }
}









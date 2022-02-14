package flight;

import passenger.Passenger;
import util.Interface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record FlightService(FlightDao flightDao) {

    public void promptAddFlight() { // TODO use getInput methods
        System.out.println("Flight Code?");

        Scanner scanner = new Scanner(System.in);
        String flightCode = scanner.nextLine();

        // TODO replace all with destination search
        System.out.println("Destination?\n1. Germany\n2. China\n3. France\n4. Denmark\n5. UAE\n6. USA\n7. Czech Republic");
        String selection = scanner.nextLine();
        Destination destination = null;


        switch (selection) {
            case "1" -> destination = Destination.BER;
            case "2" -> destination = Destination.BJS;
            case "3" -> destination = Destination.BOD;
            case "4" -> destination = Destination.CPH;
            case "5" -> destination = Destination.DXB;
            case "6" -> destination = Destination.LAX;
            case "7" -> destination = Destination.PRG;
            default -> {
                System.out.println("Not a valid choice.");
                promptAddFlight();
            }
        }

        System.out.println("Departure Date/Time:");
        System.out.println("Year?");
        int year = scanner.nextInt();
        System.out.println("Month?");
        int month = scanner.nextInt();
        System.out.println("Day?");
        int day = scanner.nextInt();
        System.out.println("Hour?");
        int hour = scanner.nextInt();
        System.out.println("Minute?");
        int minute = scanner.nextInt();
        // create date time with given values
        LocalDateTime departureTime = LocalDateTime.of(year, month, day, hour, minute);

        System.out.println("Flight capacity?");
        int capacity = scanner.nextInt();

        Flight flight = new Flight(flightCode, destination, departureTime, capacity);

        List<Flight> allFlights = flightDao.getAllFlights();
        allFlights.add(flight);
        flightDao.updateAllFlights(allFlights);
    }

    // All flights
    public static int getAvailableSeats(Flight flight) {
        int counter = 0;
        for (String passenger : flight.getPassengerIds()) {
            if (passenger == null) {
                counter++;
            }
        }
        return counter;
    }

    public static String formatFlight(Flight flight) {
        if (flight != null) {
            return flight.getFlightCode() + " | " + "Destination: " + flight.getDestination() + " | " + "Departure time: " + flight.getDepartureTime() + " | " + "Available seats: " + getAvailableSeats(flight);
        } else {
            return "There's nothing here";
        }
    }

    public void displayFlights(List<Flight> flights) {
        for (Flight flight : flights) {
            System.out.println("--------------------");
            System.out.println(formatFlight(flight));
        }
        System.out.println("--------------------");
        Interface.pause();
    }

    public void displayFullyBooked(List<Flight> flights) {
        System.out.println("Fully booked flights:");
        for (Flight flight : flights) {
            if (getAvailableSeats(flight) == 0) {
                System.out.println("--------------------");
                System.out.println(formatFlight(flight));
            }
        }
        System.out.println("--------------------");
        Interface.pause();
    }

    public void addPassengerToFlight(Passenger passenger, Flight flight) {
        for (int i = 0; i < flight.getMaxCapacity(); i++) {
            if (flight.getPassengerIds()[i] == null) {
                flight.getPassengerIds()[i] = passenger.getId();
                break;
            }
        }
    }

    public boolean isOnFlight(Passenger passenger, Flight flight) {
        for (String passengerId : flight.getPassengerIds()) {
            if (passenger.getId().equals(passengerId)) {
                return true;
            }
        }
        return false;
    }

    public void displayPassengerFlights(Passenger passenger) {
        List<Flight> onboard = new ArrayList<>();
        for (Flight flight : flightDao.getAllFlights()) {
            if (isOnFlight(passenger, flight)) {
                onboard.add(flight);
            }
        }
        System.out.println("Flights for " + passenger.getName() + ":");
        if (onboard.size() == 0) {
            onboard.add(null);
        }
        displayFlights(onboard);
    }

    public Flight getFlightByCode(String code, List<Flight> flights) {
        for (Flight f : flights) {
            if (code.equalsIgnoreCase(f.getFlightCode())) {
                return f;
            }
        }
        return null;
    }

    public void promptCancelFlight() { //TODO filter stores flight codes
        List<Flight> allFlights = flightDao.getAllFlights();
        List<String> filterCodes = new ArrayList<>(); // TODO replace with filter (currently does all)
        for (Flight f : allFlights) {
            filterCodes.add(f.getFlightCode());
        }

        // setup options
        String[] options = new String[filterCodes.size() + 1];
        for (int i = 0; i < filterCodes.size(); i++) {
            Flight f = getFlightByCode(filterCodes.get(i), allFlights); //return flights from "allFlights" matching "filterCodes"
            options[i] = formatFlight(f);
        }
        options[filterCodes.size()] = "back";

        // cancel the flight chosen and save list
        int option = Interface.getOption("Pick an available flight below:", options);
        if (option <= filterCodes.size()) {
            String code = filterCodes.get(option - 1);
            // TODO if (getInput("Type code to confirm:", "[A-Za-z0-9]+") == code);
            allFlights.remove(getFlightByCode(code, allFlights));
            flightDao.updateAllFlights(allFlights);
            System.out.println("Flight " + code + " cancelled.");

        }
    }



    public void promptBookFlight(Passenger passenger) {
        // get available flights TODO replace with filter
        List<Flight> allFlights = flightDao.getAllFlights();
        List<Flight> availableFlights = new ArrayList<>();
        for (Flight f : allFlights) {
            if (getAvailableSeats(f) > 0 && !isOnFlight(passenger, f)) {
                availableFlights.add(f);
            }
        }
        List<String> filterCodes = new ArrayList<>(); // TODO replace with filter (currently does all available)
        for (Flight f : availableFlights) {
            filterCodes.add(f.getFlightCode());
        }

        // cancel with no available options
        if (filterCodes.size() == 0) {
            System.out.println("No available flights.");
        } else { // setup options
            String[] options = new String[filterCodes.size() + 1];
            for (int i = 0; i < filterCodes.size(); i++) {
                Flight f = getFlightByCode(filterCodes.get(i), availableFlights); //return flights from "availableFlights" matching "filterCodes"
                options[i] = formatFlight(f);
            }
            options[filterCodes.size()] = "Back";

            // book a flight and save list
            int option = Interface.getOption("Pick an available flight below:", options);
            if (option <= filterCodes.size()) {
                String code = filterCodes.get(option - 1);
                Flight f = getFlightByCode(code, availableFlights);
                if (f != null){
                    addPassengerToFlight(passenger, f);
                    flightDao.updateAllFlights(allFlights);
                    System.out.println("Flight booked for " + passenger.getName() + "!");
                } else {
                    System.out.println("cannot find flight");
                }
            }
        }
    }
}

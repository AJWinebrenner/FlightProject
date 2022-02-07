package flight;

import passenger.Passenger;
import util.Interface;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record FlightService(FlightDao flightDao) {

    public void addFlight() { // TODO use getInput methods
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
                addFlight();
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
        return flight.getFlightCode() + " | " + "Destination: " + flight.getDestination() + " | " + "Departure time: " + flight.getDepartureTime() + " | " + "Available seats: " + getAvailableSeats(flight);
    }

    public void displayAllFlights() { // TODO use filter to display filtered instead?
        for (Flight flight : flightDao.getAllFlights()) {
            System.out.println("--------------------");
            System.out.println(formatFlight(flight));
        }
        System.out.println("--------------------");
    }

    public void displayFullyBooked() {
        System.out.println("Fully booked flights:");
        for (Flight flight : flightDao.getAllFlights()) {
            if (getAvailableSeats(flight) == 0) {
                System.out.println("--------------------");
                System.out.println(formatFlight(flight));
            }
        }
        System.out.println("--------------------");
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

    public void DisplayPassengerFlights(Passenger passenger) {
        List<Flight> onboard = new ArrayList<>();
        for (Flight flight : flightDao.getAllFlights()) {
            if (isOnFlight(passenger, flight)) {
                onboard.add(flight);
            }
        }
        System.out.println("Flights for passenger: " + passenger.getName());
        if (onboard.size() > 0) {
            for (Flight flight : onboard) {
                System.out.println(formatFlight(flight));
            }
        } else {
            System.out.println("No flights booked for " + passenger.getName() + ".");
            bookOrDisplay(passenger);
        }
        System.out.println("---------------------");
    }

    public Flight getFlightByCode(String code) {
        for (Flight f : flightDao.getAllFlights()) {
            if (code.equalsIgnoreCase(f.getFlightCode())) {
                return f;
            }
        }
        return null;
    }

    public void PromptCancelFlight() {
        List<Flight> allFlights = flightDao.getAllFlights();
        // TODO replace with filter
        List<Integer> filterFlights = new ArrayList<>();
        for (int i = 0; i < allFlights.size(); i++) {
            Flight f = allFlights.get(i);
            filterFlights.add(i);
        }

        // setup options
        String[] options = new String[filterFlights.size() + 1];
        for (int i = 0; i < filterFlights.size(); i++) {
            Flight f = allFlights.get(i);
            options[i] = formatFlight(f);
        }
        options[filterFlights.size()] = "back";

        // cancel the flight and save list
        int option = Interface.getOption("Pick an available flight below:", options);
        if (option <= filterFlights.size()) {
            String code = allFlights.get(option - 1).getFlightCode();
            allFlights.remove(option - 1);
            flightDao.updateAllFlights(allFlights);
            System.out.println("Flight " + code + " cancelled.");
            displayAllFlights();
        }
    }

    public void bookOrDisplay(Passenger p) { //TODO make into menu
        String[] options = {"Book a flight for " + p.getName(), "Display booked flights for " + p.getName()};
        int option = Interface.getOption("Would you like to:", options);

        switch (option) {
            case 1 -> PromptBookFlight(p);
            case 2 -> DisplayPassengerFlights(p);
        }
    }

    public void PromptBookFlight(Passenger p) {
        // get available flights TODO replace with filter
        List<Flight> allFlights = flightDao.getAllFlights();
        List<Integer> availableFlights = new ArrayList<>();
        for (int i = 0; i < allFlights.size(); i++) {
            Flight f = allFlights.get(i);
            if (getAvailableSeats(f) > 0 && !isOnFlight(p, f)) {
                availableFlights.add(i);
            }
        }
        // cancel with no available options
        if (availableFlights.size() == 0) {
            System.out.println("No available flights.");
        } else { // setup options
            String[] options = new String[availableFlights.size() + 1];
            for (int i = 0; i < availableFlights.size(); i++) {
                Flight f = allFlights.get(i);
                options[i] = formatFlight(f);
            }
            options[availableFlights.size()] = "Back";

            // book a flight and save list
            int option = Interface.getOption("Pick an available flight below:", options);
            if (option <= availableFlights.size()) {
                addPassengerToFlight(p, allFlights.get(option - 1));
                flightDao.updateAllFlights(allFlights);
                System.out.println("Flight booked for " + p.getName() + "!");
            }
        }
    }
}

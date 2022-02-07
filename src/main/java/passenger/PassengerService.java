package passenger;

import flight.FlightService;
import util.IdGenerator;
import util.Interface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record PassengerService(PassengerDao passengerDao, FlightService flightService, IdGenerator idGenerator) {

    public Passenger getById(String id) {
        for (Passenger p : passengerDao.getAllPassengers()) {
            if (id.equals(p.getId())) {
                return p;
            }
        }
        return null;
    }

    public List<Passenger> filterByName(String name) { //TODO use index numbers to ref allPassengers in a list (return Int)
        List<Passenger> matched = new ArrayList<>();
        for (Passenger p : passengerDao.getAllPassengers()) {
            if (name.trim().equalsIgnoreCase(p.getName())) { // watch for trims
                matched.add(p);
            }
        }
        return matched;
    }

    public void chooseIdOrName() { //TODO make into menu
        String[] options = {"Search by name", "Use Id"};
        int option = Interface.getOption("Select a number:", options);
        switch (option) {
            case 1 -> enterName();
            case 2 -> enterId();
        }
    }

    public void enterId() { //TODO promptGetPassengerById return passenger and pass into goToMenu...()
        System.out.println("Please enter your Id:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Passenger passenger = getById(input);
        if (passenger == null) {
            System.out.println("Cannot find passenger with that Id");
            chooseIdOrName();
        } else {
            flightService.bookOrDisplay(passenger);
        }
    }

    public void enterName() { //TODO promptGetPassengerByName return passenger and pass into goToMenu...()
        System.out.println("Please enter your name:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        List<Passenger> filteredList = filterByName(input);
        if (filteredList.size() == 0) {
            System.out.println("No matches found for " + input);
        } else {
            String[] arr = new String[filteredList.size()];
            for (int i = 0; i < filteredList.size(); i++) {
                arr[i] = formatPassenger(filteredList.get(i));
            }
            int option = Interface.getOption("Choose a passenger from the list below:", arr);
            Passenger passenger = filteredList.get(option - 1);
            flightService.bookOrDisplay(passenger);
        }
    }

    public void promptCreateNewUser() {

        List<Passenger> allPassengers = passengerDao.getAllPassengers();
        String id = idGenerator.randomIdGenerator(allPassengers);

        System.out.println("Please enter your full name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        System.out.println("Please enter your email:");
        String email = scanner.nextLine();

        System.out.println("Please enter your phone number");
        String phoneNum = scanner.nextLine();

        System.out.println("Please enter your passport number:");
        String passportNum = scanner.nextLine();

        Passenger passenger = new Passenger(id, name, email, phoneNum, passportNum);

        allPassengers.add(passenger);
        passengerDao.updateAllPassengers(allPassengers);

        System.out.println(name + " created! Your ID is " + id);
    }

    public String formatPassenger(Passenger p) {
        return p.getId() + " | " + p.getName() + " | " + p.getEmail() + " | " + p.getPassport();
    }
}


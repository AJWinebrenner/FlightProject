package util;

import passenger.Passenger;

import java.util.List;
import java.util.Random;

public class IdGenerator {


    public String randomIdGenerator(List<Passenger> p){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        boolean duplicate;

        do {
            // create id from characters
            duplicate = false;
            randomString.setLength(0);
            for (int i = 0; i < 7; i++) {
                randomString.append(characters.charAt(random.nextInt(characters.length())));
            }
            // compare with existing ids, if duplicate repeat again
            for (Passenger passenger : p) {
                if (randomString.toString().equalsIgnoreCase(passenger.getId())) {
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);

        return randomString.toString();
    }
}

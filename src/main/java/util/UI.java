package util;

import java.util.Scanner;

public class UI {

    public static int getOption(String message, String[] options) {
        Scanner scan = new Scanner(System.in);
        System.out.println(message);
        for (int i = 1; i <= options.length; i++) {
            System.out.println(i + ") " + options[i-1]);
        }
        while(true) {
            try {
                int input = scan.nextInt();
                if (input <= options.length && input > 0) {
                    return input;
                } else {
                    System.out.println(input + " out of range");
                }
            } catch (Exception e) {
                String badInput = scan.nextLine();
                System.out.println(badInput + " is invalid input");
            }
        }
    }

    public static String getInput(String message, String regex) {
        Scanner scan = new Scanner(System.in);
        System.out.println(message);
        while(true) {
            String input = scan.nextLine();
            if (input.equals("") || input.matches(regex)) {
                return input;
            }
            System.out.println(input + " is invalid");
        }
    }

    public static void pause() {
        System.out.println("Press enter to continue.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}



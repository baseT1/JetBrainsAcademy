package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {

    private static String[][] cinema;
    private static int seats;
    private static int rows;
    private static int columns;
    private static Scanner scanner;

    private static int ticketsPurchased = 0;
    private static int income = 0;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        System.out.print("> ");
        rows = Integer.parseInt(scanner.next());

        System.out.println("Enter the number of seats in each row:");
        System.out.print("> ");
        columns = Integer.parseInt(scanner.next());

        seats = rows*columns;


        cinema = new String[rows][columns];
        for (String[] row : cinema) {
            Arrays.fill(row, " S");
        }
        boolean menu = true;
        while (menu) {
            printMenu();
            switch (scanner.nextInt()) {
                case 1:
                    printCinema();
                    break;
                case 2:
                    buyTicket();
                    break;
                case 3:
                    statistics();
                    break;
                case 0:
                    menu = false;
                    break;
                default:
                    break;
            }
        }


    }

    private static void statistics() {
        int curPrice = 0;

        double per = ((double)ticketsPurchased) / ((double)seats);

        if (seats <= 60) {
            curPrice = 10 * seats;
        } else {
            for (int i=1; i <= rows; i++) {
                if (i <= (rows / 2)) {
                    curPrice += columns * 10;
                } else {
                    curPrice += columns * 8;
                }
            }

        }

        System.out.println();
        System.out.println("Number of purchased tickets: " + ticketsPurchased);
        System.out.printf("Percentage: %.2f%%%n" , per * 100.00);
        System.out.println("Current income: $" + income);


        System.out.println("Total income: $" + curPrice);

    }
    private static void buyTicket() {
        int price = 0;
        System.out.println();
        System.out.println("Enter a row number:");
        System.out.print("> ");
        int seatRow = Integer.parseInt(scanner.next());

        System.out.println("Enter a seat number in that row:");
        System.out.print("> ");
        int seatNumber = Integer.parseInt(scanner.next());

        try {
            if (cinema[seatRow - 1][seatNumber - 1].equals(" S")) {
                if (seats <= 60) {
                    price = 10;
                } else {
                    if (seatRow <= (rows / 2)) {
                        price = 10;
                    } else {
                        price = 8;
                    }
                }
                ticketsPurchased++;
                income += price;

                System.out.println("Ticket price: $" + price);
                cinema[seatRow - 1][seatNumber - 1] = " B";
            } else {
                System.out.println("That ticket has already been purchased!");
                System.out.println();
                buyTicket();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong input!");
            buyTicket();
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        System.out.print("> ");

    }

    private static void printCinema() {
        System.out.println();
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int i = 1; i <= cinema[0].length; i++) {
            System.out.print( i + " ");
        }
        System.out.println();

        for (int i = 1; i <= cinema.length; i++) {
            System.out.print(i);

            for (int j = 0; j < cinema[i-1].length; j++) {
                System.out.print(cinema[i-1][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

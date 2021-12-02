package cinema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Cinema {

    public static void main(String[] args) {

        String[][] cinemaRoom = cinemaRoomCreator();
        Scanner sc = new Scanner(System.in);

        int choose;
        do {
            System.out.println("\n1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit");
            choose = Integer.parseInt(sc.nextLine());

            switch (choose) {
                case 1:
                    cinemaRoomPrinter(cinemaRoom);
                    break;
                case 2:
                    boolean result;
                    do {
                        int[] clientsChoice = clientChoice();
                        result = seatReservationAndChecking(clientsChoice, cinemaRoom);
                    } while (!result);

                    break;
                case 3:
                    statCalculator(cinemaRoom);
                    break;
                case 0:
                    choose = 0;
//                  System.exit(0);
            }

        }
        while (choose != 0);
    }


    public static void statCalculator(String[][] cinema) {
        String blockedSeat = "B";
        int soldSeats = 0;
        int price = 0;
        int totalIncome = 0;
        int totalMaxIncome = 0;

        for (int i = 0; i < cinema.length; i++) {
            for (int j = 0; j < cinema[0].length; j++) {
                price = priceChecker(new int[]{i+1, j+1}, cinema);
                if (cinema[i][j].equals(blockedSeat)) {
                    totalIncome += price;
                    soldSeats++;
                }
                totalMaxIncome += price;
            }
        }

        System.out.printf("Number of purchased tickets: %d\n", soldSeats);
        System.out.printf("Percentage: %.2f%%\n", (soldSeats * 100 / ((float) cinema.length * cinema[0].length)));
        System.out.printf("Current income: $%d\n", totalIncome);
        System.out.printf("Total income: $%d\n", totalMaxIncome);
    }


    private static int[] clientChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter a row number:");
        int rowNumber = Integer.parseInt(sc.nextLine());
        System.out.println("Enter a seat number in that row:");
        int seatNumber = Integer.parseInt(sc.nextLine());

        return new int[]{rowNumber, seatNumber};
    }

    public static boolean seatReservationAndChecking(int[] clientsRequest, String[][] cinema) {
        String blockedSeat = "B";
        int rowNumber = clientsRequest[0] - 1;
        int seatNumber = clientsRequest[1] - 1;

        if (rowNumber > cinema.length-1 || seatNumber > cinema[0].length-1) {
            System.out.println("Wrong input!");
            return false;
        } else if (blockedSeat.equals(cinema[rowNumber][seatNumber])) {
            System.out.println("That ticket has already been purchased!");
            return false;
        } else {
            cinema[rowNumber][seatNumber] = blockedSeat;
            System.out.printf("\nTicket price: $%d\n", priceChecker(clientsRequest, cinema));
            return true;
        }
    }

    public static int priceChecker(int[] clientsRequest, String[][] cinema) {
        int rowNumber = clientsRequest[0];
        int seatNumber = clientsRequest[1];

        int numberOfAllSeats = cinema.length * cinema[0].length;
        if (numberOfAllSeats < 60) return pricesRepository().get("smallRoomPrice");
        if (rowNumber <= cinema.length / 2) return pricesRepository().get("bigRoomPriceFrontHalf");
        if (rowNumber > cinema.length / 2) return pricesRepository().get("bigRoomPriceBackHalf");
        return -1;
    }

    public static Map<String, Integer> pricesRepository() {
        Map<String, Integer> hashMap = new HashMap<>();

        hashMap.put("smallRoomPrice", 10);
        hashMap.put("bigRoomPriceFrontHalf", 10);
        hashMap.put("bigRoomPriceBackHalf", 8);

        return hashMap;
    }

    private static String[][] cinemaRoomCreator() {
        String freeSeat = "S";
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        int numberOfRows = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the number of seats in each row:");
        int numberOfSeats = Integer.parseInt(sc.nextLine());

        String[][] cinema = new String[numberOfRows][numberOfSeats];
        Arrays.stream(cinema).forEach(a -> Arrays.fill(a, freeSeat));
        return cinema;
    }

    private static void cinemaRoomPrinter(String[][] cinemaRoom) {
        int seats = cinemaRoom[0].length;
        int rowLength = cinemaRoom.length;

        System.out.print("\nCinema:\n ");
        IntStream.rangeClosed(1, seats).forEach(a -> System.out.print(" " + a));
        System.out.println();

        for (int i = 0; i < rowLength; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < seats; j++) {
                System.out.print(" " + cinemaRoom[i][j]);
            }
            System.out.println();
        }
    }
}
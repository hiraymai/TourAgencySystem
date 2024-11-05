package controllers;

import factories.TourFactory;
import models.Tour;
import models.User;
import models.Transportation;
import services.TourService;
import models.StandardTour;
import models.PremiumTour;
import models.WeekendRate;
import services.PaymentService;
import services.UserService;
import payment.HalkBankPayment;
import payment.KaspiGoldPayment;
import payment.PaypalPayment;

import java.util.List;
import java.util.Scanner;

public class TourController {
    private TourService tourService = new TourService();
    private UserService userService = new UserService();
    private static final double TRANSPORTATION_COST = 100.0;

    public void initializeTours() {
        tourService.addTour(TourFactory.createTour("standard", "City Break", 500, "Astana", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Luxury Escape", 1200, "Almaty", "5-star", "John Doe"));
        tourService.addTour(TourFactory.createTour("weekend", "Weekend Getaway", 300, "Shymkent", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Cultural Journey", 600, "Taldykorgan", "3-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Adventure in the Mountains", 1500, "Kokshetaу", "5-star", "Anna Smith"));
        tourService.addTour(TourFactory.createTour("weekend", "Beach Relaxation", 400, "Aktau", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Historical Sites Tour", 700, "Taraz", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Gourmet Food Experience", 1300, "Almaty", "5-star", "Michael Brown"));
        tourService.addTour(TourFactory.createTour("weekend", "Nature Exploration", 350, "Semey", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Wildlife Safari", 800, "Atyrau", "3-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Luxury Cruise", 2000, "Astana", "5-star", "Sara Jordan"));
        tourService.addTour(TourFactory.createTour("weekend", "Family Fun Trip", 500, "Shymkent", "", ""));
        tourService.addTour(TourFactory.createTour("standard", "Spa Retreat", 900, "Kokshetau", "4-star", ""));
        tourService.addTour(TourFactory.createTour("premium", "Winter Wonderland", 1600, "Almaty", "5-star", "David Lee"));
        tourService.addTour(TourFactory.createTour("weekend", "City Adventure", 450, "Nur-Sultan", "", ""));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Tour Agency!");

        while (true) {
            System.out.println("Choose an option: 1. Register 2. Login 3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                userService.registerUser(username, password);
            } else if (choice == 2) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                User user = userService.authenticateUser(username, password);
                if (user != null) {
                    System.out.println("Login successful! Welcome, " + user.getUsername() + ".");
                    bookTour(scanner);
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close(); // Закрываем сканер после использования
    }

    private void bookTour(Scanner scanner) {
        System.out.print("Choose tour type (Standard/Premium/Weekend): ");
        String tourType = scanner.nextLine();

        System.out.print("Enter your budget: ");
        double budget = scanner.nextDouble();
        scanner.nextLine();

        List<Tour> tours = tourService.findToursWithinBudget(budget);
        if (tours.isEmpty()) {
            System.out.println("No tours available within your budget.");
            return;
        }

        System.out.println("Available tours:");
        for (int i = 0; i < tours.size(); i++) {
            System.out.println((i + 1) + ". " + tours.get(i).getName() + " - " + tours.get(i).getCity() + " - $" + tours.get(i).getPrice());
        }

        System.out.print("Choose a tour by number: ");
        int tourChoice = scanner.nextInt();
        scanner.nextLine();
        Tour selectedTour = tours.get(tourChoice - 1);

        if (tourType.equalsIgnoreCase("standard") || tourType.equalsIgnoreCase("premium")) {
            String hotel = "";
            String guide = "";

            if (selectedTour instanceof StandardTour) {
                System.out.print("Choose hotel (3-star/4-star/5-star): ");
                hotel = scanner.nextLine();
            } else if (selectedTour instanceof PremiumTour) {
                System.out.print("Choose hotel (3-star/4-star/5-star): ");
                hotel = scanner.nextLine();
                System.out.println("Recommended guides:");
                System.out.println("1. Sara Jordan");
                System.out.println("2. Jake Chris");
                System.out.println("3. Norman Aliy");
                System.out.println("4. Emily Stone");
                System.out.println("5. Michael Brown");
                System.out.println("6. Anna Smith");
                System.out.println("7. David Lee");
                System.out.println("8. Linda White");


                System.out.print("Choose guide: ");
                guide = scanner.nextLine();
            }

            System.out.print("Would you like to include transportation for an additional $" + TRANSPORTATION_COST + "? (yes/no): ");
            String transportationChoice = scanner.nextLine().toLowerCase();
            double totalCost = selectedTour.getPrice();
            if ("yes".equals(transportationChoice)) {
                totalCost += TRANSPORTATION_COST;
            }

            if (selectedTour instanceof StandardTour) {
                ((StandardTour) selectedTour).addTransportation(new Transportation("Flight", 200, 2)); // пример
            } else if (selectedTour instanceof PremiumTour) {
                ((PremiumTour) selectedTour).addTransportation(new Transportation("Flight", 200, 2)); // пример
            }

            System.out.print("Choose payment method (HalkBank/KaspiGold/PayPal): ");
            String paymentMethod = scanner.nextLine();
            PaymentService paymentService;
            switch (paymentMethod.toLowerCase()) {
                case "halkbank":
                    paymentService = new PaymentService(new HalkBankPayment());
                    break;
                case "kaspigold":
                    paymentService = new PaymentService(new KaspiGoldPayment());
                    break;
                case "paypal":
                    paymentService = new PaymentService(new PaypalPayment());
                    break;
                default:
                    System.out.println("Invalid payment method.");
                    return;
            }

            boolean success = paymentService.processPayment(totalCost);
            if (success) {
                System.out.println("Your tour is successfully booked. Enjoy your trip!");
            } else {
                System.out.println("Sorry, something went wrong. Please try again.");
            }
        } else if (tourType.equalsIgnoreCase("weekend")) {
            // Обработка для выходного тура
            System.out.print("Would you like to include transportation for an additional $" + TRANSPORTATION_COST + "? (yes/no): ");
            String transportationChoice = scanner.nextLine().toLowerCase();
            double totalCost = selectedTour.getPrice();
            if ("yes".equals(transportationChoice)) {
                totalCost += TRANSPORTATION_COST;
            }

            System.out.print("Choose payment method (HalkBank/KaspiGold/PayPal): ");
            String paymentMethod = scanner.nextLine();
            PaymentService paymentService;
            switch (paymentMethod.toLowerCase()) {
                case "halkbank":
                    paymentService = new PaymentService(new HalkBankPayment());
                    break;
                case "kaspigold":
                    paymentService = new PaymentService(new KaspiGoldPayment());
                    break;
                case "paypal":
                    paymentService = new PaymentService(new PaypalPayment());
                    break;
                default:
                    System.out.println("Invalid payment method.");
                    return;
            }

            boolean success = paymentService.processPayment(totalCost);
            if (success) {
                System.out.println("Your weekend tour is successfully booked. Enjoy your trip!");
            } else {
                System.out.println("Sorry, something went wrong. Please try again.");
            }
        }
    }
}
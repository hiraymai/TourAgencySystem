package factories;

import models.Tour; // Убедитесь, что Tour импортирован
import models.StandardTour;
import models.PremiumTour;
import models.WeekendRate; // Убедитесь, что WeekendTour импортирован

public class TourFactory {
    public static Tour createTour(String type, String name, double price, String city, String hotel, String guide) {
        switch (type.toLowerCase()) {
            case "standard":
                return new StandardTour(name, price, city, hotel); // Убедитесь, что StandardTour имеет такой конструктор
            case "premium":
                return new PremiumTour(name, price, city, hotel, guide); // Убедитесь, что PremiumTour имеет такой конструктор
            case "weekend":
                return new WeekendRate(name, price, city); // Изменено с WeekendRate на WeekendTour
            default:
                throw new IllegalArgumentException("Unknown tour type.");
        }
    }
}


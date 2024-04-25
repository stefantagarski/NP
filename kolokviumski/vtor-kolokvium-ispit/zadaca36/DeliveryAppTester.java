package vtorKolokvium.zadaca36;

import java.util.*;

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class Address {
    String name;
    Location location;

    public Address(String name, Location location) {
        this.name = name;
        this.location = location;
    }


    public Location getLocation() {
        return location;
    }
}

class User {
    String id;
    String name;

    Map<String, Address> addresses = new HashMap<>();
    List<Float> moneySpent = new ArrayList<>();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public double total() {
        return moneySpent.stream().mapToDouble(i -> i).sum();
    }

    public double average() {
        if (moneySpent.isEmpty()) {
            return 0;
        } else {
            return total() / moneySpent.size();
        }
    }

    public void addAddress(String name, Location location) {
        addresses.putIfAbsent(name, new Address(name, location));
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id, name, moneySpent.size(), total(), average());
    }

    public void processOrder(float cost) {
        moneySpent.add(cost);
    }
}

class DeliveryPerson {
    String id;
    String name;
    Location currentLocation;

    List<Float> moneyEarned = new ArrayList<>();

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return id;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }


    public double total() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }

    public double average() {
        if (moneyEarned.isEmpty()) {
            return 0;
        } else {
            return total() / moneyEarned.size();
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, moneyEarned.size(), total(), average());
    }

    public int compareDistance(DeliveryPerson other, Location resturantLocation) {
        int currentDistance = currentLocation.distance(resturantLocation);
        int otherDistance = other.currentLocation.distance(resturantLocation);

        if (currentDistance == otherDistance) {
            return Integer.compare(this.moneyEarned.size(), other.moneyEarned.size());
        } else {
            return currentDistance - otherDistance;
        }
    }

    public void processOrder(int distance, Location location) {
        currentLocation = location;
        moneyEarned.add((float) (90 + 10 * (distance / 10)));
    }
}

class Restaurant {
    String id;
    String name;
    Location location;

    List<Float> moneyEarned = new ArrayList<>();

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public double total() {
        return moneyEarned.stream().mapToDouble(i -> i).sum();
    }

    public double average() {
        if (moneyEarned.isEmpty()) {
            return 0;
        } else {
            return total() / moneyEarned.size();
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, moneyEarned.size(), total(), average());
    }

    public void processOrder(float cost) {
        moneyEarned.add(cost);
    }
}

class DeliveryApp {
    String appName;

    public DeliveryApp(String appName) {
        this.appName = appName;
    }

    Map<String, User> users = new HashMap<>();
    Map<String, DeliveryPerson> deliveryPersons = new HashMap<>();
    Map<String, Restaurant> restaurants = new HashMap<>();

    public void addUser(String id, String name) {
        users.putIfAbsent(id, new User(id, name));
    }

    public void registerDeliveryPerson(String id, String name, Location location) {
        deliveryPersons.putIfAbsent(id, new DeliveryPerson(id, name, location));
    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.putIfAbsent(id, new Restaurant(id, name, location));
    }

    public void addAddress(String id, String name, Location location) {
        users.get(id).addAddress(name, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        User user = users.get(userId);
        Address address = user.addresses.get(userAddressName);
        Restaurant restaurant = restaurants.get(restaurantId);

        DeliveryPerson deliveryPerson = deliveryPersons.values().stream()
                .min((a, b) -> a.compareDistance(b, restaurant.getLocation())).get();

        int distance = deliveryPerson.getCurrentLocation().distance(restaurant.getLocation());

        user.processOrder(cost);
        restaurant.processOrder(cost);
        deliveryPerson.processOrder(distance, address.getLocation());
    }

    public void printUsers() {
        users.values().stream().sorted(Comparator.comparing(User::total).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream().sorted(Comparator.comparing(Restaurant::average)
                        .thenComparing(Restaurant::getId).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPersons.values().stream().sorted(Comparator.comparing(DeliveryPerson::total)
                .thenComparing(DeliveryPerson::getId).reversed()).forEach(System.out::println);
    }

}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }
        }
    }
}

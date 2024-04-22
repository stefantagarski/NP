package aud9;

import java.util.*;
import java.util.stream.Collectors;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }
}

class PhoneBook {
    Set<String> allnumbers =  new HashSet<>();
    Map<String, TreeSet<Contact>> contacts = new HashMap<>();

    Comparator<Contact> comparator = Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber);

    public void addContact(String name, String number) throws DuplicateNumberException {
        if(allnumbers.contains(number)) {
            throw new DuplicateNumberException(String.format("Duplicate number: %s",number));
        }
        contacts.putIfAbsent(name, new TreeSet<>(comparator));
        contacts.get(name).add(new Contact(name, number));
        allnumbers.add(number);
    }

    public void  contactsByName(String name) {
        if(contacts.containsKey(name)) {
             contacts.get(name).forEach(System.out::println);
        }else {
            System.out.println("NOT FOUND");
        }
    }

    public void contactsByNumber(String number) {

        if(number.length() < 3) {
            return;
        }

        TreeSet<Contact> result = contacts.values().stream()
                .flatMap(TreeSet::stream)
                .filter(contact -> contact.getNumber().contains(number))
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        if(!result.isEmpty()) {
            result.forEach(System.out::println);
        }else {
            System.out.println("NOT FOUND");
        }

    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде


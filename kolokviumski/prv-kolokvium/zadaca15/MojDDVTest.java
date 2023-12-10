package zadaca15;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

enum DDV_TYPE {
    A, B, V
}

class Item {
    int price;
    DDV_TYPE type;

    public Item() {
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(DDV_TYPE type) {
        this.type = type;
    }

    public double getTaxReturn() {
        if (type == DDV_TYPE.A) {
            return price * 0.18 * 0.15;
        }
        if (type == DDV_TYPE.B) {
            return price * 0.05 * 0.15;
        }

        return 0;
    }
}

class Smetka {
    String id;
    List<Item> items;

    public Smetka(String id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public static Smetka create(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        List<Item> items = new ArrayList<>();
        String id = parts[0];
        Item item = new Item();

        for (int i = 1; i < parts.length; i++) {
            if (i % 2 == 0) {
                item.setType((DDV_TYPE.valueOf(parts[i])));
                items.add(item);
                item = new Item();
            } else {
                item.setPrice(Integer.parseInt(parts[i]));
            }
        }
        int sum = items.stream().mapToInt(item1 -> item1.price).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }

        return new Smetka(id, items);
    }

    public int sum() {
        return items.stream().mapToInt(item -> item.price).sum();
    }

    public double taxReturn() {
        return items.stream().mapToDouble(Item::getTaxReturn).sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", id, sum(), taxReturn());
    }
}

class MojDDV {

    List<Smetka> smetki;

    public MojDDV() {
        smetki = new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        smetki = br.lines().map(line -> {
                    try {
                        return Smetka.create(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public void printTaxReturns(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        smetki.forEach(smetka -> pw.println(smetka.toString()));

        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
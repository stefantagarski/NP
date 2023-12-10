package zadaca16;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",sum));
    }
}

enum DDV_TYPE {
    A, B, V
}

class Proizvod {
    int price;
    DDV_TYPE type;

    public Proizvod() {
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(DDV_TYPE type) {
        this.type = type;
    }

    public double taxRefund() {
        if (type == DDV_TYPE.A) {
            return price * 0.18 * 0.15;
        }
        if (type == DDV_TYPE.B) {
            return price * 0.05 * 0.15;
        }
        return 0;
    }
}

class Smetki {
    String id;
    List<Proizvod> proizvodi;

    public Smetki(String id, List<Proizvod> proizvodi) {
        this.id = id;
        this.proizvodi = proizvodi;
    }

    public static Smetki create(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Proizvod> items = new ArrayList<>();
        Proizvod proizvodi = new Proizvod();

        for (int i = 1; i < parts.length ; i++) {
            if (i % 2 == 0) {
                proizvodi.setType(DDV_TYPE.valueOf(parts[i]));
                items.add(proizvodi);
                proizvodi = new Proizvod();
            } else{
                proizvodi.setPrice(Integer.parseInt(parts[i]));
            }
        }

        int sum = items.stream().mapToInt(line2 -> line2.price).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        return new Smetki(id,items);
    }

    public double TaxRefunds() {
        return proizvodi.stream().mapToDouble(Proizvod::taxRefund).sum();
    }

    public int sum() {
        return proizvodi.stream().mapToInt(item -> item.price).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f",id,sum(),TaxRefunds());
    }


}


class MojDDV {
    List<Smetki> smetki;

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

       smetki = br.lines().map(line -> {
                   try {
                       return Smetki.create(line);
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

    void printStatistics(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics summaryStatistics = smetki.stream().mapToDouble(Smetki::TaxRefunds).summaryStatistics();
        pw.println(String.format("min:\t%05.03f\nmax:\t%05.03f\nsum:\t%05.03f\ncount:\t%-5d\navg:\t%05.03f",
                summaryStatistics.getMin(),
                summaryStatistics.getMax(),
                summaryStatistics.getSum(),
                summaryStatistics.getCount(),
                summaryStatistics.getAverage()));

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

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
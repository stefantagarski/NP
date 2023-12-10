package zadaca5;

import java.util.Scanner;

class MinMax<T extends Comparable<T>> {
    T min;
    T max;
    int count;
    int maxcount = 0;
    int mincount = 0;


    public MinMax() {
        min = null;
        max = null;
        count = 0;
    }

    public void update(T s) {

            if (min == null || s.compareTo(min) < 0) {
                min = s;
                mincount = 1;
            } else if (s.compareTo(min) == 0) {
                mincount++;
            }
            if (max == null || s.compareTo(max) > 0) {
                max = s;
                maxcount = 1;
            } else if (s.compareTo(max) == 0) {
                maxcount++;
            }

        count++;
    }

    public T min() {
        return min;
    }

    public T max() {
        return max;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min(), max(), count - maxcount - mincount);
    }
}

public class MinAndMax {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        MinMax<String> strings = new MinMax<>();
        for (int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);

        MinMax<Integer> ints = new MinMax<>();
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}

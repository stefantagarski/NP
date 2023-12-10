package zadaca1;


import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int side;

    public Square(int side) {
        this.side = side;
    }

    public int Perimeter() {
        return 4 * side;
    }
}

class Canvas  implements Comparable<Canvas>{
    String id;
    List<Square> squares;

    public Canvas(String id, List<Square> squares) {
        this.id = id;
        this.squares = squares;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
//        List<Square> squareList = new ArrayList<>();
//        for (int i = 0; i < parts.length; i++) {
//            squareList.add(new Square(Integer.parseInt(parts[i])));
//        }

        List<Square> squareList = Arrays.stream(parts)
                .skip(1)
                .map(Integer::parseInt)
                .map(Square::new)
                .collect(Collectors.toList());

        return new Canvas(id, squareList);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",
                id,
                squares.size(),
                sumOfPerimeter()
        );
    }

    private int sumOfPerimeter() {
        return  squares.stream().mapToInt(Square::Perimeter).sum();

    }

    @Override
    public int compareTo(Canvas o) {
        return Integer.compare(this.sumOfPerimeter(),o.sumOfPerimeter());
    }
}

class ShapesApplication {

    List<Canvas> canvas;

    public int readCanvases(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        canvas = br.lines()
                .map(Canvas::createCanvas)
                .collect(Collectors.toList());

        return canvas.stream()
                .mapToInt(canvas -> canvas.squares.size())
                .sum();
    }

    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        Canvas max =  canvas.stream().max(Comparator.naturalOrder()).get();

        pw.println(max);
        pw.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}

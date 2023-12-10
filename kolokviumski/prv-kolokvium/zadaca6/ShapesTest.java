package zadaca6;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE

}
interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable extends Comparable<Stackable>{
    float weight();

    @Override
    default int compareTo(Stackable o) {
        return Float.compare(weight(),o.weight());
    }
}

class Shape implements Scalable,Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void scale(float scaleFactor) {

    }

    @Override
    public float weight() {
        return 0;
    }
}


class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius*= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f",id,getColor(),weight());
    }
}

class Rectangle extends Shape {
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f", id, getColor(), weight());    }
}

class Canvas {
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }
    private void addShape(Shape s) {
        if (shapes.isEmpty()) {
            shapes.add(s);
            return;
        }
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).weight() < s.weight()) {
                shapes.add(i,s);
                return;
            }
        }
        shapes.add(s);
    }
    public void add(String id, Color color, float radius) {
        Circle c = new Circle(id,color,radius);
        addShape(c);
    }

    public void add(String id, Color color, float width, float height) {
        Rectangle r = new Rectangle(id,color,width,height);
        addShape(r);
    }

    public void scale(String id, float scaleFactor) {

        for (int i = 0; i < shapes.size(); i++) {
            if (id.equals(shapes.get(i).getId())) {
                Shape s = shapes.get(i);
                shapes.remove(s);
                s.scale(scaleFactor);
                addShape(s);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(shape).append("\n");
        }
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

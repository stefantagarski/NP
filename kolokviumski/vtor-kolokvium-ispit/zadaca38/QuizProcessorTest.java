package vtorKolokvium.zadaca38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class InvalidSizeException extends Exception {
    public InvalidSizeException() {
        super("A quiz must have same number of correct and selected answers");
    }
}

class Quiz {
    String studentIndex;
    List<String> correctAnswers;
    List<String> studentAnswers;
    double studentPoints;

    public Quiz(String studentIndex, List<String> correctAnswers, List<String> studentAnswers) throws InvalidSizeException {
        this.studentIndex = studentIndex;
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;

        if (this.correctAnswers.size() != this.studentAnswers.size()) {
            throw new InvalidSizeException();
        }
        for (int i = 0; i < correctAnswers.size(); i++) {
            if(studentAnswers.get(i).equals(correctAnswers.get(i))) {
                studentPoints++;
            }else {
                studentPoints -= 0.25;
            }
        }
    }

    public static Quiz create(String line) throws InvalidSizeException {
        //151020;A, B, C;A, C, C
        String[] parts = line.split(";");
        String ID = parts[0];
        List<String> correctAnswers = new ArrayList<>(Arrays.asList(parts[1].split(",")));
        List<String> studentAnswers = new ArrayList<>(Arrays.asList(parts[2].split(",")));

        return new Quiz(ID, correctAnswers, studentAnswers);
    }

    public String getStudentIndex() {
        return studentIndex;
    }

    public double getStudentPoints() {
        return studentPoints;
    }
}

class QuizProcessor {
    public static Map<String, Double> processAnswers(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        return br.lines().map(line -> {
                    try {
                        return Quiz.create(line);
                    } catch (InvalidSizeException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getStudentIndex, Quiz::getStudentPoints, Double::sum, TreeMap::new));
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}
package zadaca23;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Question implements Comparable<Question> {
    String text;
    int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    abstract double answerQuestion(String odgovor);

    static List<String> ALLOWED_ANSWERS = Arrays.asList("A", "B", "C", "D", "E");

    public static Question create(String line) throws InvalidOperationException {
        //MC;Question2;4;E
        //TF;Question3;2;false
        String[] parts = line.split(";");
        String type = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String answer = parts[3];
        if (type.equals("MC")) {
            if (!ALLOWED_ANSWERS.contains(answer)) {
                throw new InvalidOperationException(String.format("%s is not allowed option for this question", answer));
            }
            return new MultipleChoice(text, points, answer);
        } else {
            return new TrueFalse(text, points, Boolean.parseBoolean(answer));
        }
    }

    @Override
    public int compareTo(Question o) {
        return Integer.compare(this.points, o.points);
    }
}

class TrueFalse extends Question {
    boolean answer;

    public TrueFalse(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", text, points, answer);
    }


    @Override
    double answerQuestion(String odgovor) {
        return answer == Boolean.parseBoolean(odgovor) ? points : 0.0;
    }
}

class MultipleChoice extends Question {
    String answer;

    public MultipleChoice(String text, int points, String answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", text, points, answer);
    }

    @Override
    double answerQuestion(String odgovor) {
        return answer.equals(odgovor) ? points : (points * -0.2);
    }
}


class Quiz {

    List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(String questionData) {

        try {
            questions.add(Question.create(questionData));
        } catch (InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printQuiz(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        questions.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);

        pw.flush();

    }


    public void answerQuiz(List<String> answers, OutputStream out) throws InvalidOperationException {

        if (questions.size() != answers.size()) {
            throw new InvalidOperationException(("Answers and questions must be of same length!"));
        }

        PrintWriter pw = new PrintWriter(out);

        double sum = 0;
        for (int i = 0; i < answers.size(); i++) {
            double points = questions.get(i).answerQuestion(answers.get(i));
            pw.println(String.format("%d. %.2f", i + 1, points));
            sum += points;
        }

        pw.println(String.format("Total points: %.2f", sum));

        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}

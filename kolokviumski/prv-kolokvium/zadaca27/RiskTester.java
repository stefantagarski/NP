package zadaca27;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Round {
    List<Integer> attacker;
    List<Integer> defender;

    public Round(String line) {

        String[] parts = line.split(";");
        attacker = parseDice(parts[0]);
        defender = parseDice(parts[1]);
    }

    private List<Integer> parseDice(String part) {
        return Arrays.stream(part.split("\\s+"))
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public void countWinners() {
        int countAttacker = 0;
        int countDefender = 0;

        for (int i = 0; i < 3; i++) {
            if (defender.get(i) >= attacker.get(i)){
                countDefender++;
            }else {
                countAttacker++;
            }
        }
        System.out.println(countAttacker + " " + countDefender);
    }


}

class Risk {

    public void processAttacksData(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        br.lines().map(Round::new).forEach(Round::countWinners);
    }
}
public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
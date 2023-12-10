package zadaca24;

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
        this.attacker = parseDice(parts[0]);
        this.defender = parseDice(parts[1]);

    }

    private List<Integer> parseDice(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(lines -> Integer.parseInt(lines))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public boolean AttackerWin() {
        for (int i = 0; i < 3; i++) {
            if (attacker.get(i) <= defender.get(i)) {
                return false;
            }
        }
        return true;
    }

}

class Risk {

    List<Round> rounds;

    public int processAttacksData(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        rounds =  br.lines().map(Round::new).collect(Collectors.toList());

        return (int) rounds.stream().filter(round -> round.AttackerWin()).count();

        }
}


public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}
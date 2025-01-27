import java.util.*;
import java.util.stream.Collectors;

public class LeagueRanking {

    public static class Team implements Comparable<Team> {
        String name;
        int points;

        public Team(String name, int points) {
            this.name = name;
            this.points = points;
        }

        @Override
        public int compareTo(Team other) {
            if (this.points != other.points) {
                return other.points - this.points;
            }
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            String suffix = (points == 1) ? "pt" : "pts";
            return name + ", " + points + " " + suffix;
        }
    }

    public static Map<String, Integer> parseResults(List<String> results) {
        Map<String, Integer> pointsTable = new HashMap<>();

        for (String result : results) {
            String[] parts = result.split(",");
            String[] team1Data = parts[0].trim().split(" ");
            String[] team2Data = parts[1].trim().split(" ");

            String team1 = String.join(" ", Arrays.copyOf(team1Data, team1Data.length - 1));
            int score1 = Integer.parseInt(team1Data[team1Data.length - 1]);

            String team2 = String.join(" ", Arrays.copyOf(team2Data, team2Data.length - 1));
            int score2 = Integer.parseInt(team2Data[team2Data.length - 1]);

            int team1Points = 0;
            int team2Points = 0;

            if (score1 > score2) {
                team1Points = 3;
            } else if (score1 < score2) {
                team2Points = 3;
            } else {
                team1Points = 1;
                team2Points = 1;
            }

            pointsTable.put(team1, pointsTable.getOrDefault(team1, 0) + team1Points);
            pointsTable.put(team2, pointsTable.getOrDefault(team2, 0) + team2Points);
        }

        return pointsTable;
    }

    public static String rankTeams(List<String> results) {
        Map<String, Integer> pointsTable = parseResults(results);

        List<Team> teams = pointsTable.entrySet().stream()
                .map(entry -> new Team(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(Collectors.toList());

        StringBuilder output = new StringBuilder();
        int rank = 1;
        int previousPoints = -1;
        int displayedRank = 1;

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);

            if (team.points != previousPoints) {
                displayedRank = rank;
            }

            output.append(displayedRank).append(". ").append(team).append("\n");

            previousPoints = team.points;
            rank++;
        }

        return output.toString().trim();
    }

    public static void main(String[] args) {
        List<String> sampleInput = Arrays.asList(
                "Lions 3, Snakes 3",
                "Tarantulas 1, FC Awesome 0",
                "Lions 1, FC Awesome 1",
                "Tarantulas 3, Snakes 1",
                "Lions 4, Grouches 0"
        );

        System.out.println(rankTeams(sampleInput));
    }
}

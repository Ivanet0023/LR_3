import battle.Battle;
import battle.TeamBattle;
import droids.Droid;
import droids.HealerDroid;
import droids.TankDroid;
import droids.WarriorDroid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Droid> allDroids = new ArrayList<>();
    private static List<String> lastBattleLog = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static String LOG_FILE_NAME = "log.txt";

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createDroid();
                    break;
                case 2:
                    showAllDroids();
                    break;
                case 3:
                    startOneVsOneBattle();
                    break;
                case 4:
                    startTeamBattle();
                    break;
                case 5:
                    saveLastBattleToFile();
                    break;
                case 6:
                    readBattleFromFile();
                    break;
                case 7:
                    return; // Завершуємо роботу
                default:
                    System.out.println("Incorrect choice\n");
            }
            System.out.println("\nEnter to continue");
            scanner.nextLine();
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu");
        System.out.println("1. Create droid");
        System.out.println("2. Show droid list");
        System.out.println("3. Battle 1v1");
        System.out.println("4. Team Battle");
        System.out.println("5. Save last battle to log");
        System.out.println("6. Play demo of log battle");
        System.out.println("7. Exit");
    }

    // Допоміжний метод для безпечного зчитування числа
    private static int getUserChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.print("Enter the numb");
            }
        }
    }

    private static void createDroid() {
        System.out.println("Select droid type:");
        System.out.println("1. Warrior");
        System.out.println("2. Healer");
        System.out.println("3. Tank");
        int type = getUserChoice();

        System.out.print("Enter droid name: ");
        String name = scanner.nextLine();

        Droid newDroid;
        if (type == 1) {
            newDroid = new WarriorDroid(name);
            System.out.println("Warrior " + name + " created");
        } else if (type == 2) {
            newDroid = new HealerDroid(name);
            System.out.println("Healer " + name + " created");
        } else if (type == 3) {
            newDroid = new TankDroid(name);
            System.out.println("Tank " + name + " created");
        } else {
            System.out.println("Incorrect choice, return");
            return;
        }
        allDroids.add(newDroid);
    }

    private static void showAllDroids() {
        System.out.println("\nAll droids:");
        if (allDroids.isEmpty()) {
            System.out.println("DroidList is empty");
            return;
        }
        for (int i = 0; i < allDroids.size(); i++) {
            System.out.println((i + 1) + ". " + allDroids.get(i));
        }
    }

    private static void startOneVsOneBattle() {
        System.out.println("\nBattle 1v1");
        if (allDroids.size() < 2) {
            System.out.println("You need at least 2 droid for this battle");
            return;
        }

        showAllDroids();
        System.out.println("Select first droid: ");
        int index1 = getUserChoice() - 1;
        System.out.println("Select second froid: ");
        int index2 = getUserChoice() - 1;

        if (index1 < 0 || index1 >= allDroids.size() || index2 < 0 || index2 >= allDroids.size()) {
            System.out.println("Incorrect droid ID's, battle canceled");
            return;
        }

        Droid droid1 = allDroids.get(index1);
        Droid droid2 = allDroids.get(index2);

        System.out.println("Battle begins");
        Battle battle = new Battle(droid1, droid2);
        lastBattleLog = battle.fight();
    }

    private static void startTeamBattle() {
        System.out.println("\nTeam Battle");
        if (allDroids.size() < 2) {
            System.out.println("You need at least 2 droid for this battle");
            return;
        }

        System.out.println("Team 1:");
        List<Droid> team1 = selectTeam();
        System.out.println("\nTeam 2:");
        List<Droid> team2 = selectTeam();

        if (team1.isEmpty() || team2.isEmpty()) {
            System.out.println("One of team is empty.");
            return;
        }

        System.out.println("Team battle begins");
        TeamBattle teamBattle = new TeamBattle(team1, team2);
        lastBattleLog = teamBattle.fight();
    }

    private static List<Droid> selectTeam() {
        List<Droid> team = new ArrayList<>();
        while (true) {
            System.out.println("\nAvailable droids:");
            showAllDroids();
            System.out.println("Enter droid ID to add to the team. (0 to finish):");
            int choice = getUserChoice();
            if (choice == 0) {
                break;
            }

            int index = choice - 1;
            if (index >= 0 && index < allDroids.size()) {
                team.add(allDroids.get(index));
                System.out.println(allDroids.get(index).getName() + " added to team.");
            } else {
                System.out.println("Incorrect ID.");
            }
        }
        System.out.println("Team members " + team.stream().map(Droid::getName).toList());
        return team;
    }

    private static void saveLastBattleToFile() {
        System.out.println("\nSaving logs");
        if (lastBattleLog.isEmpty()) {
            System.out.println("Log is empty");
            return;
        }

        try {
            Files.write(Paths.get(LOG_FILE_NAME), lastBattleLog,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Battle saved to file: " + LOG_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private static void readBattleFromFile() {
        System.out.println("\nReading battle log ");
        try {
            List<String> logLines = Files.readAllLines(Paths.get(LOG_FILE_NAME));

            if (logLines.isEmpty()) {
                System.out.println("Log is empty.");
                return;
            }

            System.out.println("Log begin " + LOG_FILE_NAME);
            for (String line : logLines) {
                System.out.println(line);
            }
            System.out.println("End of file");

        } catch (IOException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
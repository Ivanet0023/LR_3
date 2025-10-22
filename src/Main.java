import battle.Battle;
import battle.TeamBattle;
import droids.Droid;
import droids.HealerDroid;
import droids.WarriorDroid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<Droid> allDroids = new ArrayList<>();
    private static List<String> lastBattleLog = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String LOG_FILE_NAME = "battle_log.txt";

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
                    System.out.println("Вихід з програми...");
                    scanner.close(); // Закриваємо сканер перед виходом
                    return; // Завершуємо роботу
                default:
                    System.out.println("Невірний вибір. Будь ласка, спробуйте ще раз.");
            }
            // Пауза, щоб користувач встиг прочитати вивід
            System.out.println("\nНатисніть Enter для продовження...");
            scanner.nextLine();
        }
    }

    // --- 1. Друк меню ---
    private static void printMenu() {
        System.out.println("\n--- DROID ARENA MENU ---");
        System.out.println("1. Створити дроїда");
        System.out.println("2. Показати список створених дроїдів");
        System.out.println("3. Запустити бій 1 на 1");
        System.out.println("4. Запустити бій команда на команду");
        System.out.println("5. Записати останній бій у файл");
        System.out.println("6. Відтворити бій зі збереженого файлу");
        System.out.println("7. Вийти з програми");
        System.out.print("Ваш вибір: ");
    }

    // Допоміжний метод для безпечного зчитування числа
    private static int getUserChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                return choice;
            } catch (NumberFormatException e) {
                System.out.print("Будь ласка, введіть число: ");
            }
        }
    }

    // --- 1. Створення дроїда ---
    private static void createDroid() {
        System.out.println("\n--- Створення дроїда ---");
        System.out.println("Оберіть тип дроїда:");
        System.out.println("1. Воїн (Warrior)");
        System.out.println("2. Хілер (Healer)");
        System.out.print("Тип: ");
        int type = getUserChoice();

        System.out.print("Введіть ім'я дроїда: ");
        String name = scanner.nextLine();

        Droid newDroid;
        if (type == 1) {
            newDroid = new WarriorDroid(name);
            System.out.println("Воїн " + name + " створений!");
        } else if (type == 2) {
            newDroid = new HealerDroid(name);
            System.out.println("Хілер " + name + " створений!");
        } else {
            System.out.println("Невірний тип, створення скасовано.");
            return;
        }
        allDroids.add(newDroid); // Додаємо в загальний список
    }

    // --- 2. Показ усіх дроїдів ---
    private static void showAllDroids() {
        System.out.println("\n--- Список створених дроїдів ---");
        if (allDroids.isEmpty()) {
            System.out.println("У вас ще немає дроїдів. Створіть їх у пункті 1.");
            return;
        }
        // Нумеруємо список для зручного вибору в бою
        for (int i = 0; i < allDroids.size(); i++) {
            System.out.println((i + 1) + ". " + allDroids.get(i)); // Використовуємо .toString()
        }
    }

    // --- 3. Бій 1 на 1 ---
    private static void startOneVsOneBattle() {
        System.out.println("\n--- Бій 1 на 1 ---");
        if (allDroids.size() < 2) {
            System.out.println("Для бою потрібно щонайменше 2 дроїди.");
            return;
        }

        showAllDroids(); // Показуємо список
        System.out.print("Оберіть номер першого дроїда: ");
        int index1 = getUserChoice() - 1; // -1 бо нумерація для юзера з 1
        System.out.print("Оберіть номер другого дроїда: ");
        int index2 = getUserChoice() - 1;

        // Перевірка коректності індексів
        if (index1 < 0 || index1 >= allDroids.size() || index2 < 0 || index2 >= allDroids.size()) {
            System.out.println("Невірні номери дроїдів. Бій скасовано.");
            return;
        }

        Droid droid1 = allDroids.get(index1);
        Droid droid2 = allDroids.get(index2);

        System.out.println("Бій починається!");
        Battle battle = new Battle(droid1, droid2);
        lastBattleLog = battle.fight(); // Запускаємо бій і ЗБЕРІГАЄМО ЛОГ
    }

    // --- 4. Командний бій ---
    private static void startTeamBattle() {
        System.out.println("\n--- Командний бій ---");
        if (allDroids.size() < 2) {
            System.out.println("Для командного бою потрібно щонайменше 2 дроїди.");
            return;
        }

        System.out.println("Формуємо Команду 1:");
        List<Droid> team1 = selectTeam();
        System.out.println("\nФормуємо Команду 2:");
        List<Droid> team2 = selectTeam();

        if (team1.isEmpty() || team2.isEmpty()) {
            System.out.println("Одна з команд порожня. Бій скасовано.");
            return;
        }

        System.out.println("Командний бій починається!");
        TeamBattle teamBattle = new TeamBattle(team1, team2);
        lastBattleLog = teamBattle.fight(); // Запускаємо бій і ЗБЕРІГАЄМО ЛОГ
    }

    // Допоміжний метод для вибору команди
    private static List<Droid> selectTeam() {
        List<Droid> team = new ArrayList<>();
        while (true) {
            System.out.println("\nДоступні дроїди:");
            showAllDroids();
            System.out.println("Введіть номер дроїда, щоб додати його в команду (або 0, щоб завершити):");
            int choice = getUserChoice();

            if (choice == 0) {
                break; // Завершуємо вибір
            }
            int index = choice - 1;
            if (index >= 0 && index < allDroids.size()) {
                team.add(allDroids.get(index));
                System.out.println(allDroids.get(index).getName() + " доданий до команди.");
            } else {
                System.out.println("Невірний номер.");
            }
        }
        System.out.println("Формування команди завершено. Склад: " + team.stream().map(Droid::getName).toList());
        return team;
    }

    private static void saveLastBattleToFile() {
        System.out.println("\n--- Збереження логу ---");
        if (lastBattleLog.isEmpty()) {
            System.out.println("Лог останнього бою порожній. Спочатку проведіть бій.");
            return;
        }

        try {
            // Files.write запише список рядків у файл.
            // StandardOpenOption.CREATE - створить файл, якщо його немає.
            // StandardOpenOption.TRUNCATE_EXISTING - перезапише файл, якщо він вже існує.
            Files.write(Paths.get(LOG_FILE_NAME), lastBattleLog,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Бій успішно записано у файл: " + LOG_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Помилка! Не вдалося записати файл: " + e.getMessage());
        }
    }

    // --- 6. Читання логу ---
    private static void readBattleFromFile() {
        System.out.println("\n--- Відтворення бою з файлу ---");
        try {
            // Читаємо всі рядки з файлу в список
            List<String> logLines = Files.readAllLines(Paths.get(LOG_FILE_NAME));

            if (logLines.isEmpty()) {
                System.out.println("Файл логу порожній.");
                return;
            }

            System.out.println("--- ПОЧАТОК ЛОГУ " + LOG_FILE_NAME + " ---");
            // Просто друкуємо кожен рядок
            for (String line : logLines) {
                System.out.println(line);
                // Можна додати невелику затримку для "ефекту" відтворення
                // try { Thread.sleep(300); } catch (InterruptedException e) {}
            }
            System.out.println("--- КІНЕЦЬ ЛОГУ ---");

        } catch (IOException e) {
            System.out.println("Помилка! Не вдалося прочитати файл: " + e.getMessage());
            System.out.println("Переконайтеся, що файл " + LOG_FILE_NAME + " існує і ви зберегли бій.");
        }
    }
}
package battle;

import droids.Droid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TeamBattle {
    private List<Droid> team1;
    private List<Droid> team2;
    private Random random = new Random();
    private List<String> battleLog = new ArrayList<>();

    public TeamBattle(List<Droid> team1, List<Droid> team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public List<String> fight() {
        battleLog.clear();
        log("Battle started!");
        log("Team 1: " + getTeamNames(team1));
        log("Team 2: " + getTeamNames(team2));

        int round = 1;

        while (isTeamAlive(team1) && isTeamAlive(team2)) {
            log("\nRound " + round++);

            performRound(team1, team2);
            if (isTeamAlive(team2)) {
                performRound(team2, team1);
            }
        }

        String winnerTeam = isTeamAlive(team1) ? "Team 1" : "Team 2";
        log(winnerTeam + "Wins");

        team1.forEach(Droid::resetHealth);
        team2.forEach(Droid::resetHealth);

        return battleLog;
    }



    // Далі жуть
    private void performRound(List<Droid> attackers, List<Droid> defenders) {
        for (Droid attacker : attackers) {
            if (!attacker.isAlive()) {
                continue;
            }

            if (attacker.getHeal() > 0) {
                List<Droid> woundedAllies = attackers.stream()
                        .filter(ally -> ally.isAlive() && ally.getHealth() < ally.getMaxHealth())
                        .collect(Collectors.toList());

                if (!woundedAllies.isEmpty()) {
                    // Якщо є поранені, лікує випадкового з них
                    Droid targetToHeal = woundedAllies.get(random.nextInt(woundedAllies.size()));
                    int healAmount = attacker.getHeal();
                    int healthBefore = targetToHeal.getHealth();
                    targetToHeal.receiveHeal(healAmount);
                    log(String.format("%s (Хілер) лікує %s. Здоров'я: %d -> %d",
                            attacker.getName(), targetToHeal.getName(), healthBefore, targetToHeal.getHealth()));
                } else {
                    log(attacker.getName() + " (Хілер) не знайшов цілі для лікування.");
                }
            }
            // --- ЛОГІКА АТАКУЮЧОГО ДРОЇДА ---
            else if (attacker.getDamage() > 0) {
                // Атакуючий шукає живу ціль у команді ворога
                List<Droid> aliveDefenders = defenders.stream()
                        .filter(Droid::isAlive)
                        .collect(Collectors.toList());

                // Якщо ворогів не залишилось, нічого не робимо
                if (aliveDefenders.isEmpty()) {
                    continue;
                }

                // Атакуємо випадкового живого ворога
                Droid targetToAttack = aliveDefenders.get(random.nextInt(aliveDefenders.size()));
                int damage = attacker.getDamage();
                targetToAttack.takeDamage(damage);

                log(String.format("%s завдає %d шкоди %s. У %s залишилось %d HP.",
                        attacker.getName(), damage, targetToAttack.getName(),
                        targetToAttack.getName(), targetToAttack.getHealth()));
            }
        }
    }

    private boolean isTeamAlive(List<Droid> team) {
        for (Droid droid : team) {
            if (droid.isAlive()) {
                return true; // Знайшли живого, команда ще в бою
            }
        }
        return false; // Всі мертві
    }

    // Допоміжні методи для логування
    private String getTeamNames(List<Droid> team) {
        return team.stream().map(Droid::getName).collect(Collectors.joining(", "));
    }

    private void log(String message) {
        System.out.println(message);
        battleLog.add(message);
    }
}
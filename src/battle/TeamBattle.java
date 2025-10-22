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

    private void performRound(List<Droid> attackers, List<Droid> defenders) {
        for (Droid attacker : attackers) {
            if (!attacker.isAlive()) {
                continue;
            }

            if (attacker.getHeal() > 0) {
                List<Droid> woundedAllies = attackers.stream()
                        .filter(ally -> ally.isAlive() && ally.getHealth() < ally.getMaxHealth())
                        .toList();

                if (!woundedAllies.isEmpty()) {
                    Droid targetToHeal = woundedAllies.get(random.nextInt(woundedAllies.size()));
                    int healAmount = attacker.getHeal();
                    int healthBefore = targetToHeal.getHealth();
                    targetToHeal.receiveHeal(healAmount);
                    log(attacker.getName() + " heals " + targetToHeal.getName() + ". Health: " + healthBefore + " -> " + targetToHeal.getHealth());
                } else {
                    log(attacker.getName() + " There is no one to heal.");
                }
            }

            else if (attacker.getDamage() > 0) {
                List<Droid> aliveDefenders = defenders.stream()
                        .filter(Droid::isAlive)
                        .toList();

                if (aliveDefenders.isEmpty()) {
                    continue;
                }

                Droid targetToAttack = aliveDefenders.get(random.nextInt(aliveDefenders.size()));
                int damage = attacker.getDamage();
                targetToAttack.takeDamage(damage);

                log(attacker.getName() +" deal " + damage + " damage to: " + targetToAttack.getName() +
                                "He's HP now: " + targetToAttack.getHealth());
            }
        }
    }

    private boolean isTeamAlive(List<Droid> team) {
        for (Droid droid : team) {
            if (droid.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private String getTeamNames(List<Droid> team) {
        return team.stream().map(Droid::getName).collect(Collectors.joining(", "));
    }

    private void log(String message) {
        System.out.println(message);
        battleLog.add(message);
    }
}
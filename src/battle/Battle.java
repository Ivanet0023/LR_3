package battle;

import droids.Droid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Battle {
    private Droid droid1;
    private Droid droid2;
    private Random random = new Random();

    public Battle(Droid droid1, Droid droid2) {
        this.droid1 = droid1;
        this.droid2 = droid2;
    }

    private List<String> battleLog = new ArrayList<>();

    public List<String> fight() {
        battleLog.clear(); // Очищуємо лог перед новим боєм

        log("Battle!\n" + "Droid 1: " + droid1.getName() + "HP: " + droid1.getHealth() + "\nDroid 2: " + droid2.getName() + "HP: " + droid2.getHealth());

        Droid attacker = (random.nextBoolean()) ? droid1 : droid2;
        Droid defender = (attacker == droid1) ? droid2 : droid1;

        while (droid1.isAlive() && droid2.isAlive()) {
            int damage = attacker.getDamage();
            defender.takeDamage(damage);

            log(attacker.getName() + " deals " + damage + "damage, leaving " + defender.getName() + " with " + defender.getHealth() + "HP");

            Droid temp = attacker;
            attacker = defender;
            defender = temp;
        }

        Droid winner = (droid1.isAlive()) ? droid1 : droid2;
        log("Battle winer: " + winner.getName());

        droid1.resetHealth();
        droid2.resetHealth();

        return battleLog;
    }

    private void log(String message) {
        System.out.println(message);
        battleLog.add(message);
    }
}
package droids;

public class Droid {

    protected String name;
    protected int health;
    protected int damage;
    protected int maxHealth;
    protected int heal;

    public Droid(String name, int health, int damage, int maxHealth, int heal) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.maxHealth = maxHealth;
        this.heal = heal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void resetHealth() {
        this.health = this.maxHealth;
    }

    public void receiveHeal(int heal){
        this.health += heal;
        if (this.health >= this.maxHealth){
            this.health = this.maxHealth;
        }
    }

    @Override
    public String toString() {
        return "Droid{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", damage=" + damage +
                ", maxHealth=" + maxHealth +
                ", heal=" + heal +
                '}';
    }
}
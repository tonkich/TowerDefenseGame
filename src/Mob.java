import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Mob extends Rectangle {
    public int xCoord, yCoord;
    public int health, maxHealth = 200;
    public int healthSpace = 3, healthHeight = 6;
    public int mobSize = Screen.room.blockSize;
    public double mobWalk;
    public final int up = 0, down = 1, right = 2, left = 3;
    public int direction;
    public int mobId = Value.mobAir;
    public boolean inGame = false, died = false;
    public double xd, yd;

    public Mob() {

    }

    public void spawnMob(int mobId) {
        for (int y = 0; y < Screen.room.block.length; y++) {
            if (Screen.room.block[y][0].groundId == Value.groundRoad) {
                setBounds(Screen.room.block[y][0].x, Screen.room.block[y][0].y, mobSize, mobSize);
                xd = x;
                yd = this.y;
                xCoord = 0;
                yCoord = y;
                mobWalk = 0.0;
                direction = right;
                health = maxHealth;
                break;
            }
        }

        this.mobId = mobId;

        inGame = true;
    }

    public void deleteMob() {
        inGame = false;
    }

    public void loseHealth() {
        Screen.life -= 1;
    }

    public double walkFrame = 1, walkSpeed = (mobSize / 3) / (double) (Screen.fps);

    public void physics(int i) {

        //if (walkFrame >= walkSpeed) {
        //if (i == 0) System.out.println((int) ((Screen.timera / 60) - (Screen.timera / 60) % 1) + "\t\t" + Math.round(((Screen.timera / 60) % 1) * 60) + "\t" + (((Screen.timera / 60) % 1) * 60));

        if (direction == up) {
            yd -= walkSpeed;
            y = (int) yd;
        } else if (direction == down) {
            yd += walkSpeed;
            y = (int) yd;
        } else if (direction == right) {
            xd += walkSpeed;
            x = (int) xd;
        } else if (direction == left) {
            xd -= walkSpeed;
            x = (int) xd;
        }

        mobWalk += walkSpeed;
        if (mobWalk >= Screen.room.blockSize) {
            if (direction == up) {
                yCoord--;
            } else if (direction == down) {
                yCoord++;
            } else if (direction == right) {
                xCoord++;
            } else if (direction == left) {
                xCoord--;
            }

            try {
                if (direction != down && direction != up) {
                    if (Screen.room.block[yCoord + 1][xCoord].groundId == Value.groundRoad) {
                        direction = down;
                    } else if (Screen.room.block[yCoord - 1][xCoord].groundId == Value.groundRoad) {
                        direction = up;
                    }
                } else if (direction != right && direction != left) {
                    if (Screen.room.block[yCoord][xCoord + 1].groundId == Value.groundRoad && direction != left) {
                        direction = right;
                    } else if (Screen.room.block[yCoord][xCoord - 1].groundId == Value.groundRoad && direction != right) {
                        direction = left;
                    }
                }
            } catch (Exception e) {
            }

            if (Screen.room.block[yCoord][xCoord].airId == Value.airCave) {
                deleteMob();
                loseHealth();
            }

            mobWalk -= Screen.room.blockSize;
        }

        //walkFrame = 1;//-= walkSpeed;
        //} else
        //walkFrame++;
    }

    public void loseHealth(int amount) {
        health -= amount;
        checkDeath();
    }

    public void checkDeath() {
        if (health <= 0 && died == false) {
            deleteMob();
            died = true;
            getMoney();
            Screen.killed++;
            Screen.hasWon();
        }
    }

    public void getMoney() {
        Screen.coins += Value.deathReward[mobId];
    }

    public boolean isDead() {
        return died;
    }

    public void draw(Graphics g) {
        g.drawImage(Screen.tilesetMob[mobId], x, y, width, height, null);

        g.setColor(new Color(180, 50, 50));
        g.fillRect(x, y - healthSpace - healthHeight, mobSize, healthHeight);

        g.setColor(new Color(50, 180, 50));
        g.fillRect(x, y - healthSpace - healthHeight, mobSize * health / maxHealth, healthHeight);

        g.setColor(new Color(0, 0, 0));
        g.drawRect(x, y - healthSpace - healthHeight, mobSize, healthHeight);

    }

}
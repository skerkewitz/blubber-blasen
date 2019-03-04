package de.gierzahn.editor.map;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnemyBaseMapLayer extends BaseMapLayer {

  public static class Enemy {
    public final int id;
    public boolean isLookingLeft = true;
    public int x;
    public int y;

    public int type = 1; // zenchan

    public Enemy(int id) {
      this.id = id;
    }

    public void setPosition(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private int nextId = 1;
  private List<Enemy> enemyList = new ArrayList<>();


  public EnemyBaseMapLayer(int width, int height) {
    super(width, height);
  }


  public int getNextId() {
    int id = nextId;
    nextId += 1;
    return id;
  }

  public Enemy addEnemyAt(Point p) {
    var enemy = new Enemy(getNextId());
    enemyList.add(enemy);
    setAt(p.x, p.y, enemy.id);
    return enemy;
  }


  public Enemy getEnemyById(int enemyId) {
    Optional<Enemy> first = enemyList.stream().filter(enemy -> enemy.id == enemyId).findFirst();
    if (first.isPresent()) {
      return first.get();
    }

    throw new IllegalArgumentException("Unknown enemy id " + enemyId);
  }

  public ArrayList<Enemy> toEnemyList() {

    var exportList = new ArrayList<Enemy>();
    forEachField((x, y, currentValue) -> {
      if (currentValue != 0) {
        Enemy enemyById = getEnemyById(currentValue);
        enemyById.setPosition(x, y);
        exportList.add(enemyById);
      }
    });

    return exportList;
  }
}

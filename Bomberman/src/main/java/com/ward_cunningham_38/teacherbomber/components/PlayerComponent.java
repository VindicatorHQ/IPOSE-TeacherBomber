package com.ward_cunningham_38.teacherbomber.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import com.ward_cunningham_38.teacherbomber.TeacherBomberApp;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component {

    private CellMoveComponent cell;
    private AStarMoveComponent astar;

    private int maxBombs = 1;
    private int bombsPlaced = 0;

    public void increaseMaxBombs() {
        maxBombs++;
    }

    public void placeBomb() {
        if (bombsPlaced == maxBombs) {
            return;
        }

        bombsPlaced++;
        play("bombplant.wav");
        Entity bomb = spawn("Bomb", new SpawnData(cell.getCellX() * 120, cell.getCellY() * 120).put("radius", TeacherBomberApp.TILE_SIZE / 2));

        getGameTimer().runOnceAfter(() -> {
            bomb.getComponent(BombComponent.class).explode();
            bombsPlaced--;
        }, Duration.seconds(2));
    }

    public void moveRight() {
        play("krabwalk.wav");
        astar.moveToRightCell();
    }

    public void moveLeft() {
        play("krabwalk.wav");
        astar.moveToLeftCell();
    }

    public void moveUp() {
        play("krabwalk.wav");
        astar.moveToUpCell();
    }

    public void moveDown() {
        play("krabwalk.wav");
        astar.moveToDownCell();
    }
}

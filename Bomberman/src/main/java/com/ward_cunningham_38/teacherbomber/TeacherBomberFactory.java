package com.ward_cunningham_38.teacherbomber;

import com.ward_cunningham_38.teacherbomber.components.BombComponent;
import com.ward_cunningham_38.teacherbomber.components.PlayerComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.ward_cunningham_38.teacherbomber.TeacherBomberApp.TILE_SIZE;

public class TeacherBomberFactory implements EntityFactory {

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .viewWithBBox(texture("Floor.png", 1920, 1080))
                .zIndex(-1)
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(TeacherBomberType.WALL)
                .viewWithBBox(texture("brick.png", 120, 120))
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(TeacherBomberType.BRICK)
                .viewWithBBox(texture("thwomp.png", 120, 120))
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(60, 60), new Point2D(60, 60))
                .type(TeacherBomberType.PLAYER)
                .viewWithBBox(texture("koen.png", 120, 120))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(120, 120, 200))
                .with(new AStarMoveComponent(FXGL.<TeacherBomberApp>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("Bomb")
    public Entity newBomb(SpawnData data) {
        return entityBuilder(data)
                .type(TeacherBomberType.BOMB)
                .viewWithBBox(texture("bomb.png", 60, 80))
                .with(new BombComponent(data.get("radius")))
                .atAnchored(new Point2D(25, 25), new Point2D(data.getX() + TILE_SIZE / 2, data.getY() + TILE_SIZE / 2))
                .build();
    }

    @Spawns("Powerup")
    public Entity newPowerup(SpawnData data) {
        return entityBuilder(data)
                .type(TeacherBomberType.POWERUP)
                .viewWithBBox(texture("BLCK2.png", 120, 120))
                .with(new CollidableComponent(true))
                .build();
    }
}

/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2016 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ward_cunningham_38.teacherbomber;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.ward_cunningham_38.teacherbomber.components.PlayerComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;


import java.awt.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.ward_cunningham_38.teacherbomber.TeacherBomberType.*;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class TeacherBomberApp extends GameApplication {

    public static final int TILE_SIZE = 40;

    private AStarGrid grid;

    private Entity player1;
    private Entity player2;
    private PlayerComponent playerComponent1;
    private PlayerComponent playerComponent2;

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bomberman App");
        settings.setVersion(" ");
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setMainMenuEnabled(true);

        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up W") {
            @Override
            protected void onActionBegin() {
                playerComponent1.moveUp();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left A") {
            @Override
            protected void onActionBegin() {
                playerComponent1.moveLeft();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down S") {
            @Override
            protected void onActionBegin() {
                playerComponent1.moveDown();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right D") {
            @Override
            protected void onActionBegin() {
                playerComponent1.moveRight();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place Bomb F") {
            @Override
            protected void onActionBegin() {
                playerComponent1.placeBomb();
            }
        }, KeyCode.F);

        getInput().addAction(new UserAction("Move Up Arrow") {
            @Override
            protected void onActionBegin() {
                playerComponent2.moveUp();
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Move Left Arrow") {
            @Override
            protected void onActionBegin() {
                playerComponent2.moveLeft();
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Down Arrow") {
            @Override
            protected void onActionBegin() {
                playerComponent2.moveDown();
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Move Right Arrow") {
            @Override
            protected void onActionBegin() {
                playerComponent2.moveRight();
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Place Bomb ") {
            @Override
            protected void onActionBegin() {
                playerComponent2.placeBomb();
            }
        }, KeyCode.NUMPAD0);
    }

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(new TeacherBomberFactory());

        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);

        spawn("BG");

        grid = AStarGrid.fromWorld(getGameWorld(), 15, 15, 40, 40, type -> {
            if (type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player1 = spawn("Player_1");
        playerComponent1 = player1.getComponent(PlayerComponent.class);

        player2 = spawn("Player_2");
        playerComponent2 = player2.getComponent(PlayerComponent.class);
    }

    @Override
    protected void initPhysics() {
        onCollisionCollectible(PLAYER, POWERUP, powerup -> {
            playerComponent1.increaseMaxBombs();
        });
    }

    public void onBrickDestroyed(Entity brick) {
        int cellX = (int)((brick.getX() + 20) / TILE_SIZE);
        int cellY = (int)((brick.getY() + 20) / TILE_SIZE);

        grid.get(cellX, cellY).setState(CellState.WALKABLE);

        if (FXGLMath.randomBoolean()) {
            spawn("Powerup", cellX * 40, cellY * 40);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.ward_cunningham_38.teacherbomber;

import com.ward_cunningham_38.teacherbomber.components.PlayerComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import javafx.scene.input.KeyCode;
import org.jetbrains.annotations.NotNull;
import java.util.Random;
import static com.almasb.fxgl.dsl.FXGL.*;

public class TeacherBomberApp extends GameApplication {

    public static final int TILE_SIZE = 120;
    public static final double TILE_AXIS = 60;

    private AStarGrid grid;
    private int players = 2;

    private Entity player1;
    private Entity player2;
    private PlayerComponent playerComponent1;
    private PlayerComponent playerComponent2;

    public AStarGrid getGrid() {
        return grid;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Teacher Bomber");
        settings.setVersion("0.1");
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new MyGameMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Up W") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent1.moveUp();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Move Left A") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent1.moveLeft();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Move Down S") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent1.moveDown();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Move Right D") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent1.moveRight();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Place Bomb F") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent1.placeBomb();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.F);

        getInput().addAction(new UserAction("Move Up Arrow") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent2.moveUp();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.UP);

        getInput().addAction(new UserAction("Move Left Arrow") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent2.moveLeft();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.LEFT);

        getInput().addAction(new UserAction("Move Down Arrow") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent2.moveDown();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.DOWN);

        getInput().addAction(new UserAction("Move Right Arrow") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent2.moveRight();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.RIGHT);

        getInput().addAction(new UserAction("Place Bomb NUM0") {
            @Override
            protected void onActionBegin() {
                try
                {
                    playerComponent2.placeBomb();
                }
                catch (Exception ignored) {}
            }
        }, KeyCode.NUMPAD0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new TeacherBomberFactory());

        String[] arr={"1", "2", "3", "4", "5"};
        Random r=new Random();
        int randomNumber=r.nextInt(arr.length);

        Level level = getAssetLoader().loadLevel(arr[randomNumber]+".txt", new TextLevelLoader(TILE_SIZE, TILE_SIZE, '0'));

        getGameWorld().setLevel(level);

        spawn("BG");

        grid = AStarGrid.fromWorld(getGameWorld(), 16, 9, TILE_SIZE, TILE_SIZE, type -> {
            if (type.equals(TeacherBomberType.WALL) || type.equals(TeacherBomberType.BRICK))
            {
                return CellState.NOT_WALKABLE;
            }

            return CellState.WALKABLE;
        });

        player1 = spawn("Player_1");
        playerComponent1 = player1.getComponent(PlayerComponent.class);

        player2 = spawn("Player_2");
        playerComponent2 = player2.getComponent(PlayerComponent.class);
    }

    @Override
    protected void initPhysics() {
        onCollisionCollectible(TeacherBomberType.PLAYER, TeacherBomberType.POWERUP, powerup -> {
            playerComponent1.increaseMaxBombs();
        });
    }

    public void onBombBlowUp(Entity entity)
    {
        int cellX = (int)((entity.getX() + TILE_AXIS) / TILE_SIZE);
        int cellY = (int)((entity.getY() + TILE_AXIS) / TILE_SIZE);

        grid.get(cellX, cellY).setState(CellState.WALKABLE);
        despawnWithScale(entity);

        if (FXGLMath.randomBoolean())
        {
            spawn("Powerup", cellX * TILE_SIZE, cellY * TILE_SIZE);
        }

        if (entity.isType(TeacherBomberType.PLAYER))
        {
            players = players - 1;
        }

        if (players == 0)
        {
            gameOver();
        }

        onCollisionCollectible(TeacherBomberType.PLAYER, TeacherBomberType.POWERUP, powerup -> {
            playerComponent1.increaseMaxBombs();
        });

        onCollisionCollectible(TeacherBomberType.PLAYER, TeacherBomberType.POWERUP, powerup -> {
            playerComponent2.increaseMaxBombs();
        });
    }

    public void gameOver()
    {
        // implement highscore system
        System.out.println("the end");
    }

    public static void main(String[] args) {
        launch(args);
    }
}


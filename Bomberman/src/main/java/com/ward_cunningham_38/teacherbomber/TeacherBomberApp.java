package com.ward_cunningham_38.teacherbomber;

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
import com.ward_cunningham_38.teacherbomber.components.PlayerComponent;
import javafx.scene.input.KeyCode;


import com.almasb.fxgl.animation.Interpolators;

import com.almasb.fxgl.app.scene.MenuType;

import javafx.beans.binding.StringBinding;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

import static javafx.beans.binding.Bindings.when;
import static com.ward_cunningham_38.teacherbomber.TeacherBomberType.*;


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
            public FXGLMenu newMainMenu() {
                return new MyGameMenu();
            }
        });
    }

    public static class MyGameMenu extends FXGLMenu {

        private List<Node> buttons = new ArrayList<>();

        private int animIndex = 0;

        public MyGameMenu() {
            super(MenuType.MAIN_MENU);

            var bg = texture("background.png", getAppWidth() + 450, getAppHeight() + 200);
            bg.setTranslateY(-85);
            bg.setTranslateX(-450);

            var titleView = getUIFactoryService().newText(getSettings().getTitle(), 48);
            centerTextBind(titleView, getAppWidth() / 2.0, 100);

            var body = createBody();



            body.setTranslateY(-25);

            getContentRoot().getChildren().addAll(bg, titleView, body);


        }


        @Override
        public void onCreate() {
            animIndex = 0;

            buttons.forEach(btn -> {
                btn.setOpacity(0);

                animationBuilder(this)
                        .delay(Duration.seconds(animIndex * 0.1))
                        .interpolator(Interpolators.BACK.EASE_OUT())
                        .translate(btn)
                        .from(new Point2D(-200, 0))
                        .to(new Point2D(0, 0))
                        .buildAndPlay();

                animationBuilder(this)
                        .delay(Duration.seconds(animIndex * 0.1))
                        .fadeIn(btn)
                        .buildAndPlay();

                animIndex++;


            });
        }

        private Node createBody() {

            var btn1 = createActionButton(localizedStringProperty("menu.newGame"), this::fireNewGame);
            var btn2 = createActionButton(localizedStringProperty("menu.exit"), this::fireExit);

            Group group = new Group(btn1, btn2);


            int i = 30;
            for (Node n : group.getChildren()) {
                Point2D vector = new Point2D(0, 0);
                n.setLayoutX(vector.getX());
                n.setLayoutY(vector.getY()  + i);
                i += 80;
            }

            return group;
        }


        private Node createActionButton(StringBinding name, Runnable action) {
            var bg = new Rectangle(200, 50);
            bg.setEffect(new BoxBlur());

            var text = getUIFactoryService().newText(name);
            text.setTranslateX(15);
            text.setFill(Color.BLACK);

            var btn = new StackPane(bg, text);

            bg.fillProperty().bind(when(btn.hoverProperty())
                    .then(Color.LIGHTGREEN)
                    .otherwise(Color.DARKGRAY)
            );

            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setOnMouseClicked(e -> action.run());

            // clipping
            buttons.add(btn);

            Rectangle clip = new Rectangle(200, 50);
            clip.translateXProperty().bind(btn.translateXProperty().negate());

            btn.setTranslateX(-200);
            btn.setClip(clip);
            btn.setCache(true);
            btn.setCacheHint(CacheHint.SPEED);

            return btn;
        }
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

        String[] arr={"1", "2", "3", "4", "5"};
        Random r=new Random();
        int randomNumber=r.nextInt(arr.length);


        Level level = getAssetLoader().loadLevel(arr[randomNumber]+".txt", new TextLevelLoader(40, 40, '0'));
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

package com.ward_cunningham_38.teacherbomber;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
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

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.beans.binding.Bindings.when;

public class MyGameMenu extends FXGLMenu {

    private List<Node> buttons = new ArrayList<>();

    private int animIndex = 0;

    public MyGameMenu()
    {
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
    public void onCreate()
    {
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

    private Node createBody()
    {
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

    private Node createActionButton(StringBinding name, Runnable action)
    {
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
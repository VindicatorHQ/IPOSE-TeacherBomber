package com.ward_cunningham_38.teacherbomber.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.ward_cunningham_38.teacherbomber.TeacherBomberApp;
import com.ward_cunningham_38.teacherbomber.TeacherBomberType;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BombComponent extends Component {

    private int radius;

    public BombComponent(int radius) {
        this.radius = radius;
    }

    public void explode() {
        BoundingBoxComponent bbox = entity.getBoundingBoxComponent();

        getGameWorld()
                .getEntitiesInRange(bbox.range(radius, radius))
                .stream()
                .filter(e -> e.isType(TeacherBomberType.BRICK))
                .forEach(e -> {
                    FXGL.<TeacherBomberApp>getAppCast().onBombBlowUp(e);
                    e.removeFromWorld();
                });

        getGameWorld()
                .getEntitiesInRange(bbox.range(radius, radius))
                .stream()
                .filter(p -> p.isType(TeacherBomberType.PLAYER))
                .forEach(p -> {
                    FXGL.<TeacherBomberApp>getAppCast().onBombBlowUp(p);
                    p.removeFromWorld();
                });

        entity.removeFromWorld();
    }
}

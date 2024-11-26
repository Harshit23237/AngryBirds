package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Level1 {
    private Ground ground;
    private Catapult catapult;

    public Level1(Ground ground, Catapult catapult) {
        this.ground = ground;
        this.catapult = catapult;
    }

    public void setupLevel(Stage stage) {
        ground.ground_type1();

        // Adding example blocks, pigs, and birds with resized images
//
//        Bird bird1 = new Bird("bird1.PNG", 0.2f, 0.355f, 45, 45);
//        Ground step1=new Ground("step.png",45,45,0.125f,0.25f);
//            // width=45, height=45
//        Ground step2=new Ground("step.png",45,45,0.15f,0.25f);
//        Ground step3=new Ground("step.png",45,45,0.15f,0.3f);
//
//        Bird bird2 = new Bird("bird2.PNG", 0.125f, 0.3f, 45, 45);
//        Bird bird3 = new Bird("bird3.PNG", 0.15f , 0.35f, 45, 45);


//
//        stage.addActor(step1.getImage());
//        stage.addActor(step2.getImage());
//        stage.addActor(step3.getImage());
//        stage.addActor(bird1.getImage());
//        stage.addActor(bird2.getImage());
//        stage.addActor(bird3.getImage());
        Structure.structure1(stage);
    }
}

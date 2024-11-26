package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.Game.TutorialGame;

import java.util.ArrayList;

public class Level2 extends ScreenAdapter {
    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private static final float FPS = 90f;
    private static final float PPM = 100f; // Pixels Per Meter

    private Stage stage;
    private SpriteBatch batch;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Bird redBird;
    private Body birdBody;
    private Body groundBody;

    //    ===   blocks_
    private ArrayList<Body> blocks = new ArrayList<Body>();
    private Body blockBody;
    private Block WoodBlock;
//    ===

    //  === pig
    private ArrayList<Body> pigs = new ArrayList<Body>();
    private Body pigBody;
    private ArrayList<Body> bodiesToRemove_PIG = new ArrayList<>();
//  ===


    // ===== camera
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap tiledMap;
//    =====

    private Vector2 dragStart;   // Where the drag starts
    private Vector2 dragEnd;     // Where the drag ends
    private boolean isDragging;  // If the bird is being dragged

    private Vector2 GRAVITY;
    private ShapeRenderer shapeRenderer;

    private Texture pauseTexture;
    private ImageButton pauseButton;
    private OverlayPause overlayPause;
    private boolean showPause = false;

//    private LevelPage levelPage;

    private Game game;

    private float accumulator = 0;
    private static final float TIME_STEP = 1 / 120f; // Fixed 60 FPS time step

    private ArrayList<Body> bodiesToRemove = new ArrayList<>();

    private InputMultiplexer inputMultiplexer;


    private ArrayList<Drawable> level_button_texture(String up_texture, String down_texture ) {
        Texture buttonUpTexture = new Texture(Gdx.files.internal(up_texture));
        Texture buttonDownTexture = new Texture(Gdx.files.internal(down_texture));

        Drawable buttonUp = new TextureRegionDrawable(buttonUpTexture);
        Drawable buttonDown = new TextureRegionDrawable(buttonDownTexture);

        ArrayList<Drawable> level_button_up_down  = new ArrayList<Drawable>();
        level_button_up_down.add(buttonUp);
        level_button_up_down.add(buttonDown);
        return level_button_up_down;
    }

    private ImageButton ImageButton_create(String up_texture, String down_texture,
                                           float button_width, float button_height,
                                           float pos_X, float pos_Y) {
        ImageButton button;

        ArrayList<Drawable> button_drawable;
        button_drawable = level_button_texture(up_texture, down_texture);

        ImageButton.ImageButtonStyle button_style = new ImageButton.ImageButtonStyle();
        button_style.up = button_drawable.get(0);
        button_style.down = button_drawable.get(1);

        button = new ImageButton(button_style);
//        button.setPosition(SCREEN_WIDTH*0.5f - button.getWidth()*0.5f, SCREEN_HEIGHT*0.1f);
        button.setPosition(SCREEN_WIDTH*pos_X - button.getWidth()*0.5f, SCREEN_HEIGHT*pos_Y);

        button.setHeight(button_height); button.setWidth(button_width);
        stage.addActor(button);
        return button;
    }

    public Level2(Game game){
        this.game = game;
        System.out.println("LEVEL 2 ENTERED");
        GRAVITY = new Vector2(0,-20);
        shapeRenderer = new ShapeRenderer();
//        levelPage = new LevelPage(game);
//        overlayPause = new OverlayPause(levelPage, game);
        overlayPause = new OverlayPause(this, game);

        camera = new OrthographicCamera(1960, 1080);
//        camera = new OrthographicCamera();

        camera.setToOrtho(false, 1960, 1080);
        camera.position.set((float) 1960 /2, (float) 1080 /2, 0);

        stage = new Stage(new ScreenViewport(camera));
//        Gdx.input.setInputProcessor(stage);
        inputMultiplexer = new InputMultiplexer();

        batch = new SpriteBatch();

        redBird = new Bird("bird1.png", 100, 100); // Set the correct size in pixels
        stage.addActor(redBird.getImage());


        pauseTexture = new Texture("pause.png");
        pauseButton = ImageButton_create("pause.png","pause_down.png",100,100, 1f, 1f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked PAUSE");
//                showSettings = !showSettings;
//                settingsOverlay.setActive(showSettings);
                if (!overlayPause.isActive()) {
                    System.out.println("pause is now true");
                    showPause = true;
                    overlayPause.setActive(true);
                }
                System.out.println("returned from PAUSE" );
            }
        });

        world = new World(GRAVITY, true);
        initializeCollisionListener();

        debugRenderer = new Box2DDebugRenderer();

        createGroundBody(0, 0, 1980, 180);
        createGroundBody(270, 280, 50, 80);
        createGroundBody(0, 147, 320, 100);
        createGroundBody(1920, 162, 400, 100);
//       WALL
        createGroundBody(1980, 100, 10, 900);

        createBirdBody();

        tiledMap = new TmxMapLoader().load(String.valueOf(Gdx.files.internal("Level_assets/untitled.tmx")));
        renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load(String.valueOf(Gdx.files.internal("Level_assets/untitled.tmx"))));

        dragStart = new Vector2();
        dragEnd = new Vector2();
        isDragging = false;


        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Convert screen coordinates to stage coordinates
                Vector2 touchPoint = stage.screenToStageCoordinates(new Vector2(screenX, screenY));

                if (redBird.getImage().hit(touchPoint.x-200, touchPoint.y-340, true) != null) {
                    isDragging = true;
                    dragStart.set(touchPoint);
                    return true;
                }

                if (isWithinCustomArea(touchPoint)) {
                    isDragging = true;
                    dragStart.set(touchPoint);
                    return true;
                }

                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (isDragging) {
                    // Update drag end position
                    dragEnd.set(stage.screenToStageCoordinates(new Vector2(screenX, screenY)));
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isDragging) {
                    dragEnd.set(stage.screenToStageCoordinates(new Vector2(screenX, screenY)));
                    applyTrajectoryForce();
                    isDragging = false;
                    return true;
                }
                return false;
            }
        });

        for(MapObject obj : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)obj).getRectangle() ;
            createBlockBody(obj);
        }
//        for(MapObject obj : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)obj).getRectangle() ;
//            createStaticBody(rect);
//        }
//        FOR PIGS
        for(MapObject obj : tiledMap.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            createPigBody(obj);
        }


        Gdx.input.setInputProcessor(inputMultiplexer);
//        ========
//        camera = new OrthographicCamera();
//        renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load(String.valueOf(Gdx.files.internal("Level_assets/untitled.tmx"))));
//        camera.setToOrtho(false, 1920, 1080 ); ;
    }


    private void applyTrajectoryForce() {
        // Calculate the force vector
        Vector2 force = new Vector2(dragStart).sub(dragEnd); // Reverse the direction
//        force.scl(0.00005f); // Scale the force magnitude (adjust for gameplay)
        force.scl(1000000f);
        force.scl(1000000f);
//        Float MAX_FORCE = 0.0043f;
//        if(force.len() >= MAX_FORCE){
//            force.setLength(MAX_FORCE);
//        }

        // Apply the force to the bird's physics body
        birdBody.setLinearVelocity(0, 0); // Reset current velocity
        birdBody.setAngularVelocity(0);
        birdBody.applyLinearImpulse(force, birdBody.getWorldCenter(), true);

        // Debugging: Log force applied
        System.out.println("Force Applied: " + force);
    }

    private boolean isWithinCustomArea(Vector2 touchPoint) {
        float customX = 80; // Replace with your region's X coordinate
        float customY = 210; // Replace with your region's Y coordinate
        float width = 90;   // Width of the custom region
        float height = 90;  // Height of the custom region

        return touchPoint.x > customX && touchPoint.x < customX + width &&
            touchPoint.y > customY && touchPoint.y < customY + height;
    }


    private void createGroundBody(Integer positionX, Integer positionY, Integer Width, Integer Height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(positionX/ PPM, (positionY-10) / PPM);

        bodyDef.position.set(positionX, (positionY));

        groundBody = world.createBody(bodyDef);

        PolygonShape groundShape = new PolygonShape();
//        groundShape.setAsBox(Width / PPM, Height/ PPM);

        groundShape.setAsBox(Width , Height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.2f;

        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
    }

    private void createBlockBody(MapObject obj) {

        Rectangle rect = ((RectangleMapObject)obj).getRectangle() ;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(rect.x+rect.width/2 , rect.y+rect.height/2 );
        blockBody = world.createBody(bodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(rect.width /2, rect.height/ 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.2f;

        Fixture fixture = blockBody.createFixture(fixtureDef);
        Block block = null;
        System.out.print("OBJECT ID: "+obj.getProperties().get("id") + "\t");
        if (obj.getProperties().get("id").equals("1") || obj.getProperties().get("id").equals("2") || obj.getProperties().get("id").equals("3") ){
            System.out.println("WOOD TEXTURE");
            Integer WOOD_HEALTH = 2;
            if(obj.getProperties().get("id").equals("2") || obj.getProperties().get("id").equals("3")){
                WOOD_HEALTH = 1;
            }
            block = new Block("block1.png", rect.x, rect.y, rect.width, rect.height, WOOD_HEALTH );
        }
        else if( obj.getProperties().get("id").equals("6") ){
            System.out.println("ROCK TEXTURE");
            Integer ROCK_HEALTH = 3;
            block = new Block("block__3.png", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH );
        }
        else if(obj.getProperties().get("id").equals("5")){
            System.out.println("ROCK TEXTURE");
            Integer ROCK_HEALTH = 2;
            block = new Block("block__3_vert.jpg", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH);
        }
        else {
            System.out.println("GLASS TEXTURE");
            block = new Block("block2.png", rect.x, rect.y, rect.width, rect.height);
        }
        fixture.setUserData(block);
//        blockBody.getUserData().get

        bodyShape.dispose();
        blocks.add(blockBody);
        stage.addActor(block.getImage());

    }

    private void createStaticBody(Rectangle rect) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(rect.x+rect.width/2 , rect.y+rect.height/2 );
        blockBody = world.createBody(bodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(rect.width /2, rect.height/ 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.5f;

        Fixture fixture = blockBody.createFixture(fixtureDef);
        Block block = new Block("grd.png", rect.x, rect.y, rect.width, rect.height, 1000);
        fixture.setUserData(block);
//        blockBody.getUserData().get('id')

        bodyShape.dispose();
        blocks.add(blockBody);
        stage.addActor(block.getImage());
    }

    private void createBirdBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(200 / PPM, 500 / PPM);
        bodyDef.position.set(250 , 500);

        birdBody = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
//        circleShape.setRadius(1 / PPM);

        circleShape.setRadius(35);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.7f;

        Fixture fixture = birdBody.createFixture(fixtureDef);
        fixture.setUserData(redBird);

        circleShape.dispose();
    }

    private void createPigBody(MapObject obj){

        Rectangle rect = ((RectangleMapObject)obj).getRectangle() ;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(rect.x+rect.width/2 , rect.y+rect.height/2 );
        pigBody = world.createBody(bodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(rect.width /2, rect.height/ 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.2f;

        Fixture fixture = pigBody.createFixture(fixtureDef);
        Pig pig = null;
        System.out.print("OBJECT ID: "+obj.getProperties().get("id") + "\t");
        if (obj.getProperties().get("pig_id").equals("1") || obj.getProperties().get("pig_id").equals("2")  ){
            System.out.println("PIG 1");
            Integer WOOD_HEALTH = 2;
            pig = new Pig("pig2.png", rect.x, rect.y, rect.width, rect.height, WOOD_HEALTH );
        }
        else if( obj.getProperties().get("pig_id").equals("3") ){
            System.out.println("PIG 2");
            Integer ROCK_HEALTH = 3;
            pig = new Pig("pig1.png", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH );
        }
        else {
            System.out.println("ELSE PIG");
            pig = new Pig("pig4.png", rect.x, rect.y, rect.width, rect.height);
        }
        fixture.setUserData(pig);

        bodyShape.dispose();
        pigs.add(pigBody);
        stage.addActor(pig.getImage());
    }



    private void initializeCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if either fixture belongs to the bird
                if (isBirdFixture(fixtureA) && isBlockFixture(fixtureB)) {
                    handleCollisionWithBlock(fixtureB);
                }
                else if (isBirdFixture(fixtureB) && isBlockFixture(fixtureA)) {
                    handleCollisionWithBlock(fixtureA);
                }
                else if (isBirdFixture(fixtureB) && isPigFixture(fixtureA)) {
                    handleCollisionWithPig(fixtureA);
                }
                else if (isBirdFixture(fixtureA) && isPigFixture(fixtureB)) {
                    handleCollisionWithPig(fixtureB);
                }


            }

            @Override
            public void endContact(Contact contact) {
                // Optionally handle end of collision
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                // Optional: handle pre-solve logic
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // Optional: handle post-solve logic (e.g., check collision impulse)
            }
        });
    }

    private boolean isBirdFixture(Fixture fixture) {
        return fixture.getUserData() instanceof Bird;
    }

    private boolean isBlockFixture(Fixture fixture) {
        return fixture.getUserData() instanceof Block;
    }

    private boolean isPigFixture(Fixture fixture){
        return  fixture.getUserData() instanceof Pig;
    }

    private void handleCollisionWithBlock(Fixture blockFixture) {
        Block block = (Block) blockFixture.getUserData();
        System.out.println("Bird collided with block: " + block);

        block.decrementHealth();

        if (block.isDestroyed()) {
            bodiesToRemove.add(blockFixture.getBody());
            block.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
        }
        else {
            block.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
        }
    }

    private void handleCollisionWithPig(Fixture pigFixture) {
        Pig pig = (Pig) pigFixture.getUserData();
        System.out.println("Bird collided with Pig: " + pig);

        pig.decrementHealth();

        if (pig.isDestroyed()) {
            bodiesToRemove_PIG.add(pigFixture.getBody());
            pig.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
        }
        else {
            pig.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
        }
    }




    @Override
    public void render(float delta) {
        camera.update();
        // Step the physics world
//        world.step(1 / FPS, 6, 2);
// //        world.step(delta, 6, 2);
//        accumulator += Math.min(delta, 0.25f); // Prevent spiral of death
//
//        while (accumulator >= TIME_STEP) {
//            world.step(TIME_STEP, 6, 2);
//            accumulator -= TIME_STEP;
//        }

        renderer.setView(camera);
        renderer.render();

        // Sync Bird Actor Position with Physics Body
        birdBody.setAngularDamping(2f);
        Vector2 position = birdBody.getPosition();
        redBird.getImage().setOrigin(
            redBird.getImage().getWidth() / 2,
            redBird.getImage().getHeight() / 2
        );
        redBird.getImage().setPosition(
            position.x - redBird.getImage().getWidth() / 2 -10,
            position.y - redBird.getImage().getHeight() / 2 + 10);

        for (Body block : blocks) {
            Vector2 positionBlock = block.getPosition();
            float rotation = (float) Math.toDegrees(block.getAngle());
            Block blockData = (Block) block.getFixtureList().first().getUserData();

            blockData.getImage().setOrigin(
                blockData.getImage().getWidth() / 2,
                blockData.getImage().getHeight() / 2
            );
            blockData.getImage().setRotation(rotation);
            blockData.getImage().setPosition(
                positionBlock.x - blockData.getImage().getWidth() / 2,
                positionBlock.y - blockData.getImage().getHeight() / 2
            );
        }

        for (Body pig : pigs) {
            Vector2 positionBlock = pig.getPosition();
            float rotation = (float) Math.toDegrees(pig.getAngle());
            Pig pigData = (Pig) pig.getFixtureList().first().getUserData();

            pigData.getImage().setOrigin(
                pigData.getImage().getWidth() / 2,
                pigData.getImage().getHeight() / 2
            );
            pigData.getImage().setRotation(rotation);
            pigData.getImage().setPosition(
                positionBlock.x - pigData.getImage().getWidth() / 2,
                positionBlock.y - pigData.getImage().getHeight() / 2
            );
        }


//        PAUSE
        if (!showPause) {
            System.out.println("show pause false");
//            Gdx.input.setInputProcessor(stage);
//            inputMultiplexer = new InputMultiplexer();

            world.step(1 / FPS, 6, 2);
            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death

            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }
//            inputMultiplexer.addProcessor(stage);

//            stage.act(delta);
//            stage.draw();
        }

        else if (showPause && overlayPause.isActive()) {
            System.out.println("pause");
            Gdx.input.setInputProcessor(overlayPause.getStage());
            overlayPause.render(delta);
//            game.setScreen(this);
//            return;
        }
        else{
            System.out.println("else");
//            Gdx.input.setInputProcessor(stage);
//            inputMultiplexer = new InputMultiplexer();

            world.step(1 / FPS, 6, 2);
            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death

            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }
//            inputMultiplexer.addProcessor(stage);
        }
//        else{
//            System.out.println("ELSEEEEE");
//            inputMultiplexer.addProcessor(stage);
////
////            stage.act(delta);
////            stage.draw();
//        }
//      PAUSE - END


        if (isDragging) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.line(
                dragStart.x, dragStart.y,
                dragEnd.x, dragEnd.y
            );
            shapeRenderer.end();
        }

        debugRenderer.render(world, camera.combined);

        // Inside your render loop, after stepping the world
        for (Body body : bodiesToRemove) {
            // Remove from the block list
            blocks.remove(body);

            // Get the block's Image and remove it from the stage
            if (body.getFixtureList().size > 0 && body.getFixtureList().first().getUserData() instanceof Block) {
                Block blockData = (Block) body.getFixtureList().first().getUserData();
                if (blockData != null) {
                    stage.getActors().removeValue(blockData.getImage(), true); // Remove the actor from the stage
                }
            }
            // Destroy the body from the physics world
            world.destroyBody(body);
        }
        bodiesToRemove.clear();


        for (Body body_pig : bodiesToRemove_PIG) {
            pigs.remove(body_pig);

            if (body_pig.getFixtureList().size > 0 && body_pig.getFixtureList().first().getUserData() instanceof Pig) {
                Pig pigData = (Pig) body_pig.getFixtureList().first().getUserData();
                if (pigData != null) {
                    stage.getActors().removeValue(pigData.getImage(), true); // Remove the actor from the stage
                }
            }
            world.destroyBody(body_pig);
        }
        bodiesToRemove_PIG.clear();

        stage.act(delta);
        stage.draw();

    }


    public void resumeLevel(){
        game.setScreen(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void restartLevel() {
        System.out.println("Restarting level...");
        dispose();
//        game = null;
//        Gdx.input.setInputProcessor(inputMultiplexer);
        game.setScreen(new Level2(game));
    }

    public void navigateToTutorialPage() {
        System.out.println("Going to TutorialGame ");
        game.setScreen(new TutorialGame(game));
    }

    public InputProcessor getStage() {
        return stage;
    }



    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT );
        stage.getViewport().update(width, height, true);
        pauseButton.setPosition(Gdx.graphics.getWidth()*0.9f - pauseButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.9f - pauseButton.getHeight() *0.5f);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}

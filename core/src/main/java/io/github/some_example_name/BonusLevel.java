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

public class BonusLevel extends ScreenAdapter {

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

    private ArrayList<Bird> birds;
    private int currentBirdIndex;
    private Bird firstBird;
    private ArrayList<Bird> birdsToRemove = new ArrayList<>();

    private float birdInactiveTime = 0f; // Time the current bird has been inactive
    private static final float INACTIVE_TIME_THRESHOLD = 3f; // 3 seconds
    private static final float VELOCITY_THRESHOLD = 0.1f; // Threshold for inactivity


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


    public BonusLevel(Game game) {
        this.game = game;
        System.out.println("Bonus ENTERED");
        GRAVITY = new Vector2(0,10);
        shapeRenderer = new ShapeRenderer();
        overlayPause = new OverlayPause(this, game);

        camera = new OrthographicCamera(1960, 1080);
        camera.setToOrtho(false, 1960, 1080);
        camera.position.set((float) 1960 /2, (float) 1080 /2, 0);

        stage = new Stage(new ScreenViewport(camera));
        inputMultiplexer = new InputMultiplexer();
        batch = new SpriteBatch();

//        redBird = new Bird("bird1.png", 100, 100); // Set the correct size in pixels
//        stage.addActor(redBird.getImage());
        birds = new ArrayList<>();
        currentBirdIndex = 0;

        // Create and add multiple birds
        birds.add(new Bird("bird1.png", 100, 100, 10)); // 3 health points
        birds.add(new Bird("bird1.png", 100, 100, 10));
        birds.add(new Bird("bird1.png", 100, 100, 10));

        // Add the first bird to the stage
        firstBird = birds.get(currentBirdIndex);
        stage.addActor(firstBird.getImage());

        pauseTexture = new Texture("pause.png");
        pauseButton = ImageButton_create("pause.png","pause_down.png",100,100, 1f, 1f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("clicked PAUSE");
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

        createGroundBody(0, 0, 1980, 200);
//       WALL
        createGroundBody(1980, 100, 10, 900);
//        UP WALL
        createGroundBody(0, 1070, 1960, 10);

        createBirdBody(firstBird);

        tiledMap = new TmxMapLoader().load("bonus_level.tmx");
        renderer = new OrthogonalTiledMapRenderer(new TmxMapLoader().load(String.valueOf(Gdx.files.internal("bonus_level.tmx"))));

        dragStart = new Vector2();
        dragEnd = new Vector2();
        isDragging = false;


        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // Convert screen coordinates to stage coordinates
                Vector2 touchPoint = stage.screenToStageCoordinates(new Vector2(screenX, screenY));

                if (firstBird.getImage().hit(touchPoint.x-300, touchPoint.y-330, true) != null) {
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

        for(MapObject obj : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            createBlockBody(obj);
        }
//        for(MapObject obj : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)obj).getRectangle() ;
//            createStaticBody(rect);
//        }
//        FOR PIGS
        for(MapObject obj : tiledMap.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            createPigBody(obj);
        }


        Gdx.input.setInputProcessor(inputMultiplexer);


    }

    private void applyTrajectoryForce() {
        Vector2 force = new Vector2(dragStart).sub(dragEnd);
        force.scl(1000000f);
        force.scl(1000000f);
//        Float MAX_FORCE = 0.0043f;
//        if(force.len() >= MAX_FORCE){
//            force.setLength(MAX_FORCE);
//        }
        force.set(force.x + 10000,force.y + 10000);

        birdBody.setLinearVelocity(0, 0);
        birdBody.applyLinearImpulse(force, birdBody.getWorldCenter(), true);

        System.out.println("Force Applied: " + force);
    }

    private boolean isWithinCustomArea(Vector2 touchPoint) {
        float customX = 500; // Replace with your region's X coordinate
        float customY = 500; // Replace with your region's Y coordinate
        float width = 300;   // Width of the custom region
        float height = 300;  // Height of the custom region

        return touchPoint.x > customX && touchPoint.x < customX + width &&
            touchPoint.y > customY && touchPoint.y < customY + height;
    }


    private void createGroundBody(Integer positionX, Integer positionY, Integer Width, Integer Height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(positionX, (positionY));

        groundBody = world.createBody(bodyDef);
        PolygonShape groundShape = new PolygonShape();

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
        if (obj.getProperties().get("id").equals("1") || obj.getProperties().get("id").equals("5") || obj.getProperties().get("id").equals("2") ){
            System.out.println("WOOD TEXTURE");
            Integer WOOD_HEALTH = 1;
            block = new Block("block1.png", rect.x, rect.y, rect.width, rect.height, WOOD_HEALTH );
        }
        else if( obj.getProperties().get("id").equals("3") ){
            System.out.println("ROCK TEXTURE");
            Integer ROCK_HEALTH = 2;
            block = new Block("block__3.png", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH );
        }
        else if(obj.getProperties().get("id").equals("7")){
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

//    private void createBirdBody() {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(250 , 500);
//
//        birdBody = world.createBody(bodyDef);
//        CircleShape circleShape = new CircleShape();
//
//        circleShape.setRadius(35);
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = circleShape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 0.8f;
//        fixtureDef.restitution = 0.7f;
//
//        Fixture fixture = birdBody.createFixture(fixtureDef);
//        fixture.setUserData(firstBird);
//
//        circleShape.dispose();
//    }
    private void createBirdBody(Bird bird) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(250 , 500);

        birdBody = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(35);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.7f;

        Fixture fixture = birdBody.createFixture(fixtureDef);
        fixture.setUserData(bird);

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
            Integer WOOD_HEALTH = 1;
            pig = new Pig("pig2.png", rect.x, rect.y, rect.width, rect.height, WOOD_HEALTH );
        }
        else if( obj.getProperties().get("pig_id").equals("3") ){
            System.out.println("PIG 2");
            Integer ROCK_HEALTH = 2;
            pig = new Pig("pig2.png", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH );
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


//                Bird currentBird = birds.get(currentBirdIndex);
//                if (currentBird.isDestroyed()) {
//                    stage.getActors().removeValue(currentBird.getImage(), true);
//                    if (currentBirdIndex + 1 < birds.size()) {
//                        currentBirdIndex++;
//                        Bird nextBird = birds.get(currentBirdIndex);
//                        stage.addActor(nextBird.getImage());
//                        createBirdBody(); // Reinitialize physics body for the new bird
//                        System.out.println("Switched to the next bird!");
//                    } else {
//                        System.out.println("No more birds available! Game over or end level.");
//                        // Handle level failure or end game logic here
//                    }
//                }

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


//    private boolean isBirdFixture(Fixture fixture) {
//        return fixture.getUserData() instanceof Bird;
//    }

//    private boolean isBirdFixture(Fixture fixture) {
//        if (fixture.getBody().getUserData() instanceof Bird) {
//            Bird bird = (Bird) fixture.getBody().getUserData();
//            return birds.get(currentBirdIndex) == bird; // Ensure it's the active bird
//        }
//        return false;
//    }

    private boolean isBirdFixture(Fixture fixture) {
        if (fixture.getUserData() instanceof Bird) { // Check fixture's user data
            Bird bird = (Bird) fixture.getUserData();
            return birds.get(currentBirdIndex) == bird; // Ensure it's the active bird
        }
        return false;
    }


    private boolean isBlockFixture(Fixture fixture) {
        return fixture.getUserData() instanceof Block;
    }

    private boolean isPigFixture(Fixture fixture){
        return  fixture.getUserData() instanceof Pig;
    }

//    private void handleCollisionWithBlock(Fixture blockFixture) {
//        Block block = (Block) blockFixture.getUserData();
//        System.out.println("Bird collided with block: " + block);
//
//        block.decrementHealth();
//
//        if (block.isDestroyed()) {
//            bodiesToRemove.add(blockFixture.getBody());
//            block.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
//        }
//        else {
//            block.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
//        }
//    }
    private void handleCollisionWithBlock(Fixture blockFixture) {
        Block block = (Block) blockFixture.getUserData();
        System.out.println("Bird collided with block: " + block);

        block.decrementHealth();

        if (block.isDestroyed()) {
            bodiesToRemove.add(blockFixture.getBody());
            block.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
        } else {
            block.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
        }

        Bird currentBird = birds.get(currentBirdIndex);
        currentBird.decrementHealth();
        if (currentBird.isDestroyed()) {
            System.out.println("Current bird destroyed!");
            birdsToRemove.add(currentBird);

        }
    }

//    private void handleCollisionWithPig(Fixture pigFixture) {
//        Pig pig = (Pig) pigFixture.getUserData();
//        System.out.println("Bird collided with Pig: " + pig);
//
//        pig.decrementHealth();
//
//        if (pig.isDestroyed()) {
//            bodiesToRemove_PIG.add(pigFixture.getBody());
//            pig.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
//        }
//        else {
//            pig.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
//        }
//    }
    private void handleCollisionWithPig(Fixture pigFixture) {
        Pig pig = (Pig) pigFixture.getUserData();
        System.out.println("Bird collided with Pig: " + pig);

        pig.decrementHealth();

        if (pig.isDestroyed()) {
            bodiesToRemove_PIG.add(pigFixture.getBody());
            pig.getImage().setColor(1, 0, 0, 1); // Optional visual feedback for removal
        } else {
            pig.getImage().setColor(1, 1, 0, 1); // Optional visual feedback for one collision
        }

        // Decrement bird's health
        Bird currentBird = birds.get(currentBirdIndex);
        currentBird.decrementHealth();
        if (currentBird.isDestroyed()) {
            System.out.println("Current bird destroyed!");
            birdsToRemove.add(currentBird);

        }
    }



    @Override
//    public void render(float delta) {
//        camera.update();
//        renderer.setView(camera);
//        renderer.render();
//
//        Bird currentBird = birds.get(currentBirdIndex);
//        birdBody.setAngularDamping(2f);
//        Vector2 position = birdBody.getPosition();
//        currentBird.getImage().setOrigin(
//            currentBird.getImage().getWidth() / 2,
//            currentBird.getImage().getHeight() / 2
//        );
//        currentBird.getImage().setPosition(
//            position.x - currentBird.getImage().getWidth() / 2 - 10,
//            position.y - currentBird.getImage().getHeight() / 2 + 10
//        );
//
//        if (currentBird.isDestroyed()) {
//            stage.getActors().removeValue(currentBird.getImage(), true);
//            if (currentBirdIndex + 1 < birds.size()) {
//                currentBirdIndex++;
//                Bird nextBird = birds.get(currentBirdIndex);
//                stage.addActor(nextBird.getImage());
//                createBirdBody(nextBird);
//            } else {
//                System.out.println("No more birds available!");
//            }
//        }
//
////        camera.update();
////        renderer.setView(camera);
////        renderer.render();
////
////        // Sync Bird Actor Position with Physics Body
////        birdBody.setAngularDamping(2f);
////        Vector2 position = birdBody.getPosition();
////        redBird.getImage().setOrigin(
////            redBird.getImage().getWidth() / 2,
////            redBird.getImage().getHeight() / 2
////        );
////        redBird.getImage().setPosition(
////            position.x - redBird.getImage().getWidth() / 2 -10,
////            position.y - redBird.getImage().getHeight() / 2 + 10);
//
//        for (Body block : blocks) {
//            Vector2 positionBlock = block.getPosition();
//            float rotation = (float) Math.toDegrees(block.getAngle());
//            Block blockData = (Block) block.getFixtureList().first().getUserData();
//
//            blockData.getImage().setOrigin(
//                blockData.getImage().getWidth() / 2,
//                blockData.getImage().getHeight() / 2
//            );
//            blockData.getImage().setRotation(rotation);
//            blockData.getImage().setPosition(
//                positionBlock.x - blockData.getImage().getWidth() / 2,
//                positionBlock.y - blockData.getImage().getHeight() / 2
//            );
//        }
//
//        for (Body pig : pigs) {
//            Vector2 positionBlock = pig.getPosition();
//            float rotation = (float) Math.toDegrees(pig.getAngle());
//            Pig pigData = (Pig) pig.getFixtureList().first().getUserData();
//
//            pigData.getImage().setOrigin(
//                pigData.getImage().getWidth() / 2,
//                pigData.getImage().getHeight() / 2
//            );
//            pigData.getImage().setRotation(rotation);
//            pigData.getImage().setPosition(
//                positionBlock.x - pigData.getImage().getWidth() / 2,
//                positionBlock.y - pigData.getImage().getHeight() / 2
//            );
//        }
//
//
////        PAUSE
//        if (!showPause) {
//            System.out.println("show pause false");
////            Gdx.input.setInputProcessor(stage);
////            inputMultiplexer = new InputMultiplexer();
//
//            world.step(1 / FPS, 6, 2);
//            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death
//
//            while (accumulator >= TIME_STEP) {
//                world.step(TIME_STEP, 6, 2);
//                accumulator -= TIME_STEP;
//            }
//        }
//
//        else if (showPause && overlayPause.isActive()) {
//            System.out.println("pause");
//            Gdx.input.setInputProcessor(overlayPause.getStage());
//            overlayPause.render(delta);
//        }
//        else{
//            System.out.println("else");
//            world.step(1 / FPS, 6, 2);
//            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death
//
//            while (accumulator >= TIME_STEP) {
//                world.step(TIME_STEP, 6, 2);
//                accumulator -= TIME_STEP;
//            }
//        }
//
//
//
//        if (isDragging) {
//            shapeRenderer.setProjectionMatrix(camera.combined);
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(1, 0, 0, 1);
//            shapeRenderer.line(
//                dragStart.x, dragStart.y,
//                dragEnd.x, dragEnd.y
//            );
//            shapeRenderer.end();
//        }
//
//        debugRenderer.render(world, camera.combined);
//
//        // Inside your render loop, after stepping the world
//        for (Body body : bodiesToRemove) {
//            // Remove from the block list
//            blocks.remove(body);
//
//            // Get the block's Image and remove it from the stage
//            if (body.getFixtureList().size > 0 && body.getFixtureList().first().getUserData() instanceof Block) {
//                Block blockData = (Block) body.getFixtureList().first().getUserData();
//                if (blockData != null) {
//                    stage.getActors().removeValue(blockData.getImage(), true); // Remove the actor from the stage
//                }
//            }
//            // Destroy the body from the physics world
//            world.destroyBody(body);
//        }
//        bodiesToRemove.clear();
//
//
//        for (Body body_pig : bodiesToRemove_PIG) {
//            pigs.remove(body_pig);
//
//            if (body_pig.getFixtureList().size > 0 && body_pig.getFixtureList().first().getUserData() instanceof Pig) {
//                Pig pigData = (Pig) body_pig.getFixtureList().first().getUserData();
//                if (pigData != null) {
//                    stage.getActors().removeValue(pigData.getImage(), true); // Remove the actor from the stage
//                }
//            }
//            world.destroyBody(body_pig);
//        }
//        bodiesToRemove_PIG.clear();
//
//
//        for (Bird bird : birdsToRemove) {
//            // Remove bird's image from stage
//            stage.getActors().removeValue(bird.getImage(), true);
//
//            // Destroy the bird's body
//            if (birdBody != null) {
//                world.destroyBody(birdBody);
//                birdBody = null;
//            }
//
//            // Switch to next bird
//            if (currentBirdIndex + 1 < birds.size()) {
//                currentBirdIndex++;
//                Bird nextBird = birds.get(currentBirdIndex);
//                stage.addActor(nextBird.getImage());
//                createBirdBody(nextBird); // Create physics body for the next bird
//                System.out.println("Switched to the next bird!");
//            } else {
//                System.out.println("No more birds available! Game over or end level.");
//                // Handle level failure or end game logic here
//            }
//        }
//        birdsToRemove.clear();
//
//        stage.act(delta);
//        stage.draw();
//    }

    public void render(float delta) {
        camera.update();
        renderer.setView(camera);
        renderer.render();

        // Sync Bird Actor Position with Physics Body
        if (birdBody != null) {
            birdBody.setAngularDamping(2f);
            Vector2 position = birdBody.getPosition();
            Bird currentBird = birds.get(currentBirdIndex);
            currentBird.getImage().setOrigin(
                currentBird.getImage().getWidth() / 2,
                currentBird.getImage().getHeight() / 2
            );
            currentBird.getImage().setPosition(
                position.x - currentBird.getImage().getWidth() / 2 - 10,
                position.y - currentBird.getImage().getHeight() / 2 + 10
            );

            // Check bird's velocity to determine inactivity
            Vector2 velocity = birdBody.getLinearVelocity();
            if (velocity.len() < VELOCITY_THRESHOLD) {
                birdInactiveTime += delta;
                System.out.println("Bird inactive time: " + birdInactiveTime);
            } else {
                birdInactiveTime = 0f; // Reset timer if bird is moving
            }

            // If bird has been inactive for too long, mark it for removal
            if (birdInactiveTime >= INACTIVE_TIME_THRESHOLD) {
                System.out.println("Bird inactive for 3 seconds. Marking for removal.");
                birdsToRemove.add(currentBird);
                birdInactiveTime = 0f; // Reset timer
            }
        }

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

        // Physics step
        if (!showPause) {
            world.step(1 / FPS, 6, 2);
            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death

            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }
        } else if (showPause && overlayPause.isActive()) {
            Gdx.input.setInputProcessor(overlayPause.getStage());
            overlayPause.render(delta);
        } else {
            world.step(1 / FPS, 6, 2);
            accumulator += Math.min(delta, 0.25f); // Prevent spiral of death

            while (accumulator >= TIME_STEP) {
                world.step(TIME_STEP, 6, 2);
                accumulator -= TIME_STEP;
            }
        }

        // Render drag line if dragging
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

        // Process bodies to remove (blocks and pigs)
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

        // Process birds to remove
        for (Bird bird : birdsToRemove) {
            // Remove bird's image from stage
            stage.getActors().removeValue(bird.getImage(), true);

            // Destroy the bird's body
            if (birdBody != null) {
                world.destroyBody(birdBody);
                birdBody = null;
            }

            // Switch to next bird
            if (currentBirdIndex + 1 < birds.size()) {
                currentBirdIndex++;
                Bird nextBird = birds.get(currentBirdIndex);
                stage.addActor(nextBird.getImage());
                createBirdBody(nextBird); // Create physics body for the next bird
                System.out.println("Switched to the next bird!");
            } else {
                System.out.println("No more birds available! Game over or end level.");
                // Handle level failure or end game logic here
            }
        }
        birdsToRemove.clear();

        // Act and draw the stage
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
        game.setScreen(new BonusLevel(game));
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

package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;

public class Level2 extends ScreenAdapter {
    float SCREEN_WIDTH = Gdx.graphics.getWidth();
    float SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private static final float FPS = 120f;

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

    private Vector2 dragStart;
    private Vector2 dragEnd;
    private boolean isDragging;

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
    private float INACTIVE_TIME_THRESHOLD = 3f; // 3 seconds
    private static final float VELOCITY_THRESHOLD = 0.1f; // Threshold for inactivity


    private float accumulator = 0;
    private static final float TIME_STEP = 1 / 120f; // Fixed 60 FPS time step

    private ArrayList<Body> bodiesToRemove = new ArrayList<>();

    private InputMultiplexer inputMultiplexer;

    private Texture saveButtonTexture;
    private ImageButton saveButton;

    private Texture loadButtonTexture;
    private ImageButton loadButton;

    private Boolean hasMoved = false;

    private Music epic_music;



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


    public Level2(Game game, boolean loadSavedState) {
        this(game); // Call the main constructor
        if (loadSavedState) {
            GameState loadedState = SerializationUtil.loadGameState();
            if (loadedState != null) {
                applyGameState(loadedState);
            } else {
                Gdx.app.log("GameState", "Failed to load saved state.");
            }
        }
    }


    public Level2(Game game){
        this.game = game;
        System.out.println("LEVEL 2 ENTERED");
        GRAVITY = new Vector2(0,-18);
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

        birds = new ArrayList<>();
        currentBirdIndex = 0;

        birds.add(new Bird("bird1.png", 100, 100, 14));
        birds.add(new Bird("bird1.png", 100, 100, 14));
        birds.add(new Bird("green_bird.png", 100, 100, 8));

        firstBird = birds.get(currentBirdIndex);
        stage.addActor(firstBird.getImage());



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

        saveButtonTexture = new Texture("confirm_save.png");
        saveButton = ImageButton_create("confirm_save.png", "confirm_save.png", 100, 100, 0.1f, 1f);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveGameState();
            }
        });

        loadButtonTexture = new Texture("Load_Game.png");
        loadButton = ImageButton_create("Load_Game.png", "Load_Game.png", 100, 100, 0.2f, 1f);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Level2(game, true)); // Load from saved state
            }
        });


        world = new World(GRAVITY, true);
        initializeCollisionListener();

        debugRenderer = new Box2DDebugRenderer();

        createGroundBody(0, 0, 1980, 180);
        createGroundBody(270, 280, 50, 80);
        createGroundBody(0, 147, 320, 100);
        createGroundBody(1920, 142, 400, 100);
//       WALL
        createGroundBody(1980, 100, 10, 1000);
        createGroundBody(-15, 100, 10, 1000);

//        UP WALL
        createGroundBody(0, 1070, 1960, 10);
//        up catapult
        createGroundBody(280,500, 2, 10);



        createBirdBody(firstBird);

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

                if (firstBird.getImage().hit(touchPoint.x-300, touchPoint.y-330, true) != null) {
                    if(!hasMoved){
                        isDragging = true;
                        dragStart.set(touchPoint);
                        hasMoved = true;
                        return true;
                    }
                }

                if (isWithinCustomArea(touchPoint)) {
                    if(!hasMoved){
                        isDragging = true;
                        dragStart.set(touchPoint);
                        hasMoved = true;
                        return true;
                    }
                }

                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (isDragging) {
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



    private void applyGameState(GameState gameState) {
        clearGameObjects();

        // Recreate birds
        birds.clear();
        for (BirdData birdData : gameState.birds) {
            Gdx.app.log("1. applyGameState", "birdData.texturePath: " + birdData.texturePath);
            if (birdData.texturePath == null) {
                Gdx.app.error("applyGameState", "Texture path is null for birdData");
            }
            if (birdData.health <= 0) {
                continue;
            }
            Bird bird = new Bird(birdData.texturePath, birdData.positionX, birdData.positionY, (int) birdData.health);
            bird.getImage().setSize(100, 100);
            bird.getImage().setOrigin(bird.getImage().getWidth() /2 -10, bird.getImage().getHeight() /2 +10);

            birds.add(bird);
            stage.addActor(bird.getImage());

            if (birdData.isActive) {
                createBirdBody(bird);
                birdBody.setLinearVelocity(birdData.velocity);
                firstBird = bird;
            }

        }
        // Recreate pigs
        pigs.clear();
        for (PigData pigData : gameState.pigs) {
            Gdx.app.log("1. applyGameState", "pigData.texturePath: " + pigData.texturePath);
            Pig pig = new Pig(pigData.texturePath, pigData.positionX, pigData.positionY, pigData.width, pigData.height, (int) pigData.health);
            createPigBody(pig);
        }

        // Recreate blocks
        blocks.clear();
        for (BlockData blockData : gameState.blocks) {
            Block block = new Block(blockData.texturePath, blockData.positionX, blockData.positionY, blockData.width, blockData.height, (int) blockData.health);
            createBlockBody(block, blockData.rotation);
        }

        Gdx.app.log("GameState", "Game state applied successfully.");
    }

    private void clearGameObjects() {
        // Remove and destroy birds
        for (Bird bird : birds) {
            stage.getActors().removeValue(bird.getImage(), true);
            if (birdBody != null) {
                world.destroyBody(birdBody);
            }
        }
        birds.clear();
        currentBirdIndex = 0;

        // Remove and destroy pigs
        for (Body pigBody : pigs) {
            Pig pig = (Pig) pigBody.getFixtureList().first().getUserData();
            stage.getActors().removeValue(pig.getImage(), true);
            world.destroyBody(pigBody);
        }
        pigs.clear();

        // Remove and destroy blocks
        for (Body blockBody : blocks) {
            Block block = (Block) blockBody.getFixtureList().first().getUserData();
            stage.getActors().removeValue(block.getImage(), true);
            world.destroyBody(blockBody);
        }
        blocks.clear();
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
        float customX = 200;
        float customY = 350;
        float width = 100;
        float height = 100;

        return touchPoint.x > customX && touchPoint.x < customX + width && touchPoint.y > customY && touchPoint.y < customY + height;
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
        fixtureDef.restitution = 0.05f;

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
            block = new Block("block__3_vert2.png", rect.x, rect.y, rect.width, rect.height, ROCK_HEALTH);
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

    private void createBlockBody(Block block, float rotation) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(block.getPositionX(), block.getPositionY());

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(block.getWidth() / 2, block.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.2f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(block);

        shape.dispose();
        body.setTransform(body.getPosition(), (float) Math.toRadians(rotation));
        blocks.add(body);
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


    public void saveGameState() {
        GameState gameState = new GameState();

        // Save birds
        for (int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            if(bird.getHealth() <= 0){
                continue;
            }
            Vector2 velocity = (i == currentBirdIndex && birdBody != null) ? birdBody.getLinearVelocity() : new Vector2(0, 0);
            boolean isActive = (i == currentBirdIndex);
            BirdData birdData = new BirdData(bird, velocity, isActive);
            gameState.birds.add(birdData);
        }

        gameState.currentBirdIndex = currentBirdIndex;

        // Save pigs
        for (Body pigBody : pigs) {
            Pig pig = (Pig) pigBody.getFixtureList().first().getUserData();
            PigData pigData = new PigData(pig);
            gameState.pigs.add(pigData);
        }

        // Save blocks
        for (Body blockBody : blocks) {
            Block block = (Block) blockBody.getFixtureList().first().getUserData();
            float rotation = (float) Math.toDegrees(blockBody.getAngle());
            BlockData blockData = new BlockData(block, rotation);
            gameState.blocks.add(blockData);
        }

        SerializationUtil.saveGameState(gameState);
        Gdx.app.log("GameState", "Game state saved successfully.");
    }

    private void createBirdBody(Bird bird) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(280 , 450);

        birdBody = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();

        circleShape.setRadius(35);
//        circleShape.setRadius(bird.getWidth()/2);
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

    private void createPigBody(Pig pig){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pig.getPositionX() , pig.getPositionY());

        Body body = world.createBody(bodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(pig.getWidth() /2, pig.getHeight()/ 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution = 0.2f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(pig);

        bodyShape.dispose();
        pigs.add(body);
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
            hasMoved = false;
        }
    }

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

        Bird currentBird = birds.get(currentBirdIndex);
        currentBird.decrementHealth();
        if (currentBird.isDestroyed()) {
            System.out.println("Current bird destroyed!");
            birdsToRemove.add(currentBird);
            hasMoved = false;
        }
    }



    private void DrawTrajectory(){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 1, 1);

        Vector2 initialVelocity = new Vector2(
            -(dragEnd.x - dragStart.x) ,
            -(dragEnd.y - dragStart.y)
        );
        Vector2 birdStartPosition = birdBody != null ? birdBody.getPosition() : new Vector2(250, 500);
        Vector2 gravity = world.getGravity();
        gravity.scl(2.5f);

        Vector2 currentPosition = new Vector2(birdStartPosition);
        Vector2 velocity = new Vector2(initialVelocity);
        float timeStep = 0.3f;
        int maxSteps = 8;

        for (int i = 0; i < maxSteps; i++) {
            Vector2 nextPosition = new Vector2(
                currentPosition.x + velocity.x * timeStep,
                currentPosition.y + velocity.y * timeStep
            );

            shapeRenderer.circle(nextPosition.x, nextPosition.y, 5);
            currentPosition.set(nextPosition);

            velocity.add(gravity.x * timeStep, gravity.y * timeStep);

        }

        shapeRenderer.end();
    }


    @Override
    public void render(float delta) {
        camera.update();
        renderer.setView(camera);
        renderer.render();

        if(epic_music == null){
            if(currentBirdIndex == birds.size() - 1) {
                epic_music = Gdx.audio.newMusic(Gdx.files.internal("let_him_cook.mp3"));
                epic_music.setLooping(true);
                epic_music.play();
                epic_music.setVolume(0.2f);
            }
        }

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
            if(currentBird.getTexturePath().equalsIgnoreCase("green_bird.png")){
                System.out.println("gravity is now up");
                GRAVITY = new Vector2(0,10);
                INACTIVE_TIME_THRESHOLD = 5f;
                createGroundBody(1920, 145, 400, 100);
                createGroundBody(0, 0, 1980, 180);
                if(birdInactiveTime >= 3 && birdInactiveTime <= 3.2){
                    createGroundBody(280,497, 2, 10);
                }
                world.setGravity(GRAVITY);
            }
            else{
                GRAVITY = new Vector2(0,-18);
                world.setGravity(GRAVITY);
            }

            Vector2 velocity = birdBody.getLinearVelocity();
            if (velocity.len() < VELOCITY_THRESHOLD) {
                birdInactiveTime += delta;
                System.out.println("Bird inactive time: " + birdInactiveTime);
            } else {
                birdInactiveTime = 0f;
            }

            if (birdInactiveTime >= INACTIVE_TIME_THRESHOLD) {
                System.out.println("Bird inactive for some time.");
                birdsToRemove.add(currentBird);
                birdInactiveTime = 0f;
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
            if(birds.get(currentBirdIndex).getTexturePath().equalsIgnoreCase("green_bird.png") == false){
                GRAVITY = new Vector2(0,-18);
                world.setGravity(GRAVITY);
            }
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

        if (isDragging) {
            DrawTrajectory();
        }

//        debugRenderer.render(world, camera.combined);

        for (Body body : bodiesToRemove) {
            blocks.remove(body);

            if (body.getFixtureList().size > 0 && body.getFixtureList().first().getUserData() instanceof Block) {
                Block blockData = (Block) body.getFixtureList().first().getUserData();
                if (blockData != null) {
                    stage.getActors().removeValue(blockData.getImage(), true); // Remove the actor from the stage
                }
            }
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

        for (Bird bird : birdsToRemove) {
            stage.getActors().removeValue(bird.getImage(), true);

            if (birdBody != null) {
                world.destroyBody(birdBody);
                birdBody = null;
            }

            if (currentBirdIndex + 1 < birds.size()) {
                currentBirdIndex++;
                Bird nextBird = birds.get(currentBirdIndex);
                stage.addActor(nextBird.getImage());
                createBirdBody(nextBird);
                hasMoved = false;
                System.out.println("Switched to the next bird!");
            }
            else {
                System.out.println("No more birds available! Game over or end level.");

                if(pigs.isEmpty()){
                    if (epic_music != null) epic_music.dispose();
                    game.setScreen(new WinScreen(game));
                }
                else{
                    if (epic_music != null) epic_music.dispose();
                    game.setScreen(new LoseScreen(game));
                }
            }
        }
        birdsToRemove.clear();

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
        if (epic_music != null) epic_music.dispose();
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
        saveButton.setPosition(Gdx.graphics.getWidth()*0.6f - saveButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.9f - saveButton.getHeight() *0.5f);
        loadButton.setPosition(Gdx.graphics.getWidth()*0.4f - loadButton.getWidth()*0.5f, Gdx.graphics.getHeight() *0.9f - loadButton.getHeight() *0.5f);

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        tiledMap.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        if (epic_music != null) epic_music.dispose();

    }
}

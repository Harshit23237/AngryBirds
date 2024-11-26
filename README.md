# AngrierBirds
Members- Harshit Dawra 2023237, Ruhani Bhatia 2023450
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ScreenAdapter` extension that draws a simple GUI on the screen.


## HOW TO RUN: 
    Open the project in IDEA and run from there, using the Lwjgl3Launcher file in the lwjgl3 directory.

## Diagrams & Use Cases:
Are present in the AngryBirds2027 Folder Itself as PDF files.


## Refernces:
    https://gamedev.stackexchange.com/questions/121115/libgdx-simple-button-with-image
    https://www.youtube.com/watch?v=p2lUdy68s_M&list=PLLwCf-qdpyEnB_FO_1HkUFh7smwGNjAaC
    https://git-scm.com/
    https://libgdx.com/dev/
    https://www.geeksforgeeks.org/
    https://www.w3schools.com/java/default.asp
    https://stackoverflow.com/
    https://github.com/libgdx/libgdx 


## General Instructions to Play the Game:
    On the Start Game Click on Play Button, or Exit Button, according to your own Choice.
    After CLicking Start, you would be redirected to a level selection page.

    On the Level Selection Page: 
        For now all levels lead to a comman level.
        You Can also go to settings from there which can load a game (again connected to the same level), or you can click Mute, which shows in the console that you have clicked it.
        There is also a back Button.
    
    On Level Page:
        There would be renders of different assets seperately. and there would be a Pause page to provide Save Game Features.
        Pause Menu also includes options to Quit/Resume Level, or open Settings.
        Settings Include the same options as mentioned in the Level Selection PAge.

    After Completing a Level, User Would be Taken to a Loss or Win Page. 
        Where in Case of Win, User can click on Next, Quit Level.   
        In case of Lose, User can click on retry, Quit.
    
    That Sums Up Our Game's GUI. 

In the end Tree Looks Like this:


├───.gradle
│   ├───8.10.2
│   │   ├───checksums
│   │   ├───dependencies-accessors
│   │   ├───executionHistory
│   │   ├───expanded
│   │   ├───fileChanges
│   │   ├───fileHashes
│   │   └───vcsMetadata
│   ├───buildOutputCleanup
│   └───vcs-1
├───.idea
├───assets
│   └───ui
├───core
│   ├───build
│   │   ├───classes
│   │   │   └───java
│   │   │       └───main
│   │   │           └───io
│   │   │               └───github              <--- file in which we coded
│   │   │                   ├───Game
│   │   │                   └───some_example_name
│   │   ├───generated
│   │   │   └───sources
│   │   │       ├───annotationProcessor
│   │   │       │   └───java
│   │   │       │       └───main
│   │   │       └───headers
│   │   │           └───java
│   │   │               └───main
│   │   ├───libs
│   │   ├───lombok
│   │   │   └───effective-config
│   │   └───tmp
│   │       ├───compileJava
│   │       │   └───compileTransaction
│   │       │       ├───backup-dir
│   │       │       └───stash-dir
│   │       └───jar
│   └───src
│       └───main
│           └───java
│               └───io
│                   └───github
│                       ├───Game
│                       └───some_example_name
├───gradle
│   └───wrapper
└───lwjgl3
    ├───build
    │   ├───classes
    │   │   └───java
    │   │       └───main
    │   │           └───io
    │   │               └───github
    │   │                   └───some_example_name
    │   │                       └───lwjgl3
    │   ├───generated
    │   │   └───sources
    │   │       ├───annotationProcessor
    │   │       │   └───java
    │   │       │       └───main
    │   │       └───headers
    │   │           └───java
    │   │               └───main
    │   ├───lombok
    │   │   └───effective-config
    │   ├───resources
    │   │   └───main
    │   │       └───ui
    │   └───tmp
    │       └───compileJava
    │           └───compileTransaction
    │               ├───backup-dir
    │               └───stash-dir
    ├───icons
    └───src
        └───main
            ├───java
            │   └───io
            │       └───github
            │           └───some_example_name
            │               └───lwjgl3          <--- FILE WHERE LAUCH FILE IS PRESENT 
            └───resources



.
.
.
github
├───Game
│       MyGame.java
│       SettingsOverlay.java
│       StartGame.java
│       TutorialGame.java
│
└───some_example_name
        Bird.java
        Block.java
        Catapult.java
        Ground.java
        Level1.java
        LevelPage.java
        LoseScreen.java
        OverlayPause.java
        OverlaySaveGame.java
        PauseScreen.java
        Pig.java
        Structure.java
        WinScreen.java



.
.
.
lwjgl3
    Lwjgl3Launcher.java
    StartupHelper.java






"HOPING YOU LIKE OUR GAME" ~ Team "Nah I'd Vim"





## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

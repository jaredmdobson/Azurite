package org.azurite.util;

import org.azurite.audio.AudioMaster;
import org.azurite.graphics.Window;
import org.azurite.scene.SceneManager;
import org.azurite.util.safety.Preconditions;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * The Engine class initializes the Window and the game loop for a game engine.
 * It follows the Singleton design pattern,
 * providing a global unique instance accessible through the
 * {@link #getInstance()} method.
 * <p>
 * The Engine class provides several static methods to initialize the engine and
 * create a window, as well as methods to control the engine's state, such as
 * checking if the engine is running, stopping the window, and interrupting the engine
 * with an exit code. It also provides methods to access the window, delta time
 * (time elapsed since the last frame), scene manager, and to enable logging to log files.
 */
public final class Engine {

  private static final Engine instance = new Engine();

  private final long startMillis;
  private Window window;
  private boolean running;
  private float deltaTime;

  // private to prevent creating new instances
  private Engine() {
    running = true;
    deltaTime = 0;
    startMillis = System.currentTimeMillis();
  }

  /**
   * Get the global unique instance of the Engine object.
   */
  public static Engine getInstance() {
    return instance;
  }

  public static float deltaTime() {
    return getInstance().getDeltaTime();
  }

  public static void updateDeltaTime(float deltaTime) {
    getInstance().deltaTime = deltaTime;
  }

  public static boolean isRunning() {
    return getInstance().running;
  }

  public static Window window() {
    return getInstance().getWindow();
  }

  public static void showWindow() {
    try {

      Preconditions.nonNull("window", window()).showWindow();
    } catch (Exception e) {
      Log.logger.error("Unable to show window!", e);
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public static SceneManager scenes() {
    return window().getSceneManager();
  }

  // Internal method called before any init
  private static void preInit() {

    Log.logger.debug("pre init of engine called");

    System.setProperty("java.awt.headless", "true");

    // ensure that the Engine is running on main thread
    Preconditions.ensureMainThread("engine initialization");

    GLFWErrorCallback.createPrint(System.err).set();

    if (!glfwInit()) {
      Log.logger.error("failed to initialoze GLFW");
      throw new IllegalStateException("[FATAL] Failed to initialize GLFW.");
    }

    AudioMaster.get();

    Log.logger.info("engine pre init complete");
  }

  /**
   * Start the engine, and initialize GLFW.
   *
   * @param windowWidth      Width of the window to be created
   * @param windowHeight     Height of the window to be created
   * @param windowTitle      Title of the window to be created
   * @param minSceneLighting float from 0-1 indicating the minimum scene light
   *                         level
   */
  public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting) {
    preInit();
    getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting, true);
  }

  /**
   * Start the engine, and initialize GLFW.
   *
   * @param windowWidth  Width of the window to be created
   * @param windowHeight Height of the window to be created
   * @param windowTitle  Title of the window to be created
   */
  public static void init(int windowWidth, int windowHeight, String windowTitle) {
    preInit();
    getInstance().window = new Window(windowWidth, windowHeight, windowTitle, false);
  }

  /**
   * Start the engine, and initialize GLFW. This will create a fullscreen window.
   *
   * @param windowTitle      Title of the window to be created
   * @param minSceneLighting float from 0-1 indicating the minimum scene light
   *                         level
   */
  public static void init(String windowTitle, float minSceneLighting) {
    preInit();
    getInstance().window = new Window(windowTitle, minSceneLighting, false);
  }

  public static void init(int windowWidth, int windowHeight, String windowTitle, float minSceneLighting,
                          boolean recalculateProjectionOnResize) {
    preInit();
    getInstance().window = new Window(windowWidth, windowHeight, windowTitle, minSceneLighting,
        recalculateProjectionOnResize);
  }

  /**
   * Start the engine, and initialize GLFW.
   *
   * @param windowTitle Title of the window to be created
   */
  public static void init(String windowTitle) {
    preInit();
    getInstance().window = new Window(windowTitle);
  }

  /**
   * @return Returns the number of milliseconds since the engine started. (since
   * the first call)
   */
  public static double millisRunning() {
    return System.currentTimeMillis() - getInstance().startMillis;
  }

  /**
   * Not meant to be called. Only used for {@link Window} to stop the engine.
   */
  public void windowStopped() {
    this.running = false;
  }

  /**
   * Cuts off the engine and exits out of the program immediately with saving the
   * logs.
   *
   * @param code the exit code
   */
  public void interrupt(int code) {
    windowStopped();
    System.exit(code);
  }

  public Window getWindow() {
    return window;
  }

  public float getDeltaTime() {
    return deltaTime;
  }
}

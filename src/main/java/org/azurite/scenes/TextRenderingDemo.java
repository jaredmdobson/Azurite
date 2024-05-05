package org.azurite.scenes;

import org.azurite.graphics.Camera;
import org.azurite.graphics.Color;
import org.azurite.graphics.Window;
import org.azurite.input.Mouse;
import org.azurite.scene.Scene;
import org.azurite.ui.Text;
import org.azurite.ui.fonts.Font;
import org.azurite.util.Engine;
import org.joml.Vector2f;

import static org.azurite.graphics.Graphics.setDefaultBackground;

public class TextRenderingDemo extends Scene {

  Font maghrib;
  Font openSans;
  Text titleText;
  Text centeredText;
  Text movingText;
  Text rainbowText;

  public static void main(String[] args) {
    Engine.init(900, 600, "Azurite Font Rendering Demo", 0, true);
    Engine.scenes().switchScene(new TextRenderingDemo());
    Engine.showWindow();
  }

  public void awake() {

    camera = new Camera();
    setDefaultBackground(Color.WHITE);

    maghrib = new Font("fonts/Maghrib-MVZpx.ttf", 100, true);
    openSans = new Font("fonts/OpenSans-Regular.ttf", 20, true);

    float halfWindowWidth = Window.getWidth() / 2.0f;
    titleText = new Text("Azurite text rendering demo", maghrib, Color.BLACK, halfWindowWidth, 5, 1, true, true);
    movingText = new Text("HAHA", openSans, Color.RED, 200, 200);
    rainbowText = new Text("Rainbow text", openSans, Color.BLUE, 10, 50);
    centeredText = new Text("(Centered Text)\n(Centered Text line 2)", openSans, Color.BLACK, halfWindowWidth, 80, 1, true, true);
  }

  public void update() {
    rainbowText.change("Azurite Engine demo\nDT: " + Engine.deltaTime() + "\nFPS: " + (int) Engine.getInstance().getWindow().getFPS() + "\nMouse " + Mouse.mouse.x + " | " + Mouse.mouse.y);

    movingText.setPosition(new Vector2f(Mouse.mouse.x(), Mouse.mouse.y));
    movingText.change("X " + Mouse.mouse.x + " | Y " + Mouse.mouse.y);

    rainbowText.rainbowify();

  }

}

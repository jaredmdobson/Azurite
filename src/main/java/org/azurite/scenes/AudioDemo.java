package org.azurite.scenes;

import org.azurite.audio.AudioSource;
import org.azurite.ecs.GameObject;
import org.azurite.graphics.Camera;
import org.azurite.input.Keyboard;
import org.azurite.input.Keys;
import org.azurite.scene.Scene;
import org.azurite.util.Assets;
import org.azurite.util.Engine;
import org.joml.Vector2f;

import static org.azurite.graphics.Graphics.setDefaultBackground;

/**
 * Minimal usage example of the AudioListener and AudioSource components.
 */
public class AudioDemo extends Scene {
  GameObject barFoo;
  GameObject barFoo1;
  boolean isPressed = false;

  public static void main(String[] args) {
    Engine.init(1080, 720, "Azurite Audio Demo", 0.01f);
    Engine.scenes().switchScene(new AudioDemo());
    Engine.showWindow();
  }

  public void awake() {
    camera = new Camera();
    setDefaultBackground(0);

    barFoo = new GameObject(new Vector2f(0, 0));
    barFoo.addComponent(new AudioSource(Assets.getAudioBuffer("sounds/lines_of_code.wav")));
    //barFoo.getComponent(AudioSource.class).play(0, true);

    barFoo1 = new GameObject(new Vector2f(0, 0));
    barFoo1.addComponent(new AudioSource(Assets.getAudioBuffer("sounds/hit.wav")));
  }

  public void update() {
    if (Keyboard.getKeyDown(Keys.KEY_SPACE)) {
      if (!isPressed)
        barFoo1.getComponent(AudioSource.class).play(0, false);
      isPressed = true;
    } else {
      isPressed = false;
    }
  }
}

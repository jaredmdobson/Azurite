package org.azurite.util;

import org.azurite.audio.AudioBuffer;
import org.azurite.graphics.Shader;
import org.azurite.graphics.Spritesheet;
import org.azurite.graphics.Texture;
import org.azurite.io.bin.BinaryIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Objects;

/**
 * The Assets class contains methods to assist in loading common resources used by the engine from the filesystem as well as HashMaps to keep track of loaded resources.
 * If the same path is loaded again via the Assets class, it will call the item up from the hashmap rather than reload it.
 */
public class Assets {
  private static HashMap<String, Shader> shaders = new HashMap<>();
  private static HashMap<String, ByteBuffer> dataFiles = new HashMap<>();
  private static HashMap<String, Texture> textures = new HashMap<>();
  private static HashMap<String, AudioBuffer> audioBuffers = new HashMap<>();
  private static HashMap<String, Spritesheet> spritesheets = new HashMap<>();


  public static Shader getShader(String path, boolean fromResources) {
    String key = fromResources ? path : new File(path).getAbsolutePath();
    if (shaders.containsKey(key)) {
      return shaders.get(key);
    }
    Log.logger.debug("shader requested to load: \"" + path + "\"");
    Shader shader;
    if (fromResources) {
      shader = new Shader(path, getAzuriteLibraryResourceAsStream(path));
    } else {
      shader = new Shader(path);
    }
    shader.compile();
    shaders.put(key, shader);
    Log.logger.debug("shader compilation successful");
    return shader;
  }

  /**
   * Loads a text file from the filesystem and returns it in a String
   *
   * @param path to data file
   * @return returns type String
   */
  public static ByteBuffer getDataFile(String path, boolean fromResources) {
    try {
      String key = fromResources ? path : new File(path).getAbsolutePath();
      if (dataFiles.containsKey(key)) {
        return dataFiles.get(key);
      }
      Log.logger.debug("data file requested to load: \"" + path + "\"");
      ByteBuffer data = fromResources ? ByteBuffer.wrap(getAzuriteLibraryResourceAsStream(path).readAllBytes()) : BinaryIO.readData(new File(path));
      Log.logger.debug("data successfully loaded");
      dataFiles.put(new File(path).getAbsolutePath(), data);
      return data;
    } catch (IOException e) {
      Log.logger.error("file not found: \"" + path + "\"", 1);
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Loads a image from the filesystem, and returns a Texture.
   *
   * @param path to Texture resource (usually a .png file)
   * @return returns type Texture
   */
  public static Texture getTexture(String path, boolean fromResources) {
    String key = fromResources ? path : new File(path).getAbsolutePath();
    if (textures.containsKey(key)) {
      return textures.get(key);
    }
    Log.logger.debug("texture requested to load: \"" + path + "\"");
    Texture texture = new Texture(path, fromResources);
    Log.logger.debug("loading texture successfully");
    textures.put(key, texture);
    return texture;
  }

  /**
   * Loads an audio file from the filesystem and returns an AudioBuffer.
   *
   * @param path to audio file
   * @return returns type AudioBuffer
   */
  public static AudioBuffer getAudioBuffer(String path) {
    File file = new File(path);
    if (audioBuffers.containsKey(file.getAbsolutePath())) {
      return audioBuffers.get(file.getAbsolutePath());
    }
    Log.logger.debug("audiobuffer requested to load: \"" + path + "\"");
    AudioBuffer audioBuffer = new AudioBuffer(path);
    Log.logger.debug("loading audiobuffer successfully");
    audioBuffers.put(file.getAbsolutePath(), audioBuffer);
    return audioBuffer;
  }

  /**
   * Adds a filePath and spritesheet to the Asset class's spritesheet hashmap. (private)
   *
   * @param path        to Texture resource (usually a .png file)
   * @param spritesheet object
   */
  private static void addSpritesheet(String path, Spritesheet spritesheet) {
    File file = new File(path);
    if (!Assets.spritesheets.containsKey(file.getAbsolutePath())) {
      Assets.spritesheets.put(file.getAbsolutePath(), spritesheet);
    }
  }

  /**
   * Gets the {@link Spritesheet} object corresponding to the given file path.
   *
   * @param path The file path of the spritesheet.
   * @return The {@link Spritesheet} object associated with the given file path, or {@code null} if not found.
   * @throws AssertionError If the spritesheet does not exist or is not loaded.
   */
  private static Spritesheet getSpritesheet(String path) {
    File file = new File(path);
    if (!Assets.spritesheets.containsKey(file.getAbsolutePath()))
      Log.logger.error("tried to access spritesheet \"" + path + "\", but it does not exist or is not loaded, try using \"Assets.loadSpritesheet()\".");
    assert Assets.spritesheets.containsKey(file.getAbsolutePath()) : "[ERROR] Tried to access spritesheet \"" + path + "\", but it does not exist or is not loaded, try using \"Assets.loadSpritesheet()\".";
    return Assets.spritesheets.getOrDefault(file.getAbsolutePath(), null);
  }

  /**
   * Loads a image from the filesystem, and returns a Spritesheet object.
   *
   * @param path         to Texture resource (usually a .png file)
   * @param spriteWidth  of each sprite
   * @param spriteHeight of each sprite
   * @param numSprites   of sprites in the sheet
   * @param spacing      spacing between sprites (0 if no spacing)
   * @return returns type Spritesheet
   */
  public static Spritesheet loadSpritesheet(String path, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
    addSpritesheet(path, new Spritesheet(getTexture(path, true), spriteWidth, spriteHeight, numSprites, spacing));
    return getSpritesheet(path);
  }

  public static InputStream getAzuriteLibraryResourceAsStream(String fileName) {
    //TODO: Fix hack for tiles
    InputStream stream = Assets.class.getClassLoader().getResourceAsStream(fileName.replace('\\', '/').replace("tiles/images", "images"));
    return Objects.requireNonNull(stream);
  }

}

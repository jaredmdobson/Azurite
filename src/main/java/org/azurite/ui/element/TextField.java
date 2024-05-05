package org.azurite.ui.element;

import org.azurite.ui.Element;
import org.azurite.ui.EventHandler;
import org.lwjgl.glfw.GLFW;

/**
 * @author Juyas
 * @version 07.11.2021
 * @since 07.11.2021
 */
public class TextField extends Element implements TextHolder {

  /**
   * The text displayed in the TextField, that can be edited.
   */
  private String text;

  public TextField() {
    this("");
  }

  public TextField(String text) {
    this.text = text;
    this.setCursor(GLFW.GLFW_IBEAM_CURSOR);
    //this component requests focus when clicked on it for receiving input
    this.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, eventHandler -> {
      if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT))
        requestFocus();
    });
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

}
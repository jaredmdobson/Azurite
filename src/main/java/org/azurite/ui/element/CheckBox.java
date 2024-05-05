package org.azurite.ui.element;

import org.azurite.graphics.Color;
import org.azurite.graphics.Sprite;
import org.azurite.ui.EventHandler;
import org.azurite.ui.Frame;
import org.azurite.ui.RenderableElement;
import org.azurite.ui.Text;
import org.azurite.ui.fonts.Font;
import org.azurite.util.Engine;
import org.azurite.util.Observable;
import org.lwjgl.glfw.GLFW;

/**
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class CheckBox extends RenderableElement implements TextHolder {

  private final Sprite uncheckedSprite, checkedSprite;
  private final CheckBoxGroup group;
  /**
   * The label/text displayed next to the CheckBox
   */
  private Text label;
  /**
   * Whether the CheckBox is currently checked
   */
  private Observable<Boolean> checked;
  private int optionIndex;

  public CheckBox(CheckBoxGroup group, int optionIndex, String label, Sprite unchecked, Sprite checked, Frame frame, float yOffset) {
    super(unchecked, new Frame(frame.getX(), frame.getY() + yOffset, frame.getWidth(), unchecked.getHeight()));
    this.group = group;
    this.optionIndex = optionIndex;
    float fontSize = this.frame.getHeight() / 2;
    this.label = new Text(label, new Font((int) fontSize), Color.BLACK, this.frame.getX() + unchecked.getWidth(), this.frame.getY() - 3 + fontSize / 2);
    this.checked = new Observable<>(false);
    this.cursor = GLFW.GLFW_POINTING_HAND_CURSOR;
    this.uncheckedSprite = unchecked;
    this.checkedSprite = checked;

    setRenderFrame(new Frame(frame.getX(), this.frame.getY(), unchecked.getWidth(), unchecked.getHeight()));

//        GameObject dummy = new GameObject(new Vector2f(this.frame.getX(), this.frame.getY() + 1)).addComponent(new SpriteRenderer(new Color(0, 0, 0, 50), new Vector2f(this.frame.getWidth(), this.frame.getHeight() - 2)));

    this.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, eventHandler -> {
      if (eventHandler.isMouseButtonClicked(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
        group.select(this.optionIndex);
      }
    });

    Engine.scenes().currentScene().addUIElement(this);
  }

  public boolean isChecked() {
    return this.checked.getValue();
  }

  public void setChecked(boolean check) {
    this.checked.setValue(check);

    if (check) setSprite(checkedSprite);
    else setSprite(uncheckedSprite);
  }

  public Observable<Boolean> getCheckedObservable() {
    return this.checked;
  }

  @Override
  public String getText() {
    return label.getText();
  }

  @Override
  public void setText(String text) {
    this.label.change(text);
  }

  public static enum Type {
    RADIO_SELECT,
    MULTI_SELECT
  }
}
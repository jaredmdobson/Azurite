package org.azurite.ui.element;

import org.azurite.ui.Alignment;
import org.azurite.ui.Element;
import org.azurite.util.MathUtils;
import org.azurite.util.Observable;

/**
 * A bar with a single node on it, that can be slided across the bar.
 * Could be described as value picker as well.
 *
 * @author Juyas
 * @version 09.11.2021
 * @since 09.11.2021
 */
public class Slider extends Element implements ValueHolder {

  /**
   * the current scale of the slider between min=0 and max=1
   */
  private final Observable<Float> currentScale;
  /**
   * the maximum and minimum of the slider
   */
  private float minScale, maxScale;
  /**
   * In which direction the slider is pointing/in which direction is maximum;
   */
  private Alignment orientation;

  public Slider(float minScale, float maxScale, float currentScale, Alignment orientation) {
    this.minScale = minScale;
    this.maxScale = maxScale;
    this.currentScale = new Observable<>(currentScale);
    this.orientation = orientation;
  }

  public Slider(float minScale, float maxScale) {
    this(minScale, maxScale, 0, Alignment.RIGHT);
  }

  /**
   * Gets the current absolute value between min and max the slider is set on.
   *
   * @return the current absolute value
   */
  public float getCurrentScale() {
    return MathUtils.map(currentScale.getValue(), 0, 1, minScale, maxScale);
  }

  /**
   * Set the current scale to a value between 0 and 1
   *
   * @param currentScale the current scale
   */
  public void setCurrentScale(float currentScale) {
    this.currentScale.setValue(MathUtils.constrain(currentScale, 0, 1));
  }

  /**
   * The exact current scale as a value between 0 and 1
   *
   * @return exact current scale.
   */
  public float getCurrentRelativeScale() {
    return currentScale.getValue();
  }

  /**
   * The maximum value of this slider
   *
   * @return the maximum value
   */
  public float getMaxScale() {
    return maxScale;
  }

  /**
   * Change the maximum value
   *
   * @param maxScale the new maximum value
   */
  public void setMaxScale(float maxScale) {
    this.maxScale = maxScale;
  }

  /**
   * The minimum value of this slider
   *
   * @return the minimum value
   */
  public float getMinScale() {
    return minScale;
  }

  /**
   * Change the minimum value
   *
   * @param minScale the new minimum value
   */
  public void setMinScale(float minScale) {
    this.minScale = minScale;
  }

  /**
   * Get the observable for the exact current scale value.
   *
   * @return the observable for the current scale
   */
  public Observable<Float> getCurrentScaleObservable() {
    return currentScale;
  }

  /**
   * Get the current orientation
   *
   * @return the current orientation
   * @see this#orientation
   */
  public Alignment getOrientation() {
    return orientation;
  }

  /**
   * Change the orientation
   *
   * @param orientation the new orientation
   */
  public void setOrientation(Alignment orientation) {
    this.orientation = orientation;
  }

  @Override
  public float getValue() {
    return getCurrentScale();
  }

  @Override
  public void setValue(float newValue) {
    setCurrentScale(MathUtils.map(newValue, minScale, maxScale, 0, 1));
  }
}
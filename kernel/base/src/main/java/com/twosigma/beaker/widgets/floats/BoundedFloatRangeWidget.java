package com.twosigma.beaker.widgets.floats;

import java.io.Serializable;
import java.util.HashMap;

public class BoundedFloatRangeWidget extends FloatRangeWidget{

  public static final String STEP = "step";
  public static final String MAX = "max";
  public static final String MIN = "min";

  private Double step = 1D;
  private Double max = 100D;
  private Double min = 0D;

  protected BoundedFloatRangeWidget() {
    super();
  }

  @Override
  protected HashMap<String, Serializable> content(HashMap<String, Serializable> content) {
    super.content(content);
    content.put(MAX, this.getMax());
    content.put(MIN, this.getMin());
    content.put(STEP, this.getStep());
    return content;
  }

  public Double getStep() {
    return step;
  }

  public void setStep(Double step) {
    this.step = step;
    sendUpdate(STEP, step);
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
    sendUpdate(MAX, max);
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
    sendUpdate(MIN, min);
  }
  
}
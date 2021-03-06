/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beaker.easyform.formitem.widgets;

import com.twosigma.beaker.easyform.formitem.RadioButtonComponent;
import com.twosigma.beaker.jupyter.comm.Comm;
import com.twosigma.beaker.widgets.CommFunctionality;
import com.twosigma.beaker.widgets.DOMWidget;
import com.twosigma.beaker.widgets.selections.RadioButtons;
import com.twosigma.beaker.widgets.box.Box;
import com.twosigma.beaker.widgets.box.HBox;
import com.twosigma.beaker.widgets.box.VBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

import static java.util.Arrays.asList;

public class RadioButtonComponentWidget extends RadioButtonComponent implements CommFunctionality, EasyFormWidget {

  private RadioButtons radioButtons;

  public RadioButtonComponentWidget() {
    this.radioButtons = new RadioButtons();
  }

  @Override
  public String getLabel() {
    return radioButtons.getDescription();
  }

  @Override
  public Comm getComm() {
    return radioButtons.getComm();
  }

  @Override
  public void setLabel(String label) {
    radioButtons.setDescription(label);
  }

  @Override
  public String getValue() {
    return radioButtons.getValue();
  }

  @Override
  public void setValue(String value) {
    radioButtons.setValue(value);
  }

  @Override
  public void setValues(Collection<String> values) {
    this.radioButtons.setOptions(values.toArray(new String[0]));
  }

  @Override
  public Collection<String> getValues() {
    return Arrays.stream(radioButtons.getOptions()).collect(Collectors.toList());
  }

  @Override
  public DOMWidget getWidget() {
    if (getHorizontal()) {
      return new HBox(Collections.singletonList(this.radioButtons));
    } else {
      return this.radioButtons;
    }
  }
  
  @Override
  public void close() {
    getComm().close();
  }

}
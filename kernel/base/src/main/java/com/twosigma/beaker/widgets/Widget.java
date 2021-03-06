/*
 *  Copyright 2014 TWO SIGMA OPEN SOURCE, LLC
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
package com.twosigma.beaker.widgets;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.twosigma.jupyter.handler.KernelHandlerWrapper.wrapBusyIdle;

import com.twosigma.beaker.jupyter.KernelManager;
import com.twosigma.beaker.jupyter.comm.Comm;
import com.twosigma.beaker.jupyter.comm.TargetNamesEnum;
import com.twosigma.jupyter.message.Message;

public abstract class Widget implements CommFunctionality, DisplayableWidget {
  
  public enum CommActions {

    ONDOUBLECLICK("ondoubleclick"),
    ONCLICK("onclick"),
    ONKEY("onkey"),
    ACTIONDETAILS("actiondetails"),
    ONCONTEXTMENU("oncontextmenu"),
    CLICK("click");

    private String action;

    CommActions(String action) {
      this.action = action;
    }

    public String getAction() {
      return action;
    }
    
    public static CommActions getByAction(final String input){
      CommActions ret = null;
      if(input != null){
        for (CommActions item : CommActions.values()) {
          if(item.name().equalsIgnoreCase(input.trim())){
            ret = item;
            break;
          }
        }
      }
      return ret;
    }

  }

  public static final String MODEL_MODULE = "_model_module";
  public static final String MODEL_NAME = "_model_name";
  public static final String VIEW_MODULE = "_view_module";
  public static final String VIEW_NAME = "_view_name";

  public static final String MODEL_MODULE_VALUE = "jupyter-js-widgets";
  public static final String VIEW_MODULE_VALUE = "jupyter-js-widgets";

  public static final String VALUE = "value";
  public static final String DISABLED = "disabled";
  public static final String VISIBLE = "visible";
  public static final String DESCRIPTION = "description";
  public static final String MSG_THROTTLE = "msg_throttle";

  public static final String METHOD = "method";
  public static final String DISPLAY = "display";

  private Comm comm;

  public Widget() {
    comm = new Comm(TargetNamesEnum.JUPYTER_WIDGET);
  }

  protected void openComm() {
    comm.setData(createContent());
    addValueChangeMsgCallback();
    comm.open();
  }

  public void close() {
    if (this.comm != null) {
      this.comm.close();
    }
  }

  @Override
  public void display() {
    HashMap<String, Serializable> content = new HashMap<>();
    content.put(METHOD, DISPLAY);
    getComm().setData(content);
    getComm().send();
  }

  private HashMap<String, Serializable> createContent() {
    HashMap<String, Serializable> result = new HashMap<>();
    result.put(MODEL_MODULE, getModelModuleValue());
    result.put(VIEW_MODULE, getViewModuleValue());
    String mn = getModelNameValue();
    if(mn != null && !mn.isEmpty()){
      result.put(MODEL_NAME, mn);
    }
    String vnv = getViewNameValue();
    if(vnv != null && !vnv.isEmpty()){
      result.put(VIEW_NAME, vnv);
    }
    result = content(result);
    return result;
  }
  
  public abstract String getModelNameValue();

  public abstract String getViewNameValue();

  public String getModelModuleValue(){
    return MODEL_MODULE_VALUE;
  }

  public String getViewModuleValue(){
    return VIEW_MODULE_VALUE;
  }

  protected abstract void addValueChangeMsgCallback();

  protected abstract HashMap<String, Serializable> content(HashMap<String, Serializable> content);

  @Override
  public Comm getComm() {
    return this.comm;
  }

  public void sendUpdate(String propertyName, Object value) {
    this.comm.sendUpdate(propertyName, value);
  }
  
  public void handleCommEventSync(Message message, CommActions action, ActionPerformed handlerAction) {
    wrapBusyIdle(KernelManager.get(), message, () -> handleCommEvent(message, action, handlerAction));
  }
  
  private void handleCommEvent(Message message, CommActions action, ActionPerformed handlerAction) {
    if (message.getContent() != null) {
      Serializable data = message.getContent().get("data");
      if (data != null && data instanceof LinkedHashMap) {
        Object contentObject = ((LinkedHashMap) data).get("content");
        if (contentObject instanceof LinkedHashMap) {
          HashMap content = (LinkedHashMap) contentObject;
          Object event = content.get("event");
          if (event.equals(action.getAction())) {
            handlerAction.executeAction(content, message);
          }
        }
      }
    }
  }
  
  public interface ActionPerformed {
    void executeAction(HashMap content, Message message);
  }

}
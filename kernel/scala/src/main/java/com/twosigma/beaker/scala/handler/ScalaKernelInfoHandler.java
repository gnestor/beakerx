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
package com.twosigma.beaker.scala.handler;

import com.twosigma.beaker.KernelInfoHandler;
import com.twosigma.jupyter.KernelFunctionality;

import java.io.Serializable;
import java.util.HashMap;

public class ScalaKernelInfoHandler  extends KernelInfoHandler {

  public ScalaKernelInfoHandler(KernelFunctionality kernel) {
    super(kernel);
  }

  @Override
  protected HashMap<String, Serializable> doLanguageInfo(HashMap<String, Serializable> languageInfo) {
    languageInfo.put("name", "Scala");
    languageInfo.put("version", scala.util.Properties.scalaPropOrElse("version.number", "(unknown)"));
    languageInfo.put("mimetype", "");
    languageInfo.put("file_extension", ".scala");
    languageInfo.put("codemirror_mode", "text/x-scala");
    languageInfo.put("nbconverter_exporter", "");
    return languageInfo;
  }

  @Override
  protected HashMap<String, Serializable> doContent(HashMap<String, Serializable> content) {
    content.put("implementation", "scala");
    content.put("banner", "BeakerX kernel for Scala");
    return content;
  }

}

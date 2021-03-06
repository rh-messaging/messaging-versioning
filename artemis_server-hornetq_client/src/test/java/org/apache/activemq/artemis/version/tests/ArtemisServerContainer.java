/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.version.tests;

import java.util.LinkedList;

import org.apache.activemq.artemis.version.base.ServerContainer;
import org.apache.activemq.artemis.version.base.util.SpawnVMSupport;
import org.apache.activemq.artemis.version.tests.serverContainer.ArtemisServerProcess;

public class ArtemisServerContainer implements ServerContainer {

   Process process;

   private String folder;
   private String id;
   private String[] queues;
   private String[] topics;

   public void setInfo(String folder, String id, String[] queues, String[] topics) throws Exception {
      this.folder = folder;
      this.id = id;
      this.queues = queues;
      this.topics = topics;
   }


   public ArtemisServerContainer() {
   }

   public void start() throws Exception {
      String classPath = System.getProperty("serverClassPath");
      if (classPath == null) {
         classPath = System.getProperty("java.class.path");
      }

      LinkedList<String> parameters = new LinkedList<String>();
      parameters.add(folder);
      parameters.add(id);
      parameters.add(Integer.toString(queues.length));
      for (String q : queues) {
         parameters.add(q);
      }

      parameters.add(Integer.toString(topics.length));
      for (String t: topics) {
         parameters.add(t);
      }

      process = SpawnVMSupport.spawnVM(ArtemisServerProcess.wordStart, classPath, ArtemisServerProcess.class.getName(), new String[]{}, true, true, "srv:" + id, parameters.toArray(new String[parameters.size()]));
   }

   public void stop() throws Exception {
      if (process != null) {
         process.destroy();
      }
   }
}

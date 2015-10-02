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

import java.io.File;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.JournalType;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.activemq.artemis.version.base.ServerContainer;
import org.apache.activemq.artemis.version.base.util.SpawnVMSupport;

public class ArtemisServerContainer implements ServerContainer {

   Process process;

   private String folder;
   private int id;
   private String[] queues;
   private String[] topics;

   public void setInfo(String folder, int id, String[] queues, String[] topics) throws Exception {
      this.folder = folder;
      this.id = id;
      this.queues = queues;
      this.topics = topics;
   }

   public ArtemisServerContainer() throws Exception {
   }

   @Override
   public void start() throws Exception {
//      SpawnVMSupport.spawnVM()
//      embeddedJMS.start();
   }

   @Override
   public void stop() throws Exception {
//      embeddedJMS.stop();
   }
}

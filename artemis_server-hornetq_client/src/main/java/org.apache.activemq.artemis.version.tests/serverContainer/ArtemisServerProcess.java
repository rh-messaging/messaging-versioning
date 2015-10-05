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

package org.apache.activemq.artemis.version.tests.serverContainer;

import java.io.File;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.JournalType;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.TopicConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;

public class ArtemisServerProcess {

   public static final String wordStart = "**SERVER STARTED**";

   static Configuration configuration;
   static JMSConfiguration jmsConfiguration;
   /**
    * String folder, int id, String[] queues, String[] topics
    * @param arg
    */
   public static void main(String arg[]) {
      String folder = arg[0];
      String id = arg[1];

      int numberOfQueues = Integer.parseInt(arg[2]);

      int placeOnArg = 3;

      String[] queues = new String[numberOfQueues];
      for (int i = 0; i < numberOfQueues; i++) {
         queues[i] = arg[placeOnArg + i];
      }

      placeOnArg += numberOfQueues;

      int numberOfTopics = Integer.parseInt(arg[placeOnArg]);
      placeOnArg++;

      String topics[] = new String[numberOfTopics];
      for (int i = 0; i < numberOfTopics; i++) {
         topics[i] = arg[placeOnArg + i];
      }

      try {
         configuration = new ConfigurationImpl();
         configuration.setJournalType(JournalType.NIO);
         configuration.setBrokerInstance(new File(folder + "/" + id));
         configuration.addAcceptorConfiguration("artemis", "tcp://0.0.0.0:61616");
         configuration.setSecurityEnabled(false);

         jmsConfiguration = new JMSConfigurationImpl();

         for (String queue : queues) {
            JMSQueueConfigurationImpl queueConfiguration = new JMSQueueConfigurationImpl().setName(queue);
            jmsConfiguration.getQueueConfigurations().add(queueConfiguration);
         }
         for (String t : topics) {
            TopicConfigurationImpl toicConfiguration = new TopicConfigurationImpl().setName(t);
            jmsConfiguration.getTopicConfigurations().add(toicConfiguration);
         }

         EmbeddedJMS embeddedJMS = new EmbeddedJMS();
         embeddedJMS.setConfiguration(configuration);
         embeddedJMS.setJmsConfiguration(jmsConfiguration);
         embeddedJMS.start();

         System.out.println(wordStart);
      }
      catch (Exception e) {
         e.printStackTrace();
      }



   }
}

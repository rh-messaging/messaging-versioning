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
import java.util.HashMap;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.hornetq.jms.server.config.impl.TopicConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;

public class HornetQServerProcess {


   public static final String wordStart = "**SERVER STARTED**";

   static Configuration configuration;
   static JMSConfiguration jmsConfiguration;
   static EmbeddedJMS embeddedJMS;

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

      configuration = new ConfigurationImpl();
      configuration.setSecurityEnabled(false);
      configuration.setJournalDirectory(mkdir(folder + "/" + id + "/data/journal"));
      configuration.setBindingsDirectory(mkdir(folder + "/" + id + "/data/binding"));
      configuration.setPagingDirectory(mkdir(folder + "/" + id + "/data/binding"));
      configuration.setLargeMessagesDirectory(mkdir(folder + "/" + id + "/data/binding"));
      configuration.setJournalType(org.hornetq.core.server.JournalType.NIO);

      HashMap map = new HashMap();
      TransportConfiguration tpc = new TransportConfiguration(NettyAcceptorFactory.class.getName(), map);
      configuration.getAcceptorConfigurations().add(tpc);

      try {
         jmsConfiguration = new JMSConfigurationImpl();

         for (String queue : queues) {
            JMSQueueConfigurationImpl queueConfiguration = new JMSQueueConfigurationImpl(queue, null, true);
            jmsConfiguration.getQueueConfigurations().add(queueConfiguration);
         }
         for (String t : topics) {
            TopicConfigurationImpl toicConfiguration = new TopicConfigurationImpl(t);
            jmsConfiguration.getTopicConfigurations().add(toicConfiguration);
         }
         embeddedJMS = new EmbeddedJMS();
         embeddedJMS.setConfiguration(configuration);
         embeddedJMS.setJmsConfiguration(jmsConfiguration);
         embeddedJMS.start();

         // TODO: topics
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      System.out.println(wordStart);

   }


   private static String mkdir(String directory) {
      File file = new File(directory);
      file.mkdirs();
      return directory;
   }



}

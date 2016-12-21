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

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.artemis.version.base.ClientContainer;
import org.apache.activemq.artemis.version.base.ClientServerExchange;
import org.apache.activemq.artemis.version.base.ServerContainer;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSConstants;
import org.hornetq.core.message.impl.MessageImpl;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQJMSConnectionFactory;

public class ArtemisServerExchange implements ClientServerExchange {

   private String folder;

   public ArtemisServerExchange(String folder) {
      this.folder = folder;
   }

   public ClientContainer newClient() {
      return new HornetQClientConnector();
   }

   public ServerContainer newServerContainer() throws Exception {
      return new ArtemisServerContainer();

   }

   class HornetQClientConnector implements ClientContainer {

      public void close() {

      }

      public String get_LargeMessageInputStream() {
         return HornetQJMSConstants.JMS_HORNETQ_INPUT_STREAM;
      }

      public ConnectionFactory getFactory() {
         Map properties = new HashMap();
         properties.put(TransportConstants.HOST_PROP_NAME, "localhost");
         properties.put(TransportConstants.PORT_PROP_NAME, "61616");
         TransportConfiguration configuration = new TransportConfiguration(NettyConnectorFactory.class.getName(), properties);
         HornetQJMSConnectionFactory factory = new HornetQJMSConnectionFactory(false, configuration);
         return factory;
      }

      public String get_HDR_DUPLICATE_DETECTION_ID() {
         return MessageImpl.HDR_DUPLICATE_DETECTION_ID.toString();
      }
   }
}

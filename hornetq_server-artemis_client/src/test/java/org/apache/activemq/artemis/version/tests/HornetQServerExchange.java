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

import org.apache.activemq.artemis.api.jms.ActiveMQJMSConstants;
import org.apache.activemq.artemis.core.message.impl.MessageImpl;
import org.apache.activemq.artemis.core.protocol.hornetq.client.HornetQClientProtocolManagerFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.version.base.ClientContainer;
import org.apache.activemq.artemis.version.base.ClientServerExchange;

public class HornetQServerExchange implements ClientServerExchange {

   private String folder;

   public HornetQServerExchange(String folder) {
      this.folder = folder;
   }

   @Override
   public ClientContainer newClient() {
      return new ArtemisClientContainer();
   }

   @Override
   public HornetQServerContainer newServerContainer() {
      return new HornetQServerContainer();
   }

   class ArtemisClientContainer implements ClientContainer {

      @Override
      public void close() {

      }

      @Override
      public String get_LargeMessageInputStream() {
         return ActiveMQJMSConstants.JMS_ACTIVEMQ_INPUT_STREAM;
      }

      @Override
      public ConnectionFactory getFactory() {
         ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:5445?protocolManagerFactoryStr=" + HornetQClientProtocolManagerFactory.class.getName());
         return factory;
      }

      @Override
      public String get_HDR_DUPLICATE_DETECTION_ID() {
         return MessageImpl.HDR_DUPLICATE_DETECTION_ID.toString();
      }
   }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.version.tests;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.IOException;
import java.io.InputStream;

import org.apache.activemq.artemis.version.base.ClientContainer;
import org.apache.activemq.artemis.version.base.ServerContainer;
import org.apache.activemq.artemis.version.tests.base.IsolatedServerVersionBaseTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractLargeMessageTest extends IsolatedServerVersionBaseTest {

   public static final int LARGE_MESSAGE_SIZE = 10 * 1024;
   ServerContainer serverContainer;

   ClientContainer clientContainer;

   String queueName = "test.hq.queue";

   @Before
   @Override
   public void setUp() throws Exception {
      super.setUp();

      this.serverContainer = startServer("0", new String[]{queueName}, new String[0]);

      clientContainer = exchange.newClient();
   }

   @After
   public void tearDown() throws Exception {
      clientContainer.close();
      serverContainer.stop();
   }

   // Creates a Fake LargeStream without using a real file
   public static InputStream createFakeLargeStream(final long size) throws Exception {
      return new InputStream() {
         private long count;

         private boolean closed = false;

         @Override
         public void close() throws IOException {
            super.close();
            closed = true;
         }

         @Override
         public int read() throws IOException {
            if (closed) {
               throw new IOException("Stream was closed");
            }
            if (count++ < size) {
               return getSamplebyte(count - 1);
            }
            else {
               return -1;
            }
         }

      };

   }

   public static byte getSamplebyte(final long position) {
      return (byte) ('a' + position % ('z' - 'a' + 1));
   }

   @Test
   public void testMessagePropertiesAreTransformedBetweenCoreAndHQProtocols() throws Exception {

      ConnectionFactory connectionFactory = clientContainer.getFactory();
      Connection connection = connectionFactory.createConnection();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue queue =  session.createQueue(queueName);

      MessageProducer producer = session.createProducer(queue);
      MessageConsumer consumer = session.createConsumer(queue);

      // HornetQ Client Objects
      connection.start();

      for (int i = 0; i < 10; i++) {
         BytesMessage m = session.createBytesMessage();
         m.setIntProperty("count", i);

         m.setObjectProperty(clientContainer.get_LargeMessageInputStream(), createFakeLargeStream(LARGE_MESSAGE_SIZE));

         producer.send(m);
      }

      connection.close();

      connection = connectionFactory.createConnection();

      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      consumer = session.createConsumer(queue);

      connection.start();

      for (int m = 0; m < 10; m++) {
         BytesMessage rm = (BytesMessage) consumer.receive(10000);
         Assert.assertNotNull(rm);
         Assert.assertEquals(m, rm.getIntProperty("count"));

         byte[] data = new byte[1024];

         System.out.println("Message = " + rm);

         for (int i = 0; i < LARGE_MESSAGE_SIZE; i += 1024) {
            int numberOfBytes = rm.readBytes(data);
            Assert.assertEquals(1024, numberOfBytes);
            for (int j = 0; j < 1024; j++) {
               Assert.assertEquals(getSamplebyte(i + j), data[j]);
            }
         }
      }

      connection.close();
   }
}

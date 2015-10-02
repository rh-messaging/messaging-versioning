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

package org.apache.activemq.artemis.version.tests.base;

import java.io.File;

import org.apache.activemq.artemis.version.base.ClientServerExchange;
import org.apache.activemq.artemis.version.base.ServerContainer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public abstract class IsolatedServerVersionBaseTest {

   @Rule
   public RemoveFolder removeFolder = new RemoveFolder("./target/tmp");

   @Rule
   public final TemporaryFolder temporaryFolder;

   protected ClassLoader serverClassLoader;
   protected Class serverClass;
   protected ClientServerExchange exchange;

   public IsolatedServerVersionBaseTest() {
      File parent = new File("./target/tmp");
      parent.mkdirs();
      temporaryFolder = new TemporaryFolder(parent);
   }

   protected abstract ClientServerExchange newExchange() throws Exception;

   @Before
   public void setUp() throws Exception {
      exchange = newExchange();
   }

   protected ServerContainer startServer(int serverID, String[] queues, String[] topics) throws Exception {
      ServerContainer container = exchange.newServerContainer();
      container.setInfo(temporaryFolder.getRoot().getAbsolutePath(), serverID, queues, topics);
      container.start();
      return container;

   }

}

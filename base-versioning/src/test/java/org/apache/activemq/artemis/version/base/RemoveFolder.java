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

package org.apache.activemq.artemis.version.base;

import java.io.File;

import org.junit.rules.ExternalResource;

public class RemoveFolder extends ExternalResource {

   private final String folderName;

   public RemoveFolder(String folderName) {
      this.folderName = folderName;
   }

   /**
    * Override to tear down your specific external resource.
    */
   protected void after() {
      deleteDirectory(new File(folderName));
   }

   protected static final boolean deleteDirectory(final File directory) {
      if (directory.isDirectory()) {
         String[] files = directory.list();
         int num = 5;
         int attempts = 0;
         while (files == null && (attempts < num)) {
            try {
               Thread.sleep(100);
            }
            catch (InterruptedException e) {
            }
            files = directory.list();
            attempts++;
         }

         for (String file : files) {
            File f = new File(directory, file);
            if (!deleteDirectory(f)) {
               System.err.println("Could not remove directory " + f);
            }
         }
      }

      return directory.delete();
   }

}

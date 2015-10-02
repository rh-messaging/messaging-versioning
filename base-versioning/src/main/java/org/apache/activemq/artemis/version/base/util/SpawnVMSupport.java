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

package org.apache.activemq.artemis.version.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

public final class SpawnVMSupport {


   public static Process spawnVM(final String classpath,
                                 final String className,
                                 final String memoryArg1,
                                 final String memoryArg2,
                                 final String[] vmargs,
                                 final boolean logOutput,
                                 final boolean logErrorOutput,
                                 final String... args) throws Exception {
      ProcessBuilder builder = new ProcessBuilder();
      final String javaPath = Paths.get(System.getProperty("java.home"), "bin", "java").toAbsolutePath().toString();
      builder.command(javaPath, memoryArg1, memoryArg2, "-cp", classpath);

      List<String> commandList = builder.command();

      if (vmargs != null) {
         for (String arg : vmargs) {
            commandList.add(arg);
         }
      }
      commandList.add(className);
      for (String arg : args) {
         commandList.add(arg);
      }

      System.out.println("####");
      for (String string : builder.command()) {
         System.out.print(string + " ");
      }
      System.out.println();

      Process process = builder.start();

      if (logOutput) {
         SpawnVMSupport.startLogger(className, process);

      }

      // Adding a reader to System.err, so the VM won't hang on a System.err.println as identified on this forum thread:
      // http://www.jboss.org/index.html?module=bb&op=viewtopic&t=151815
      ProcessLogger errorLogger = new ProcessLogger(logErrorOutput, process.getErrorStream(), className);
      errorLogger.start();

      return process;
   }

   /**
    * @param className
    * @param process
    * @throws ClassNotFoundException
    */
   public static void startLogger(final String className, final Process process) throws ClassNotFoundException {
      ProcessLogger outputLogger = new ProcessLogger(true, process.getInputStream(), className);
      outputLogger.start();
   }

   /**
    * Redirect the input stream to a logger (as debug logs)
    */
   static class ProcessLogger extends Thread {

      private final InputStream is;

      private final String className;

      private final boolean print;

      ProcessLogger(final boolean print, final InputStream is, final String className) throws ClassNotFoundException {
         this.is = is;
         this.print = print;
         this.className = className;
         setDaemon(true);
      }

      @Override
      public void run() {
         try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
               if (print) {
                  System.out.println(className + ":" + line);
               }
            }
         }
         catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }
   }
}

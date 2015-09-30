package org.apache.activemq.artemis.version.base;

public interface ServerContainer {
   void start();
   void stop();
   void createQueue(String queueName);
}

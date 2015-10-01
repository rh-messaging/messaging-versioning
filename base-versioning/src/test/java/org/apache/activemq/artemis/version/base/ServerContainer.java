package org.apache.activemq.artemis.version.base;

public interface ServerContainer {
   void start() throws Exception;
   void stop() throws Exception;
   void createQueue(String queueName) throws Exception;
}

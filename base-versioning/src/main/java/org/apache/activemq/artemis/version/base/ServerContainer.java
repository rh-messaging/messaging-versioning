package org.apache.activemq.artemis.version.base;

public interface ServerContainer {
   void setInfo(String place, String id, String[] queues, String[] topics) throws Exception;
   void start() throws Exception;
   void stop() throws Exception;
}

#!/bin/sh -e -v
mvn clean
mvn -PHQ_2_4_6 install
mvn -PHQ_2_4_7 install
mvn -PHQ_2_3_25 install
mvn -PHQ_2_2_16 install -PNO_HQSERVER

### running version tests

The property artemis.version could be changed to use a different artemis.version.


There are several profiles that you can use to interchange hornetqClient->ArtemisServer communication and ArtemisClient->HornetQServer communication.


The tests on this project are defined in both directions.



Currently we don't support running tests using hornetq 2.2.x servers (only clients), so for any 2.2.x clients you have to define the Profile ```NO_HQSERVER```.


### shell script


You can use this following script to run all the supported profiles:

```sh
./runAllTests.sh
```


### Running on Jenkins

You will have to create one Jenkins build for each one of these builds:

```sh
mvn -PHQ_2_4_6 install
mvn -PHQ_2_4_7 install
mvn -PHQ_2_3_25 install
mvn -PHQ_2_2_30 install -PNO_HQSERVER
mvn -PHQ_2_2_16 install -PNO_HQSERVER
```


* Refer to runAllTests.sh for an always updated list as we may add more profiles as required.


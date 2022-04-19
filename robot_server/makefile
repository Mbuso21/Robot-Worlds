all: compile package verify

compile:
	mvn compile

testReference:
	java -jar libs/reference-server-0.2.2.jar&
	sleep 10 && mvn test
	bash /scripts/shell/closeServer.sh

testPersonal:
	mvn exec:java&
	sleep 10 && mvn test
	bash /scripts/shell/closeServer.sh

verify:
	mvn verify

package:
	mvn package

clean:
	mvn clean
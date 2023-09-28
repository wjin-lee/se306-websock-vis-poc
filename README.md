# se306-websock-vis-poc
A small proof-of-concept for an all-in-one web application configured to compile into a single JAR file.

**IMPORTANT: This proof-of-concept targets Java 11!**

Underneath, it hosts a websocket endpoint which state data can be published from.
The web application is then served through an embedded Jetty server and subscribes to state information via the websocket.\
The web application can be found at `src/main/webapp`

First, clean then target maven to run until the `package` goal.
```
mvn clean package
```

Then, the compiled application can be run with:
```
java -jar <PATH_TO_JAR>
```

E.g.
```
java -jar target/websock-vis-poc.jar
```

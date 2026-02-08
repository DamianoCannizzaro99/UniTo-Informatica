JUNIT_JAR=junit-4.10.jar
JUNIT_CP=-cp .:$(JUNIT_JAR)
CSV_FILE=italian_dist_graph.csv

# File di classe da compilare
CLASSES = AbstractEdge.class AbstractGraph.class AbstractQueue.class Edge.class Graph.class GraphTest.class Prim.class TestPriorityQueue.class PriorityQueue.class

all: $(CLASSES)

AbstractEdge.class: AbstractEdge.java
	javac $<

AbstractGraph.class: AbstractGraph.java
	javac $<

AbstractQueue.class: AbstractQueue.java
	javac $<

Edge.class: Edge.java
	javac $<

Graph.class: Graph.java
	javac $<

GraphTest.class: GraphTest.java
	javac $(JUNIT_CP) $<

Prim.class: Prim.java
	javac $<

PriorityQueue.class: PriorityQueue.java
	javac $<

TestPriorityQueue.class: TestPriorityQueue.java
	javac $(JUNIT_CP) $<

testGraph: GraphTest.class
	java $(JUNIT_CP):classes org.junit.runner.JUnitCore GraphTest
	
testQueue: TestPriorityQueue.class
	java $(JUNIT_CP):classes org.junit.runner.JUnitCore TestPriorityQueue

run: Prim.class
	java Prim $(CSV_FILE)

clean:
	rm -f *.class

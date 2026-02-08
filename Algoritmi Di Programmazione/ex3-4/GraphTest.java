import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;

public class GraphTest {
    private Graph<Integer, String> graph;

    @Before
    public void setUp() {
        graph = new Graph<>(false, true);
    }

    @Test
    public void testAddNode() {
        assertTrue(graph.addNode(1));
        assertTrue(graph.containsNode(1));
        assertFalse(graph.addNode(1)); 
    }

    @Test
    public void testAddEdge() {
        graph.addNode(1);
        graph.addNode(2);
        assertTrue(graph.addEdge(1, 2, "edge1"));
        assertTrue(graph.containsEdge(1, 2));
        assertFalse(graph.addEdge(1, 2, "edge2"));
    }

    @Test
    public void testRemoveNode() {
        graph.addNode(1);
        assertTrue(graph.removeNode(1));
        assertFalse(graph.containsNode(1));
        assertFalse(graph.removeNode(1)); 
    }

    @Test
    public void testRemoveEdge() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addEdge(1, 2, "edge1");
        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.containsEdge(1, 2));
        assertFalse(graph.removeEdge(1, 2)); 
    }

    @Test
public void testGetLabel() {
    graph.addNode(1);
    graph.addNode(2);
    graph.addEdge(1, 2, "edge1");
    assertEquals("edge1", graph.getLabel(1, 2));
}
@Test
public void testGetNeighbours() {
    graph.addNode(1);
    graph.addNode(2);
    graph.addEdge(1, 2, "edge1");
    Collection<Integer> neighbours = graph.getNeighbours(1);
    assertTrue(neighbours.contains(2));
}
@Test
public void testIsDirected() {
    Graph<Integer, String> directedGraph = new Graph<>(true, true);
    assertTrue(directedGraph.isDirected());
    Graph<Integer, String> undirectedGraph = new Graph<>(false, true);
    assertFalse(undirectedGraph.isDirected());
}

@Test
public void testIsLabelled() {
    Graph<Integer, String> labelledGraph = new Graph<>(true, true);
    assertTrue(labelledGraph.isLabelled());
    Graph<Integer, String> unlabelledGraph = new Graph<>(true, false);
    assertFalse(unlabelledGraph.isLabelled());
}

@Test
public void testGetNodes() {
    graph.addNode(1);
    graph.addNode(2);
    Collection<Integer> nodes = graph.getNodes();
    assertTrue(nodes.contains(1));
    assertTrue(nodes.contains(2));
}

@Test
public void testGetEdges() {
    graph.addNode(1);
    graph.addNode(2);
    graph.addEdge(1, 2, "edge1");
    Collection<? extends AbstractEdge<Integer, String>> edges = graph.getEdges();
    for (AbstractEdge<Integer, String> edge : edges) {
        if (edge.getStart().equals(1) && edge.getEnd().equals(2)) {
            assertEquals("edge1", edge.getLabel());
        }
    }
}
}

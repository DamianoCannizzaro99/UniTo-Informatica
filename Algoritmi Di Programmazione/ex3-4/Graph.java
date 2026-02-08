import java.util.*;

public class Graph<V, L> implements AbstractGraph<V, L> {
    private final boolean directed;
    private final boolean labelled;
    private final Map<V, Map<V, L>> adjList;
    private int edgeCount;

    public Graph(boolean directed, boolean labelled) {
        this.directed = directed;
        this.labelled = labelled;
        this.adjList = new HashMap<>();
        this.edgeCount = 0;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public boolean isLabelled() {
        return labelled;
    }

    @Override
    public boolean addNode(V a) {
        if (adjList.containsKey(a)) {
            return false;
        }
        adjList.put(a, new HashMap<>());
        return true;
    }

    @Override
    public boolean addEdge(V a, V b, L l) {
        if (!adjList.containsKey(a) || !adjList.containsKey(b)) {
            return false;
        }
        if (adjList.get(a).containsKey(b)) {
            return false;
        }
        adjList.get(a).put(b, labelled ? l : null);
        if (!directed) {
            adjList.get(b).put(a, labelled ? l : null);
        }
        edgeCount++;
        return true;
    }

    @Override
    public boolean containsNode(V a) {
        return adjList.containsKey(a);
    }

    @Override
    public boolean containsEdge(V a, V b) {
        return adjList.containsKey(a) && adjList.get(a).containsKey(b);
    }

    @Override
    public boolean removeNode(V a) {
        if (!adjList.containsKey(a)) {
            return false;
        }
        for (V neighbour : adjList.get(a).keySet()) {
            adjList.get(neighbour).remove(a);
            edgeCount--;
        }
        adjList.remove(a);
        return true;
    }

    @Override
    public boolean removeEdge(V a, V b) {
        if (!adjList.containsKey(a) || !adjList.get(a).containsKey(b)) {
            return false;
        }
        adjList.get(a).remove(b);
        if (!directed) {
            adjList.get(b).remove(a);
        }
        edgeCount--;
        return true;
    }

    @Override
    public int numNodes() {
        return adjList.size();
    }

    @Override
    public int numEdges() {
        return edgeCount;
    }

    @Override
    public Collection<V> getNodes() {
        return adjList.keySet();
    }

    @Override
    public Collection<? extends AbstractEdge<V, L>> getEdges() {
        List<AbstractEdge<V, L>> edges = new ArrayList<>();
        for (V start : adjList.keySet()) {
            for (Map.Entry<V, L> entry : adjList.get(start).entrySet()) {
                edges.add(new Edge<>(start, entry.getKey(), entry.getValue()));
            }
        }
        return edges;
    }

    @Override
    public Collection<V> getNeighbours(V a) {
        if (!adjList.containsKey(a)) {
            return Collections.emptyList();
        }
        return adjList.get(a).keySet();
    }

    @Override
    public L getLabel(V a, V b) {
        if (!adjList.containsKey(a) || !adjList.get(a).containsKey(b)) {
            return null;
        }
        return adjList.get(a).get(b);
    }
}

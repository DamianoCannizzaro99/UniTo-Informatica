import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Prim2 {
    public static <V, L extends Number> Collection<? extends AbstractEdge<V, L>> minimumSpanningForest(Graph<V, L> graph) {
        Collection<AbstractEdge<V, L>> result = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        PriorityQueue<AbstractEdge<V, L>> pq = new PriorityQueue<>(Comparator.comparing(AbstractEdge::getLabel, Comparator.comparingDouble(Number::doubleValue)));

        for (V node : graph.getNodes()) {
            if (!visited.contains(node)) {
                visited.add(node);
                for (AbstractEdge<V, L> edge : graph.getEdges()) {
                    if (edge.getStart().equals(node) || edge.getEnd().equals(node)) {
                        pq.push(edge);
                    }
                }

                while (!pq.empty()) {
                    AbstractEdge<V, L> edge = pq.top();
                    pq.pop();
                    V start = edge.getStart();
                    V end = edge.getEnd();
                    if (visited.contains(start) && visited.contains(end)) {
                        continue;
                    }
                    result.add(edge);
                    V nextNode = visited.contains(start) ? end : start;
                    visited.add(nextNode);
                    for (V neighbour : graph.getNeighbours(nextNode)) {
                        if (!visited.contains(neighbour)) {
                            pq.push(new Edge<>(nextNode, neighbour, graph.getLabel(nextNode, neighbour)));
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Prim <graph-csv-file>");
            System.exit(1);
        }

        Graph<String, Double> graph = new Graph<>(false, true);

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length != 3) continue;
                String node1 = parts[0];
                String node2 = parts[1];
                Double weight = Double.parseDouble(parts[2]);
                graph.addNode(node1);
                graph.addNode(node2);
                graph.addEdge(node1, node2, weight);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Collection<? extends AbstractEdge<String, Double>> mst = minimumSpanningForest(graph);

        for (AbstractEdge<String, Double> edge : mst) {
            System.out.println(edge.getStart() + "," + edge.getEnd() + "," + edge.getLabel());
        }

        System.err.println("Numero di nodi nella foresta: " + graph.numNodes());
        System.err.println("Numero di archi nella foresta: " + mst.size());
        double totalWeight = mst.stream().mapToDouble(edge -> edge.getLabel().doubleValue()).sum();
        System.err.println("Peso totale della foresta: " + totalWeight);
    }
}

import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.*;

public class Prim {
  public static <V, L extends Number> Collection<? extends AbstractEdge<V, L>> minimumSpanningForest(Graph<V, L> graph) {
    List<AbstractEdge<V, L>> mstEdges = new ArrayList<>();
    Set<V> visited = new HashSet<>();
    PriorityQueue<AbstractEdge<V, L>> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(edge -> edge.getLabel().doubleValue()));
    
    for (V startNode : graph.getNodes()) {
      if (!visited.contains(startNode)) {
        visit(graph, startNode, visited, edgeQueue);
        
        while (!edgeQueue.empty()) {
          AbstractEdge<V, L> edge = edgeQueue.top();
          edgeQueue.pop();
          
          V v = edge.getStart();
          V w = edge.getEnd();
          
          if (visited.contains(v) && visited.contains(w)) continue;
          
          mstEdges.add(edge);
          
          if (!visited.contains(v)) visit(graph, v, visited, edgeQueue);
          if (!visited.contains(w)) visit(graph, w, visited, edgeQueue);
        }
      }
    }
    
    return mstEdges;
  }

  private static <V, L extends Number> void visit(Graph<V, L> graph, V node, Set<V> visited, PriorityQueue<AbstractEdge<V, L>> edgeQueue) {
    visited.add(node);
    for (V neighbor : graph.getNeighbours(node)) {
      if (!visited.contains(neighbor)) {
        L label = graph.getLabel(node, neighbor);
        edgeQueue.push(new Edge<>(node, neighbor, label));
      }
    }
  }

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java Prim <path to CSV file>");
      System.exit(1);
    }

    String csvFilePath = args[0];
    Graph<String, Float> graph = new Graph<>(true, true);

    try {
      List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
      for (String line : lines) {
        String[] fields = line.split(",");
        if (fields.length == 3) {
          String place1 = fields[0].trim();
          String place2 = fields[1].trim();
          Float distance = Float.parseFloat(fields[2].trim());

          graph.addNode(place1);
          graph.addNode(place2);
          graph.addEdge(place1, place2, distance);
          graph.addEdge(place2, place1, distance); // Arco non orientato
        }
      }

      Collection<? extends AbstractEdge<String, Float>> mstEdges = minimumSpanningForest(graph);
      
   
      for (AbstractEdge<String, Float> edge : mstEdges) {
        System.out.println(edge.getStart() + "," + edge.getEnd() + "," + edge.getLabel());
      }

  
      int numNodes = mstEdges.stream()
                             .map(edge -> Arrays.asList(edge.getStart(), edge.getEnd()))
                             .flatMap(Collection::stream)
                             .collect(Collectors.toSet()).size();
      int numEdges = mstEdges.size();
      double totalWeight = mstEdges.stream()
                                   .mapToDouble(edge -> edge.getLabel().doubleValue())
                                   .sum();
      System.err.println("Number of nodes: " + numNodes);
      System.err.println("Number of edges: " + numEdges);
      System.err.println("Total weight: " + totalWeight / 1000 + " Km");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

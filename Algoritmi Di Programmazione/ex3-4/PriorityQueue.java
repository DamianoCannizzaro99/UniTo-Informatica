

import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;


public class PriorityQueue<E> implements AbstractQueue<E> {
    private Comparator<? super E> comparator = null;
    private ArrayList<E> heap;
    private HashMap<E, Integer> map; // aggiunta di una mappa hash per memorizzare la posizione di ogni elemento

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new ArrayList<>();
        this.map = new HashMap<>(); // inizializzazione della mappa hash
        this.comparator = comparator;
    }

    @Override
    public boolean empty() {
        return heap.isEmpty();
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int child_l(int i) {
        return 2 * i + 1;
    }

    private int child_r(int i) {
        return 2 * i + 2;
    }

    @Override
    public boolean push(E e) {
        if (!contains(e)) {
            heap.add(e);
            int i = heap.size() - 1;
            map.put(e, i); // aggiornamento della mappa hash
            heapifyUp(i);
            return true;
        }
        return false;
    }

    @Override
    public E top() {
        if (empty()) return null;
        return heap.get(0);
    }

    @Override
    public void pop() {
        if (!empty()) {
            E lastElement = heap.get(heap.size() - 1);
            heap.set(0, lastElement);
            heap.remove(heap.size() - 1);
            map.remove(lastElement); // rimozione dell'elemento dalla mappa hash
            if (!heap.isEmpty()) {
                map.put(heap.get(0), 0); // aggiornamento della mappa hash
                heapifyDown(0);
            }
        }
    }

    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        map.put(heap.get(i), i); // aggiornamento della mappa hash
        map.put(heap.get(j), j); // aggiornamento della mappa hash
    }

    @Override
    public boolean contains(E e) {
        return map.containsKey(e); // utilizzo della mappa hash per il controllo dell'esistenza
    }

    @Override
    public boolean remove(E e) {
        if (map.containsKey(e)) { // utilizzo della mappa hash per il controllo dell'esistenza
            int i = map.get(e);
            E lastElement = heap.get(heap.size() - 1);
            heap.remove(heap.size() - 1);
            map.remove(e); // rimozione dell'elemento dalla mappa hash
            if (i < heap.size()) {
                heap.set(i, lastElement);
                map.put(lastElement, i); // aggiornamento della mappa hash
                heapifyDown(i);
                heapifyUp(i); 
            }
            return true;
        }
        return false;
    }

    private void heapifyDown(int i) {
        int left = child_l(i);
        int right = child_r(i);
        int smallest = i;

        if (left < heap.size() && comparator.compare(heap.get(left), heap.get(smallest)) < 0) smallest = left;

        if (right < heap.size() && comparator.compare(heap.get(right), heap.get(smallest)) < 0) smallest = right;

        if (smallest != i) {
            swap(i, smallest);
            heapifyDown(smallest);
        }
    }

    private void heapifyUp(int i) {
        while (i > 0 && comparator.compare(heap.get(i), heap.get(parent(i))) < 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < heap.size(); i++) {
            sb.append(heap.get(i));
            if (i < heap.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
};

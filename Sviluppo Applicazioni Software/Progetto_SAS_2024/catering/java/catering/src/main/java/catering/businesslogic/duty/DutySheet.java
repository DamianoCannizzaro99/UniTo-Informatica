package catering.businesslogic.duty;

import catering.businesslogic.event.EventInfo;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.BatchUpdateHandler;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DutySheet {
    protected int id;
    protected EventInfo event;
    protected User owner;
    protected boolean active;
    protected static Map<Integer, DutySheet> loadedDS = FXCollections.observableHashMap();
    protected ObservableList<Task> tasks = FXCollections.observableArrayList();
    protected ObservableList<Task> overflowTasks = FXCollections.observableArrayList();




    public DutySheet(EventInfo ev, boolean active) {
        this.event = ev;
        this.active = active;
        this.owner = ev.getOrganizer();
    }




    public int getEventId(){
        return this.event.getId();
    }

    public int getOwnerId(){
        return this.owner.getId();
    }

    public Task addTask(String name, String description, int qty) {
        Task task = new Task(name, description, qty);
        tasks.add(task.getPosition(),task);
        return task;
    }
    public void moveTask(Task taskToMove, Task task2) {
        if (tasks == null || tasks.isEmpty()) {
            throw new IllegalStateException("La lista delle task è vuota.");
        }

        int pos1 = tasks.indexOf(taskToMove);
        int pos2 = tasks.indexOf(task2);


        if (pos1 == -1 || pos2 == -1) {
            throw new IllegalArgumentException("Uno o entrambi i task non esistono nella lista.");
        }

        tasks.remove(taskToMove);
        tasks.remove(task2);

        if (pos1 < pos2) {
            tasks.add(pos1, task2);
            tasks.add(pos2, taskToMove);
        } else {
            tasks.add(pos2, taskToMove);
            tasks.add(pos1, task2);
        }
    }


    public void assignTask(Task task, Shift[] shifts, User[] staff){
        task.assignTask(shifts,staff);
    }

    public void removeAssignment(Task task, User[] staff) {
        task.removeAssignment(staff);
    }


    //------------------------persistence methods---------------------------------
    public static void saveNewDutySheet(DutySheet ds) {
        String newDS = "INSERT INTO catering.DutySheets (event_id, owner_id, active) VALUES (?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(newDS, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, ds.getEventId());
                ps.setInt(2, ds.getOwnerId());
                ps.setBoolean(3, ds.active);
            }
            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0){
                    ds.id = rs.getInt(1);
                }
            }
        });
        if (result[0]>0) loadedDS.put(ds.id, ds);
    }

    public static void deleteDutySheetFromDB(DutySheet ds) {
        String strDelDS = "DELETE FROM catering.DutySheets WHERE event_id = ? AND owner_id = ? AND active = ?;";
        int[] result = PersistenceManager.executeBatchUpdate(strDelDS, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,ds.getEventId());
                ps.setInt(2,ds.getOwnerId());
                ps.setBoolean(3,ds.active);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // Non è necessario gestire gli ID generati in un'operazione di DELETE
            }
        });
        if (result[0]>0) loadedDS.remove(ds.id,ds);
    }


    public Task setOverflow( Task task, int qty, int guests) {
        Task ofTask = task.setOverflow(qty,guests);
        overflowTasks.add(ofTask);
        return ofTask;
    }

    public static DutySheet openDutySheetFromDB(DutySheet ds) {
        String strDS = "SELECT * FROM catering.DutySheets WHERE event_id=? AND owner_id=? AND active=?";
        int[] result = PersistenceManager.executeBatchUpdate(strDS,1, new BatchUpdateHandler() {

            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, ds.getEventId());
                ps.setInt(2, ds.getOwnerId());
                ps.setBoolean(3, ds.active);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    int id = rs.getInt("id");
                    int eventId = rs.getInt("event_id");
                    int ownerId = rs.getInt("owner_id");
                    Boolean active = rs.getBoolean("active");

                    System.out.printf("%d | %d | %d | %b\n", id, eventId, ownerId, active);
                }
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

            }
        });
        return ds;
    }

    public String toString(){
        return event.getName() + " | " + owner.getUserName() + " | " + active;
    }

    public void testString(){
        System.out.printf("%-10s | %-25s | %-10s | %s\n","id","Evento","Proprietario","attivo");
        for(Map.Entry<Integer,DutySheet> entry : loadedDS.entrySet()){
            int chiave = entry.getKey();
            DutySheet val = entry.getValue();
            System.out.printf("\n%-10s | %s",chiave,val);
        }
        if (tasks.isEmpty()) System.out.println("\nTasks array is empty, create new task and assign to a Duty Sheet. \n");
        for (Task task : tasks){
            System.out.println(task);
        }
    }

}

package catering.businesslogic.duty;

import catering.businesslogic.shift.Shift;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.BatchUpdateHandler;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Arrays;



public class Task {
    private int id;
    private String name;
    private String description;
    private int qty;
    private boolean overflow;
    private int position;
    private ObservableList<Shift> designedShifts = FXCollections.observableArrayList();
    private ObservableList<User> designedStaff = FXCollections.observableArrayList();

    public Task(String name, String description, int qty) {
        this.name = name;
        this.description = description;
        this.qty = qty;
        this.overflow = false;
    }

    public int getPosition() { return position; }
    public int getQty() { return qty; }

    public Task updateTask(String upName, String upDesc, int upQty) {
        if (!upName.equals(this.name) && upName != null) this.name = upName;
        if(!upDesc.equals(this.description) && upDesc!= null) this.description = upDesc;
        if(upQty!=this.qty) this.qty = upQty;
        return this;
    }
    public void assignTask(Shift[] shift, User[] staff) {
        designedShifts.addAll(Arrays.asList(shift));
        designedStaff.addAll(Arrays.asList(staff));
    }

    public void removeAssignment(User[] staff) {
        designedStaff.removeAll(Arrays.asList(staff));
    }

    public Task setOverflow( int qty, int guests) {
        this.overflow = true;
        this.qty = qty -guests;
        return this;
    }

    @Override
    public String toString(){
        return "Task Details: " +
                "\nID= " +id +
                "\nPosition= " +position+
                "\nName= " + name +
                "\nDescription= " + description+
                "\nQuantity= " + qty+
                "\nOverflow= " +overflow;

    }

//--------------------------PERSISTENCE METHODS-------------------------------
    public static void saveNewTask(Task task) {
        String newT = "INSERT INTO catering.Tasks (position, name, description, qty, overflow) VALUES (?,?,?,?,?);";
        int[] result = PersistenceManager.executeBatchUpdate(newT, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,task.position);
                ps.setString(2, task.name);
                ps.setString(3, task.description);
                ps.setInt(4, task.qty);
                ps.setBoolean(5, task.overflow);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0){
                    task.id = rs.getInt(1);
                    task.position = task.id;
                    setPosition(task,task.id);
                }
            }
        });
    }
    private static void setPosition(Task task, int pos){
        String strPos = "UPDATE catering.Tasks SET position = ? WHERE name = ? AND description = ? AND qty = ?; ";
        int[]result = PersistenceManager.executeBatchUpdate(strPos, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,pos);
                ps.setString(2, task.name);
                ps.setString(3, task.description);
                ps.setInt(4,task.qty);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

            }
        });
    }

    public static void updateTaskPosition(Task task,int position) {
        String update = "UPDATE catering.Tasks SET position = ? WHERE name = ? AND position = ?;" ;
        int[] result = PersistenceManager.executeBatchUpdate(update, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,position);
                ps.setString(2, task.name);
                ps.setInt(3,task.position);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

            }
        });
        task.position = position;
    }
    public static void deleteTask(Task task) {
    String strDel = "DELETE FROM catering.Tasks Where id = ?;";
    int[] result = PersistenceManager.executeBatchUpdate(strDel, 1, new BatchUpdateHandler() {
        @Override
        public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
            ps.setInt(1,task.id);
        }

        @Override
        public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
            //Non è necessario gestire ID generati in un operazione di DELETE
        }
    });
    }


    public static int getTaskId(Task task) {
        String strID = "SELECT id FROM catering.tasks WHERE name = ? AND description = ? AND qty = ? AND overflow = ? AND position = ?";
        int taskId = -1; // Valore di default in caso di mancato recupero

        try (Connection conn = DriverManager.getConnection(PersistenceManager.getUrl(), PersistenceManager.getUsername(), PersistenceManager.getPassword());
             PreparedStatement ps = conn.prepareStatement(strID)) {

            // Imposta i parametri della query
            ps.setString(1, task.name);
            ps.setString(2, task.description);
            ps.setInt(3, task.qty);
            ps.setBoolean(4, task.overflow);
            ps.setInt(5, task.getPosition());

            // Esegui la query
            try (ResultSet rs = ps.executeQuery()) {
                // Recupera l'ID dal ResultSet
                if (rs.next()) {
                    taskId = rs.getInt("id");
                } else {
                    System.out.println("Nessuna task trovata con i criteri forniti.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskId;
    }


    public static void saveTaskAssigned(Task task, User[] staff) {
        for (User user : staff) {
            String newQ = "INSERT INTO catering.TaskAssignment (task_id, user_id) VALUES (?,?);";
            int task_id = getTaskId(task);
            int user_id = user.getId();
            int[] result = PersistenceManager.executeBatchUpdate(newQ, 1, new BatchUpdateHandler() {

                @Override
                public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                    ps.setInt(1, task_id);
                    ps.setInt(2, user_id);

                }

                @Override
                public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

                }
            });
        }
    }
    public static void removeAssignmentFromDB(Task task, User[] staff) {
        for (User user : staff) {
            String strDel = "DELETE FROM catering.taskassignment WHERE task_id = ? AND user_id = ?;";
            int[] result = PersistenceManager.executeBatchUpdate(strDel, 1, new BatchUpdateHandler() {
                @Override
                public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                    ps.setInt(1, task.id);
                    ps.setInt(2, user.getId());
                }

                @Override
                public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                    //Non è necessario gestire ID generati in un operazione di DELETE
                }
            });
        }
    }
    public static void updateOverflowTask(Task task) {
        String update = "UPDATE catering.Tasks SET overflow = ? WHERE name = ? AND position = ?;";
        int[] result = PersistenceManager.executeBatchUpdate(update, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setBoolean(1, true);
                ps.setString(2, task.name);
                ps.setInt(3, task.position);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

            }
        });

    }


}

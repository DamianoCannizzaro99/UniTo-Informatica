package catering.businesslogic.shift;

import catering.businesslogic.event.EventInfo;
import catering.businesslogic.user.User;
import catering.businesslogic.persistence.BatchUpdateHandler;
import catering.businesslogic.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.sql.*;
import java.util.Date;
import java.util.Map;

public abstract class ShiftTable {
    private static Map<Integer, ShiftTable> loadedTables = FXCollections.observableHashMap();
    protected int id;
    protected EventInfo event;
    protected String type;
    protected boolean order;
    User owner;
    protected ObservableList<Shift> Shifts = FXCollections.observableArrayList();
    protected final ObservableMap<Date, ShiftTable> recurringTable = FXCollections.observableHashMap();


    public boolean isEmpty() {
        return Shifts.isEmpty();
    }
    public ObservableList<Shift> getShifts() {
        return Shifts;
    }
    public ShiftTable checkTable(ShiftTable st) {return openShiftTable(st);}
    
    
    public Shift addShift(ShiftTable st, Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName) {
        Shift newS = new Shift(st.event.getId(), st.type,startTime, endTime, jobDate, deadline, group, groupName);
        Shifts.add(newS);
        return newS;

    }

    public Shift updateShift(Shift s, Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName) {
        return s.UpdateShift(s, startTime, endTime, jobDate, deadline, group, groupName);
    }
    public void deleteShift(Shift s) {
        Shifts.remove(s);
    }




    public void addRecurringTable(ShiftTable st, Date date) {
            recurringTable.put(date,st);
    }
    public void removeInstanceRecurringTable(Date date) {
            recurringTable.remove(date);
     }

    public void updateInstanceInRecurring(Date[] dateToUpdate, Date[] dateUpdate) {
    for (int i =0; i<dateToUpdate.length; i++) {
        recurringTable.remove(dateToUpdate[i]);
        recurringTable.put(dateUpdate[i],this);
    }

    }

    public void printRecurringTable() {
        for ( var entry : recurringTable.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }




    //PERSISTENCE AND FORMAT METHODS
    public static void saveNewShiftTable(ShiftTable st) {
        String stInsert = "INSERT INTO catering.ShiftTables (event, ev_type, owner_id, `order`) VALUES (?, ?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(stInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setString(1, PersistenceManager.escapeString(st.event.getName()));
                ps.setString(2, PersistenceManager.escapeString(st.type));
                ps.setInt(3, st.owner.getId());
                ps.setBoolean(4, st.order);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    st.id = rs.getInt(1);
                }
            }
        });
        if (result[0] > 0) {
            loadedTables.put(st.id, st);
        }

    }
    public static ShiftTable openShiftTable(ShiftTable st) {
        String strST = "SELECT * FROM catering.ShiftTables WHERE event = ? AND ev_type = ? AND owner_id = ? AND `order` = ?;";
        try (Connection conn = DriverManager.getConnection(PersistenceManager.getUrl(), PersistenceManager.getUsername(), PersistenceManager.getPassword());
             PreparedStatement ps = conn.prepareStatement(strST)) {

            ps.setString(1, PersistenceManager.escapeString(st.event.getName()));
            ps.setString(2, PersistenceManager.escapeString(st.type));
            ps.setInt(3, st.owner.getId());
            ps.setBoolean(4, st.order);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String event = rs.getString("event");
                    String evType = rs.getString("ev_type");
                    int ownerId = rs.getInt("owner_id");
                    boolean order = rs.getBoolean("order");

                    System.out.printf("%-10s | %-20s | %-10s | %-10s | %s\n","id","event","ev_type","owner_id","order");
                    System.out.printf("%-10d | %-20s | %-10s | %-10d | %b\n", id, event, evType, ownerId, order);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return st;
    }

    public static void addRecurringTableToDB(ShiftTable st, Date date) {
        String strIn = "INSERT INTO catering.RecurringTable (ShiftTable_id, recurrence_date)VALUES(?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(strIn, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,st.id);
                ps.setDate(2, (java.sql.Date) date);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {

            }
        });

    }

    public static void removeRecurringTable(ShiftTable st, Date date) {
        String strRemove  = "DELETE FROM catering.RecurringTable WHERE ShiftTable_id = ? AND recurrence_date = ?";
        int[] result = PersistenceManager.executeBatchUpdate(strRemove, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,st.id);
                ps.setDate(2, (java.sql.Date) date);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // Non è necessario gestire gli ID generati in un'operazione di DELETE
            }
        });
        if (result != null && result.length > 0 && result[0] > 0) {
            // Se almeno una riga è stata eliminata
            System.out.println("ShiftTable con ID " + st.id + " eliminata con successo.");
        } else {
            // Nessuna riga eliminata
            System.out.println("Nessuna ShiftTable trovata con l'ID fornito: " + st.id);
        }
    }



    public String toString() {
        return event.getName() + " | " + type + " | " + owner.getUserName() + " | " + order;
    }

    public void testString() {
        String t = "";
        if(type.equals("c")){
            System.out.println("Tabella dei turni di Cucina:");
            t = type;
        }else {
            System.out.println("Tabella dei turni di Servizio:");
            t = type;
        }
        System.out.printf("%-10s | %-20s | %-1s | %-10s | %s\n  ", "id", "Evento", "tipo", "proprietario", "ordine");
        for (Map.Entry<Integer, ShiftTable> entry : loadedTables.entrySet()) {
            int chiave = entry.getKey();
            ShiftTable val = entry.getValue();
            if (val.type.equals(t)) {
            System.out.printf("%-10s | %s\n", chiave, val);
            }
        }
        for (Shift shift : this.Shifts) {
            System.out.println(shift.toString());
        }
    }




}


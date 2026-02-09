package catering.businesslogic.shift;

import catering.businesslogic.persistence.BatchUpdateHandler;
import catering.businesslogic.persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Shift {
    private final String type;
    private final int event_id;
    private Time startTime;
    private Time endTime;
    private Date jobDate;
    private Date deadline;
    private boolean group = false;
    private String groupName = "";
    private int id ;

    //costruttore
    public Shift(int event_id,String type, Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName) {
        this.event_id = event_id;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.jobDate = jobDate;
        this.deadline = deadline;
        this.group = group;
        if(group)this.groupName = groupName;
    }


    public Date getJobDate() {return jobDate;}


    public Shift UpdateShift(Shift s, Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName) {
        if(s.startTime != startTime && startTime !=null)s.startTime = startTime;
        if(s.endTime != endTime && endTime !=null)s.endTime = endTime;
        if(s.jobDate != jobDate && jobDate!=null)s.jobDate = jobDate;
        if(s.deadline != deadline && deadline.before(jobDate) && jobDate !=null)s.deadline = deadline;
        if(group) s.group = true;
        if(!s.groupName.equals(groupName) && group && groupName !=null) {
            s.groupName = groupName;
        }else if (!group) {
            s.group = false;
            s.groupName = null;
        }
        return s;
    }

    public String getType() {return type;}

    @Override
    public String toString() {
        return "Shift Details: " +
                "ID=" + id +
                "\n Event ID=" + event_id +
                "\n Type=" + type +
                "\n Start Time=" + (startTime != null ? startTime.toString() : "N/A") +
                "\n End Time=" + (endTime != null ? endTime.toString() : "N/A") +
                "\n Job Date=" + (jobDate != null ? jobDate.toString() : "N/A") +
                "\n Deadline=" + (deadline != null ? deadline.toString() : "N/A") +
                "\n Group=" + (group ? true : "N/A") +
                "\n Group Name=" +  (groupName != null ? "\'" + groupName + "\'" : "N/A") ;
    }


    //-----------------------PERSISTENCE METHODS------------------------------
        public static void saveNewShift(Shift s) {
        String newS = "INSERT INTO catering.Shifts (event_id, startTime, endTime, jobDate, deadLine, `group`, groupName) VALUES (?, ?, ?, ?, ?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(newS, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1,s.event_id );
                ps.setTime(2, s.startTime);
                ps.setTime(3, s.endTime);
                ps.setDate(4, (java.sql.Date) s.jobDate);
                ps.setDate(5, (java.sql.Date) s.deadline);
                ps.setBoolean(6, s.group);
                ps.setString(7,PersistenceManager.escapeString(s.groupName));
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0){
                    s.id = rs.getInt(1);
                }
            }
        });

    }

    public static void removeShift(Shift s) {
        if (s == null || s.id <= 0) {
            throw new IllegalArgumentException("Shift non valido o privo di ID.");
        }

        String deleteQuery = "DELETE FROM catering.Shifts WHERE id = ?;";
        int[] result = PersistenceManager.executeBatchUpdate(deleteQuery, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, s.id); // Usa l'ID del turno fornito dall'oggetto Shift
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // Non è necessario gestire gli ID generati in un'operazione di DELETE
            }
        });

        // Opzionale: gestione del risultato dell'operazione
        if (result[0] == 0) {
            System.out.println("Nessun turno trovato con l'ID fornito: " + s.id);
        } else {
            System.out.println("Turno con ID " + s.id + " eliminato con successo.");
        }
    }


}

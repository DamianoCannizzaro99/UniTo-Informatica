package catering.businesslogic.event;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import catering.businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import catering.businesslogic.persistence.PersistenceManager;
import catering.businesslogic.persistence.ResultHandler;

public class EventInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private int participants;
    private User organizer;
    private User chef;

    private ObservableList<User> assignedUsers = FXCollections.observableArrayList();
    private ObservableList<ServiceInfo> services = FXCollections.observableArrayList();

    public EventInfo(String name) {
        this.name = name;
        id = 0;
    }

    public EventInfo(String name, Date dateStart, Date dateEnd, int participants, User organizer, User chef) {
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.participants = participants;
        this.organizer = organizer;
        this.chef = chef;

    }

    public ObservableList<ServiceInfo> getServices() {
        return FXCollections.unmodifiableObservableList(this.services);
    }
    public int getId() {            return id;}
    public String getName(){        return this.name;}
    public Date getDateEnd() {      return dateEnd;}
    public Date getDateStart() {    return dateStart;}
    public int getParticipants() {  return participants;}
    public User getOrganizer() {    return this.organizer;}
    public User getChef() {         return this.chef;}

    public void AssignUser(User user) {
        assignedUsers.add(user);
    }

    public boolean isAssigned(User user) {
        return assignedUsers.contains(user);
    }




    public String toString() {
        return name + ": " + dateStart + "-" + dateEnd + ", " + participants + " pp. (" + organizer.getUserName() + ")";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static EventInfo EventInfoFromName(String name) {
        String query = "SELECT * FROM events WHERE name = '" + name + "'";
        final EventInfo[] ev = {new EventInfo(name)};
        System.out.println("test nome event: " + name);
        System.out.println("test query: " +query);

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo e = new EventInfo(n);
                e.id = rs.getInt("id");
                e.dateStart = rs.getDate("date_start");
                e.dateEnd = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.organizer = User.loadUserById(org);
                ev[0] = e;
            }
        });
        return ev[0];
    }


    public static ObservableList<EventInfo> loadAllEventInfo() {
        ObservableList<EventInfo> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String n = rs.getString("name");
                EventInfo e = new EventInfo(n);
                e.id = rs.getInt("id");
                e.dateStart = rs.getDate("date_start");
                e.dateEnd = rs.getDate("date_end");
                e.participants = rs.getInt("expected_participants");
                int org = rs.getInt("organizer_id");
                e.organizer = User.loadUserById(org);
                all.add(e);
            }
        });

        for (EventInfo e : all) {
            e.services = ServiceInfo.loadServiceInfoForEvent(e.id);
        }
        return all;
    }


}

package catering.businesslogic.shift;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.EventInfo;
import catering.businesslogic.user.User;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class ShiftManager {
    private ShiftTable currCookSTable;
    private ShiftTable currServiceSTable;
    private ArrayList<ShiftEventReceiver> eventReceivers;

    //Costruttore ShiftManager
    public ShiftManager() { eventReceivers = new ArrayList<>();}



    public ShiftTable createCookShiftTable(String type, EventInfo ev) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isManager() || !ev.isAssigned(user)) throw new UseCaseLogicException("UseCaseLogic Exception: user is not manager or not assigned ");

        if (type.equals("c")) {
            ShiftTable cst = new CookShiftTable(type, ev, false);
            setCurrentCShiftTable(cst);
            notifyShiftTableCreated(cst);

            return cst;
        }
        else if (type.equals("s")) {
            System.out.println("Table type is " + type + ", redirecting to correct method");
            return createServiceShiftTable(type, ev);
        } else throw new UseCaseLogicException("UseCaseLogic Exception: table type is not correct");
    }

    public ShiftTable createServiceShiftTable(String type, EventInfo ev) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isManager() || !ev.isAssigned(user)) {throw new UseCaseLogicException("UseCaseLogic Exception: user is not manager or not assigned ");}
        if (type.equals("s")) {
            ShiftTable sst = new ServiceShiftTable(type, ev, false);
            setCurrentSShiftTable(sst);
            notifyShiftTableCreated(sst);

            return sst;
        } else if (type.equals("c")) {
            System.out.println("Table type is " + type + ", redirecting to correct method");
            return createCookShiftTable(type, ev);
        } else throw new UseCaseLogicException("UseCaseLogic Exception: table type is not correct");
    }

    public void setCurrentCShiftTable(ShiftTable currentST) {
        this.currCookSTable = currentST;
    }
    public void setCurrentSShiftTable(ShiftTable currentST) {
        this.currServiceSTable = currentST;
    }

    public ShiftTable checkTable(ShiftTable currentST) throws UseCaseLogicException {
       if(currentST == null) throw new UseCaseLogicException("UseCaseLogic Exception: Shift Table is null");
       notifyTableOpened(currentST);
       return currentST;
    }



    public ShiftTable getCurrentTableFromShift(Shift s){
        if (s.getType().equals("c")) {return currCookSTable;}
        else return currServiceSTable;
    }

 /** Metodo per aggiungere un turno alla tabella corrispettiva.
  * Si effettua un controllo sul parametro type e viene aggiornata
  * la tabella di riferimento
  * */
public Shift addShiftToTable(ShiftTable st , Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName) throws UseCaseLogicException {
    Shift newShift;
    if (st.type.equals("c")) {
            newShift = currCookSTable.addShift(st, startTime, endTime, jobDate, deadline, group, groupName );
    }else if(st.type.equals("s")) {
            newShift = currServiceSTable.addShift(st, startTime, endTime, jobDate, deadline, group, groupName );
    } else throw new UseCaseLogicException("UseCaseLogic Exception: table type is not correct");

        notifyShiftCreated(newShift);
        return newShift;
}
    public void deleteShift(Shift s,ShiftTable st) {
    if(st.type.equals("c")) {
        currCookSTable.deleteShift(s);
        notiftyShiftRemoved(s);
    }
    else if(st.type.equals("s")) {
        currServiceSTable.deleteShift(s);
        notiftyShiftRemoved(s);
    }
    }




    public Shift updateShift(Shift s, ShiftTable st,  Time startTime, Time endTime, Date jobDate, Date deadline, boolean group, String groupName){
        return st.updateShift(s,startTime,endTime,jobDate,deadline,group,groupName);
    }

    public void addRecurringTable(ShiftTable st, Date[] dates) throws UseCaseLogicException {
        if (dates == null || dates.length == 0) throw new UseCaseLogicException("UseCaseLogic Exception: dates not set properly");
        else {
            for (Date date : dates){
                st.addRecurringTable(st, date);
            notifyRecurringTableAdded(st,date);
            }
        }
    }



    public void updateInstanceInRecurring(ShiftTable st, Date[] dateToUpdate, Date[] dateUpdate) {
    st.updateInstanceInRecurring(dateToUpdate, dateUpdate);
    }

    public void printRecurringTable(ShiftTable st) {
        st.printRecurringTable();
    }

    public void removeInstanceRecurringTable(ShiftTable st, Date[] dates) throws UseCaseLogicException {
        if (dates == null || dates.length == 0) throw new UseCaseLogicException("UseCaseLogic Exception: dates not set properly");
        for (Date date: dates) {
            notifyRecurringTableRemoved(st,date);
            st.removeInstanceRecurringTable(date);

        }
    }


    //--------------------Notify methods-----------------------------

    private void notifyShiftCreated(Shift newShift) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateShiftCreated(newShift);
        }
    }
    private void notiftyShiftRemoved(Shift s) {
    for (ShiftEventReceiver er : eventReceivers) {
        er.updateShiftRemoved(s);
    }
    }

    private void notifyShiftTableCreated(ShiftTable st) {
        for (ShiftEventReceiver er: eventReceivers) {
            er.updateShiftTableCreated(st);
        }
    }
    private void notifyTableOpened(ShiftTable st) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateTableOpened(st);
        }
    }

    private void notifyRecurringTableAdded(ShiftTable st, Date date) {
    for (ShiftEventReceiver er: eventReceivers){
        er.updateRecurringTableAdded(st,date);
    }
    }

    private void notifyRecurringTableRemoved(ShiftTable st, Date date) {
        for (ShiftEventReceiver er : eventReceivers){
            er.updateRecurringTableRemoved(st,date);
        }
    }
    public void addEventReceiver(ShiftEventReceiver rec) { this.eventReceivers.add(rec); }
    public void removeEventReceiver(ShiftEventReceiver rec) { this.eventReceivers.remove(rec); }



}

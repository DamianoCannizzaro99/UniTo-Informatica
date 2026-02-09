package catering.businesslogic.persistence;

import catering.businesslogic.shift.ShiftEventReceiver;
import catering.businesslogic.shift.ShiftTable;
import catering.businesslogic.shift.Shift;

import java.util.Date;

public class ShiftPersistence implements ShiftEventReceiver {
    @Override
    public void updateShiftTableCreated(ShiftTable st) {ShiftTable.saveNewShiftTable(st);}

    @Override
    public void updateShiftCreated(Shift newShift) {
        Shift.saveNewShift(newShift);
    }
    @Override
    public void updateRecurringTableAdded(ShiftTable st, Date date){
        ShiftTable.addRecurringTableToDB(st,date);
    }

    @Override
    public void updateRecurringTableRemoved(ShiftTable st, Date date) {
     ShiftTable.removeRecurringTable(st,date);
    }

    @Override
    public void updateShiftRemoved(Shift s) {
        Shift.removeShift(s);
    }

    @Override
    public void updateTableOpened(ShiftTable st) {
        ShiftTable.openShiftTable(st);
    }


}

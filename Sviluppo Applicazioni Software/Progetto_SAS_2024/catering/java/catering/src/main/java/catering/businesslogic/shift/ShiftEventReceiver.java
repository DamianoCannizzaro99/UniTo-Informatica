package catering.businesslogic.shift;

import java.util.Date;

public interface ShiftEventReceiver {

    public void updateShiftTableCreated(ShiftTable st);

    public void updateShiftCreated(Shift newShift);

    public void updateShiftRemoved(Shift s);

    void updateTableOpened(ShiftTable st);

    void updateRecurringTableAdded(ShiftTable st, Date date);

    void updateRecurringTableRemoved(ShiftTable st, Date date);
}

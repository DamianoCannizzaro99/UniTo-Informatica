package catering.businesslogic.shift;

import catering.businesslogic.event.EventInfo;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalTime;
import java.util.Date;

public class ServiceShiftTable extends ShiftTable{

    public ServiceShiftTable(String typ, EventInfo ev, boolean ord) {
            event = ev;
            order = ord;
            type = "s";
            this.owner = ev.getOrganizer();
    }
}

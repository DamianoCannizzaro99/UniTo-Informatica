package catering.businesslogic.shift;

import catering.businesslogic.event.EventInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.time.LocalTime;
import java.util.Date;

public class CookShiftTable extends ShiftTable{

    //Costruttore per tabella turni di cucina
    public CookShiftTable(String type, EventInfo ev, boolean ord) {

            event = ev;
            order = ord;
            this.type = "c";
            this.owner = ev.getOrganizer();
        }
    }


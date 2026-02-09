module catering {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens catering.ui to javafx.fxml;
    opens catering.ui.menu to javafx.fxml;
    opens catering.ui.general to javafx.fxml;

    exports catering.businesslogic.TestingShifts;
    opens catering.businesslogic.TestingShifts to javafx.fxml;

    exports catering.businesslogic;
    opens catering.businesslogic to javafx.fxml;
}

module ucr.lab.laboratory10 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ucr.lab.laboratory10 to javafx.fxml;
    exports ucr.lab.laboratory10;
}
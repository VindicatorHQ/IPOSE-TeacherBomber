module com.ward_cunningham_38.teacherbomber {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ward_cunningham_38.teacherbomber to javafx.fxml;
    exports com.ward_cunningham_38.teacherbomber;
}
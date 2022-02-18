module sample {
    requires javafx.controls;
    requires javafx.fxml;


    opens sample to javafx.fxml;
    opens Dtos to javafx.base, javafx.fxml;
    exports sample;
    exports Dtos;
}
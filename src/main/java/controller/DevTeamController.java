package controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DevTeamController {
    @FXML
    private TableView<ObservableList<String>> tableView;
    @FXML
    private TableColumn<ObservableList<String>, String> nameColumn;
    @FXML
    private TableColumn<ObservableList<String>, String> idColumn;
    @FXML
    private TableColumn<ObservableList<String>, String> emailColumn;
    @FXML
    private Button backButton;

    public void initialize() {
        nameColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(0)));
        idColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(1)));
        emailColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(2)));

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        data.add(FXCollections.observableArrayList("Iva Silva", "1241176", "1241176@isep.ipp.pt"));
        data.add(FXCollections.observableArrayList("João Ferreira", "1241166", "1241166@isep.ipp.pt"));
        data.add(FXCollections.observableArrayList("José Cardoso", "1241323", "1241323@isep.ipp.pt"));
        data.add(FXCollections.observableArrayList("Rafael Capão", "1241285", "1241285@isep.ipp.pt"));

        tableView.setItems(data);
    }

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
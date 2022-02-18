package sample;


import Controller.Controller;
import Dtos.HeapDto;
import Dtos.SymDto;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IList;
import Model.adt.IMyHeap;
import Model.adt.IStack;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class
ToyLanguageController {
    @FXML
    public Button runBtn;
    @FXML
    public ListView<String> prgStatesList;
    @FXML
    public ListView<String> outList;
    @FXML
    public ListView<String> fileList;
    @FXML
    public ListView<String> exeStackList;
    @FXML
    public TableView<SymDto> symTable;
    @FXML
    public TableView<HeapDto> heapTable;
    @FXML
    public TextField progStatesNumField;
    @FXML
    public ListView<String> programsList;
    @FXML
    public TableColumn<SymDto, String> varNameColumn;
    @FXML
    public TableColumn<SymDto, IValue> symValueColumn;
    @FXML
    public TableColumn<HeapDto, Integer> addressColumn;
    @FXML
    public TableColumn<HeapDto, IValue> heapValueColumn;
    @FXML
    private ListProperty<String> programsText = new SimpleListProperty<>();
    @FXML
    private ListProperty<HeapDto> heapDtos = new SimpleListProperty<>();
    @FXML
    private ListProperty<SymDto> symDtos = new SimpleListProperty<>();
    @FXML
    private ListProperty<String> outLst = new SimpleListProperty<>();
    @FXML
    private ListProperty<String> files = new SimpleListProperty<>();
    @FXML
    private ListProperty<String> exeStackElements = new SimpleListProperty<>();
    @FXML
    private ListProperty<String> prgStatesIdentifiers = new SimpleListProperty<>();
    private ArrayList<Controller> ctrs;


    public ToyLanguageController() {

    }

    @FXML
    private void initialize() {
        ctrs = new ArrayList<>();
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        heapValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        symValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        varNameColumn.setCellValueFactory(new PropertyValueFactory<>("varName"));
        programsList.setOnMouseClicked(event -> {
            System.out.println("clicked on " + programsList.getSelectionModel().getSelectedIndex());
            initAllWidgets(programsList.getSelectionModel().getSelectedIndex());
        });
    }

    private void initAllWidgets(int selectedIndex) {
        Controller ctr = this.ctrs.get(selectedIndex);
        PrgState prgState = ctr.getProgram();
        initHeapTbl(prgState);
        initSymTbl(prgState);
        initOutList(prgState);
        initFileLst(prgState);
        initExeStackLst(prgState);
        initPrgStatesLst(ctr);
        initNumProgStates(ctr);
    }

    private void initNumProgStates(Controller ctr) {
        int numPrgs = ctr.getIdentifiers().size();
        progStatesNumField.setText(numPrgs + " ");
    }

    private void initPrgStatesLst(Controller ctr) {
        List<String> identifiers = ctr.getIdentifiers();
        prgStatesList.itemsProperty().bind(prgStatesIdentifiers);
        prgStatesIdentifiers.set(FXCollections.observableArrayList(identifiers));
    }

    private void initExeStackLst(PrgState prgState) {
        List<String> exeStack = new ArrayList<>();

        IStack<IStmt> exStack = prgState.getStack();
        for (IStmt stmt : exStack.getAll()) {
            exeStack.add(stmt.toString());
        }
        exeStackList.setItems(FXCollections.observableList(exeStack));
        exeStackList.refresh();

    }

    private void initFileLst(PrgState prgState) {
        List<String> filesLst = new ArrayList<>();

        IDict<StringValue, BufferedReader> filesState = prgState.getFileTable();

        for (Map.Entry<StringValue, BufferedReader> entry : filesState.getContent()) {
            filesLst.add(entry.getKey().getValue());
        }

        this.fileList.itemsProperty().bind(files);
        files.set(FXCollections.observableArrayList(filesLst));

    }

    private void initOutList(PrgState prgState) {
        List<String> outTxt = new ArrayList<>();

        IList<IValue> out = prgState.getOutput();
        for (int i = 0; i < out.size(); i++) {
            outTxt.add(out.getFromIndex(i).toString());
        }
        outList.itemsProperty().bind(outLst);
        outLst.set(FXCollections.observableArrayList(outTxt));
    }

    private void initSymTbl(PrgState prgState) {
        IDict<String, IValue> symTbl = prgState.getSymTable();
        List<SymDto> symDtoList = new ArrayList<>();
        for (Map.Entry<String, IValue> entry : symTbl.getContent()) {

            symDtoList.add(new SymDto(entry.getKey(), entry.getValue()));
        }
        symTable.itemsProperty().bind(symDtos);
        symDtos.set(FXCollections.observableArrayList(symDtoList));
    }

    private void initHeapTbl(PrgState programState) {
        IMyHeap<Integer, IValue> heap = programState.getHeapTable();
        List<HeapDto> heapDtoList = new ArrayList<>();
        for (Map.Entry<Integer, IValue> entry : heap.getContent()) {

            heapDtoList.add(new HeapDto(entry.getKey(), entry.getValue()));
        }
        heapTable.itemsProperty().bind(heapDtos);
        heapDtos.set(FXCollections.observableArrayList(heapDtoList));
    }

    private void initList() {
        List<String> prgsTxt = getProgramsText();
        programsList.itemsProperty().bind(programsText);
        programsText.set(FXCollections.observableArrayList(prgsTxt));
    }

    private List<String> getProgramsText() {
        List<String> progs = new ArrayList<>();
        int cnt = 1;
        System.out.println(ctrs.size());
        for (Controller ctr : this.ctrs) {
            progs.add("ID: " + cnt + " " + ctr.getProgram().getStack());
            cnt += 1;
        }
        return progs;
    }


    public void addController(Controller ctr) {
        this.ctrs.add(ctr);
    }

    public void runOneStepForAll(ActionEvent actionEvent) {
        Integer index = programsList.getSelectionModel().getSelectedIndex();
        if (index == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Controller controller = ctrs.get(index);

        boolean programStateLeft = controller.getProgram().getStack().isEmpty();
        if (programStateLeft) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing left to execute", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        controller.executeOneStep();
        initAllWidgets(index);
    }

    public void setControllers(ArrayList<Controller> controllers) {
        this.ctrs = controllers;
        initList();
    }
}
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class App extends Application {

    Label currentStateLabel = new Label("Current State");
    Label tapePositionLabel = new Label("Tape position");
    Label tapeContentLabel = new Label("");
    Label fullTapeContentLabel = new Label("Full Tape Content");
    Label resultLabel = new Label("Result: ");

    int currentStep = 0;

    private static final int TAPE_CELL_WIDTH = 40;
    private static final int TAPE_CELL_HEIGHT = 40;
    private static final int TAPE_HEIGHT = 60;

    int numCells = 7;
    Rectangle[] tapeCells = new Rectangle[numCells];
    String[] cellText = new String[numCells];
    boolean buttonsDisabled = true;
    
    ArrayList<ArrayList<String>> simulationSteps = new ArrayList<>();

    public ArrayList<ArrayList<String>> TuringSim(String[] inp){
        ArrayList<ArrayList<String>> simSteps = new ArrayList<>();
        try {
            int numRules = Integer.parseInt(inp[1]);
            int arrRow = 2;
            String[][] rules = new String[numRules][5];
    
            for(int i = 0; i < numRules; i++){
                rules[i] = inp[arrRow].split(" ");
                arrRow++;
            }
    
            int acceptState = Integer.parseInt(inp[arrRow]);
            arrRow++;
            String[] str = inp[arrRow].split("");
    
            int pos = 1;
            boolean halt = false;
            int state = 0;
            String[] currRule = {};
    
            ArrayList<String> initialStep = new ArrayList<>();
            initialStep.add("0");
            initialStep.add("1");
            initialStep.add(String.join("", str));
            simSteps.add(initialStep);
    
            while(!halt){
                ArrayList<String> step = new ArrayList<>();
    
                String symbol = str[pos];
                String[] target = {String.valueOf(state), symbol};
                halt = true;
                for (String[] rule : rules) {
                    if(rule[0].equals(target[0]) && rule[1].equals(target[1])){
                        currRule = rule;
                        halt = false;
                    }
                }
                if(halt == true){break;}
                state = Integer.parseInt(currRule[2]);
                str[pos] = currRule[3];
    
                if(currRule[4].equals("L")){pos--;}
                else if(currRule[4].equals("R")){pos++;}
                else{halt = true;}
    
                if(pos >= str.length || pos < 0){
                    halt = true;
                }
                step.add(String.valueOf(state));
                step.add(String.valueOf(pos));
                step.add(String.join("", str));
                simSteps.add(step);
            }
            if(state == acceptState){
                resultLabel.setText("Result: Accept");
            }
            else{
                resultLabel.setText("Result: Fail");
            }
        } catch (Exception e) {
            resultLabel.setText("Invalid Input");
        }
        // int numStates = Integer.parseInt(inp[0]);
        return simSteps;
    }

    private void updateTapeContent(int currStep) {
        for (int i = 0; i < cellText.length; i++) {
            cellText[i] = "";
        }
        ArrayList<String> step = simulationSteps.get(currStep);
        int currState = Integer.parseInt(step.get(0));
        int currPos = Integer.parseInt(step.get(1));
        String[] tape = step.get(2).split("");

        //in front of currPos
        int itPos = currPos;
        for (int i = 3; i < 7 && itPos < tape.length; i++) {
            cellText[i] = tape[itPos];
            itPos++;
        }

        //behind currPos
        itPos = currPos - 1;
        for (int i = 2; i >= 0 && itPos >= 0; i--) {
            cellText[i] = tape[itPos];
            itPos--;
        }
        initializeTapeCells();

        currentStateLabel.setText("Current State: " + currState);
        tapePositionLabel.setText("Tape Position: " + currPos);
        fullTapeContentLabel.setText("Full Tape Contents: " + step.get(2));
    }

    private void initializeTapeCells(){
        HBox parentContainer = ((HBox) tapeContentLabel.getParent());

        HBox tapeCellsContainer = (HBox) parentContainer.lookup("#tapeCellsContainer");
        if (tapeCellsContainer == null) {
            tapeCellsContainer = new HBox();
            tapeCellsContainer.setId("tapeCellsContainer");
            parentContainer.getChildren().add(tapeCellsContainer);
        } else {
            tapeCellsContainer.getChildren().clear(); // Clear existing cells
        }
        for(int i = 0; i < numCells; i++){
            Rectangle cell = new Rectangle(i * TAPE_CELL_WIDTH + 20, TAPE_HEIGHT, TAPE_CELL_WIDTH, TAPE_CELL_HEIGHT);
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.BLACK);
            tapeCells[i] = cell;

            Text text = new Text(cellText[i]);
            StackPane stack = new StackPane();
            stack.getChildren().addAll(cell, text);

            tapeCellsContainer.getChildren().add(stack);
        }
    }

    @Override
    public void start(Stage primaryStage){
        TextArea inputBox = new TextArea("Input TM Info");
        inputBox.setMaxHeight(250);
        inputBox.setMaxWidth(200);

        Text inputInstructions = new Text("Input Structure:\nNumber of States\nNumber of Rules in Turing Machine\nRules: (State    Character on Tape    Next State    Write Character    Move Direction(L or R))\nAccept State\nInput String");

        Button nextStepButton = new Button();
        nextStepButton.setText("Next Step");
        nextStepButton.setDisable(true);
        nextStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(currentStep < simulationSteps.size() - 1){
                    currentStep++;
                }
                updateTapeContent(currentStep);
            }
        });

        Button prevStepButton = new Button();
        prevStepButton.setText("Previous Step");
        prevStepButton.setDisable(true);
        prevStepButton.setDisable(buttonsDisabled);
        prevStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(currentStep > 0){
                    currentStep--; 
                }
                updateTapeContent(currentStep);
            }
        });

        Button submitButton = new Button();
        submitButton.setText("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                
                String[] inp = inputBox.getText().split("\n");
                simulationSteps = TuringSim(inp);

                currentStep = 0;
                nextStepButton.setDisable(false);
                prevStepButton.setDisable(false);
                buttonsDisabled = false;
                updateTapeContent(0);
            }
        });

        VBox layout = new VBox();

        HBox inputLayout = new HBox();
        inputLayout.getChildren().addAll(inputBox, inputInstructions);

        HBox buttonLayout = new HBox();
        buttonLayout.getChildren().addAll(submitButton, nextStepButton, prevStepButton);

        HBox labelLayout = new HBox();
        HBox tapeCellsContainer = new HBox();
        labelLayout.getChildren().addAll(tapeContentLabel, tapeCellsContainer);

        layout.getChildren().addAll(inputLayout, buttonLayout, currentStateLabel, tapePositionLabel,fullTapeContentLabel, resultLabel, labelLayout);
        Scene scene = new Scene(layout, 700, 500);
        
        primaryStage.setTitle("Computer Theory");
        primaryStage.setScene(scene);
        primaryStage.show();

        initializeTapeCells();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

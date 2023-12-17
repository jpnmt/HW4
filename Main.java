package org.example;

//https://www.geeksforgeeks.org/javafx-tutorial/
//https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html?external_link=true
//https://codepal.ai/code-generator/query/vBt6gFt5/java-gridpane-color-change

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Main extends Application {

    public static class Global extends HttpServlet {
        public static int xgridSize;
        public static int ygridSize;
        public static int pColor1, pColor2, pColor3;
        public static int pStopCriterion;

        // Variables for Data Collection
        public static int totalPaintDrops = 0;
        public static int paintDrops_Color1 = 0;
        public static int paintDrops_Color2 = 0;
        public static int paintDrops_Color3 = 0;

        public static int[] colorDrops = new int[3]; // Assuming 3 colors
        public static int maxPaintDropsOnSquare = 0;
        public static double averagePaintDrops = 0.0;

        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String colorSelection = request.getParameter("color-selection");
            try {
                int paintDrops_Color1 = Integer.parseInt(colorSelection);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            String colorSelection2 = request.getParameter("color-selection2");
            try {
                int paintDrops_Color2 = Integer.parseInt(colorSelection2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            String colorSelection3 = request.getParameter("color-selection3");
            try {
                int paintDrops_Color3 = Integer.parseInt(colorSelection3);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }
    @GetMapping
    public String launchJavaFX() {

        try {
            String classesPath = "../target/classes";
            String dependanciesPath = "org.apache.commons";
            String dependanciesPath2 = "org.openjfx";
            String dependanciesPath3 = "org.mortbay.jetty";
            String dependanciesPath4 = "org.springframework.boot";

            String mainClass = "/src/main/Main";

            String classpath = classesPath + File.pathSeparator + dependanciesPath + File.pathSeparator +
                    dependanciesPath2 + File.pathSeparator + dependanciesPath3 + File.pathSeparator +
                    dependanciesPath4 + "/";

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", classpath, mainClass );
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "JavaFX application launched successfully!";
            } else {
                return "Error launching JavaFX application. Exit code: " + exitCode;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error launching JavaFX application: " + e.getMessage();
        }

    }

    private static ArrayList<Integer> sixParameters = new ArrayList<>(6);
    private static final int SQUARE_SIZE = 50; // Size of each square in pixels
    private Rectangle[][] createRectangle; // 2D array to store the squares
    private int[][] paintCount;
    private Timeline timeline;
    private int completedGrids = 0;
    private int totalGrids = 0;
    private List<Stage> gridWindows = new ArrayList<>();
    private List<SimulationData> simulationDataList = new ArrayList<>();
    private Stage graphStage;
    public class SimulationData {
        private final SimpleIntegerProperty totalPaintDrops;
        private final SimpleIntegerProperty paintDropsColor1;
        private final SimpleIntegerProperty paintDropsColor2;
        private final SimpleIntegerProperty paintDropsColor3;
        private final SimpleIntegerProperty maxPaintDropsOnSquare;
        private final SimpleDoubleProperty averagePaintDrops;

        public SimulationData(int totalDrops, int dropsColor1, int dropsColor2, int dropsColor3, int maxDrops, double avgDrops) {
            this.totalPaintDrops = new SimpleIntegerProperty(totalDrops);
            this.paintDropsColor1 = new SimpleIntegerProperty(dropsColor1);
            this.paintDropsColor2 = new SimpleIntegerProperty(dropsColor2);
            this.paintDropsColor3 = new SimpleIntegerProperty(dropsColor3);
            this.maxPaintDropsOnSquare = new SimpleIntegerProperty(maxDrops);
            this.averagePaintDrops = new SimpleDoubleProperty(avgDrops);
        }

        public int getTotalPaintDrops() {
            return totalPaintDrops.get();
        }

        public SimpleIntegerProperty totalPaintDropsProperty() {
            return totalPaintDrops;
        }

        public int getPaintDropsColor1() {
            return paintDropsColor1.get();
        }

        public SimpleIntegerProperty paintDropsColor1Property() {
            return paintDropsColor1;
        }

        public int getPaintDropsColor2() {
            return paintDropsColor2.get();
        }

        public SimpleIntegerProperty paintDropsColor2Property() {
            return paintDropsColor2;
        }

        public int getPaintDropsColor3() {
            return paintDropsColor3.get();
        }

        public SimpleIntegerProperty paintDropsColor3Property() {
            return paintDropsColor3;
        }

        public int getMaxPaintDropsOnSquare() {
            return maxPaintDropsOnSquare.get();
        }

        public SimpleIntegerProperty maxPaintDropsOnSquareProperty() {
            return maxPaintDropsOnSquare;
        }

        public double getAveragePaintDrops() {
            return averagePaintDrops.get();
        }

        public SimpleDoubleProperty averagePaintDropsProperty() {
            return averagePaintDrops;
        }
    }




    @Override
    public void start(Stage primaryStage) {

        // Initialize the grid and rectangles
        createRectangle = new Rectangle[Global.xgridSize][Global.ygridSize];
        paintCount = new int[Global.xgridSize][Global.ygridSize];
        GridPane gridPane = createGridPane(createRectangle);

        // Set up the scene and stage
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Grid Pane Color Change");
        primaryStage.show();
        primaryStage.toFront();

        // Initialize the Timeline
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> moveObjectsRandomly(primaryStage, gridPane)));
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timeline.play(); // Start the timeline
    }

    private GridPane createGridPane(Rectangle[][] createRectangle) {
        GridPane gridPane = new GridPane();
        int xSize = createRectangle.length;
        int ySize = createRectangle[0].length;
        for (int row = 0; row < xSize; row++) {
            for (int col = 0; col < ySize; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                gridPane.add(square, col, row);
                createRectangle[row][col] = square;
            }
        }
        return gridPane;
    }

    private void configureSquareGridPane(GridPane gridPane, Rectangle[][] createRectangle, int size) {
        gridPane.setStyle("-fx-background-color: white;");
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                // Configure your square as needed
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                gridPane.add(square, col, row);
                createRectangle[row][col] = square;
            }
        }
    }

    private void configureRecGridPane(GridPane gridPane, Rectangle[][] createRectangle, int xSize, int ySize) {
        gridPane.setStyle("-fx-background-color: white;");
        for (int row = 0; row < ySize; row++) {
            for (int col = 0; col < xSize; col++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.WHITE);
                square.setStroke(Color.BLACK);
                gridPane.add(square, col, row);
                createRectangle[col][row] = square;
            }
        }
    }

    private void moveObjectsRandomly(Stage primaryStage, GridPane gridPane) {
        Random random = new Random();

        int row = random.nextInt(Global.xgridSize);
        int col = random.nextInt(Global.ygridSize);

        int RandomColor = random.nextInt(3);
        int[] colors = {Global.pColor1, Global.pColor2, Global.pColor3};
        Color newPaintColor = generateColor(colors[RandomColor]);
        Color currentColor = (Color) createRectangle[row][col].getFill();

        if (!currentColor.equals(Color.WHITE)) {
            // Mix the new color with the current color
            Color mixedColor = mixColors(currentColor, newPaintColor);
            createRectangle[row][col].setFill(mixedColor);
        } else {
            // If the square is still white, just fill it with the new color
            createRectangle[row][col].setFill(newPaintColor);
        }

        paintCount[row][col]++;
        Global.totalPaintDrops++;
        Global.colorDrops[RandomColor]++;

        if (paintCount[row][col] > Global.maxPaintDropsOnSquare) {
            Global.maxPaintDropsOnSquare = paintCount[row][col];
        }

        // Stopping criteria check
        boolean criteriaMet = false;
        if (Global.pStopCriterion == 1 && isLastUnpaintedSquare(paintCount, row, col, createRectangle)) {
            criteriaMet = true;
        } else if (Global.pStopCriterion == 2 && paintCount[row][col] == 2) {
            criteriaMet = true;
        }

        if (criteriaMet) {
            calculateAveragePaintDrops(paintCount, Global.xgridSize, Global.ygridSize);
            timeline.stop();
            Platform.runLater(() -> showResults(primaryStage));
        }

        Global.totalPaintDrops++;

        switch (RandomColor) {

            case 0:
                Global.paintDrops_Color1++;
                break;
            case 1:
                Global.paintDrops_Color2++;
                break;
            case 2:
                Global.paintDrops_Color3++;
                break;
        }

    }

    private void moveObjectsRandomly2(Stage gridStage, GridPane gridPane, Rectangle[][] createRectangle, int[][] paintCount, int xSize, int ySize, Timeline timeline, Stage primaryStage) {
        Random random = new Random();

        int row = random.nextInt(xSize);
        int col = random.nextInt(ySize);
        int RandomColor = random.nextInt(3);
        int[] colors = {Global.pColor1, Global.pColor2, Global.pColor3};

        Color newPaintColor = generateColor(colors[RandomColor]);
        Color currentColor = (Color) createRectangle[row][col].getFill();

        // Mix new color with the current color, or just fill if white
        if (!currentColor.equals(Color.WHITE)) {
            Color mixedColor = mixColors(currentColor, newPaintColor);
            createRectangle[row][col].setFill(mixedColor);
        } else {
            createRectangle[row][col].setFill(newPaintColor);
        }

        // Update counts
        paintCount[row][col]++;
        Global.totalPaintDrops++;
        Global.colorDrops[RandomColor]++;

        // Update max paint drops on a square
        if (paintCount[row][col] > Global.maxPaintDropsOnSquare) {
            Global.maxPaintDropsOnSquare = paintCount[row][col];
        }

        // Check stopping criteria
        boolean criteriaMet = false;
        if (Global.pStopCriterion == 1 && isLastUnpaintedSquare(paintCount, row, col, createRectangle)) {
            criteriaMet = true;
        } else if (Global.pStopCriterion == 2 && paintCount[row][col] == 2) {
            criteriaMet = true;
        }

        // When criteria met, calculate averages, stop timeline, and store data
        if (criteriaMet) {
            calculateAveragePaintDrops(paintCount, xSize, ySize);
            timeline.stop();
            storeSimulationData(); // Store the data as soon as the simulation is done
            incrementAndCheckCompletedGrids(primaryStage); // Then, check if all simulations are done
        }
    }


    private void calculateAveragePaintDrops(int[][] paintCount, int xSize, int ySize) {
        int totalSquares = xSize * ySize;
        int totalDrops = 0;
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                totalDrops += paintCount[i][j];
            }
        }
        Global.averagePaintDrops = (double) totalDrops / totalSquares;
    }

    private void showContinueButton1(Stage primaryStage, GridPane gridPane) {
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(event -> showExperimentInstructions(primaryStage));

        // Create a VBox to hold the grid and the button
        VBox layout = new VBox(10); // 10 is the spacing between elements
        layout.getChildren().addAll(gridPane, continueButton);

        // Set the new layout in the scene
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);

        //System.out.println("A: Total paint drops: " + Global.totalPaintDrops);
        // System.out.println("A1. Total paint drops of Color 1: " + Global.paintDrops_Color1);
        // System.out.println("A2. Total paint drops of Color 2: " + Global.paintDrops_Color2);
        //ystem.out.println("A3. Total paint drops of Color 3: " + Global.paintDrops_Color3);

        showResults(primaryStage);
    }

    private void showResults(Stage primaryStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Painting Results");
        alert.setHeaderText("Results of the Random Painting");

        String content = String.format(
                "Total number of paint drops: %d\n" +
                        "Number of paint drops of Color 1: %d\n" +
                        "Number of paint drops of Color 2: %d\n" +
                        "Number of paint drops of Color 3: %d\n" +
                        "Maximum number of paint drops on a square: %d\n" +
                        "Average number of paint drops per square: %.2f",
                Global.totalPaintDrops / 2, Global.colorDrops[0], Global.colorDrops[1], Global.colorDrops[2],
                Global.maxPaintDropsOnSquare, Global.averagePaintDrops);

        alert.setContentText(content);

        // Handle the OK button action
        alert.setOnCloseRequest(event -> {
            primaryStage.close(); // Close the main grid window
            System.out.println("Opening experiment instructions"); // Debugging line
            showExperimentInstructions(new Stage()); // Open the experiment information window in a new stage
        });

        alert.showAndWait();
    }


    //From initial simulation to experiment directions
    private boolean isLastUnpaintedSquare(int[][] paintCount, int currentRow, int currentCol, Rectangle[][] createRectangle) {
        // Add checks for bounds
        if (currentRow < 0 || currentRow >= paintCount.length || currentCol < 0 || currentCol >= paintCount[currentRow].length) {
            System.out.println("Index out of bounds: currentRow=" + currentRow + ", currentCol=" + currentCol);
            return false; // Index out of bounds
        }

        if (paintCount[currentRow][currentCol] > 1) {
            return false; // This square was already painted before
        }
        int xSize = createRectangle.length;
        int ySize = createRectangle[0].length;
        for (int row = 0; row < xSize; row++) {
            for (int col = 0; col < ySize; col++) {
                if (paintCount[row][col] == 0 && (row != currentRow || col != currentCol)) {
                    return false; // Found another unpainted square
                }
            }
        }
        return true; // No other unpainted squares found
    }
    private synchronized void incrementAndCheckCompletedGrids(Stage primaryStage) {
        completedGrids++;
        if (completedGrids >= totalGrids) {
            Platform.runLater(() -> showContinueButton2(primaryStage != null ? primaryStage : new Stage()));
        }
    }

    private void showContinueButton2(Stage stage) {
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(event -> {
            // Close all grid windows and the current stage
            gridWindows.forEach(Stage::close);
            gridWindows.clear();
            stage.close();

            // Open the results table in a new window
            displayExperimentResults(new Stage());
        });

        StackPane layout = new StackPane(continueButton);
        Scene continueScene = new Scene(layout, 200, 100);
        stage.setScene(continueScene);
        stage.centerOnScreen();
        stage.show();
    }

    private void displayExperimentResults(Stage currentStage) {
        TableView<SimulationData> table = new TableView<>();

        // Creating columns for the table
        TableColumn<SimulationData, Number> colTotalDrops = new TableColumn<>("Total Paint Drops");
        colTotalDrops.setCellValueFactory(new PropertyValueFactory<>("totalPaintDrops"));

        TableColumn<SimulationData, Number> colDropsColor1 = new TableColumn<>("Paint Drops - Color 1");
        colDropsColor1.setCellValueFactory(new PropertyValueFactory<>("paintDropsColor1"));

        TableColumn<SimulationData, Number> colDropsColor2 = new TableColumn<>("Paint Drops - Color 2");
        colDropsColor2.setCellValueFactory(new PropertyValueFactory<>("paintDropsColor2"));

        TableColumn<SimulationData, Number> colDropsColor3 = new TableColumn<>("Paint Drops - Color 3");
        colDropsColor3.setCellValueFactory(new PropertyValueFactory<>("paintDropsColor3"));

        TableColumn<SimulationData, Number> colMaxDropsSquare = new TableColumn<>("Max Drops on a Square");
        colMaxDropsSquare.setCellValueFactory(new PropertyValueFactory<>("maxPaintDropsOnSquare"));

        TableColumn<SimulationData, Number> colAverageDrops = new TableColumn<>("Average Drops per Square");
        colAverageDrops.setCellValueFactory(new PropertyValueFactory<>("averagePaintDrops"));

        table.getColumns().addAll(colTotalDrops, colDropsColor1, colDropsColor2, colDropsColor3, colMaxDropsSquare, colAverageDrops);
        table.setItems(FXCollections.observableArrayList(simulationDataList));

        // Checkboxes for showing/hiding table columns
        CheckBox showTotalDrops = new CheckBox("Show Total Paint Drops");
        CheckBox showDropsColor1 = new CheckBox("Show Paint Drops - Color 1");
        CheckBox showDropsColor2 = new CheckBox("Show Paint Drops - Color 2");
        CheckBox showDropsColor3 = new CheckBox("Show Paint Drops - Color 3");
        CheckBox showMaxDropsSquare = new CheckBox("Show Max Drops on a Square");
        CheckBox showAverageDrops = new CheckBox("Show Average Drops per Square");

        // Set all checkboxes to checked by default
        showTotalDrops.setSelected(true);
        showDropsColor1.setSelected(true);
        showDropsColor2.setSelected(true);
        showDropsColor3.setSelected(true);
        showMaxDropsSquare.setSelected(true);
        showAverageDrops.setSelected(true);

        // Update table columns visibility based on checkbox selection
        showTotalDrops.setOnAction(e -> table.getColumns().get(0).setVisible(showTotalDrops.isSelected()));
        showDropsColor1.setOnAction(e -> table.getColumns().get(1).setVisible(showDropsColor1.isSelected()));
        showDropsColor2.setOnAction(e -> table.getColumns().get(2).setVisible(showDropsColor2.isSelected()));
        showDropsColor3.setOnAction(e -> table.getColumns().get(3).setVisible(showDropsColor3.isSelected()));
        showMaxDropsSquare.setOnAction(e -> table.getColumns().get(4).setVisible(showMaxDropsSquare.isSelected()));
        showAverageDrops.setOnAction(e -> table.getColumns().get(5).setVisible(showAverageDrops.isSelected()));

        // Button for continuing to the graph
        Button continueButton = new Button("Continue");
        Label infoLabel = new Label("Select up to two columns to continue");

        continueButton.setDisable(true); // Initially disable the button

        // Listener to enable continue button based on selection
        ChangeListener<Boolean> columnVisibilityListener = (obs, oldVal, newVal) -> {
            long visibleColumns = Stream.of(showTotalDrops, showDropsColor1, showDropsColor2, showDropsColor3, showMaxDropsSquare, showAverageDrops)
                    .filter(CheckBox::isSelected).count();
            continueButton.setDisable(visibleColumns > 2);
        };

        // Add listeners to checkboxes
        showTotalDrops.selectedProperty().addListener(columnVisibilityListener);
        showDropsColor1.selectedProperty().addListener(columnVisibilityListener);
        showDropsColor2.selectedProperty().addListener(columnVisibilityListener);
        showDropsColor3.selectedProperty().addListener(columnVisibilityListener);
        showMaxDropsSquare.selectedProperty().addListener(columnVisibilityListener);
        showAverageDrops.selectedProperty().addListener(columnVisibilityListener);

        // Action for continue button
        continueButton.setOnAction(e -> {
            currentStage.close();
            openGraphWindow();
        });

        HBox checkBoxContainer = new HBox(10, showTotalDrops, showDropsColor1, showDropsColor2, showDropsColor3, showMaxDropsSquare, showAverageDrops);
        HBox buttonContainer = new HBox(10, continueButton, infoLabel);

        VBox layout = new VBox(10, checkBoxContainer, table, buttonContainer);
        Scene scene = new Scene(layout, 800, 400);
        currentStage.setScene(scene);
        currentStage.show();
    }
    private void openGraphWindow() {
        graphStage = new Stage();
        graphStage.setTitle("Graphical Representation of Data");

        // Create the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Independent Variable"); // Set according to your experiment
        yAxis.setLabel("Values");

        // Create the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Experiment Data Visualization");

        // Add data to the chart
        addChartData(lineChart);


// Buttons for navigation
        Button btnBackToTable = new Button("Back to Data Table");
        Button btnBackToExperimentSelection = new Button("Back to Experiment Selection");
        Button btnEndProgram = new Button("End Program");

        // Event for Back to Data Table button
        btnBackToTable.setOnAction(e -> {
            graphStage.close(); // Close the graph stage
            displayExperimentResults(new Stage()); // Open the data table in a new stage
        });

        // Event for Back to Experiment Selection button
        btnBackToExperimentSelection.setOnAction(e -> {
            graphStage.close(); // Close the graph stage
            Stage stage = new Stage();
            showExperimentSelection(stage); // Open the experiment selection in a new stage
            stage.show();
        });

        // Event for End Program button
        btnEndProgram.setOnAction(e -> {
            graphStage.close(); // Close the graph stage
            Platform.exit(); // End the program
        });

        // Create button layout
        HBox buttonLayout = new HBox(10, btnBackToTable, btnBackToExperimentSelection, btnEndProgram);
        buttonLayout.setAlignment(Pos.CENTER);

        // Add the graph and buttons to the layout
        VBox layout = new VBox(10, lineChart, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        // Set the scene
        Scene scene = new Scene(layout, 800, 650);
        graphStage.setScene(scene);
        graphStage.show();
    }

    private void addChartData(LineChart<Number, Number> lineChart) {
        // Example of adding data, modify according to your application logic
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Sample Data");

        // Dummy data - replace this with actual data retrieval logic
        for (int i = 0; i < 10; i++) {
            series.getData().add(new XYChart.Data<>(i, Math.random() * 100));
        }

        lineChart.getData().add(series);
    }


    private void updateTableColumnsVisibility(TableView<SimulationData> table,
                                              boolean showTotalDrops,
                                              boolean showDropsColor1,
                                              boolean showDropsColor2,
                                              boolean showDropsColor3,
                                              boolean showMaxDropsSquare,
                                              boolean showAverageDrops) {
        table.getColumns().get(0).setVisible(showTotalDrops);
        table.getColumns().get(1).setVisible(showDropsColor1);
        table.getColumns().get(2).setVisible(showDropsColor2);
        table.getColumns().get(3).setVisible(showDropsColor3);
        table.getColumns().get(4).setVisible(showMaxDropsSquare);
        table.getColumns().get(5).setVisible(showAverageDrops);
    }


    private void resetGlobalVariables() {
        Global.totalPaintDrops = 0;
        Global.paintDrops_Color1 = 0;
        Global.paintDrops_Color2 = 0;
        Global.paintDrops_Color3 = 0;
        Global.maxPaintDropsOnSquare = 0;
        Global.averagePaintDrops = 0.0;
        Arrays.fill(Global.colorDrops, 0); // Reset the color drops array if it's being used
    }

    private Color generateColor(int numColor) {
        Color c = Color.rgb(255, 255, 255);
        switch (numColor) {
            case 1:  //red
                c = Color.rgb(255, 0, 0);
                break;
            case 2:   //orange
                c = Color.rgb(255, 165, 0);
                break;
            case 3:   //yellow
                c = Color.rgb(255, 255, 0);
                break;
            case 4:   //green
                c = Color.rgb(0, 255, 0);
                break;
            case 5:   //blue
                c = Color.rgb(0, 0, 255);
                break;
            case 6:   //indigo
                c = Color.rgb(75, 0, 130);
                break;
            case 7:   //violet
                c = Color.rgb(127, 0, 255);
                break;
            case 8:   //black
                c = Color.rgb(0, 0, 0);
                break;
        }
        return c;
    }
    private Color mixColors(Color color1, Color color2) {
        double red = (color1.getRed() + color2.getRed()) / 2;
        double green = (color1.getGreen() + color2.getGreen()) / 2;
        double blue = (color1.getBlue() + color2.getBlue()) / 2;
        return new Color(red, green, blue, 1.0); // Alpha (opacity) is set to 1.0 (fully opaque)
    }
    private void showExperimentInstructions(Stage primaryStage) {
        VBox instructionsLayout = new VBox(20);
        instructionsLayout.getChildren().clear(); // Clear existing children

        Label instructionsLabel = new Label("Instructions for Experiments...");
        instructionsLabel.setText("Experiment 1: You will input a series of values for the grids that apply to both the X and Y values of the grid. Number of repetitions is the same across all sizes.\n\nExperiment 2: The Y value and number of repetitions are the same across all grids while the X value varies.\n\nExperiment 3: Input X and Y grid sizes. The same grid will have different repetitions across multiple simulations.");

        Button selectExperimentButton = new Button("Select Experiment");
        selectExperimentButton.setOnAction(event -> showExperimentSelection(primaryStage));

        instructionsLayout.getChildren().addAll(instructionsLabel, selectExperimentButton);

        Scene instructionsScene = new Scene(instructionsLayout, 850, 250);
        primaryStage.setScene(instructionsScene);
        primaryStage.show(); // Make sure to call show() to display the stage
    }
    private void showExperimentSelection(Stage primaryStage) {
        VBox selectionLayout = new VBox(20);
        Label selectionLabel = new Label("Select an Experiment:");

        Button experimentOneButton = new Button("Experiment 1");
        experimentOneButton.setOnAction(event ->  showExperimentOneInput(primaryStage));

        Button experimentTwoButton = new Button("Experiment 2");
        experimentTwoButton.setOnAction(event -> showExperimentTwoInput(primaryStage));

        Button experimentThreeButton = new Button("Experiment 3");
        experimentThreeButton.setOnAction(event -> showExperimentThreeInput(primaryStage));

        selectionLayout.getChildren().addAll(selectionLabel, experimentOneButton, experimentTwoButton, experimentThreeButton);

        Scene selectionScene = new Scene(selectionLayout, 300, 250);
        primaryStage.setScene(selectionScene);
    }
    private void showExperimentOneInput(Stage primaryStage) {
        VBox inputLayout = new VBox(20);
        Label instructionsLabel = new Label("Enter grid sizes (separated by space, max 20 for each dimension), number of repetitions, color choices, and stopping criterion:");
        TextField gridSizeInput = new TextField();
        gridSizeInput.setPromptText("Enter grid sizes (e.g. 5 10 15)");
        TextField repetitionsInput = new TextField();
        repetitionsInput.setPromptText("Enter number of repetitions (max 10)");
        ComboBox<String> color1 = new ComboBox<>();
        color1.setPromptText("Color 1");
        ComboBox<String> color2 = new ComboBox<>();
        color2.setPromptText("Color 2");
        ComboBox<String> color3 = new ComboBox<>();
        color3.setPromptText("Color 3");
        setupColorComboBoxes(color1, color2, color3);
        ToggleGroup stoppingCriterionGroup = new ToggleGroup();
        RadioButton criterion1 = new RadioButton("Last unpainted square is painted for the first time");
        RadioButton criterion2 = new RadioButton("First time any square gets its second paint blob");
        criterion1.setToggleGroup(stoppingCriterionGroup);
        criterion2.setToggleGroup(stoppingCriterionGroup);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            List<Integer> gridSizes = parseGridSizes(gridSizeInput.getText());
            int repetitions = parseRepetitions(repetitionsInput.getText());
            int selectedColor1 = parseColorChoice(color1.getValue());
            int selectedColor2 = parseColorChoice(color2.getValue());
            int selectedColor3 = parseColorChoice(color3.getValue());
            int selectedCriterion = stoppingCriterionGroup.getSelectedToggle().equals(criterion1) ? 1 : 2;
            if (validateInputs(gridSizes, repetitions, selectedColor1, selectedColor2, selectedColor3)) {
                Global.pColor1 = selectedColor1;
                Global.pColor2 = selectedColor2;
                Global.pColor3 = selectedColor3;
                Global.pStopCriterion = selectedCriterion;
                try {
                    runExperimentOne(gridSizes, repetitions, primaryStage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                primaryStage.close(); // Close the input window
            }
        });
        inputLayout.getChildren().addAll(instructionsLabel, gridSizeInput, repetitionsInput, color1, color2, color3, criterion1, criterion2, submitButton);
        Scene inputScene = new Scene(inputLayout, 700, 400);
        primaryStage.setScene(inputScene);
    }

    private void showExperimentTwoInput(Stage primaryStage) {
        VBox inputLayout = new VBox(20);
        Label instructionsLabel = new Label("Enter fixed y-dimension, x-dimensions (separated by space, max 20), number of repetitions, color choices, and stopping criterion:");
        TextField yDimensionInput = new TextField();
        yDimensionInput.setPromptText("Enter fixed y-dimension (max 12)");
        TextField xDimensionsInput = new TextField();
        xDimensionsInput.setPromptText("Enter x-dimensions (e.g. 5 10 15)");
        TextField repetitionsInput = new TextField();
        repetitionsInput.setPromptText("Enter number of repetitions (max 10)");
        ComboBox<String> color1 = new ComboBox<>();
        color1.setPromptText("Color 1");
        ComboBox<String> color2 = new ComboBox<>();
        color2.setPromptText("Color 2");
        ComboBox<String> color3 = new ComboBox<>();
        color3.setPromptText("Color 3");
        setupColorComboBoxes(color1, color2, color3);
        ToggleGroup stoppingCriterionGroup = new ToggleGroup();
        RadioButton criterion1 = new RadioButton("Last unpainted square is painted for the first time");
        RadioButton criterion2 = new RadioButton("First time any square gets its second paint blob");
        criterion1.setToggleGroup(stoppingCriterionGroup);
        criterion2.setToggleGroup(stoppingCriterionGroup);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int fixedYDimension = parseYDimension(yDimensionInput.getText());
            List<Integer> xDimensions = parseXDimensions(xDimensionsInput.getText());
            int repetitions = parseRepetitions(repetitionsInput.getText());
            int selectedColor1 = parseColorChoice(color1.getValue());
            int selectedColor2 = parseColorChoice(color2.getValue());
            int selectedColor3 = parseColorChoice(color3.getValue());
            int selectedCriterion = stoppingCriterionGroup.getSelectedToggle().equals(criterion1) ? 1 : 2;
            if (validateInputsForExperimentTwo(fixedYDimension, xDimensions, repetitions, selectedColor1, selectedColor2, selectedColor3, selectedCriterion)) {
                Global.pColor1 = selectedColor1;
                Global.pColor2 = selectedColor2;
                Global.pColor3 = selectedColor3;
                Global.pStopCriterion = selectedCriterion;
                try {
                    runExperimentTwo(fixedYDimension, xDimensions, repetitions, primaryStage);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                primaryStage.close(); // Close the input window
            }
        });
        inputLayout.getChildren().addAll(instructionsLabel, yDimensionInput, xDimensionsInput, repetitionsInput, color1, color2, color3, criterion1, criterion2, submitButton);
        Scene inputScene = new Scene(inputLayout, 700, 450);
        primaryStage.setScene(inputScene);
    }

    private void showExperimentThreeInput(Stage primaryStage) {
        VBox inputLayout = new VBox(20);
        Label instructionsLabel = new Label("Enter constant x-dimension, y-dimension, different repetition amounts (separated by space, max 10 each), color choices, and stopping criterion:");
        TextField xDimensionInput = new TextField();
        xDimensionInput.setPromptText("Enter constant x-dimension (max 25)");
        TextField yDimensionInput = new TextField();
        yDimensionInput.setPromptText("Enter constant y-dimension (max 12)");
        TextField repetitionsInput = new TextField();
        repetitionsInput.setPromptText("Enter different repetition amounts (e.g. 3 5 7)");
        ComboBox<String> color1 = new ComboBox<>();
        color1.setPromptText("Color 1");
        ComboBox<String> color2 = new ComboBox<>();
        color2.setPromptText("Color 2");
        ComboBox<String> color3 = new ComboBox<>();
        color3.setPromptText("Color 3");
        setupColorComboBoxes(color1, color2, color3);
        ToggleGroup stoppingCriterionGroup = new ToggleGroup();
        RadioButton criterion1 = new RadioButton("Last unpainted square is painted for the first time");
        RadioButton criterion2 = new RadioButton("First time any square gets its second paint blob");
        criterion1.setToggleGroup(stoppingCriterionGroup);
        criterion2.setToggleGroup(stoppingCriterionGroup);
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int xDimension = parseXDimension(xDimensionInput.getText());
            int yDimension = parseYDimension(yDimensionInput.getText());
            List<Integer> repetitionsList = parseRepetitionsList(repetitionsInput.getText());
            int selectedColor1 = parseColorChoice(color1.getValue());
            int selectedColor2 = parseColorChoice(color2.getValue());
            int selectedColor3 = parseColorChoice(color3.getValue());
            int selectedCriterion = stoppingCriterionGroup.getSelectedToggle().equals(criterion1) ? 1 : 2;
            if (validateInputsForExperimentThree(xDimension, yDimension, repetitionsList, selectedColor1, selectedColor2, selectedColor3, selectedCriterion)) {
                Global.xgridSize = xDimension;
                Global.ygridSize = yDimension;
                Global.pColor1 = selectedColor1;
                Global.pColor2 = selectedColor2;
                Global.pColor3 = selectedColor3;
                Global.pStopCriterion = selectedCriterion;
                try {
                    runExperimentThree(xDimension, yDimension, repetitionsList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                primaryStage.close(); // Close the input window
            }
        });
        inputLayout.getChildren().addAll(instructionsLabel, xDimensionInput, yDimensionInput, repetitionsInput, color1, color2, color3, criterion1, criterion2, submitButton);
        Scene inputScene = new Scene(inputLayout, 775, 500);
        primaryStage.setScene(inputScene);
    }

    private void setupColorComboBoxes(ComboBox<String> color1, ComboBox<String> color2, ComboBox<String> color3) {
        List<String> colorOptions = Arrays.asList("Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet", "Black");
        color1.getItems().addAll(colorOptions);
        color2.getItems().addAll(colorOptions);
        color3.getItems().addAll(colorOptions);
    }
    private int parseColorChoice(String colorName) {
        switch (colorName) {
            case "Red": return 1;
            case "Orange": return 2;
            case "Yellow": return 3;
            case "Green": return 4;
            case "Blue": return 5;
            case "Indigo": return 6;
            case "Violet": return 7;
            case "Black": return 8;
            default: return 0; // Invalid color
        }
    }
    private boolean validateInputs(List<Integer> gridSizes, int repetitions, int color1, int color2, int color3) {
        // Validate grid sizes
        if (gridSizes.isEmpty() || Collections.min(gridSizes) <= 0 || Collections.max(gridSizes) > 20) {
            System.out.println("Invalid grid sizes. Sizes must be between 1 and 20.");
            return false;
        }

        // Validate repetitions
        if (repetitions <= 0 || repetitions > 10) {
            System.out.println("Invalid number of repetitions. Must be between 1 and 10.");
            return false;
        }

        // Validate colors
        if (color1 == 0 || color2 == 0 || color3 == 0) {
            System.out.println("One or more selected colors are invalid.");
            return false;
        }

        // Check if colors are distinct
        if (color1 == color2 || color1 == color3 || color2 == color3) {
            System.out.println("Please select three distinct colors.");
            return false;
        }

        return true;
    }
    private List<Integer> parseGridSizes(String input) {
        List<Integer> sizes = new ArrayList<>();
        try {
            for (String sizeStr : input.split("\\s+")) {
                int size = Integer.parseInt(sizeStr);
                if (size > 0 && size <= 20) {
                    sizes.add(size);
                } else {
                    System.out.println("Invalid grid size: " + size);
                    return Collections.emptyList(); // Return empty list if invalid input
                }
            }

            //Silvia added this for loop 12/16/2023
            //Check if the values are increasing
            for (int i = 0; i < sizes.size()-1; i++) {
                if (sizes.get(i) > sizes.get(i + 1)) {
                    System.out.println("Not increasing values in the list of grid-sizes");
                    return Collections.emptyList(); // Return empty list if invalid input
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for grid sizes.");
            return Collections.emptyList();
        }
        return sizes;
    }
    private int parseRepetitions(String input) {
        try {
            int repetitions = Integer.parseInt(input);
            if (repetitions > 0 && repetitions <= 10) {
                return repetitions;
            } else {
                System.out.println("Invalid number of repetitions: " + repetitions);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for repetitions.");
        }
        return 0; // Return 0 to indicate invalid input
    }
    private List<Integer> parseXDimensions(String input) {
        List<Integer> xDimensions = new ArrayList<>();
        try {
            for (String dimStr : input.split("\\s+")) {
                int xDimension = Integer.parseInt(dimStr);
                if (xDimension > 0 && xDimension <= 20) {
                    xDimensions.add(xDimension);
                } else {
                    System.out.println("Invalid x-dimension: " + xDimension);
                    return Collections.emptyList(); // Return empty list if invalid input
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for x-dimensions.");
            return Collections.emptyList();
        }
        return xDimensions;
    } //Exp.2
    private int parseYDimension(String input) {
        try {
            int yDimension = Integer.parseInt(input);
            if (yDimension > 0 && yDimension <= 12) {
                return yDimension;
            } else {
                System.out.println("Invalid y-dimension. Must be between 1 and 12.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for y-dimension.");
        }
        return 0; // Indicate invalid input
    } //Exp. 2
    private int parseXDimension(String input) {
        try {
            int xDimension = Integer.parseInt(input);
            if (xDimension > 0 && xDimension <= 25) {
                return xDimension;
            } else {
                System.out.println("Invalid x-dimension. Must be between 1 and 25.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for x-dimension.");
        }
        return 0; // Indicate invalid input
    } //Exp. 3
    private List<Integer> parseRepetitionsList(String input) {
        List<Integer> repetitionsList = new ArrayList<>();
        try {
            for (String repStr : input.split("\\s+")) {
                int repetitions = Integer.parseInt(repStr);
                if (repetitions > 0 && repetitions <= 10) {
                    repetitionsList.add(repetitions);
                } else {
                    System.out.println("Invalid repetition value: " + repetitions);
                    return Collections.emptyList(); // Return empty list if invalid input
                }
            }
            //Silvia added this for loop line 655-661 12/16/2023
            //Check if the values are increasing
            for (int i = 0; i < repetitionsList.size()-1; i++) {
                if (repetitionsList.get(i) > repetitionsList.get(i + 1)) {
                    System.out.println("Not increasing values in the list of repetitionsList");
                    return Collections.emptyList(); // Return empty list if invalid input
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format for repetitions.");
            return Collections.emptyList();
        }
        return repetitionsList;
    } //Exp. 3
    private boolean validateInputsForExperimentTwo(int yDimension, List<Integer> xDimensions, int repetitions, int color1, int color2, int color3, int stopCriterion) {
        // Validate y-dimension
        if (yDimension <= 0 || yDimension > 12) {
            System.out.println("Invalid y-dimension. Must be between 1 and 12.");
            return false;
        }

        // Validate x-dimensions
        if (xDimensions.isEmpty() || Collections.min(xDimensions) <= 0 || Collections.max(xDimensions) > 20) {
            System.out.println("Invalid x-dimensions. Each must be between 1 and 20.");
            return false;
        } //Silvia added this else statement
        else if (Collections.min(xDimensions) > 0 && Collections.max(xDimensions) <= 20) {
            //Check if the values are increasing
            for (int i = 0; i < xDimensions.size()-1; i++) {
                if (xDimensions.get(i) > xDimensions.get(i + 1)) {
                    System.out.println("Not increasing values in the list of x-dimensions");
                    return false;
                }
            }
        }


        // Validate repetitions
        if (repetitions <= 0 || repetitions > 10) {
            System.out.println("Invalid number of repetitions. Must be between 1 and 10.");
            return false;
        }

        // Validate color choices
        if (color1 == 0 || color2 == 0 || color3 == 0) {
            System.out.println("One or more selected colors are invalid.");
            return false;
        }
        if (color1 == color2 || color1 == color3 || color2 == color3) {
            System.out.println("Please select three distinct colors.");
            return false;
        }

        // Validate stopping criterion
        if (stopCriterion != 1 && stopCriterion != 2) {
            System.out.println("Invalid stopping criterion. Choose either 1 or 2.");
            return false;
        }

        return true;
    }
    private boolean validateInputsForExperimentThree(int xDimension, int yDimension, List<Integer> repetitionsList, int color1, int color2, int color3, int stopCriterion) {
        // Validate x-dimension
        if (xDimension <= 0 || xDimension > 25) {
            System.out.println("Invalid x-dimension. Must be between 1 and 25.");
            return false;
        }

        // Validate y-dimension
        if (yDimension <= 0 || yDimension > 12) {
            System.out.println("Invalid y-dimension. Must be between 1 and 12.");
            return false;
        }

        // Validate repetitions list
        if (repetitionsList.isEmpty() || Collections.min(repetitionsList) <= 0 || Collections.max(repetitionsList) > 10) {
            System.out.println("Invalid repetitions. Each must be between 1 and 10.");
            return false;
        }

        // Validate color choices
        if (color1 == 0 || color2 == 0 || color3 == 0) {
            System.out.println("One or more selected colors are invalid.");
            return false;
        }
        if (color1 == color2 || color1 == color3 || color2 == color3) {
            System.out.println("Please select three distinct colors.");
            return false;
        }

        // Validate stopping criterion
        if (stopCriterion != 1 && stopCriterion != 2) {
            System.out.println("Invalid stopping criterion. Choose either 1 or 2.");
            return false;
        }

        return true;
    }
    private void runExperimentOne(List<Integer> gridSizes, int repetitions, Stage primaryStage) throws InterruptedException {
        totalGrids = gridSizes.size() * repetitions;
        completedGrids = 0;
        int gapBetweenWindows = 50; // Desired gap between windows
        int baseX = 100; // Base X position for the first window
        int baseY = 100; // Base Y position for the first window
        int windowBorderPadding = 20; // Estimate additional space for window borders and padding

        for (int size : gridSizes) {
            int windowWidth = size * SQUARE_SIZE + windowBorderPadding;

            for (int i = 0; i < repetitions; i++) {
                resetGlobalVariables();
                // Create a new GridPane for each grid
                GridPane gridPane = new GridPane();
                Rectangle[][] createRectangle = new Rectangle[size][size];
                int[][] paintCount = new int[size][size];

                // Configure the gridPane and add rectangles
                configureSquareGridPane(gridPane, createRectangle, size);

                Stage newStage = new Stage();
                newStage.setScene(new Scene(gridPane));
                gridWindows.add(newStage);

                // Set position for each window
                newStage.setX(baseX + i * (windowWidth + gapBetweenWindows));
                newStage.setY(baseY);

                newStage.show();
                newStage.toFront();

                // Initialize the Timeline
                final Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event ->
                        moveObjectsRandomly2(newStage, gridPane, createRectangle, paintCount, size, size, timeline, primaryStage)));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            baseY += 100; // Increment base Y position for the next size
        }
    }
    private void runExperimentTwo(int fixedYDimension, List<Integer> xDimensions, int repetitions, Stage primaryStage) throws InterruptedException {
        totalGrids = xDimensions.size() * repetitions;
        completedGrids = 0;

        int gapBetweenWindows = 50; // Desired gap between windows
        int baseX = 100; // Base X position for the first window
        int baseY = 100; // Base Y position for the first window
        int windowBorderPadding = 20; // Estimate additional space for window borders and padding

        for (int xDimension : xDimensions) {
            int windowWidth = xDimension * SQUARE_SIZE + windowBorderPadding;

            for (int rep = 0; rep < repetitions; rep++) {
                resetGlobalVariables();
                // Create a new GridPane for each grid
                GridPane gridPane = new GridPane();
                Rectangle[][] createRectangle = new Rectangle[xDimension][fixedYDimension];
                int[][] paintCount = new int[xDimension][fixedYDimension];

                // Configure the gridPane and add rectangles
                configureRecGridPane(gridPane, createRectangle, xDimension, fixedYDimension);

                Stage newStage = new Stage();
                newStage.setScene(new Scene(gridPane));
                gridWindows.add(newStage);

                // Set position for each window
                newStage.setX(baseX + rep * (windowWidth + gapBetweenWindows));
                newStage.setY(baseY);

                newStage.show();
                newStage.toFront();

                // Initialize the Timeline
                final Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event ->
                        moveObjectsRandomly2(newStage, gridPane, createRectangle, paintCount, xDimension, fixedYDimension, timeline, primaryStage)));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            baseY += windowWidth + gapBetweenWindows; // Increment base Y position for the next x-dimension
        }
    }
    private void runExperimentThree(int xGridSize, int yGridSize, List<Integer> repetitionsList) throws InterruptedException {
        totalGrids = repetitionsList.stream().reduce(0, Integer::sum);
        completedGrids = 0;

        int gapBetweenWindows = 50; // Desired gap between windows
        int baseX = 100; // Base X position for the first window
        int baseY = 100; // Base Y position for the first window
        int windowBorderPadding = 20; // Estimate additional space for window borders and padding

        for (int repetitions : repetitionsList) {
            System.out.println("Running simulation with " + repetitions + " repetitions.");

            for (int i = 0; i < repetitions; i++) {
                resetGlobalVariables();
                // Create a new GridPane for each grid
                GridPane gridPane = new GridPane();
                Rectangle[][] createRectangle = new Rectangle[xGridSize][yGridSize];
                int[][] paintCount = new int[xGridSize][yGridSize];

                // Configure the gridPane and add rectangles
                configureRecGridPane(gridPane, createRectangle, xGridSize, yGridSize);

                Stage newStage = new Stage();
                newStage.setScene(new Scene(gridPane));
                gridWindows.add(newStage);

                // Set position for each window
                newStage.setX(baseX + i * (xGridSize * SQUARE_SIZE + gapBetweenWindows + windowBorderPadding));
                newStage.setY(baseY);

                newStage.show();
                newStage.toFront();

                // Initialize the Timeline
                final Timeline timeline = new Timeline();
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event ->
                        moveObjectsRandomly2(newStage, gridPane, createRectangle, paintCount, xGridSize, yGridSize, timeline, null)));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            baseY += yGridSize * SQUARE_SIZE + gapBetweenWindows + windowBorderPadding; // Increment base Y position for the next set of repetitions
        }

        // TODO: Implement data collection and analysis for each repetition
        // TODO: Implement any additional steps after each set of repetitions
        // TODO: Implement any final steps after all simulations are complete
    }

    private void storeSimulationData() {
        // Make sure the correct counts are being passed to the constructor
        SimulationData data = new SimulationData(
                Global.totalPaintDrops,
                Global.colorDrops[0], // Color 1 drops
                Global.colorDrops[1], // Color 2 drops
                Global.colorDrops[2], // Color 3 drops
                Global.maxPaintDropsOnSquare,
                Global.averagePaintDrops
        );

        simulationDataList.add(data);
    }

    private synchronized void updateGlobalCounts(int RandomColor, int row, int col) {
        Global.totalPaintDrops++;
        Global.colorDrops[RandomColor]++;
        if (paintCount[row][col] > Global.maxPaintDropsOnSquare) {
            Global.maxPaintDropsOnSquare = paintCount[row][col];
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int xDimension;
        int yDimension;
        int color1, color2, color3;
        int stopCriterion;
        boolean check;     //Hold if the input is right
        do {
            check = false;      //Initialize the input
            //Silvia updated this try-catch block 12/16/2023
            try {
                //Create a Scanner object
                Scanner input = new Scanner(System.in);

                //Prompt the user to enter a time
                System.out.println("The users need to enter six valid integers:");
                System.out.println("1.x dimension is the size (0,25] of the grid from left to right.");
                System.out.print("Please enter the x dimension: ");
                xDimension = input.nextInt();
                while (xDimension <= 0 || xDimension > 25) {
                    System.out.print("x dimension must be a positive integer <= 25: ");
                    xDimension = input.nextInt();
                }
                sixParameters.add(xDimension);

                System.out.println("\n2.y dimension is the size (0,12] of the grid from down to up.");
                System.out.print("Please enter the y dimension: ");
                yDimension = input.nextInt();

                //Check if the input is valid number
                while ( yDimension <= 0 || yDimension > 12) {
                    System.out.print("y dimension must be a positive integer <= 12: ");
                    yDimension = input.nextInt();
                }
                sixParameters.add(yDimension);

                System.out.println("\nPlease choose three distinct colors from 1.Red,2.Orange,3.Yellow,4.Green,5.Blue,6.Indigo,7.Violet,8.Black");
                System.out.println("Please choose three distinct colors based on the relative numbers as above: ");
                System.out.print("3.Pick the color1 from the above 8 choices: ");
                color1 = input.nextInt();
                while (color1 < 1 || color1 > 8) {
                    System.out.print("Please choose one of number from [1,8] for color1: ");
                    color1 = input.nextInt();
                }

                System.out.print("\n4.Pick the color2 from the above 8 choices: ");
                color2 = input.nextInt();
                while (color2 < 1 || color2 > 8) {
                    System.out.print("Please choose one of number from [1,8] for color2: ");
                    color2 = input.nextInt();
                }
                while (color1 == color2) {
                    System.out.print("Please choose a different number from color1: ");
                    color2 = input.nextInt();
                }

                System.out.print("\n5.Pick the color3 from the above 8 choices: ");
                color3 = input.nextInt();
                while (color3 < 1 || color3 > 8) {
                    System.out.print("Please choose one of number from [1,8] for color3: ");
                    color3 = input.nextInt();
                }
                while (color1 == color3 || color2 == color3) {
                    System.out.print("Please choose a different number from color1 and color2:");
                    color3 = input.nextInt();
                }
                sixParameters.add(color1);
                sixParameters.add(color2);
                sixParameters.add(color3);

                System.out.println("\nPlease pick a stopping criterion for the random painting:");
                System.out.println("1.The last unpainted square is painted for the first time.");
                System.out.println("2.The first time any square gets its second paint blob.");
                System.out.print("Please choose the stopping criterion based on the relative number as above:");
                stopCriterion = input.nextInt();

                //Check if the input is valid number
                while (stopCriterion != 1 && stopCriterion != 2) {
                    System.out.print("Please enter a valid number 1 or 2: ");
                    stopCriterion = input.nextInt();
                }
                sixParameters.add(stopCriterion);

            } catch (InputMismatchException exception) {
                System.out.println("Caught " + exception.getMessage());
            }

        } while (check);

        System.out.println(sixParameters);

        Global.xgridSize = sixParameters.get(0);
        Global.ygridSize = sixParameters.get(1);
        Global.pColor1 = sixParameters.get(2);
        Global.pColor2 = sixParameters.get(3);
        Global.pColor3 = sixParameters.get(4);
        Global.pStopCriterion = sixParameters.get(5);

        System.out.println("xgridSize = " + Global.xgridSize);
        System.out.println("ygridSize = " + Global.ygridSize);
        System.out.println("pColor1 = " + Global.pColor1);
        System.out.println("pColor2 = " + Global.pColor2);
        System.out.println("pColor3 = " + Global.pColor3);
        System.out.println("pStopCriterion = " + Global.pStopCriterion);

        //Rectangle[][] createRectangle = new Rectangle[Global.xgridSize][Global.ygridSize];
        //GridPane gridPane = new Main().createGridPane();
        //new Main().moveObjectsRandomly();

        //new Main().Paint_Once();
        launch(args);
        Scanner input = new Scanner(System.in);

        // Prompt the user to choose an experiment
        while (true) {
            System.out.println("\nPlease select an experiment to run:");
            System.out.println("1. Experiment 1: Grid Size Variation");
            System.out.println("2. Experiment 2: X-Dimension Variation");
            System.out.println("3. Experiment 3: Repetition Variation");
            System.out.print("Enter the number of the experiment you want to run (1, 2, or 3): ");

            int experimentChoice = input.nextInt();

            if (experimentChoice == 1) {
                input.nextLine(); // Consume the newline left-over
                System.out.println("Enter grid sizes for Experiment 1 (separated by space, max 20):");
                String[] sizeInputs = input.nextLine().trim().split("\\s+");
                List<Integer> gridSizes = new ArrayList<>();

                for (String sizeInput : sizeInputs) {
                    try {
                        int size = Integer.parseInt(sizeInput);
                        if (size > 0 && size <= 20) {
                            gridSizes.add(size);
                        } else {
                            System.out.println("Grid size must be between 1 and 20.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format for grid size: " + sizeInput);
                    }
                }

                System.out.println("Enter the number of repetitions for each grid size (max 10):");
                int repetitions = input.nextInt();
                repetitions = Math.min(repetitions, 10); // Ensure repetitions do not exceed 10

                // Correctly instantiate the Main class and call runExperimentOne
                //new Main().runExperimentOne(gridSizes, repetitions);

            } else if (experimentChoice == 2) {
                System.out.println("Enter the y-dimension value for the grid (1-12):");
                yDimension = input.nextInt();  // Using the already declared yDimension variable
                while (yDimension <= 0 || yDimension > 12) {
                    System.out.println("Invalid input. Please enter a y-dimension value between 1 and 12:");
                    yDimension = input.nextInt();
                }

                System.out.println("Enter x-dimension values for Experiment 2 (separated by space, max 20):");
                input.nextLine(); // Consume the newline character after the number input
                String[] xDimInputs = input.nextLine().trim().split("\\s+");
                List<Integer> xDimensions = new ArrayList<>();

                for (String xDimInput : xDimInputs) {
                    xDimension = Integer.parseInt(xDimInput); // Reuse the already declared xDimension variable
                    if (xDimension > 0 && xDimension <= 20) {
                        xDimensions.add(xDimension);
                    } else {
                        System.out.println("X-Dimension value must be between 1 and 20.");
                    }
                }

                System.out.println("Enter the number of repetitions for each x-dimension value (max 10):");
                int repetitions = input.nextInt();
                repetitions = Math.min(repetitions, 10); // Ensure repetitions do not exceed 10

                // Run Experiment 2
                Stage primaryStage = new Stage();
                new Main().runExperimentTwo(yDimension, xDimensions, repetitions, primaryStage);
            } else if (experimentChoice == 3) {
                System.out.println("Enter the constant x-dimension value for the grid (1-25):");
                xDimension = input.nextInt(); // Use the already declared variable

                while (xDimension <= 0 || xDimension > 25) {
                    System.out.println("Invalid input. Please enter an x-dimension value between 1 and 25:");
                    xDimension = input.nextInt();
                }

                System.out.println("Enter the constant y-dimension value for the grid (1-12):");
                yDimension = input.nextInt(); // Use the already declared variable

                while (yDimension <= 0 || yDimension > 12) {
                    System.out.println("Invalid input. Please enter a y-dimension value between 1 and 12:");
                    yDimension = input.nextInt();
                }

                input.nextLine(); // Consume the newline character after the number input

                System.out.println("Enter different repetition amounts for Experiment 3 (separated by space, max 10 each):");
                String[] repetitionInputs = input.nextLine().trim().split("\\s+");
                List<Integer> repetitionsList = new ArrayList<>();

                for (String repInput : repetitionInputs) {
                    int repetitions = Integer.parseInt(repInput);
                    if (repetitions > 0 && repetitions <= 10) {
                        repetitionsList.add(repetitions);
                    } else {
                        System.out.println("Repetition amount must be between 1 and 10.");
                    }
                }

                // Run Experiment 3
                new Main().runExperimentThree(xDimension, yDimension, repetitionsList);
            } else {
                System.out.println("Experiment choice not recognized.");
            }


            // Close the scanner

        }
        //input.close();
    }
}
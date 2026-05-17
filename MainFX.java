package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.TextArea;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MainFX extends Application {

    // Services
    private DictionaryWords dictionary;
    private AddWords addService;
    private SearchWords searchService;
    private UpdateWords updateService;
    private DeleteWords deleteService;
    private Report reportService;
    private SentenceService sentenceService;
    private FileHandler fileHandler;
    private TranslationService translationService;

    // GUI
    private Stage primaryStage;
    private BorderPane mainLayout;
    private TableView<WordTableData> tableView;
    private ObservableList<WordTableData> tableData;
    private Label statusLabel;
    private String currentFileName = "dictionary.txt";
    
    // TextArea declarations
    private TextArea sentencesOutputArea;
    private TextArea translateOutputArea;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        initializeServices();
        
        primaryStage.setTitle("Dictionary Management System - COMP242 Project");
        
        Scene scene = createMainScene();
        primaryStage.setScene(scene);
        primaryStage.setWidth(1100);
        primaryStage.setHeight(750);
        primaryStage.show();
    }

    private void initializeServices() {
        dictionary = new DictionaryWords();
        addService = new AddWords(dictionary);
        searchService = new SearchWords(dictionary);
        updateService = new UpdateWords(dictionary);
        deleteService = new DeleteWords(dictionary);
        reportService = new Report(dictionary);
        sentenceService = new SentenceService(dictionary);
        fileHandler = new FileHandler();
        translationService = new TranslationService(dictionary);
        tableData = FXCollections.observableArrayList();
    }

    // ==================== MAIN SCENE ====================
    private Scene createMainScene() {
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Top Menu
        HBox menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Center - Default view
        mainLayout.setCenter(createDashboard());

        // Bottom Status Bar
        HBox statusBar = createStatusBar();
        mainLayout.setBottom(statusBar);

        return new Scene(mainLayout);
    }

    // ==================== MENU BAR ====================
    private HBox createMenuBar() {
        HBox menuBar = new HBox(10);
        menuBar.setPadding(new Insets(10));
        menuBar.setStyle("-fx-background-color: #2c3e50;");
        menuBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Dictionary System");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.WHITE);

        Button dashboardBtn = createMenuButton("Dashboard");
        Button wordsBtn = createMenuButton("Words");
        Button translateBtn = createMenuButton("Translate");
        Button sentencesBtn = createMenuButton("Sentences");
        Button reportsBtn = createMenuButton("Reports");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button loadBtn = createMenuButton("Load");
        Button saveBtn = createMenuButton("Save");

        // Actions
        dashboardBtn.setOnAction(e -> mainLayout.setCenter(createDashboard()));
        wordsBtn.setOnAction(e -> mainLayout.setCenter(createWordsPanel()));
        translateBtn.setOnAction(e -> mainLayout.setCenter(createTranslatePanel()));
        sentencesBtn.setOnAction(e -> mainLayout.setCenter(createSentencesPanel()));
        reportsBtn.setOnAction(e -> mainLayout.setCenter(createReportsPanel()));
        loadBtn.setOnAction(e -> loadDictionaryFile());
        saveBtn.setOnAction(e -> saveDictionaryFile());

        menuBar.getChildren().addAll(title, dashboardBtn, wordsBtn, translateBtn, 
                                     sentencesBtn, reportsBtn, spacer, loadBtn, saveBtn);
        return menuBar;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(100);
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-background-radius: 5;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5;");
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5;");
        });
        
        return btn;
    }

    // ==================== STATUS BAR ====================
    private HBox createStatusBar() {
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #34495e;");
        statusBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("Ready");
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setFont(Font.font("Arial", 12));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, spacer);
        return statusBar;
    }

    // ==================== DASHBOARD ====================
    private VBox createDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        dashboard.setStyle("-fx-background-color: #f0f0f0;");
        dashboard.setAlignment(Pos.TOP_CENTER);

        Label welcome = new Label("Dictionary Management System");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcome.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("COMP242 Project - English/Arabic Dictionary");
        subtitle.setFont(Font.font("Arial", 16));
        subtitle.setTextFill(Color.GRAY);

        // Stats Cards
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setPadding(new Insets(20, 0, 20, 0));

        VBox totalCard = createSimpleCard("Total Words", "0", "#3498db");
        VBox nounsCard = createSimpleCard("Nouns", "0", "#e74c3c");
        VBox verbsCard = createSimpleCard("Verbs", "0", "#2ecc71");
        VBox adjCard = createSimpleCard("Adjectives", "0", "#f39c12");

        statsGrid.add(totalCard, 0, 0);
        statsGrid.add(nounsCard, 1, 0);
        statsGrid.add(verbsCard, 0, 1);
        statsGrid.add(adjCard, 1, 1);

        // Quick Actions
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER);
        actionsBox.setPadding(new Insets(20, 0, 0, 0));

        Label actionsLabel = new Label("Quick Actions");
        actionsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        actionsLabel.setTextFill(Color.web("#2c3e50"));

        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);

        Button addBtn = createActionButton("Add Word");
        Button searchBtn = createActionButton("Search");
        Button translateBtn = createActionButton("Translate");
        Button generateBtn = createActionButton("Generate Sentence");

        addBtn.setOnAction(e -> mainLayout.setCenter(createWordsPanel()));
        searchBtn.setOnAction(e -> mainLayout.setCenter(createWordsPanel()));
        translateBtn.setOnAction(e -> mainLayout.setCenter(createTranslatePanel()));
        generateBtn.setOnAction(e -> mainLayout.setCenter(createSentencesPanel()));

        actionButtons.getChildren().addAll(addBtn, searchBtn, translateBtn, generateBtn);
        actionsBox.getChildren().addAll(actionsLabel, actionButtons);

        dashboard.getChildren().addAll(welcome, subtitle, statsGrid, actionsBox);
        updateDashboardStats(totalCard, nounsCard, verbsCard, adjCard);
        
        return dashboard;
    }

    private VBox createSimpleCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPrefSize(150, 100);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                     "-fx-border-color: " + color + "; -fx-border-width: 2; -fx-border-radius: 10; " +
                     "-fx-padding: 10;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(color));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.DARKGRAY);

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(150);
        btn.setPrefHeight(40);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                    "-fx-background-radius: 5;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                        "-fx-background-radius: 5;");
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-background-radius: 5;");
        });
        
        return btn;
    }

    private void updateDashboardStats(VBox totalCard, VBox nounsCard, VBox verbsCard, VBox adjCard) {
        int total = 0;
        int nouns = 0;
        int verbs = 0;
        int adj = 0;

        LetterNode cur = dictionary.getHead();
        while (cur != null) {
            total += cur.getTree().countWordsInTree();
            nouns += countTypeInTree(cur.getTree().getRoot(), "noun");
            verbs += countTypeInTree(cur.getTree().getRoot(), "verb");
            adj += countTypeInTree(cur.getTree().getRoot(), "adjective");
            cur = cur.getNext();
        }

        Label val1 = (Label) totalCard.getChildren().get(0);
        Label val2 = (Label) nounsCard.getChildren().get(0);
        Label val3 = (Label) verbsCard.getChildren().get(0);
        Label val4 = (Label) adjCard.getChildren().get(0);

        val1.setText(String.valueOf(total));
        val2.setText(String.valueOf(nouns));
        val3.setText(String.valueOf(verbs));
        val4.setText(String.valueOf(adj));
    }

    private int countTypeInTree(AVLTreeNode node, String type) {
        if (node == null) return 0;
        WordEA word = (WordEA) node.getElement();
        int count = word.getType().equalsIgnoreCase(type) ? 1 : 0;
        return count + countTypeInTree(node.getLeft(), type) + countTypeInTree(node.getRight(), type);
    }

    // ==================== WORDS PANEL ====================
    private BorderPane createWordsPanel() {
        BorderPane panel = new BorderPane();
        panel.setStyle("-fx-background-color: #f0f0f0;");

        // Top: Form
        VBox formArea = new VBox(15);
        formArea.setPadding(new Insets(20));
        formArea.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label formTitle = new Label("Word Management");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#2c3e50"));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 0, 0));

        TextField wordField = createFormField("Word");
        TextField engField = createFormField("English Meaning");
        TextField arbField = createFormField("Arabic Meaning");
        TextField exField = createFormField("Example");
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Noun", "Verb", "Adjective", "Adverb", "Pronoun", "Preposition", "Conjunction");
        typeCombo.setPromptText("Word Type");
        typeCombo.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
        typeCombo.setPrefWidth(200);

        form.add(new Label("Word:"), 0, 0);
        form.add(wordField, 1, 0);
        form.add(new Label("English:"), 2, 0);
        form.add(engField, 3, 0);
        
        form.add(new Label("Arabic:"), 0, 1);
        form.add(arbField, 1, 1);
        form.add(new Label("Example:"), 2, 1);
        form.add(exField, 3, 1);
        
        form.add(new Label("Type:"), 0, 2);
        form.add(typeCombo, 1, 2);

        // Action Buttons
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        Button addBtn = createFormButton("Add", "#2ecc71");
        Button updateBtn = createFormButton("Update", "#3498db");
        Button deleteBtn = createFormButton("Delete", "#e74c3c");
        Button clearBtn = createFormButton("Clear", "#95a5a6");

        buttonBar.getChildren().addAll(addBtn, updateBtn, deleteBtn, clearBtn);

        // Search Section
        VBox searchBox = new VBox(10);
        searchBox.setPadding(new Insets(15, 0, 0, 0));
        
        Label searchLabel = new Label("Search Word");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        searchLabel.setTextFill(Color.web("#2c3e50"));

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter word to search...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
        
        Button searchEngBtn = createFormButton("English Search", "#f39c12");
        Button searchArbBtn = createFormButton("Arabic Search", "#9b59b6");

        searchBar.getChildren().addAll(searchField, searchEngBtn, searchArbBtn);

        formArea.getChildren().addAll(formTitle, form, buttonBar, new Separator(), searchLabel, searchBar);

        // Center: Table
        tableView = createTable();
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(20));
        
        Label tableTitle = new Label("Dictionary Entries");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setTextFill(Color.web("#2c3e50"));
        
        tableBox.getChildren().addAll(tableTitle, tableView);

        panel.setTop(formArea);
        panel.setCenter(tableBox);

        // Button Actions
        addBtn.setOnAction(e -> handleAddWord(wordField, engField, arbField, exField, typeCombo));
        updateBtn.setOnAction(e -> handleUpdateWord(wordField, engField, arbField, exField, typeCombo));
        deleteBtn.setOnAction(e -> handleDeleteWord(wordField));
        clearBtn.setOnAction(e -> clearFormFields(wordField, engField, arbField, exField, typeCombo));
        searchEngBtn.setOnAction(e -> handleSearchEng(searchField, wordField, engField, arbField, exField, typeCombo));
        searchArbBtn.setOnAction(e -> handleSearchArb(searchField, wordField, engField, arbField, exField, typeCombo));

        return panel;
    }

    private TextField createFormField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
        field.setPrefWidth(200);
        return field;
    }

    private Button createFormButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(100);
        btn.setPrefHeight(35);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-background-radius: 5;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: derive(" + color + ", -20%); " +
                        "-fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 5;");
        });
        
        return btn;
    }

    private TableView<WordTableData> createTable() {
        TableView<WordTableData> table = new TableView<>();
        table.setItems(tableData);
        table.setStyle("-fx-background-color: white;");

        TableColumn<WordTableData, String> wordCol = new TableColumn<>("Word");
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        wordCol.setPrefWidth(120);

        TableColumn<WordTableData, String> engCol = new TableColumn<>("English");
        engCol.setCellValueFactory(new PropertyValueFactory<>("englishMeaning"));
        engCol.setPrefWidth(180);

        TableColumn<WordTableData, String> arbCol = new TableColumn<>("Arabic");
        arbCol.setCellValueFactory(new PropertyValueFactory<>("arabicMeaning"));
        arbCol.setPrefWidth(150);

        TableColumn<WordTableData, String> exCol = new TableColumn<>("Example");
        exCol.setCellValueFactory(new PropertyValueFactory<>("example"));
        exCol.setPrefWidth(200);

        TableColumn<WordTableData, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(100);

        table.getColumns().addAll(wordCol, engCol, arbCol, exCol, typeCol);
        return table;
    }

    // ==================== TRANSLATE PANEL ====================
    private VBox createTranslatePanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Text Translation");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        HBox mainBox = new HBox(20);
        mainBox.setAlignment(Pos.CENTER);

        // Left: Input
        VBox leftBox = new VBox(10);
        leftBox.setPrefWidth(400);
        
        Label inputLabel = new Label("Input Text:");
        inputLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea inputArea = new TextArea();
        inputArea.setPromptText("Enter text here...");
        inputArea.setPrefHeight(250);
        inputArea.setWrapText(true);
        inputArea.setStyle("-fx-control-inner-background: white; -fx-border-color: #ccc;");

        Button loadFileBtn = createFormButton("Load File", "#3498db");
        loadFileBtn.setPrefWidth(120);
        loadFileBtn.setOnAction(e -> loadTextFile(inputArea));

        leftBox.getChildren().addAll(inputLabel, inputArea, loadFileBtn);

        // Center: Arrows
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPrefWidth(100);

        Button toArabicBtn = new Button("→ Arabic");
        toArabicBtn.setPrefWidth(100);
        toArabicBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button toEnglishBtn = new Button("← English");
        toEnglishBtn.setPrefWidth(100);
        toEnglishBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");

        centerBox.getChildren().addAll(toArabicBtn, toEnglishBtn);

        // Right: Output
        VBox rightBox = new VBox(10);
        rightBox.setPrefWidth(400);
        
        Label outputLabel = new Label("Translation:");
        outputLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        translateOutputArea = new TextArea();
        translateOutputArea.setEditable(false);
        translateOutputArea.setPrefHeight(250);
        translateOutputArea.setWrapText(true);
        translateOutputArea.setStyle("-fx-control-inner-background: #f9f9f9; -fx-border-color: #ccc;");

        Button saveFileBtn = createFormButton("Save", "#e74c3c");
        saveFileBtn.setPrefWidth(120);
        saveFileBtn.setOnAction(e -> saveTextToFile(translateOutputArea.getText(), "translation.txt"));

        rightBox.getChildren().addAll(outputLabel, translateOutputArea, saveFileBtn);

        mainBox.getChildren().addAll(leftBox, centerBox, rightBox);

        // Button Actions
        toArabicBtn.setOnAction(e -> {
            String result = translationService.translateToArabic(inputArea.getText());
            translateOutputArea.setText(result);
            updateStatus("Translated to Arabic");
        });

        toEnglishBtn.setOnAction(e -> {
            String result = translationService.translateToEnglish(inputArea.getText());
            translateOutputArea.setText(result);
            updateStatus("Translated to English");
        });

        panel.getChildren().addAll(title, mainBox);
        return panel;
    }

    // ==================== SENTENCES PANEL ====================
    private VBox createSentencesPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #f0f0f0;");
        panel.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Random Sentence Generator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        HBox controlBox = new HBox(15);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(10, 0, 10, 0));

        Label countLabel = new Label("Number of sentences:");
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Spinner<Integer> countSpinner = new Spinner<>(1, 100, 5);
        countSpinner.setPrefWidth(80);

        Button genEngBtn = createFormButton("Generate English", "#3498db");
        genEngBtn.setPrefWidth(150);

        Button genArbBtn = createFormButton("Generate Arabic", "#2ecc71");
        genArbBtn.setPrefWidth(150);

        controlBox.getChildren().addAll(countLabel, countSpinner, genEngBtn, genArbBtn);

        // Output area
        sentencesOutputArea = new TextArea();
        sentencesOutputArea.setEditable(false);
        sentencesOutputArea.setPrefHeight(300);
        sentencesOutputArea.setWrapText(true);
        sentencesOutputArea.setStyle("-fx-control-inner-background: white; -fx-border-color: #ccc;");

        Button saveBtn = createFormButton("Save Sentences", "#e74c3c");
        saveBtn.setPrefWidth(150);
        saveBtn.setOnAction(e -> saveTextToFile(sentencesOutputArea.getText(), "sentences.txt"));

        HBox saveBox = new HBox(saveBtn);
        saveBox.setAlignment(Pos.CENTER);

        // Button Actions
        genEngBtn.setOnAction(e -> {
            int count = countSpinner.getValue();
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= count; i++) {
                result.append(i).append(". ").append(sentenceService.generateEnglish()).append("\n");
            }
            sentencesOutputArea.setText(result.toString());
            updateStatus("Generated " + count + " English sentences");
        });

        genArbBtn.setOnAction(e -> {
            int count = countSpinner.getValue();
            StringBuilder result = new StringBuilder();
            for (int i = 1; i <= count; i++) {
                result.append(i).append(". ").append(sentenceService.generateArabic()).append("\n");
            }
            sentencesOutputArea.setText(result.toString());
            updateStatus("Generated " + count + " Arabic sentences");
        });

        panel.getChildren().addAll(title, controlBox, sentencesOutputArea, saveBox);
        return panel;
    }

    // ==================== REPORTS PANEL ====================
    private VBox createReportsPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Dictionary Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        FlowPane buttonPanel = new FlowPane(10, 10);
        buttonPanel.setAlignment(Pos.CENTER);

        Button letterCountBtn = createReportButton("Words per Letter");
        Button typeCountBtn = createReportButton("Words by Type");
        Button letterWordsBtn = createReportButton("Words for Letter");
        Button allWordsBtn = createReportButton("All Words A-Z");
        Button heightsBtn = createReportButton("Tree Heights");

        buttonPanel.getChildren().addAll(letterCountBtn, typeCountBtn, letterWordsBtn, 
                                         allWordsBtn, heightsBtn);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(400);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-control-inner-background: white; -fx-border-color: #ccc;");

        // Button Actions
        letterCountBtn.setOnAction(e -> {
            outputArea.setText(reportService.countPerLetter());
            updateStatus("Words per letter report generated");
        });

        typeCountBtn.setOnAction(e -> {
            outputArea.setText(reportService.countWordTypes());
            updateStatus("Word types report generated");
        });

        letterWordsBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("A");
            dialog.setTitle("Words for Letter");
            dialog.setHeaderText("Enter a letter (A-Z):");
            dialog.setContentText("Letter:");
            
            dialog.showAndWait().ifPresent(letter -> {
                if (!letter.isEmpty()) {
                    outputArea.setText(reportService.wordsForLetter(letter.charAt(0)));
                    updateStatus("Words for letter " + letter.toUpperCase());
                }
            });
        });

        allWordsBtn.setOnAction(e -> {
            StringBuilder result = new StringBuilder("=== ALL WORDS (A-Z) ===\n\n");
            LetterNode cur = dictionary.getHead();
            while (cur != null) {
                printTreeInOrder(cur.getTree().getRoot(), result);
                cur = cur.getNext();
            }
            outputArea.setText(result.toString());
            updateStatus("All words displayed");
        });

        heightsBtn.setOnAction(e -> {
            StringBuilder result = new StringBuilder("=== AVL TREE HEIGHTS ===\n\n");
            LetterNode cur = dictionary.getHead();
            while (cur != null) {
                if (cur.getTree().getRoot() != null) {
                    result.append("Letter ").append(cur.getLetter()).append(": Height = ")
                          .append(cur.getTree().getRoot().getHeight()).append("\n");
                }
                cur = cur.getNext();
            }
            outputArea.setText(result.toString());
            updateStatus("Tree heights displayed");
        });

        panel.getChildren().addAll(title, buttonPanel, outputArea);
        return panel;
    }

    private Button createReportButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setPrefHeight(40);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                    "-fx-background-radius: 5;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                        "-fx-background-radius: 5;");
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-background-radius: 5;");
        });
        
        return btn;
    }

    // ==================== EVENT HANDLERS ====================
    private void handleAddWord(TextField wordField, TextField engField, TextField arbField,
                               TextField exField, ComboBox<String> typeCombo) {
        if (wordField.getText().isEmpty() || engField.getText().isEmpty() ||
            arbField.getText().isEmpty() || exField.getText().isEmpty() ||
            typeCombo.getValue() == null) {
            showAlert("Error", "Please fill all fields!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = addService.add(wordField.getText(), engField.getText(),
                                        arbField.getText(), exField.getText(),
                                        typeCombo.getValue());
        if (success) {
            showAlert("Success", "Word added successfully!", Alert.AlertType.INFORMATION);
            addToTable(wordField.getText(), engField.getText(), arbField.getText(),
                      exField.getText(), typeCombo.getValue());
            autoSave();
            updateStatus("Word added: " + wordField.getText());
        } else {
            showAlert("Error", "Word already exists!", Alert.AlertType.ERROR);
        }
    }

    private void handleUpdateWord(TextField wordField, TextField engField, TextField arbField,
                                  TextField exField, ComboBox<String> typeCombo) {
        if (wordField.getText().isEmpty()) {
            showAlert("Error", "Enter word to update!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = updateService.update(wordField.getText(), engField.getText(),
                                              arbField.getText(), exField.getText(),
                                              typeCombo.getValue());
        if (success) {
            showAlert("Success", "Word updated!", Alert.AlertType.INFORMATION);
            refreshTable();
            autoSave();
            updateStatus("Word updated: " + wordField.getText());
        } else {
            showAlert("Error", "Word not found!", Alert.AlertType.ERROR);
        }
    }

    private void handleDeleteWord(TextField wordField) {
        if (wordField.getText().isEmpty()) {
            showAlert("Error", "Enter word to delete!", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Word?");
        confirm.setContentText("Are you sure you want to delete: " + wordField.getText());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            boolean success = deleteService.delete(wordField.getText());
            if (success) {
                showAlert("Success", "Word deleted!", Alert.AlertType.INFORMATION);
                refreshTable();
                autoSave();
                updateStatus("Word deleted: " + wordField.getText());
            } else {
                showAlert("Error", "Word not found!", Alert.AlertType.ERROR);
            }
        }
    }

    private void handleSearchEng(TextField searchField, TextField wordField, TextField engField,
                                 TextField arbField, TextField exField, ComboBox<String> typeCombo) {
        WordEA word = searchService.searchEnglish(searchField.getText());
        if (word != null) {
            wordField.setText(word.getWord());
            engField.setText(word.getEngMeaning());
            arbField.setText(word.getArMeaning());
            exField.setText(word.getExample());
            typeCombo.setValue(word.getType());
            updateStatus("Word found: " + word.getWord());
        } else {
            showAlert("Not Found", "Word not found!", Alert.AlertType.WARNING);
        }
    }

    private void handleSearchArb(TextField searchField, TextField wordField, TextField engField,
                                 TextField arbField, TextField exField, ComboBox<String> typeCombo) {
        WordEA word = searchService.searchArabic(searchField.getText());
        if (word != null) {
            wordField.setText(word.getWord());
            engField.setText(word.getEngMeaning());
            arbField.setText(word.getArMeaning());
            exField.setText(word.getExample());
            typeCombo.setValue(word.getType());
            updateStatus("Word found: " + word.getWord());
        } else {
            showAlert("Not Found", "الكلمة غير موجودة!", Alert.AlertType.WARNING);
        }
    }

    // ==================== UTILITY METHODS ====================
    private void clearFormFields(TextField wordField, TextField engField, TextField arbField,
                                TextField exField, ComboBox<String> typeCombo) {
        wordField.clear();
        engField.clear();
        arbField.clear();
        exField.clear();
        typeCombo.setValue(null);
    }

    private void addToTable(String word, String eng, String arb, String ex, String type) {
        tableData.add(new WordTableData(word, eng, arb, ex, type));
    }

    private void refreshTable() {
        tableData.clear();
        LetterNode cur = dictionary.getHead();
        while (cur != null) {
            collectWordsForTable(cur.getTree().getRoot());
            cur = cur.getNext();
        }
    }

    private void collectWordsForTable(AVLTreeNode node) {
        if (node == null) return;
        collectWordsForTable(node.getLeft());
        WordEA word = (WordEA) node.getElement();
        addToTable(word.getWord(), word.getEngMeaning(), word.getArMeaning(),
                  word.getExample(), word.getType());
        collectWordsForTable(node.getRight());
    }

    private void printTreeInOrder(AVLTreeNode node, StringBuilder sb) {
        if (node == null) return;
        printTreeInOrder(node.getLeft(), sb);
        WordEA word = (WordEA) node.getElement();
        sb.append(word.getWord()).append(" - ")
          .append(word.getEngMeaning()).append(" - ")
          .append(word.getArMeaning()).append("\n");
        printTreeInOrder(node.getRight(), sb);
    }

    private boolean loadDictionaryFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Dictionary File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                currentFileName = file.getAbsolutePath();
                int count = fileHandler.loadFromFile(currentFileName, dictionary);
                showAlert("Success", count + " words loaded!", Alert.AlertType.INFORMATION);
                refreshTable();
                updateStatus("Loaded " + count + " words from " + file.getName());
                return true;
            } catch (IOException e) {
                showAlert("Error", "Failed to load file!", Alert.AlertType.ERROR);
            }
        }
        return false;
    }

    private void saveDictionaryFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("dictionary.txt");
        
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                fileHandler.saveToFile(file.getAbsolutePath(), dictionary);
                currentFileName = file.getAbsolutePath();
                showAlert("Success", "Dictionary saved!", Alert.AlertType.INFORMATION);
                updateStatus("Dictionary saved to " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to save!", Alert.AlertType.ERROR);
            }
        }
    }

    private void autoSave() {
        try {
            fileHandler.saveToFile(currentFileName, dictionary);
        } catch (IOException e) {
            System.err.println("Auto-save failed: " + e.getMessage());
        }
    }

    private void loadTextFile(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                updateStatus("Text file loaded");
            } catch (IOException e) {
                showAlert("Error", "Failed to load file!", Alert.AlertType.ERROR);
            }
        }
    }

    private void saveTextToFile(String content, String defaultName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName(defaultName);
        
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write(content);
                showAlert("Success", "File saved!", Alert.AlertType.INFORMATION);
                updateStatus("File saved: " + file.getName());
            } catch (IOException e) {
                showAlert("Error", "Failed to save!", Alert.AlertType.ERROR);
            }
        }
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ==================== INNER CLASS ====================
    public static class WordTableData {
        private String word;
        private String englishMeaning;
        private String arabicMeaning;
        private String example;
        private String type;

        public WordTableData(String word, String englishMeaning, String arabicMeaning,
                           String example, String type) {
            this.word = word;
            this.englishMeaning = englishMeaning;
            this.arabicMeaning = arabicMeaning;
            this.example = example;
            this.type = type;
        }

        public String getWord() { return word; }
        public String getEnglishMeaning() { return englishMeaning; }
        public String getArabicMeaning() { return arabicMeaning; }
        public String getExample() { return example; }
        public String getType() { return type; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
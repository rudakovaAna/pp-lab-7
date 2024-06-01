import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

    private TextField directoryPathField; //pole txt do wprowadzenia ścieżki katalogu
    private TextField searchField; //pole txt do wprowadzenia frazy wyszukiwania
    private TextArea resultArea; // nowa prywatna zmienna typu TextArea do wyświetlania wyników

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Browser and Search");

        directoryPathField = new TextField();
        directoryPathField.setPromptText("Enter directory path");

        searchField = new TextField();
        searchField.setPromptText("Enter search phrase");

        resultArea = new TextArea();
        resultArea.setPrefHeight(400);

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> {
            System.out.println("Browse button clicked");
            browseDirectory();
        });

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            System.out.println("Search button clicked");
            searchFiles();
        });

        HBox hBox = new HBox(10, directoryPathField, browseButton);
        VBox vBox = new VBox(10, hBox, searchField, searchButton, resultArea);

        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            System.out.println("Selected directory: " + selectedDirectory.getAbsolutePath());
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }
    private void searchFiles() {
        String directoryPath = directoryPathField.getText();
        if (directoryPath.isEmpty()) { 
            resultArea.setText("Please provide a directory path.");
            return;
        }

        String searchPhrase = searchField.getText(); 
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) { //ckeck zy podana ścieżka jest katalogiem
            resultArea.setText("The provided path is not a directory.");
            return;
        }

        StringBuilder results = new StringBuilder(); //utworzenie instancji StringBuilder do przechowywania wyników
        searchInDirectory(directory, searchPhrase, results);
        resultArea.setText(results.toString()); 
    }

    
    private void searchInDirectory(File directory, String searchPhrase, StringBuilder results) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && containsPhrase(file, searchPhrase)) {
                    results.append(file.getAbsolutePath()).append("\n");
                } else if (file.isDirectory()) {
                    searchInDirectory(file, searchPhrase, results); 
                }
            }
        }
    }

    //metoda sprawdzająca, czy plik zawiera wyszukiwaną frazę
    private boolean containsPhrase(File file, String searchPhrase) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchPhrase)) {
                    return true; //true, jeśli linia zawiera wyszukiwaną frazę
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; //blad-zwróć false
        }
        return false; 
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package it.unicas.project.template.address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import it.unicas.project.template.address.model.Colleghi;
import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import it.unicas.project.template.address.model.dao.xml.ColleghiListWrapper;
import it.unicas.project.template.address.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Colleghis.
     */
    private ObservableList<Colleghi> colleghiData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        //colleghiData.add(new Colleghi("Mario", "Rossi", "0824981", "molinara@uni.it", "21.10.2017", null));
        //colleghiData.add(new Colleghi("Carlo", "Ciampi", "0824982", "ciampi@uni.it", "22.02.2017", null));
        //colleghiData.add(new Colleghi("Ornella", "Vaniglia", "0824983", "vaniglia@uni.it", "23.05.2017", null));
        //colleghiData.add(new Colleghi("Cornelia", "Crudelia", "0824984", "crudelia@uni.it", "24.05.2017", null));
        //colleghiData.add(new Colleghi("Franco", "Bertolucci", "0824985", "bertolucci@uni.it", "25.10.2017", null));
        //colleghiData.add(new Colleghi("Carmine", "Labagnara", "0824986", "lagbagnara@uni.it", "26.10.2017", null));
        //colleghiData.add(new Colleghi("Mauro", "Cresta", "0824987", "cresta@uni.it", "27.12.2017", null));
        //colleghiData.add(new Colleghi("Andrea", "Coluccio", "0824988", "coluccio@uni.it", "28.01.2017", null));
    }

    /**
     * Returns the data as an observable list of Colleghis.
     * @return
     */
    public ObservableList<Colleghi> getColleghiData() {
        return colleghiData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Colleghi app");

        //this.primaryStage.show();

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));

        initRootLayout();
        //this.primaryStage.show();

        showColleghiOverview();

        this.primaryStage.show();

    }

    /**
     * Initializes the root layout and tries to load the last opened
     * Colleghi file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows the Colleghi overview inside the root layout.
     */
    public void showColleghiOverview() {
        try {
            // Load Colleghi overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ColleghiOverview.fxml"));
            AnchorPane ColleghiOverview = (AnchorPane) loader.load();

            // Set Colleghi overview into the center of root layout.
            rootLayout.setCenter(ColleghiOverview);

            // Give the controller access to the main app.
            ColleghiOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showSettingsEditDialog(DAOMySQLSettings daoMySQLSettings){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SettingsEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("DAO settings");
            dialogStage.initModality((Modality.WINDOW_MODAL));
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);



            // Set the colleghi into the controller.
            SettingsEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSettings(daoMySQLSettings);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();


            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Opens a dialog to edit details for the specified colleghi. If the user
     * clicks OK, the changes are saved into the provided colleghi object and true
     * is returned.
     *
     * @param colleghi the colleghi object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showColleghiEditDialog(Colleghi colleghi, boolean verifyLen) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ColleghiEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Colleghi");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the colleghi into the controller.
            ColleghiEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage, verifyLen);
            controller.setColleghi(colleghi);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the Colleghis into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setColleghiData(colleghiData);

            // Set the dialog icon.
            dialogStage.getIcons().add(new Image("file:resources/images/calendar.png"));

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Colleghi file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getColleghiFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setColleghiFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    public void loadColleghiDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ColleghiListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ColleghiListWrapper wrapper = (ColleghiListWrapper) um.unmarshal(file);

            colleghiData.clear();
            colleghiData.addAll(wrapper.getColleghis());

            // Save the file path to the registry.
            setColleghiFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    public void saveColleghiDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(ColleghiListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our Colleghi data.
            ColleghiListWrapper wrapper = new ColleghiListWrapper();
            wrapper.setColleghis(colleghiData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setColleghiFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
    public void start(Stage primaryStage) throws Exception {
       primaryStage.setTitle("IMat");
       Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
       Scene scene = new Scene(myPane);
       primaryStage.setScene(scene);
       primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}

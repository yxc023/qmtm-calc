package com.yangxiaochen.app.calc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangxiaochen on 15/12/24.
 */
public class Main extends Application {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        try {
            launch(args);
        } catch (Exception e) {
            log.error(e.getMessage(),e);

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/main.fxml"));
        primaryStage.setTitle("计算");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
}

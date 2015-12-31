package com.yangxiaochen.app.calc.gui.controller;

import com.jfoenix.controls.*;
import com.yangxiaochen.app.calc.calc.Calculator;
import com.yangxiaochen.app.calc.calc.vo.Param;
import com.yangxiaochen.app.calc.calc.vo.Result;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import org.apache.commons.lang3.math.NumberUtils;
import org.datafx.controller.FXMLController;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yangxiaochen on 15/12/27.
 */
@FXMLController(value = "views/main.fxml")
public class MainController implements Initializable {

    @FXML
    JFXRadioButton radioPCIAMA;
    @FXML
    JFXRadioButton radioPCI2014;
    @FXML
    JFXRadioButton radioPCI2015;

    @FXML
    JFXTextField textFieldA;
    @FXML
    JFXTextField textFieldQ;
    @FXML
    JFXTextField textFieldZ;
    @FXML
    JFXTextField textFieldG;
    @FXML
    JFXTextField textFieldX;

    @FXML
    Text msgA;
    @FXML
    Text msgQ;
    @FXML
    Text msgZ;
    @FXML
    Text msgG;
    @FXML
    Text msgX;

    @FXML
    JFXTextField textFieldR1;
    @FXML
    JFXTextField textFieldR2;
    @FXML
    JFXTextField textFieldR3;

    @FXML
    JFXSpinner spinner;
    @FXML
    JFXButton submitButton;


    private HashMap<String, JFXTextField> textFieldHashMap;
    private HashMap<String, Text> msgTextMap;
    private HashMap<String, String> infoMsgMap;

    private String model;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    AtomicBoolean isCalculating = new AtomicBoolean(false);

    @FXML
    public void submit() {

        boolean isValid = true;


        for (String shortId : textFieldHashMap.keySet()) {
            String text = textFieldHashMap.get(shortId).getText();
            if (!NumberUtils.isNumber(text)) {
                msgTextMap.get(shortId).setText("数据格式错误");
                msgTextMap.get(shortId).setVisible(true);
                isValid = false;
            }
        }

        if (!isValid) {
            return;
        }
        if (isCalculating.get()) {
            return;
        }
        submitButton.setVisible(false);
        spinner.setVisible(true);
        isCalculating.set(true);

        Param param = new Param();
        param.a = Double.parseDouble(textFieldA.getText());
        param.q = Double.parseDouble(textFieldQ.getText());
        param.z = Double.parseDouble(textFieldZ.getText());
        param.g = Double.parseDouble(textFieldG.getText());
        param.x = Double.parseDouble(textFieldX.getText());


        Calculator calculator = new Calculator();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Result result = calculator.calc(param,model);
                    textFieldR1.setText(String.valueOf(result.r1));
                    textFieldR2.setText(String.valueOf(result.r2));
                    textFieldR3.setText(String.valueOf(result.r3));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isCalculating.set(false);
                    spinner.setVisible(false);
                    submitButton.setVisible(true);
                }

            }
        });
        isCalculating.set(false);

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final ToggleGroup group = new ToggleGroup();

        radioPCIAMA.setToggleGroup(group);
        radioPCIAMA.setUserData("AMA");
        this.model = (String) radioPCIAMA.getUserData();
        radioPCI2014.setToggleGroup(group);
        radioPCI2014.setUserData("PCI2014");
        radioPCI2015.setToggleGroup(group);
        radioPCI2015.setUserData("PCI2015");
        radioPCIAMA.setSelected(true);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                MainController.this.model = (String) newValue.getUserData();
            }
        });

        textFieldHashMap = new HashMap<>();
        textFieldHashMap.put("A", textFieldA);
        textFieldHashMap.put("Q", textFieldQ);
        textFieldHashMap.put("Z", textFieldZ);
        textFieldHashMap.put("G", textFieldG);
        textFieldHashMap.put("X", textFieldX);

        msgTextMap = new HashMap<>();
        msgTextMap.put("A", msgA);
        msgTextMap.put("Q", msgQ);
        msgTextMap.put("Z", msgZ);
        msgTextMap.put("G", msgG);
        msgTextMap.put("X", msgX);

        infoMsgMap = new HashMap<>();
        infoMsgMap.put("A", "(10-6A/cm2)");
        infoMsgMap.put("Q", "(mm)");
        infoMsgMap.put("Z", "(干燥条件请输入：0.0001)");
        infoMsgMap.put("G", "(单位: 1)");
        infoMsgMap.put("X", "(%)");

        for (String shortId : textFieldHashMap.keySet()) {
            textFieldHashMap.get(shortId).focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        msgTextMap.get(shortId).setText(infoMsgMap.get(shortId));
                        msgTextMap.get(shortId).setVisible(true);
                    } else {
                        msgTextMap.get(shortId).setVisible(false);
                    }
                }
            });

        }

    }

}

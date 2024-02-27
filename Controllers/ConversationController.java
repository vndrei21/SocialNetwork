package com.example.lab8.Controllers;

import com.example.lab8.domain.Message;
import com.example.lab8.observer.Observer;
import com.example.lab8.service.Service;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class ConversationController implements Observer {

    @FXML
    VBox vBox;
    @FXML
    Button save;
    @FXML
    Label nume;
    @FXML
    TextField textField;
    Long user1;
    Long user2;
    private Service service;

    @FXML
    private void initialize() {
    }

    public void Conversation(Service service,Long user1,Long user2)
    {
        this.service = service;
       // service.addObserver(this);
        this.user1 = user1;
        this.user2 =user2;
        this.nume.setText(service.find_one(user2).getFirstName()+" "+ service.find_one(user2).getLastName());
        init();
    }

    private void init()
    {
        vBox.getChildren().clear();
        var list = service.conversation(user1,user2);
        list.forEach(message->{
            HBox hBox= new HBox();
            if(message.getLeft().getId() == user1) {
                hBox.setAlignment(Pos.CENTER_RIGHT);
            }else {
                hBox.setAlignment(Pos.CENTER_LEFT);
            }hBox.setPadding(new Insets(10,10,10,10));
            Text text= new Text(message.getRight());
            text.setStyle("-fx-text-fill: red;" +
                    "-fx-border-color: yellow;");
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #353a56;"+
                    "-fx-background-radius:20px;"+
                    "-fx-cursor: hand");
            textFlow.setPadding(new Insets(10,10,10,10));
            textFlow.setTextAlignment(TextAlignment.RIGHT);
            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);
        });
    }

    @FXML
    private void handle_save() {
        if (!textField.getText().isEmpty()) {
            var msg = textField.getText();
            textField.clear();
            List<String> to = new ArrayList<>();
            to.add(user2.toString());
            service.save_message(user1, to, msg);
            init();
        }
    }
    @Override
    public void update() {
        init();
    }
}

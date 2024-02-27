package com.example.lab8.Controllers;

import com.example.lab8.domain.RequestStatus;
import com.example.lab8.observer.Observer;
import com.example.lab8.service.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RequestController implements Observer {

    private Service service;
    private Long to;

    @FXML
    private VBox request_list;

    public void Set_Service(Service service,Long id)
    {
        this.service = service;
        //this.service.addObserver(this);
        this.to = id;
        init_data();
    }




    @FXML
    private void initialize(){

    }

    public void accept_handle(Long from)
    {
        service.update_request(this.to,from, RequestStatus.ACCEPTED);
        init_data();
    }
    public void reject_handle(Long from)
    {
        service.update_request(this.to,from,RequestStatus.REJECTED);
        init_data();
    }
    private void init_data()
    {
        System.out.println(service.requests(to));
        request_list.getChildren().clear();
        service.requests(this.to).forEach(request ->{
            HBox hBox = new HBox(10);
            var user = service.find_one(request.getFrom());
            if(user != null)
            {
                Text text = new Text(user.getFirstName() + " " + user.getLastName());
                text.setStyle("-fx-text-fill: yellow;");
                TextFlow textFlow = new TextFlow(text);

                textFlow.setStyle("-fx-alignment: left;" +
                        "-fx-background-radius:20px;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: Arial;");
                Button accept = new Button();
                accept.setText("accept");
                Button decline = new Button();
                decline.setText("decline");
                accept.setAlignment(Pos.CENTER_RIGHT);
                accept.setStyle(  "-fx-background-color:#b5b8d2;"+
                        "-fx-border-width: 2px 2px 2px 2px;"+
                        "-fx-border-radius: 20px;"+
                        "-fx-background-radius: 20px;"+
                        "-fx-border-style: solid;"+
                        "-fx-focus-traversable: 1s;"+
                        "-fx-border-color: #353a56;"+
                        "-fx-text-fill: #353a56;"+
                        "-fx-cursor: hand;"+
                        "-fx-cursor: hand;" +
                        "-fx-alignment: left;" +
                        "-fx-pref-height: 33.0;" +
                        "-fx-pref-width: 142.0;" +
                        "-fx-font-size: 14px;");
                decline.setStyle(  "-fx-background-color:#b5b8d2;"+
                        "-fx-background-size: 100;" +
                        "-fx-border-width: 2px 2px 2px 2px;"+
                        "-fx-border-radius: 20px;"+
                        "-fx-background-radius: 20px;"+
                        "-fx-border-style: solid;"+
                        "-fx-border-color: black;"+
                        "-fx-focus-traversable: 1s;"+
                        "-fx-border-color: #353a56;"+
                        "-fx-text-fill: #353a56;"+
                        "-fx-cursor: hand;"+
                        "-fx-alignment: right;"+
                        "-fx-pref-height: 33.0;" +
                        "-fx-pref-width: 142.0;" +
                        "-fx-font-size: 14px");
                accept.setAlignment(Pos.CENTER);
                decline.setAlignment(Pos.CENTER);
                accept.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        accept_handle(user.getId());
                    }
                });
                decline.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        reject_handle(user.getId());
                    }
                });
                hBox.setHgrow(textFlow, javafx.scene.layout.Priority.ALWAYS);///face display text in stanga butoane in dreapta
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().add(textFlow);
                hBox.getChildren().add(accept);
                hBox.getChildren().add(decline);
                request_list.getChildren().add(hBox);
            }
        } );

    }
    //java nu are executabil
    @Override
    public void update() {

        init_data();

    }
}
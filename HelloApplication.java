package com.example.lab8;

import com.example.lab8.domain.Message;
import com.example.lab8.domain.Prietenie;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.Utilizator;
import com.example.lab8.domain.validators.MessageValidator;
import com.example.lab8.domain.validators.PrietenieValidator;
import com.example.lab8.domain.validators.UtilizatorValidator;
import com.example.lab8.domain.validators.Validator;
import com.example.lab8.repository.*;
import com.example.lab8.service.ConnectionService;
import com.example.lab8.service.Service;
import com.example.lab8.service.ServicePrieteneie;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {
    Service service;
    ServicePrieteneie servicePrieteneie;

    ConnectionService connectionService;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/Home-view.fxml"));
       // Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";
        String user = "user1";
        String password= "user1";
        UserDBRepository userDBRepository = new UserDBRepository(url,user,password);
        Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
        Validator<Prietenie> prietenieValidator = new PrietenieValidator();
        MessageValidator messageValidator = new MessageValidator();
        PrietenieDBRepository prietenieDBRepository = new PrietenieDBRepository(url,user,password,prietenieValidator);
        MessageDBrepository messageDBrepository = new MessageDBrepository(url,user,password,messageValidator);
        RequestDBRepository requestDBRepository = new RequestDBRepository(url,user,password);
        this.service = new Service(userDBRepository,prietenieDBRepository,messageDBrepository,requestDBRepository);
        this.servicePrieteneie = new ServicePrieteneie(prietenieDBRepository);
        ConnectedDBRepository connectedDBRepository = new ConnectedDBRepository(url,user,password);
        this.connectionService= new ConnectionService(connectedDBRepository);
        initview(stage);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // Add your cleanup or other actions here
                connectionService.logout();
            }
        });
    }
    private void initview(Stage stage)throws IOException{
        FXMLLoader msgloader= new FXMLLoader();
        msgloader.setLocation(getClass().getResource("view/homeview2.fxml"));
        //HBox layout = msgloader.load();
        AnchorPane layout = msgloader.load();
        stage.setScene(new Scene(layout));
        Controller controller = msgloader.getController();
        controller.SetService(this.service,this.servicePrieteneie,this.connectionService);
    }
    public static void main(String[] args) {
        launch();
    }
}
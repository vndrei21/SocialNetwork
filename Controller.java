package com.example.lab8;
import com.example.lab8.Controllers.ConversationController;
import com.example.lab8.Controllers.RequestController;
import com.example.lab8.UI.MovieAction.MovieAction;
import com.example.lab8.domain.RequestStatus;
import com.example.lab8.domain.Utilizator;
import com.example.lab8.observer.Observer;
import com.example.lab8.repository.paging.page;
import com.example.lab8.repository.paging.pageable;
import com.example.lab8.service.ConnectionService;
import com.example.lab8.service.Service;
import com.example.lab8.service.ServicePrieteneie;
import com.example.lab8.service.criptare.Criptare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

import java.util.stream.StreamSupport;

public class Controller implements Observer {

    private Service UserService;

    @FXML
    TableView<Utilizator> tableView;

    @FXML
    TableColumn<Utilizator,Long> tableColumnID;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;

    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;
    @FXML
    TextField to;
    @FXML
    TextField from;

    @FXML
    Button requests;

    @FXML
    Button startconversation;

    @FXML
    TextField id;

    @FXML
            TextField First;
    @FXML
            TextField Last;
    @FXML
            Button save_request;
    @FXML
    TableView<Utilizator> tableViewFriends;

    @FXML
    TableColumn<Utilizator,Long> IdColumn;

    @FXML
    TableColumn<Utilizator,String> FirstNameColumn;
    @FXML
    TableColumn<Utilizator,String> LastNameColumn;

    @FXML
            Button users;
    @FXML
            Button friendships;
    @FXML
    HBox hBox;
    ObservableList<Utilizator> model;
    @FXML
    BorderPane stage2, stage1;
    ObservableList<Utilizator> loginfriends;
    ObservableList<Utilizator> searchModel;

    @FXML
    Button previous;
    @FXML
    Button next;
    private int currentPage =0;
    private int currentSearchPage = 0;
    private int totalnumberofelements;

    private final int numberofRecordPerPage=5;
    ObservableList<Utilizator> friendsmodel;
    @FXML
    BorderPane Logins;
    @FXML
    BorderPane leftPane;


    ///Login Panes
    @FXML
    private Pane LoginPane;
    @FXML
    private Pane CreateAccPane;

    @FXML Button back_to_login;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    ///Create account panes

    ConnectionService connectionService;
    ///Tabela pt utilizatori si pt Prietenii unui user
    @FXML
    TableView<Utilizator> prieteni;
    @FXML
    TableColumn<Utilizator,String> first_name_column;
    @FXML
    TableColumn<Utilizator,String> last_name_column;
    @FXML
    BorderPane UserBorderPane;
    @FXML
    BorderPane FriendsBorderPane;
    @FXML
    Label user_label1;
    @FXML
    TableView<Utilizator> searchTable;
    @FXML
    TableColumn<Utilizator,String> searchFirstColumn;
    @FXML
    TableColumn<Utilizator,String> searchLastColumn;

    @FXML
    Label user_label2;
    @FXML
    Button previouslogin;
    @FXML
    Button nextlogin;

    @FXML
    TextField createusername;

    @FXML
    PasswordField createPassword;
    @FXML
    PasswordField confirmpassword;
    @FXML
    TextField createFirstName;
    @FXML
    TextField createLastName;


    public void SetService(Service service,ServicePrieteneie servicePrieteneie,ConnectionService connectionService)
    {
        stage2.setVisible(false);
        stage1.setVisible(false);
        Logins.setVisible(true);
        leftPane.setVisible(false);
        CreateAccPane.setVisible(false);
        LoginPane.setVisible(true);
        UserBorderPane.setVisible(false);
        FriendsBorderPane.setVisible(false);
        this.UserService = service;
        this.connectionService = connectionService;
        init();
        this.UserService.addObserver(this);

    }
    @FXML
    public void initialize()
    {

        model = FXCollections.observableArrayList();
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
        friendsmodel = FXCollections.observableArrayList();
        /*IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewFriends.setItems(friendsmodel);
         */
        loginfriends = FXCollections.observableArrayList();
        first_name_column.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        last_name_column.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        prieteni.setItems(loginfriends);
        searchModel = FXCollections.observableArrayList();
        searchFirstColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        searchLastColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        searchTable.setItems(searchModel);


    }

    public void init()
    {

        page<Utilizator> utilizators = UserService.getUsersOnPage(new pageable(this.currentPage,this.numberofRecordPerPage));
        totalnumberofelements = utilizators.getTotalnumberofelements();
        List<Utilizator> utilizatorList = StreamSupport.stream(utilizators.getElementsonpage().spliterator(),false).toList();
        System.out.println(utilizatorList);
        model.setAll(utilizatorList);
        handlePageNavigationChecks();
    }
    public void initSearch()
    {
        page<Utilizator> utilizators = UserService.getUsersOnPage(new pageable(this.currentSearchPage,this.numberofRecordPerPage));
        totalnumberofelements = utilizators.getTotalnumberofelements();
        List<Utilizator> utilizatorList = StreamSupport.stream(utilizators.getElementsonpage().spliterator(),false).toList();
        System.out.println(utilizatorList);
        searchModel.setAll(utilizatorList);
        handlePageNavigationChecksLogin();
    }
    public void handle_save() throws Exception {
        try{
        String First_name = First.getText();
        String Last_name = Last.getText();
        if(First_name.length() < 3 || Last_name.length() < 3)
            throw new Exception("Parametru invalid");
        UserService.add_user(First_name, Last_name);
        First.clear();
        Last.clear();
            MovieAction.showMessage(null, Alert.AlertType.INFORMATION, "Operation Status", "SUCCESS");
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }



    }

    private void handlePageNavigationChecks()
    {
        previous.setDisable(currentPage == 0);
        next.setDisable((currentPage+1)*numberofRecordPerPage >=totalnumberofelements);

    }
    private void handlePageNavigationChecksLogin()
    {
        previouslogin.setDisable(currentSearchPage == 0);
        nextlogin.setDisable((currentSearchPage+1)*numberofRecordPerPage >=totalnumberofelements);
    }


    public void handle_delete() {
        try {
            var index = tableView.getSelectionModel().getSelectedIndex();
            var value = tableColumnID.getCellData(index);
            UserService.delete_user(value);
            MovieAction.showMessage(null, Alert.AlertType.INFORMATION, "Operation Status", "SUCCESS");
            First.clear();
            Last.clear();
        }catch (Exception e)
        {
            MovieAction.showMessage(null, Alert.AlertType.ERROR, "Operation Status", e.getMessage());
        }
    }

    public void goToNextPage(){
        System.out.println("Next Page");
        currentPage++;
        init();
    }
    public void goToPreviousPage()
    {
        System.out.println("Previous Page");
        currentPage--;
        init();
    }
    public void goToNextPagelogin(){
        System.out.println("Next Page");
        currentSearchPage++;
        initSearch();
    }
    public void goToPreviousPagelogin()
    {
        System.out.println("Previous Page");
        currentSearchPage--;
        initSearch();
    }
    public void update_handle()
    {
        try {
            var index = tableView.getSelectionModel().getSelectedIndex();
            var id = tableColumnID.getCellData(index);
            var first = this.First.getText();
            var last = this.Last.getText();
            UserService.update(id,first,last);
            init();
            MovieAction.showMessage(null, Alert.AlertType.INFORMATION, "Operation Status", "SUCCESS");
            First.clear();
            Last.clear();
    }catch (Exception e)
        {
            MovieAction.showMessage(null, Alert.AlertType.ERROR, "Operation Status", "Try Again");
        }
    }
public void handle_conversation() throws IOException {
    FXMLLoader msgloader = new FXMLLoader();
    if (to.getText() != null && from.getText() != null) {
        var to = Long.valueOf(this.to.getText());
        var from = Long.valueOf(this.from.getText());
        msgloader.setLocation(getClass().getResource("view/conv-box.fxml"));

        AnchorPane root =  msgloader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        ConversationController controller = msgloader.getController();
        controller.Conversation(UserService, to, from);
        stage.show();
    }
}
public void start_chat() throws IOException {
    FXMLLoader msgloader = new FXMLLoader();
    var selectedItem = prieteni.getSelectionModel().getSelectedItem();
    if(selectedItem != null){
        var id = selectedItem.getId();
        System.out.println(id);
        msgloader.setLocation(getClass().getResource("view/conv-box.fxml"));
        AnchorPane root = msgloader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ConversationController controller = msgloader.getController();
        controller.Conversation(UserService, connectionService.find().getId(), id);
        stage.show();
    }
}
    public void handle_requests() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        var index = tableView.getSelectionModel().getSelectedIndex();
        var id = tableColumnID.getCellData(index);
        loader.setLocation(getClass().getResource("view/Request-list.fxml"));
        AnchorPane root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        System.out.println(id);
        RequestController requestController = loader.getController();
        requestController.Set_Service(this.UserService,id);
        stage.show();

    }

    public void handle_save_request() {
        if (to.getText() != null && from.getText() != null) {
            var too = Long.valueOf(to.getText());
            var fromm = Long.valueOf(from.getText());
            var value = UserService.save_request(too, fromm, RequestStatus.PENDING);
            to.clear();
            from.clear();

            if (value == null) {
                MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "O cere de prietenie a fost deja trimisa/primita");
            } else MovieAction.showMessage(null, Alert.AlertType.INFORMATION, "Cerere", "Cerere trimisa");
        }
    }
    private void init_Stage2(Long id)
    {


        Iterable<Utilizator> utilizators = UserService.friendslist(id);
        List<Utilizator> utilizatorList = StreamSupport.stream(utilizators.spliterator(),false).toList();
        System.out.println(utilizatorList);
        friendsmodel.setAll(utilizatorList);


    }
    public void login_stage()
    {
        System.out.println(connectionService.find().getId());
        Iterable<Utilizator> utilizators = UserService.friendslist(connectionService.find().getId());
        List<Utilizator> utilizatorList = StreamSupport.stream(utilizators.spliterator(),false).toList();
        System.out.println(utilizatorList);
        loginfriends.setAll(utilizatorList);
        initSearch();

    }
    public void switch_to_stage2() {

        var index = tableView.getSelectionModel().getSelectedIndex();
        var id = tableColumnID.getCellData(index);
        if (id != null) {
            init_Stage2(id);
            stage1.setVisible(false);
            stage2.setVisible(true);
        }
        else
            MovieAction.showMessage(null, Alert.AlertType.WARNING, "Warning", "Nu ati selectat nimic!");

    }
    public void switch_to_stage1() {

        init();
        stage1.setVisible(true);
        stage2.setVisible(false);

    }
    public void add_to_Field() {
        var index = tableView.getSelectionModel().getSelectedIndex();
        var id = tableColumnID.getCellData(index);
        if (id != null) {
            if (!this.to.getText().isEmpty() && !this.from.getText().isEmpty()) {
                this.to.clear();
                this.from.clear();
            }
            if (this.from.getText().isEmpty())
                this.from.setText(id.toString());
            else this.to.setText(id.toString());
        }
    }



    @Override
    public void update() {
        init();
        initSearch();
        if(connectionService.find() != null)
            login_stage();
    }


    public void handle_login()  {
        var username = this.username.getText();
        var password = new Criptare(this.password.getText()).criptare();
        var entity = UserService.FindByName(username,password);
        if(username.equals("admin") && this.password.getText().equals("user1"))
        {
            Logins.setVisible(false);
            leftPane.setVisible(true);
            switch_to_stage1();
            this.username.clear();
            this.password.clear();
        }
        else
            if(entity == null)
                MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "Username-ul/Parola e invalida");
            else
            {
                user_label1.setText(entity.getFirstName() + " " +  entity.getLastName());
                user_label2.setText(entity.getFirstName() + " " +  entity.getLastName());

                connectionService.login(entity.getId(),entity.getFirstName(),entity.getLastName());
                login_stage();
                UserBorderPane.setVisible(true);




            }
    }
    public void SwitchToCreate()
    {
        CreateAccPane.setVisible(true);
        LoginPane.setVisible(false);
    }
    public void SwitchToLogin()
    {
        CreateAccPane.setVisible(false);
        LoginPane.setVisible(true);

    }
    public void logout()
    {
        connectionService.logout();
        username.clear();
        password.clear();
        stage1.setVisible(false);
        stage2.setVisible(false);
        leftPane.setVisible(false);
        FriendsBorderPane.setVisible(false);
        UserBorderPane.setVisible(false);
        Logins.setVisible(true);
    }

    public void SwitchFromFriendstoSearch()
    {
        UserBorderPane.setVisible(false);
        FriendsBorderPane.setVisible(true);
    }
    public void SwitchFromSearchtoFriends()
    {
        UserBorderPane.setVisible(true);
        FriendsBorderPane.setVisible(false);
    }
    public void login_requests() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/Request-list.fxml"));
        AnchorPane root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        RequestController requestController = loader.getController();
        requestController.Set_Service(this.UserService,connectionService.find().getId());
        stage.show();

    }
    public void sendloggedrequests() {
        var med = searchTable.getSelectionModel().getSelectedItem();
        if (med != null){
            var id = med.getId();
            var value = UserService.save_request(id, connectionService.find().getId(), RequestStatus.PENDING);
            if (value == null) {
                MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "O cere de prietenie a fost deja trimisa/primita");
            } else MovieAction.showMessage(null, Alert.AlertType.INFORMATION, "Status Cerere!", "Cerere Trimisa!");
        }
    }
    public void createAccount() {
        if(!createFirstName.getText().isEmpty() && !createLastName.getText().isEmpty() && !createusername.getText().isEmpty() && !createPassword.getText().isEmpty() && !confirmpassword.getText().isEmpty())
        {
            if(UserService.FindByName(createusername.getText(),createPassword.getText())!=null)
                MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "Username folosit deja");
            else
            if(createPassword.getText().equals(confirmpassword.getText())) {
                UserService.createAccount(createFirstName.getText(), createLastName.getText(), createusername.getText(), createPassword.getText());
                var entity = UserService.FindByName(createusername.getText(),createPassword.getText());
                if(entity !=null) {
                    connectionService.login(entity.getId(), entity.getFirstName(), entity.getLastName());
                    login_stage();
                    Logins.setVisible(false);
                    leftPane.setVisible(true);
                    UserBorderPane.setVisible(true);
                    user_label1.setText(createFirstName.getText() + " " + createLastName.getText());
                    createFirstName.clear();
                    createLastName.clear();
                    createPassword.clear();
                    confirmpassword.clear();
                    createusername.clear();
                }
                else System.out.println("Eroare");
            }
            else
                MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "Password-ul nu corespunde ");
        }
        else
            MovieAction.showMessage(null, Alert.AlertType.WARNING, "Decline", "Completati toate campuriile!");
    }


}


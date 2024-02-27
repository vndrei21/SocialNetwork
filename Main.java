package com.example.lab8;
import com.example.lab8.Testeee.TesteCriptare;
import com.example.lab8.UI.UI;
import com.example.lab8.domain.Prietenie;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.Utilizator;
import com.example.lab8.domain.validators.MessageValidator;
import com.example.lab8.domain.validators.PrietenieValidator;
import com.example.lab8.domain.validators.UtilizatorValidator;
import com.example.lab8.domain.validators.Validator;
import com.example.lab8.repository.*;
import com.example.lab8.service.Service;
import com.example.lab8.service.ServicePrieteneie;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[]args) {
        String url="jdbc:postgresql://localhost:5432/SocialNetwork";
        String user = "user1";
        String password= "user1";
        UserDBRepository userDBRepository = new UserDBRepository(url,user,password);
        Validator<Utilizator> utilizatorValidator = new UtilizatorValidator();
        Validator<Prietenie> prietenieValidator = new PrietenieValidator();
        PrietenieDBRepository prietenieDBRepository = new PrietenieDBRepository(url,user,password,prietenieValidator);
        InMemoryRepository<Long, Utilizator> utilizatorInMemoryRepository = new InMemoryRepository<>(utilizatorValidator);
        InMemoryRepository<Tuple<Long,Long>, Prietenie> prietenieInMemoryRepository = new InMemoryRepository<>(prietenieValidator);
        MessageValidator validator = new MessageValidator();
        MessageDBrepository messageDBrepository = new MessageDBrepository(url,user,password,validator);
        RequestDBRepository requestDBRepository = new RequestDBRepository(url,user,password);
        Service service = new Service(userDBRepository,prietenieDBRepository,messageDBrepository,requestDBRepository);
        ServicePrieteneie prieteneie = new ServicePrieteneie(prietenieDBRepository);
        UI ui = new UI(service,prieteneie);
       // ui.run();
        Tests tests = new Tests();
        tests.test();
        TesteCriptare testeCriptare = new TesteCriptare();
        testeCriptare.test_criptare();;
    }
}
package com.example.lab8.UI;



import com.example.lab8.domain.Utilizator;
import com.example.lab8.domain.validators.ValidationException;
import com.example.lab8.service.Service;
import com.example.lab8.service.ServicePrieteneie;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class UI {
    Service service;
    ServicePrieteneie prieteneie;

    /**
     * constructor
     * @param service service-ul aplicatiei
     */
    public UI(Service service,ServicePrieteneie prieteneie){
        this.service = service;
        this.prieteneie = prieteneie;
    }
    /**
     * adauga un utilizator
     */
    public void adauagare_utilizator()
    {
        Scanner in  = new Scanner(System.in);
        System.out.println("Introduce-ti numele de familie:");
        String First = in.nextLine();

        System.out.println("Introduce-ti prenumele:");
        String second = in.nextLine();
        try {

        service.add_user(First,second);
        }catch  (ValidationException e)
        {
            System.out.println(e);
        }
    }

    /**
     * afiseaza toti utilizatorii
     */
    public void print_all()
    {
        Consumer<String> consumer = System.out::println;
        if(!service.getAll().iterator().hasNext())
            System.out.println("Nu exista useri!");
        else
            //for (Utilizator user:service.getAll()) {
            service.getAll().forEach(x->consumer.accept(x.toString()+" "));
        consumer.accept("\n");
    }
    /**
     * afiseaza toate prieteniile
     */
    public void print_all_prietenii()
    {
        Consumer<String> consumer = System.out::println;
        if(!prieteneie.getAll().iterator().hasNext())
            System.out.println("Nu exista useri!");
        else
            prieteneie.getAll().forEach(x-> consumer.accept(x.toString()+" "));

    }

    /**
     * adauga prietenii
     */
    public void adauga_prietenie()
    {
        print_all();
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul primului User:");
        Long id1 = in.nextLong();
        System.out.println("Introdu id-ul celui de al doilea user:");
        Long id2 = in.nextLong();
        System.out.println("Introdu ziua:");
        int ziua = in.nextInt();
        System.out.println("Introdu luna");
        int luna = in.nextInt();
        System.out.println("Introdu anul");
        int anul = in.nextInt();
        LocalDateTime data=LocalDateTime.of(anul,luna,ziua,LocalDateTime.MAX.getHour(), LocalDateTime.MAX.getHour());



        try {
        service.add_friend(id1,id2,data);
        }catch (ValidationException e){
        System.out.println(e);
    }
    }

    /**
     * sterge prietenii
     */
    public void sterge_prietenie() {
        print_all();
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul primului User:");
        Long id1 = in.nextLong();
        System.out.println("Introdu id-ul celui de al doilea user:");
        Long id2 = in.nextLong();
        try {


            service.delete_prieten(id1, id2);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    /**
     * sterge useri
     */
    public void sterge_user()
    {
        print_all();
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul user-ului dorit a fi sters:");
        Long id = in.nextLong();
        try {
            service.delete_user(id);
            //prieteneie.sterge_prietenii_user(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * afiseaza nr comunitati
     */
    public void Nr_comunitati()
    {
        System.out.println(service.Nr_Comunitati());
    }

    /**
     * afiseaza comunitatea maxima cu useri sai
     */
    public void Comunitate_max()
    {
        if(service.Comunitate_max().isEmpty())
            System.out.println("Nu exista useri!");
        else
            for (Utilizator user: service.Comunitate_max()) {
                System.out.println(user.toString());
            }
    }

    /**
     * autopopuleaza aplicatia
     */
    public void autopopulare()
    {
        service.add_user("u1FirstName", "u1LastName");
        service.add_user("u2FirstName", "u2LastName");
        service.add_user("u3FirstName", "u3LastName");
        service.add_user("u4FirstName", "u4LastName");
        service.add_user("u5FirstName", "u5LastName");
        service.add_user("u6FirstName", "u6LastName");
        service.add_user("u7FirstName", "u7LastName");
        service.add_user("u8FirstName", "u8LastName");
        service.add_user("u9FirstName", "u9LastName");
        service.add_user("u10FirstName", "u10LastName");
    }

    /**
     * afiseaza toti prietenii
     */
    public void afisare_prieteni()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul primului User:");
        Long id = in.nextLong();
        System.out.println("Introdu data");
        int data = in.nextInt();
        var list = service.filtrare_prietenii(id, data);
        if(list.isEmpty())
            System.out.println("Utilizatorul cu id:"+id+" nu are prieteni!");
        else
            list.forEach(prieten -> {
                var x = service.FindOne(prieten.getLeft());
                System.out.println(x.getFirstName() + "|" + x.getLastName() + "|" + prieten.getRight().toString() + "\n");
            });


    }

    void update()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul Userului care va fi modificat:");
        Long id = in.nextLong();
        in.nextLine();
        System.out.println("Introduce-ti numele de familie:");
        String First = in.nextLine();

        System.out.println("Introduce-ti prenumele:");
        String second = in.nextLine();
        try {
            service.update(id,First,second);
        } catch  (ValidationException e)
        {
            System.out.println(e);
        }
    }
    void conv()
    {
        try {


            Scanner in = new Scanner(System.in);
            System.out.println("Introdu id-ul unui user");
            Long from = in.nextLong();
            System.out.println("Introdu id-ul celuilalt user:");
            Long to = in.nextLong();
            var conversatie = service.conversation(from, to);
            conversatie.forEach(mesaj -> {
                System.out.println(mesaj.getLeft().getFirstName()+" "+mesaj.getLeft().getLastName()+":"+mesaj.getRight() + '\n');
            });
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    void add_mesaj()
    {
        Scanner in = new Scanner(System.in);
        System.out.println("Introdu id-ul Userului care va trimite mesajul");
        Long from = in.nextLong();
        in.nextLine();
        System.out.println("Introduce-ti Utilizatorul sau Utilizatorii care vor primi mesajul");
        String to = in.nextLine();

        System.out.println("Introduce-ti prenumele:");
        String message = in.nextLine();

        try {
           List<String> array = Arrays.stream(to.split(" ")).collect(Collectors.toList());
            System.out.println(array);
            service.save_message(from,array,message);
        } catch  (ValidationException e)
        {
            System.out.println(e);
        }

    }

    /**
     * porneste aplicatia
     */
    public void run()
    {
        Scanner in = new Scanner(System.in);
        while(true)
        {
            System.out.println("""
                    Meniu:
                    1.Adaugare Utilizator
                    2.Adaugare Prietenie
                    3.Stergere Utilizator
                    4.Steregere Prietenie
                    5.Numarul de comunitati
                    6.Comunitate Maxima
                    7.Afisare Utilizatori
                    8.Afiseaza prietenii
                    9.Afisare lista prieteni
                    10.Exit
                    12.modificare User
                    """);
            System.out.println("Introduce-ti comanda dorita:");
            int command = in.nextInt();
            switch (command) {
                case 1:
                    adauagare_utilizator();
                    break;
                case 2:
                    adauga_prietenie();
                    break;
                case 3:
                    sterge_user();
                    break;
                case 4:
                    sterge_prietenie();
                    break;
                case 5:
                    Nr_comunitati();
                    break;
                case 6:
                    Comunitate_max();
                    break;
                case 7:
                    print_all();
                    break;
                case 10:
                    System.exit(0);
                case 11:
                    autopopulare();
                    break;
                case 8:
                    print_all_prietenii();
                    break;
                case 9:
                    afisare_prieteni();
                    break;
                case 12:
                    update();
                    break;
                case 13:
                    conv();
                    break;
                case 14:
                    add_mesaj();
                default:
                    System.out.println("Comanda gresita!\n");
            }
        }
    }
}

package com.example.lab8.service;






import com.example.lab8.DFS.DFS;
import com.example.lab8.domain.*;
import com.example.lab8.domain.validators.ValidationException;
import com.example.lab8.observer.Observable;
import com.example.lab8.observer.Observer;
import com.example.lab8.repository.MessageDBrepository;
import com.example.lab8.repository.PrietenieDBRepository;
import com.example.lab8.repository.RequestDBRepository;
import com.example.lab8.repository.UserDBRepository;
import com.example.lab8.repository.paging.page;
import com.example.lab8.repository.paging.pageable;
import com.example.lab8.service.criptare.Criptare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Service extends AbstractService<Long, Utilizator> implements Observable {

    private final UserDBRepository repositoryUser;
    private final PrietenieDBRepository repositoryPrietenie;

    private final MessageDBrepository messageDBrepository;
    private long generateId;
    private final RequestDBRepository requestDBRepository;

    private List<Observer> observers = new ArrayList<>();


    public Service(UserDBRepository userDBRepository, PrietenieDBRepository prietenieDBRepository,MessageDBrepository messageDBrepository,RequestDBRepository requestDBRepository) {

        this.messageDBrepository = messageDBrepository;
        this.repositoryUser = userDBRepository;
        this.repositoryPrietenie = prietenieDBRepository;
        this.requestDBRepository = requestDBRepository;
        //this.generateId  = LocalDate.now().toEpochDay();
        //generateId = LocalDateTime.now().getSecond();

        generateId = LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.MIN);


    }

    public List<Tuple<Utilizator,String>> conversation(Long from, Long to)
    {
        List<Tuple<Utilizator,String>> conversatie = new ArrayList<>();
        if(repositoryUser.findOne(from).isEmpty() || repositoryUser.findOne(to).isEmpty())
            return null;
        else {
            messageDBrepository.conversation(from,to).forEach(mesaj->{
                var user= new Utilizator(repositoryUser.findOne(mesaj.getLeft()).get().getFirstName(),repositoryUser.findOne(mesaj.getLeft()).get().getLastName());
                user.setId(mesaj.getLeft());
                conversatie.add(new Tuple<>(user,mesaj.getRight()));
            });
            return conversatie;
        }
    }

    public Requests save_request(Long to, Long from, RequestStatus status) {
        Requests request = new Requests(status, from, to);
        if (repositoryPrietenie.findOne(new Tuple<>(to, from)).isEmpty())
            if (requestDBRepository.findOne(new Tuple<>(from, to)).isEmpty() && requestDBRepository.findOne(new Tuple<>(to, from)).isEmpty()) {
                requestDBRepository.save(request);
                System.out.println("Da");
                if (status == RequestStatus.ACCEPTED)
                    add_friend(to, from, LocalDateTime.now());
                return request;
            }
        return null;
    }

    public Requests update_request(Long to, Long from, RequestStatus Status)
    {
        var  request = new Requests(Status,from,to);
        requestDBRepository.update(request);
        if(Status==RequestStatus.ACCEPTED)
            add_friend(to,from,LocalDateTime.now());
        notifyObservers();
        return request;
    }

    public Iterable<Requests> requests(Long id)
    {
        List<Requests> list = new ArrayList<>();
        requestDBRepository.findAll().forEach(requests -> {
            if(Objects.equals(requests.getTo(), id) && requests.getStatus()==RequestStatus.PENDING)
                list.add(requests);
        });
        return list;
    }

    public Message save_message(Long from,List<String> to, String message)
    {
        List<Utilizator> prieteni = new ArrayList<>();
        to.forEach(pr -> {
            if(pr !="") {
                var a = repositoryUser.findOne(Long.valueOf(pr));
                a.ifPresent(prieteni::add);
            }else
                throw new ValidationException("mesaj invalid!");
        });
        if (repositoryUser.findOne(from).isPresent()) {
            Message mesaj = new Message(repositoryUser.findOne(from).get(), prieteni, message);
            messageDBrepository.save(mesaj);
           // notifyObservers();
            return mesaj;
        }
        else
            return null;
    }
    public LinkedList<Utilizator> ListaPrieteni(Utilizator utilizator, Long id) {
        LinkedList<Utilizator> utilizators = utilizator.getFriends();
        utilizators.forEach(x -> {
            if (Objects.equals(x.getId(), id)) utilizators.remove(x);
        });
        return utilizators;
    }

    public Utilizator createAccount(String first,String last, String pass,String username)
    {
        Utilizator utilizator = new Utilizator(first,last);
        utilizator.setId(generateId);
        return repositoryUser.createAccount(utilizator,new Criptare(pass).criptare(),username).get();
    }
    public List<Utilizator> friendslist(Long id)
    {
        List<Utilizator> prieteni = new ArrayList<>();
        repositoryPrietenie.findAll().forEach(user->{
            if(Objects.equals(user.get_id().getLeft(), id))
                prieteni.add(repositoryUser.findOne(user.get_id().getRight()).get());
            else if(Objects.equals(user.get_id().getRight(), id))
                prieteni.add(repositoryUser.findOne(user.get_id().getLeft()).get());
        });

        return prieteni;
    }


    /**
     * adauga un utilizator
     *
     * @param FirstName prenumele
     * @param LastName  numele de familie
     */
    public void add_user(String FirstName, String LastName) {
        Utilizator utilizator = new Utilizator(FirstName, LastName);
        utilizator.setId(generateId);
        generateId++;// = LocalDate.now().toEpochSecond(LocalTime.now(), ZoneOffset.MIN);
        repositoryUser.save(utilizator);
        notifyObservers();
    }

    /**
     * adauga prieten
     *
     * @param id1 -string id-ul primului user
     * @param id2 -string id-ul celui de al doilea user
     */
    public void add_friend(Long id1, Long id2, LocalDateTime data) {
        Prietenie prietenie = new Prietenie(id1, id2, data);
        Tuple<Long, Long> t = new Tuple<>(id1, id2);
        prietenie.setId(t);
        repositoryPrietenie.save(prietenie);
        Optional<Utilizator> user1 = repositoryUser.findOne(id1);
        Optional<Utilizator> user2 = repositoryUser.findOne(id2);
       /* for (Utilizator user:repositoryUser.findAll()) {
            if(user.getId().equals(id1))
                user.SaveInList(user2.get());
            else
                if(user.getId().equals(id2))
                    user.SaveInList(user1.get());
        }
        */
        repositoryUser.findAll().forEach(x -> {
            if (x.getId().equals(id1))
                if (user2.isPresent())
                    x.SaveInList(user2.get());
                else if (x.getId().equals(id2))
                    user1.ifPresent(x::SaveInList);
        });

        notifyObservers();
    }

    /**
     * sterge user
     *
     * @param id-string-ul userului dorit a fi sters
     */
    public void delete_user(Long id) throws InterruptedException {
        repositoryUser.findOne(id);
        repositoryUser.delete(id);
        /*for (Prietenie p: repositoryPrietenie.findAll()) {
            if(p.getId().getLeft().equals(id) || p.getId().getRight().equals(id)) {
                repositoryPrietenie.delete(p.getId());
            }
        }
        for (Utilizator users:repositoryUser.findAll())
            users.setFriends(ListaPrieteni(users,id));


         repositoryPrietenie.findAll().forEach(p->{
                    if(p.getId().getLeft().equals(id))
                        repositoryPrietenie.delete(p.getId());
                    else if( p.getId().getRight().equals(id))
                        repositoryPrietenie.delete(p.getId());



        });



         */
        repositoryUser.findAll().forEach(user -> user.setFriends(ListaPrieteni(user, id)));
        repositoryUser.findAll().forEach(u -> {
            if (u.getId() > id)
                u.setId(u.getId() - 1);
        });
        notifyObservers();
    }

    /**
     * sterege o relatie de prietenie
     *
     * @param id1 long id-ul primului user
     * @param id2 long id-ul celui de al doilea user
     */
    public void delete_prieten(Long id1, Long id2) {
        Tuple<Long, Long> id = new Tuple<>(id1, id2);
        /*for(Utilizator user:repositoryUser.findAll()) {
            if(user.getId().equals(id1))
                ListaPrieteni(user,id2);
            else
                if(user.getId().equals(id2))
                    ListaPrieteni(user,id1);

        }


        repositoryUser.findAll().forEach(u->{
            if(u.getId().equals(id1))
                ListaPrieteni(u,id2);
            else
            if(u.getId().equals(id2))
                ListaPrieteni(u,id1);
        });

         */
        repositoryPrietenie.delete(id);
    }


    /**
     * calculeaza nr de comunitati
     *
     * @return nr de comunitati
     */
    public int Nr_Comunitati() {
        List<Utilizator> utilizators = new ArrayList<>();
        /*for (Utilizator utilizator : repositoryUser.findAll()) {
            utilizators.add(utilizator);
        }

         */
        repositoryUser.findAll().forEach(utilizators::add);
        List<Prietenie> prietenies = new ArrayList<>();
        /*for(Prietenie prietenie:repositoryPrietenie.findAll())
        {
            prietenies.add(prietenie);
        }*/
        repositoryPrietenie.findAll().forEach(prietenies::add);

        DFS graf = new DFS(utilizators);
        for (Prietenie prietenie : repositoryPrietenie.findAll()) {
            if (prietenie.get_id().getRight() != null && prietenie.get_id().getLeft() != null)
                graf.addEdge(prietenie.get_id().getLeft(), prietenie.get_id().getRight());
        }
        return graf.dfs();
    }

    /**
     * calculeaza comunitatea maxima
     *
     * @return comunitatea cu cei mai multi membri
     */
    public List<Utilizator> Comunitate_max() {
        List<Utilizator> utilizators = new ArrayList<>();
        /*for (Utilizator utilizator : repositoryUser.findAll()) {
            utilizators.add(utilizator);
        }*/
        repositoryUser.findAll().forEach(utilizators::add);
        List<Prietenie> priestess = new ArrayList<>();
        /*for(Prietenie prietenie:repositoryPrietenie.findAll())
        {
            priestess.add(prietenie);
        }*/
        repositoryPrietenie.findAll().forEach(priestess::add);

        DFS graf = new DFS(utilizators);
        /*for (Prietenie prietenie:repositoryPrietenie.findAll()){
            graf.addEdge(prietenie.getId().getLeft(),prietenie.getId().getRight());
        }*/
        repositoryPrietenie.findAll().forEach(prietenie -> {
            graf.addEdge(prietenie.get_id().getLeft(), prietenie.get_id().getRight());
        });
        graf.dfs();
        return graf.comunitate_max();
    }

    /**
     * @return returneaza toti utilizastorii
     */
    public Iterable<Utilizator> getAll() {
        return repositoryUser.findAll();
    }

    /**
     * returneaza lista de prietenii realizate intr-o anumita luna a unui utilizator
     *
     * @param id        -id-ul utilizatorului
     * @param luna-luna
     * @return lista cu prietenii
     */
    public List<Tuple<Long, LocalDateTime>> filtrare_prietenii(Long id, int luna) {
        var list = repositoryUser.friendslist(id);
        return list.stream().filter(x -> x.getRight().getMonthValue() == luna).collect(Collectors.toList());
    }

    /**
     * gaseste un utilizator dupa id
     * @param id long id-ul care va vi cautat
     * @return utilizatorul cautat
     */
    public Utilizator FindOne(Long id) {
        if (repositoryUser.findOne(id).isPresent())
            return repositoryUser.findOne(id).get();
        return null;
    }

    /**
     * modifica un utilizator
     * @param id -long idul utilizatorului care va fi modificat
     * @param first_name -String numele de familie
     * @param last_name String prenumele
     * @return utilizatorul modificat
     */
    public Utilizator update(Long id, String first_name, String last_name) {

        Utilizator utilizator = new Utilizator(first_name, last_name);
        utilizator.setId(id);
        var entity =  repositoryUser.update(utilizator).get();
        notifyObservers();
        return entity;
    }


    /**
     * adauga un observer in lista de observers
     * @param observer - observer-ul care va fi adaugat
     */
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * updateaza toti observeri
     */
    @Override
    public void notifyObservers() {
        observers.stream().forEach(x -> x.update());
    }
    public Utilizator find_one(Long id)
    {
       return repositoryUser.findOne(id).get();

    }
    public page<Utilizator> getUsersOnPage(pageable pageable)
    {
        return repositoryUser.findall(pageable);
    }
    public Utilizator FindByName(String username,String password)
    {
        return repositoryUser.findbyName(username,password);
    }
}

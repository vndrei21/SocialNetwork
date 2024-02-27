package com.example.lab8.repository;



import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.Utilizator;
import com.example.lab8.repository.paging.PagingRepository;
import com.example.lab8.repository.paging.page;
import com.example.lab8.repository.paging.pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDBRepository implements PagingRepository<Long, Utilizator> {
    private final String Url;
    private final String User;
    private final String Password;

    public UserDBRepository(String url, String user, String password)
    {
        this.Url = url;
        this.User = user;
        this.Password = password;

    }

    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(Url,User,Password);
            PreparedStatement statement = connection.prepareStatement("select * from users where user_id = ?;")) {
            statement.setInt(1,Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator utilizator = new Utilizator(firstName,lastName);
                utilizator.setId(aLong);
                return Optional.of(utilizator);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        ArrayList<Utilizator> utilizators = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Url,User,Password);
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next())
            {
                Long id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("Last_name");
                Utilizator utilizator = new Utilizator(firstName,lastName);
                utilizator.setId(id);
                utilizators.add(utilizator);
            }
            return utilizators;
        }
    catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Utilizator> createAccount(Utilizator entity,String username,String password)
    {
        try(Connection connection =DriverManager.getConnection(this.Url,this.User,this.Password);
        PreparedStatement statement = connection.prepareStatement("insert into users(user_id,first_name,last_name,pass,username)values (?,?,?,?,?);")) {
        statement.setInt(1,entity.getId().intValue());
        statement.setString(2,entity.getFirstName());
        statement.setString(3,entity.getLastName());
        statement.setString(4,password);
        statement.setString(5,username);
        statement.executeUpdate();
        return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try (Connection connection = DriverManager.getConnection(Url,User,Password);
            PreparedStatement statement = connection.prepareStatement("insert into users(user_id,first_name,last_name)values (?,?,?);")
        ) {
            statement.setInt(1,Math.toIntExact(entity.getId()));
            statement.setString(2,entity.getFirstName());
            statement.setString(3,entity.getLastName());
            statement.executeUpdate();

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(Url,User,Password);
        PreparedStatement statement = connection.prepareStatement("DELETE from users where user_id=?")) {
            statement.setInt(1,Math.toIntExact(aLong));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        try(Connection connection = DriverManager.getConnection(Url,User, Password);
            PreparedStatement statement = connection.prepareStatement("update users set first_name=? , last_name= ? where user_id=?;")
        ){
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setInt(3,Math.toIntExact(entity.getId()));
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Tuple<Long, LocalDateTime>> friendslist(Long id)
    {
        List<Tuple<Long,LocalDateTime>> prietenii = new ArrayList<>();
            try(Connection connection = DriverManager.getConnection(Url,User,Password);
                PreparedStatement statement = connection.prepareStatement("select * from friendships where id1 = ? or id2=?")
            ) {
                statement.setInt(1,Math.toIntExact(id));
                statement.setInt(2,Math.toIntExact(id));
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next())
                {
                    long id1 = resultSet.getLong("id1");
                    long id2 = resultSet.getLong("id2");
                    var dt = resultSet.getDate("dataa").toLocalDate();
                    LocalDateTime dateTime = LocalDateTime.of(dt, LocalTime.now());
                    if(id1 == id)
                        id1 = id2;
                    prietenii.add(new Tuple<>(id1,dateTime));
                }
                return prietenii;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }


    public int returnNumberOfElements()
    {
        int numberOfElements = 0;
        try(Connection connection =DriverManager.getConnection(this.Url,this.User,this.Password);
            PreparedStatement preparedStatement = connection.prepareStatement("select count(*)as count from users;");
            ResultSet resultSet = preparedStatement.executeQuery()
            )
        {
            while (resultSet.next())
            {
                numberOfElements = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return numberOfElements;
    }
    @Override
    public page<Utilizator> findall(pageable pageable) {
        int numberofelements  = returnNumberOfElements();
        int limit = pageable.getPagesize();
        int offset = pageable.getPagesize()*pageable.getPagenumber();
        System.out.println(pageable.getPagesize() +" " + pageable.getPagenumber() +'\n');

        if(offset >= numberofelements)
            return new page<>(new ArrayList<>(),numberofelements);
        List<Utilizator> utilizatorList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(this.Url,this.User,this.Password);
            PreparedStatement statement = connection.prepareStatement("select * from users limit ? offset ?;")){
            statement.setInt(1,limit);
            statement.setInt(2,offset);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                Long id = set.getLong("user_id");
                String firstName = set.getString("first_name");
                String lastName = set.getString("last_name");
                Utilizator utilizator = new Utilizator(firstName,lastName);
                utilizator.setId(id);
                utilizatorList.add(utilizator);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new page<>(utilizatorList,numberofelements);
    }

    public Utilizator findbyName(String username, String password)
    {
        System.out.println(username.length());
        System.out.println(password.length());
        try(Connection connection =DriverManager.getConnection(this.Url,this.User,this.Password);
        PreparedStatement statement = connection.prepareStatement("select * from users where username=? and pass=?")) {
            statement.setString(1,username);
            statement.setString(2,password);
            System.out.println(username+'\n');
            System.out.println(password+'\n');
            ResultSet set= statement.executeQuery();

            if(set.next())
            {
                Long id = set.getLong("user_id");
                String firstName = set.getString("first_name");
                String lastName = set.getString("last_name");
                String pasd = set.getString("pass");
                System.out.println(pasd +" " + password);
                Utilizator entity = new Utilizator(firstName,lastName);
                entity.setId(id);
                return entity;
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

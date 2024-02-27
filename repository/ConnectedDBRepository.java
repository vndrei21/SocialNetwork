package com.example.lab8.repository;

import com.example.lab8.domain.Utilizator;
import javafx.scene.input.Dragboard;

import java.security.PublicKey;
import java.sql.*;
import java.util.Optional;

public class ConnectedDBRepository implements Repository<Long, Utilizator> {

    private String url;
    private String user;
    private String password;
    public ConnectedDBRepository(String Url,String user,String password)
    {
        this.url = Url;
        this.user = user;
        this.password = password;
    }
    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        try(Connection connection=DriverManager.getConnection(this.url,this.user,this.password);
        PreparedStatement statement = connection.prepareStatement("select * from Connected")){
            ResultSet set =statement.executeQuery();
            if(set.next())
            {
                var id = set.getLong("user_id");
                var first_name = set.getString("first_name");
                var last_name = set.getString("last_name");
                Utilizator utilizator = new Utilizator(first_name,last_name);
                utilizator.setId(id);
                return Optional.ofNullable(utilizator);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Utilizator> findAll() {
        return null;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        try(Connection connection = DriverManager.getConnection(this.url,this.user,this.password);
            PreparedStatement statement = connection.prepareStatement("insert into Connected values(?,?,?)")) {
            statement.setInt(1,entity.getId().intValue());
            statement.setString(2,entity.getFirstName());
            statement.setString(3,entity.getLastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
             PreparedStatement statement = connection.prepareStatement("delete from Connected")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        return Optional.empty();
    }
}

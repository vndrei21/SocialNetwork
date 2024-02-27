package com.example.lab8.repository;

import com.example.lab8.domain.Message;
import com.example.lab8.domain.Tuple;
import com.example.lab8.domain.validators.MessageValidator;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBrepository implements Repository<Long, Message>{

    private final String Url;
    private MessageValidator validator;
    private final String User;
    private final String password;

    public MessageDBrepository(String url, String user, String password,MessageValidator validator)
    {
        this.Url = url;
        this.User= user;
        this.password = password;
        this.validator =validator;
    }

    @Override
    public Optional<Message> findOne(Long aLong) {
        return Optional.empty();

    }

    @Override
    public Iterable<Message> findAll() {

        return null;
    }

    @Override
    public Optional<Message> save(Message entity) {
        entity.getTo().forEach(recievers-> {
            try(Connection connection = DriverManager.getConnection(Url,User,password);
                PreparedStatement statement = connection.prepareStatement("insert into message(sent_by,recieved_by,times,msg) values (?,?,?,?);")) {

                statement.setInt(1,Math.toIntExact(entity.getFrom().getId()));
                statement.setInt(2,Math.toIntExact(recievers.getId()));
                statement.setDate(3, Date.valueOf(LocalDate.now()));
                statement.setString(4,entity.getMesagge());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
        return Optional.of(entity);

    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
    public List<Tuple<Long,String>> conversation(Long from, Long to)
    {
        List<Tuple<Long,String>> conv = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Url,User,password);
        PreparedStatement statement = connection.prepareStatement("select sent_by,msg from message where (sent_by=? and recieved_by=?) or (sent_by = ? and recieved_by=?)order by times")) {
            statement.setInt(1,Math.toIntExact(from));
            statement.setInt(2,Math.toIntExact(to));
            statement.setInt(3,Math.toIntExact(to));
            statement.setInt(4,Math.toIntExact(from));
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next())
            {

                //var id = resultSet.getLong("id_msg");
                var sent_by = resultSet.getLong("sent_by");
                var msg = resultSet.getString("msg");
  //            var time  = LocalDateTime.of(resultSet.getDate("time").toLocalDate(), LocalTime.now());
                Tuple<Long,String> mesaj;
                if(sent_by== from)
                     mesaj = new Tuple<>(from,msg);
                else
                    mesaj = new Tuple<>(to,msg);
                conv.add(mesaj);
            }
            return conv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.lab8.repository;


import com.example.lab8.domain.RequestStatus;
import com.example.lab8.domain.Requests;
import com.example.lab8.domain.Tuple;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestDBRepository implements Repository<Tuple<Long,Long>, Requests> {


    private final String Url;
    private final String User;
    private final String Password;

    public RequestDBRepository(String url, String user, String password) {
        Url = url;
        User = user;
        Password = password;
    }

    @Override
    public Optional<Requests> findOne(Tuple<Long, Long> longLongTuple) {

        try (Connection connection = DriverManager.getConnection(Url, User, Password);
             PreparedStatement statement = connection.prepareStatement("select too,fromm,status from request where too=? and fromm =?")) {
            statement.setInt(1, Math.toIntExact(longLongTuple.getRight()));
            statement.setInt(2, Math.toIntExact(longLongTuple.getLeft()));
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                var to = (long) set.getInt("too");
                var from = (long) set.getInt("fromm");
                RequestStatus status = RequestStatus.valueOf(set.getString("status").toUpperCase());
                Requests requests = new Requests(status, from, to);
                return Optional.of(requests);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Requests> findAll() {
        List<Requests> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Url, User, Password);
             PreparedStatement statement = connection.prepareStatement("select * from request;")) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                var to = set.getLong("too");
                var from = set.getLong("fromm");
                var statut = RequestStatus.valueOf(set.getString("status").toUpperCase());
                list.add(new Requests(statut, from, to));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Requests> save(Requests entity) {
        try (Connection connection = DriverManager.getConnection(Url, User, Password);
             PreparedStatement statement = connection.prepareStatement("insert into request(too,fromm,status) values(?,?,?)")) {
            statement.setInt(1, Math.toIntExact(entity.getTo()));
            statement.setInt(2, Math.toIntExact(entity.getFrom()));
            statement.setString(3, entity.getStatus().toString());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Requests> delete(Tuple<Long, Long> longLongTuple) {
        try (Connection connection = DriverManager.getConnection(Url, User, Password);
             PreparedStatement statement = connection.prepareStatement("Delete from requests where (too=? and fromm=?)")) {
            statement.setInt(1, Math.toIntExact(longLongTuple.getLeft()));
            statement.setInt(2, Math.toIntExact(longLongTuple.getRight()));
            statement.executeQuery();
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Requests> update(Requests entity) {
        try (Connection connection = DriverManager.getConnection(Url, User, Password);
             PreparedStatement statement = connection.prepareStatement("update request set status=? where too=? and fromm=?")) {
            statement.setString(1, entity.getStatus().toString());
            statement.setInt(2, Math.toIntExact(entity.getId().getRight()));
            statement.setInt(3, Math.toIntExact(entity.getId().getLeft()));
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

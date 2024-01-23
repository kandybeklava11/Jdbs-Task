package peaksoft.dao;

import peaksoft.model.User;

import java.util.List;

public class UserDaoJdbcImpl implements UserDao {

    public UserDaoJdbcImpl() {

    }

    public void createUsersTable() {
    String sql= """ create table if not exists User(
            id serial primary key,
            name varchar,
            last_name varchar,
            age int)
            """;
    Statement statement;
    try {
       statement=connection.createStatement();
       statement.executeUpdate(sql);
        System.out.println("Table successfully created! ");
        statement.close();
    }catch (SQLException e){
        throw new RuntimeException(e);
    }  }

    public void dropUsersTable() {
    String sql= """ drop table if not exists User
            """;
    Statement statement;
    try {
        statement=connection.createStatement();
        statement.executeUpdate(sql);
        System.out.println("Successfully droped!");
        statement.close();
    }catch (SQLException e){
        throw new RuntimeException(e);
    }  }

    public void saveUser(String name, String lastName, byte age) {
        PreparedStatement preparedStatement;

        String sql= """
                insert into User(name,lastName,age)
                values(?,?,?)
                """;

        try {

            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setInt(3,age);
            preparedStatement.executeUpdate();
            System.out.println("Successfully saved!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeUserById(long id) {
        PreparedStatement preparedStatement;

        String sql= """ 
                delete from User where id=?
                """;
        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,id);
            int check=preparedStatement.executeUpdate();
            if(check>0){
                System.out.println("Deleted user with id"+id);
            }else{
                System.out.println("Not fount");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            String sql = """
                    select * from User
                   """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving users from database", e);
        }

        return users;

    }


    public void cleanUsersTable() {
        Statement statement;

        String sql= """
                delete * from User
                """;
        try {
            statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully deleted!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
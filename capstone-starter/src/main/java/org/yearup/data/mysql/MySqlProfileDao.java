package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@SuppressWarnings("unused")
@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {

        try (Connection connection = getConnection()) {
            String sql = """
                    SELECT *
                    FROM profiles
                    WHERE user_id = ?""";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();
            if (results.next())
                return mapProfile(results);

        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    @Override
    public void update(int userId, Profile profile) {
        String sql = """
                UPDATE profiles
                SET first_name = ?,
                last_name = ?,
                phone = ?,
                email = ?,
                address = ?,
                city = ?,
                state = ?,
                zip = ?
                WHERE user_id = ?;""";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getEmail());
            statement.setString(5, profile.getAddress());
            statement.setString(6, profile.getCity());
            statement.setString(7, profile.getState());
            statement.setString(8, profile.getZip());
            statement.setInt(9, userId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Profile mapProfile(ResultSet results) throws SQLException {
        int id = results.getInt("user_id");
        String firstName = results.getString("first_name");
        String lastName = results.getString("last_name");
        String phone = results.getString("phone");
        String email = results.getString("email");
        String address = results.getString("address");
        String city = results.getString("city");
        String state = results.getString("state");
        String zip = results.getString("zip");

        return new Profile(id, firstName, lastName, phone, email, address, city, state, zip);
    }

}

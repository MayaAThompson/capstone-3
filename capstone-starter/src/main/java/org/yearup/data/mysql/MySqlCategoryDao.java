package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{

    @Autowired
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();
        try(Connection connection = getConnection()) {
            String query = """
                    SELECT *
                    FROM categories;""";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                categories.add(mapRow(results));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        Category category;
        try(Connection connection = getConnection()) {
            String query = """
                    SELECT *
                    FROM categories
                    WHERE category_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            ResultSet results = statement.executeQuery();
            results.next();
            category = mapRow(results);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try(Connection connection = getConnection()) {
            String insert = """
                    INSERT INTO categories (name, description)
                    VALUES (?, ?);""";
            PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int primaryKey = keys.getInt(1);
            category.setCategoryId(primaryKey);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        try(Connection connection = getConnection()) {
            String update = """
                    UPDATE categories
                    SET name = ?, description = ?
                    WHERE category_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(update);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        try(Connection connection = getConnection()) {

            String delete = """
                    DELETE FROM categories
                    WHERE category_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(delete);
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category(categoryId, name, description);
    }
}

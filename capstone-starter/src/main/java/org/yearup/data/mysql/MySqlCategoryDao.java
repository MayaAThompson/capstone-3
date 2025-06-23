package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();
        try {
            Connection connection = getConnection();
            String query = """
                    SELECT *
                    FROM categories;""";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                int id = results.getInt("category_id");
                String name = results.getString("name");
                String description = results.getString("description");
                categories.add(new Category(id, name, description));
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
        try {
            Connection connection = getConnection();
            String query = """
                    SELECT *
                    FROM categories
                    WHERE category_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, categoryId);
            ResultSet results = statement.executeQuery();
            results.next();
            int id = results.getInt("category_id");
            String name = results.getString("name");
            String description = results.getString("description");
            category = new Category(id, name, description);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try {
            Connection connection = getConnection();
            String create = """
                    INSERT INTO categories (name, description)
                    VALUES (?, ?);""";
            PreparedStatement statement = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
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
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}

package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    protected static Product mapRow(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        try (Connection connection = getConnection()) {
            String query = """
                    SELECT *
                    FROM shopping_cart
                    JOIN products
                    WHERE shopping_cart.product_id = products.product_id
                    AND shopping_cart.user_id = ?""";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();

            ShoppingCart cart = new ShoppingCart();

            while (results.next()) { //add new product, map from results, convert to ShoppingCartProduct, add to cart.
                Product product = mapRow(results);
                ShoppingCartItem cartItem = new ShoppingCartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(results.getInt("quantity"));
                cart.add(cartItem);
            }

            return cart;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void add(int userId, int productId) {
        try (Connection connection = getConnection()) {
            String sql = """
                    INSERT INTO shopping_cart (user_id, product_id, quantity)
                    VALUES (?, ?, 1);""";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(int userId, int productId, int quantity) {

        try (Connection connection = getConnection()) {
            String sql = """
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ? AND product_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, userId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(int userId) {

        try (Connection connection = getConnection()) {
            String sql = """
                    DELETE FROM shopping_cart
                    WHERE user_id = ?;""";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

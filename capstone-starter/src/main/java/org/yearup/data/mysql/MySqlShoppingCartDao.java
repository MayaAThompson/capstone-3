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

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoppingCart add(ShoppingCart cart, ShoppingCartItem item) {

        return null;
    }

    @Override
    public ShoppingCart update(ShoppingCart cart, ShoppingCartItem item, int quantity) {

        return null;
    }

    @Override
    public ShoppingCart delete(ShoppingCart cart) {

        return null;
    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
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
}

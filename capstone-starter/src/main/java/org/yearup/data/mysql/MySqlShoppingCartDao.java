package org.yearup.data.mysql;

import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        return null;
    }

    @Override
    public void add(ShoppingCart cart, ShoppingCartItem item) {

    }

    @Override
    public void update(ShoppingCart cart, ShoppingCartItem item, int quantity) {

    }

    @Override
    public void delete(ShoppingCart cart) {

    }
}

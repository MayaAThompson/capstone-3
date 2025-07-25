package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void add(int userId, int productId);
    void update(int userId, int productId, int quantity);
    void delete(int userId);
    // add additional method signatures here
}

package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    ShoppingCart add(ShoppingCart cart, ShoppingCartItem item);
    ShoppingCart update(ShoppingCart cart, ShoppingCartItem item, int quantity);
    ShoppingCart delete(ShoppingCart cart);
    // add additional method signatures here
}

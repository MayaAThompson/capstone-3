package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface OrderDao {

    void add(ShoppingCart cart);
}

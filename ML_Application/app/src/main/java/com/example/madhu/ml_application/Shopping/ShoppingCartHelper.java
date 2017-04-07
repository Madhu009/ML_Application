package com.example.madhu.ml_application.Shopping;

/**
 * Created by Madhu on 07-04-2017.
 */
import java.util.List;
import java.util.Vector;
import android.content.res.Resources;

import com.example.madhu.ml_application.R;

public class ShoppingCartHelper {

    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    private static List<Product> catalog;
    private static List<Product> cart;

    public static List<Product> getCatalog(Resources res){
        if(catalog == null) {
            catalog = new Vector<Product>();
            catalog.add(new Product("Dead or Alive", res
                    .getDrawable(R.drawable.ic_basket),
                    "Dead or Alive by Tom Clancy with Grant Blackwood", 29.99));
            catalog.add(new Product("Switch", res
                    .getDrawable(R.drawable.ic_basket),
                    "Switch by Chip Heath and Dan Heath", 24.99));
            catalog.add(new Product("Watchmen", res
                    .getDrawable(R.drawable.ic_basket),
                    "Watchmen by Alan Moore and Dave Gibbons", 14.99));
        }

        return catalog;
    }

    public static List<Product> getCart() {
        if(cart == null) {
            cart = new Vector<Product>();
        }

        return cart;
    }

}


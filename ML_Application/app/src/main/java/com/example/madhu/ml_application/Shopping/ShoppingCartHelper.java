package com.example.madhu.ml_application.Shopping;

/**
 * Created by Madhu on 07-04-2017.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.res.Resources;

import com.example.madhu.ml_application.R;

public class ShoppingCartHelper {

    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    private static List<Product> catalog;
    private static Map<Product, ShoppingCartEntry> cartMap = new HashMap<Product, ShoppingCartEntry>();

    public static List<Product> getCatalog(Resources res){
        if(catalog == null) {
            catalog = new Vector<Product>();
            catalog.add(new Product("Fruits", res
                    .getDrawable(R.drawable.ic_basket),
                    "Product Description:Fruits", 29.99));
            catalog.add(new Product("Nutritions", res
                    .getDrawable(R.drawable.itm2),
                    "Product Description:Nutritions", 24.99));
            catalog.add(new Product("Vegetables", res
                    .getDrawable(R.drawable.itm3),
                    "Product Description:Vegetables", 14.99));
            catalog.add(new Product("Drinks and beverages", res
                    .getDrawable(R.drawable.itm4),
                    "Product Description:Drinks and beverages", 19.99));
        }

        return catalog;
    }

    public static void setQuantity(Product product, int quantity) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        // If the quantity is zero or less, remove the products
        if(quantity <= 0) {
            if(curEntry != null)
                removeProduct(product);
            return;
        }

        // If a current cart entry doesn't exist, create one
        if(curEntry == null) {
            curEntry = new ShoppingCartEntry(product, quantity);
            cartMap.put(product, curEntry);
            return;
        }

        // Update the quantity
        curEntry.setQuantity(quantity);
    }

    public static int getProductQuantity(Product product) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        if(curEntry != null)
            return curEntry.getQuantity();

        return 0;
    }

    public static void removeProduct(Product product) {
        cartMap.remove(product);
    }

    public static List<Product> getCartList() {
        List<Product> cartList = new Vector<Product>(cartMap.keySet().size());
        for(Product p : cartMap.keySet()) {
            cartList.add(p);
        }

        return cartList;
    }


}

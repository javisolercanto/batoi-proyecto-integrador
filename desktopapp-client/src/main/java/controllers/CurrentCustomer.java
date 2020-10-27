package controllers;

import dao.CustomersDAO;
import java.util.HashMap;

import models.BatoiLogicCustomer;
import models.BatoiLogicProduct;

/**
 *
 * @author javi & Sergi
 */
public class CurrentCustomer
{
    
    private static BatoiLogicCustomer currentCustomer;
    private static String previousScreen;
    private static String realPassword;
    private static HashMap<Integer, Integer> shoppingCart = new HashMap<>();

    public static final String DELETED = "_$@";

    public static BatoiLogicCustomer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public static void setUser(BatoiLogicCustomer customer) {
        currentCustomer = customer;
    }
    
    public static void setPreviousScreen(String screen) {
        previousScreen = screen;
    }
    
    public static String getPreviousScreen() {
        return previousScreen;
    }
    
    public static void disconnect() {
        currentCustomer = null;
        previousScreen = null;
        realPassword = null;
        shoppingCart = new HashMap<>();
    }

    public static String getRealPassword() {
        return realPassword;
    }

    public static void setRealPassword(String realPassword) {
        CurrentCustomer.realPassword = realPassword;
    }
    
    public static HashMap<Integer, Integer> getShoppingCart() {
        return shoppingCart;
    }
    
    public static void addToCart(BatoiLogicProduct product, int quantity) {
        for (int i = 0; i < quantity; i++) {
//            shoppingCart.add(new ArrayList<>().add(product));
        }
    }
    
    public static void removeFromCart(BatoiLogicProduct product) {
        shoppingCart.remove(product);
    }
    
    public static void removeAllCart() {
        shoppingCart.clear();
    }
    
    public static void refreshCustomer(CustomersDAO dao) throws Exception {
        currentCustomer = dao.findByNickname(currentCustomer.getNickname());
    }
}

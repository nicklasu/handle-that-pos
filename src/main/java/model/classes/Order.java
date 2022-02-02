package model.classes;

import model.interfaces.IOrder;
import java.util.ArrayList;

public class Order implements IOrder {
    private ArrayList<Product> productList = new ArrayList<>();

    @Override
    public ArrayList<Product> getProductList() {
        return this.productList;
    }

    /*public void setArrayList(ArrayList<Product> newOrder) {
        order = newOrder;
    }*/

    @Override
    public boolean addProductToOrder(Product product) {
        try{
            productList.add(product);
            return true;
        }catch(Exception e){
            System.out.println("Error adding a product to the order " + e);
        }
        return false;
    }
}
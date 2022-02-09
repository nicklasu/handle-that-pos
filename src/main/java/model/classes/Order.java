package model.classes;

import model.interfaces.IOrder;
import java.util.ArrayList;

public class Order implements IOrder {
    private ArrayList<Product> productList = new ArrayList<>();
    private int totalPrice = 0;

    @Override
    public ArrayList<Product> getProductList() {
        if(productList.size()==0){
            throw new RuntimeException("No products in the order");
        }
        return this.productList;
    }

    @Override
    public int getTotalPrice() {return totalPrice;}

    @Override
    public boolean addProductToOrder(Product product) {
        try{
            productList.add(product);
            totalPrice += product.getPrice();
            return true;
        }catch(Exception e){
            System.out.println("Error adding a product to the order " + e);
        }
        return false;
    }

    @Override
    public boolean removeProductFromOrder(Product product) {
        if (this.productList.contains(product)) {
            this.productList.remove(product);
            this.totalPrice -= product.getPrice();
            return true;
        } else return false;
    }


}
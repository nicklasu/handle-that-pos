package model.classes;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.ArrayList;

public class Order implements  model.interfaces.IOrder{
    ArrayList<Product> order = new ArrayList<>();

    public ArrayList<Product> getArrayList() {
        return order;
    }

    public void setArrayList(ArrayList<Product> newOrder) {
        order = newOrder;
    }

    @Override
    public boolean addProductToOrder(Product x) {
        try{
            order.add(x);
            return true;
        }catch(Exception e){
            System.out.println("Error adding a product to the order " + e);
        }
        return false;
    }
}
package model.classes;

import java.util.ArrayList;

public class Order {
    ArrayList<Product> order = new ArrayList<Product>();

    public ArrayList<Product> getArrayList(){
        return order;
    }
    public void setArrayList(ArrayList<Product> newOrder){
        order = newOrder;
    }
}

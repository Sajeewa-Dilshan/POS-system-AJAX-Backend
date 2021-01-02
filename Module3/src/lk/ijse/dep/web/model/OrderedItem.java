package lk.ijse.dep.web.model;

import java.util.ArrayList;

public class OrderedItem {

   private ArrayList<String> item;
   private ArrayList<String> qty;
   private double total;

    public OrderedItem() {
    }

    public OrderedItem(ArrayList<String> item, ArrayList<String> qty, double total) {
        this.setItem(item);
        this.setQty(qty);
        this.setTotal(total);
    }


    public ArrayList<String> getItem() {
        return item;
    }

    public void setItem(ArrayList<String> item) {
        this.item = item;
    }

    public ArrayList<String> getQty() {
        return qty;
    }

    public void setQty(ArrayList<String> qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

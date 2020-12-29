package lk.ijse.dep.web.model;

import java.util.List;

public class OrderedItem {

   private List<String> item;
   private List<String> qty;

    public OrderedItem() {
    }

    public OrderedItem(List<String> item, List<String> qty) {
        this.setItem(item);
        this.setQty(qty);
    }


    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

    public List<String> getQty() {
        return qty;
    }

    public void setQty(List<String> qty) {
        this.qty = qty;
    }
}

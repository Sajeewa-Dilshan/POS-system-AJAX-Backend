package lk.ijse.dep.web.model;

import java.util.HashMap;

public class OrderedItem {

    private HashMap<String,String> items;

    public OrderedItem() {
    }

    public OrderedItem(HashMap<String, String> items) {
        this.setItems(items);
    }


    public HashMap<String, String> getItems() {

        return items;
    }

    public void setItems(HashMap<String, String> items) {
        this.items = items;
    }
}

package lk.ijse.dep.web.model;

import java.util.ArrayList;

public class OrderDetail {

    private String cusId;
   private ArrayList<String> itemCode;
   private ArrayList<String> des;
   private ArrayList<Integer> qty;
   private ArrayList<Double> unitPrice;
   private Double totalPrice;
   private String date;

    public OrderDetail() {
    }

    public OrderDetail(String cusId, ArrayList<String> itemCode, ArrayList<String> des, ArrayList<Integer> qty, ArrayList<Double> unitPrice, Double totalPrice, String date) {
        this.setCusId(cusId);
        this.setItemCode(itemCode);
        this.setDes(des);
        this.setQty(qty);
        this.setUnitPrice(unitPrice);
        this.setTotalPrice(totalPrice);
        this.setDate(date);
    }


    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public ArrayList<String> getItemCode() {
        return itemCode;
    }

    public void setItemCode(ArrayList<String> itemCode) {
        this.itemCode = itemCode;
    }

    public ArrayList<String> getDes() {
        return des;
    }

    public void setDes(ArrayList<String> des) {
        this.des = des;
    }

    public ArrayList<Integer> getQty() {
        return qty;
    }

    public void setQty(ArrayList<Integer> qty) {
        this.qty = qty;
    }

    public ArrayList<Double> getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(ArrayList<Double> unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

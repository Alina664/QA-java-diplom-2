package order;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OrdersList {
    private boolean success;
    private ArrayList<OrdersInfo> orders;
    //количество всех заказов
    private int total;
    //количество заказов за день
    private int totalToday;

    public OrdersList(boolean success, ArrayList<OrdersInfo> orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public OrdersList() {
    }
}

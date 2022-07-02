package order;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OrdersInfo {
    private ArrayList<String> ingredients;
    private String _id;
    private String status;
    private int number;
    private String createdAt;
    private String updatedAt;

    public OrdersInfo(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrdersInfo(ArrayList<String> ingredients, String _id, String status, int number, String createdAt, String updatedAt) {
        this.ingredients = ingredients;
        this._id = _id;
        this.status = status;
        this.number = number;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrdersInfo() {

    }
}

package order;

import account.User;
import ingredients.Ingredients;

import java.util.ArrayList;

public class Order {
    private ArrayList<Ingredients> ingredients;
    private String _id;
    private User owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;

    public Order(ArrayList<Ingredients> ingredients, String _id, User owner, String status, String name, String createdAt, String updatedAt, int number, int price) {
        this.ingredients = ingredients;
        this._id = _id;
        this.owner = owner;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
        this.price = price;
    }
}

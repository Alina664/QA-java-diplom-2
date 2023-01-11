package ingredients;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {
    private boolean success;
    private ArrayList<Data> data;
    private ArrayList<String> ingredients;

    public ArrayList<Data> getData() {
        return data;
    }

    public Ingredients(boolean success, ArrayList<Data> data) {
        this.success = success;
        this.data = data;
    }

    public Ingredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }
}

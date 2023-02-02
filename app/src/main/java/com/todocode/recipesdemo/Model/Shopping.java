package com.todocode.recipesdemo.Model;

public class Shopping {
    public String id, quantity, ingredient, recipe_id, chef_id;

    public Shopping(String id, String quantity, String ingredient, String recipe_id, String chef_id) {
        this.id = id;
        this.quantity = quantity;
        this.ingredient = ingredient;
        this.recipe_id = recipe_id;
        this.chef_id = chef_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getChef_id() {
        return chef_id;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }
}

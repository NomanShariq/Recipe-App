package com.todocode.recipesdemo.Model;

public class Ingredient {
    public String id, ingredient, quantity, recipe_id;

    public Ingredient(String id, String ingredient, String quantity, String recipe_id) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.recipe_id = recipe_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }
}


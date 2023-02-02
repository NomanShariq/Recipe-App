package com.todocode.recipesdemo.Model;

public class Step {
    private String id, description, step_number, recipe_id;

    public Step(String id, String description, String step_number, String recipe_id) {
        this.id = id;
        this.description = description;
        this.step_number = step_number;
        this.recipe_id = recipe_id;
    }

    public String getStep_number() {
        return step_number;
    }

    public void setStep_number(String step_number) {
        this.step_number = step_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }
}




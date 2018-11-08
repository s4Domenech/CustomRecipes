package com.example.s4domenech.customrecipes.Model;

import android.graphics.Bitmap;

import java.util.List;

public class Recipe {

    private Bitmap photo;
    private String name;
    private List<String> steps;

    public Recipe(Bitmap photo, String name, List<String> steps) {
        this.photo = photo;
        this.name = name;
        this.steps = steps;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "photo=" + photo +
                ", name='" + name + '\'' +
                ", steps=" + steps +
                '}';
    }
}
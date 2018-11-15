package com.example.s4domenech.customrecipes.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.s4domenech.customrecipes.R;
import com.example.s4domenech.customrecipes.datasource.database.Recipe;
import com.example.s4domenech.customrecipes.usecase.BlobConverter;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    List<Recipe> recipes;
    OnRecipeClicked listener;
    BlobConverter blobConverter;

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView photo;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_recipe);
            this.photo = itemView.findViewById(R.id.iv_recipe);
        }
    }

    public RecipeAdapter(List<Recipe> recipes, BlobConverter blobConverter, OnRecipeClicked listener) {
        this.recipes = recipes;
        this.listener = listener;
        this.blobConverter = blobConverter;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe, parent, false);
        RecipeViewHolder vh = new RecipeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(recipes.get(position).getName());

        Bitmap bm = blobConverter.blobToBitmap(recipes.get(position).getImageBlob());

        holder.photo.setImageBitmap(bm);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.recipeClicked(recipes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface OnRecipeClicked {
        void recipeClicked(Recipe recipe);
    }
}
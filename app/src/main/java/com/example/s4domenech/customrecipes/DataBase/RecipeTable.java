package com.example.s4domenech.customrecipes.DataBase;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = Database.class)
public class RecipeTable extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    int id;

    @Column
    String name;

    @Column
    Blob imageBlob;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }
}
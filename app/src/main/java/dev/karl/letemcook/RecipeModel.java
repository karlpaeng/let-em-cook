package dev.karl.letemcook;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RecipeModel implements Parcelable {
    String title, ingredients, servings, instructions;

    public RecipeModel() {
    }

    public RecipeModel(String title, String ingredients, String servings, String instructions) {
        this.title = title;
        this.ingredients = ingredients;
        this.servings = servings;
        this.instructions = instructions;
    }

    protected RecipeModel(Parcel in) {
        title = in.readString();
        ingredients = in.readString();
        servings = in.readString();
        instructions = in.readString();
    }

    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };

    @Override
    public String toString() {
        return "RecipeModel{" +
                "title='" + title + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", servings='" + servings + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(ingredients);
        parcel.writeString(servings);
        parcel.writeString(instructions);
    }
}

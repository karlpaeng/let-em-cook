package dev.karl.letemcook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {

    public final RecViewInterface recViewInterface;
    private ArrayList<RecipeModel> list;
    Context context;

    public RecAdapter(ArrayList<RecipeModel> list, RecViewInterface recViewInterface, Context context) {
        this.recViewInterface = recViewInterface;
        this.list = list;
        this.context = context;

//        this.list.add(" ");
//        this.list.add(" ");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dish, ingredients, servings;
        private ConstraintLayout cl;

        public MyViewHolder(final View view, RecViewInterface recViewInterface, Context context) {
            super(view);
            dish = view.findViewById(R.id.tvDishName);
            ingredients = view.findViewById(R.id.tvIngredients);
            servings = view.findViewById(R.id.tvServings);

            cl = view.findViewById(R.id.cl02);

            itemView.setOnClickListener(view1 -> {
                if (recViewInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        recViewInterface.onClickItem(pos);

                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recview, parent, false);
        return new RecAdapter.MyViewHolder(itemView, recViewInterface, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.MyViewHolder holder, int position) {
        int lSize = list.size();

        holder.dish.setText(list.get(position).title);
        holder.ingredients.setText(list.get(position).ingredients);
        holder.servings.setText(list.get(position).servings);

        if(position >= lSize-1){
            holder.cl.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

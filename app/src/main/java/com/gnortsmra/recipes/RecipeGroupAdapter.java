package com.gnortsmra.recipes;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecipeGroupAdapter extends BaseAdapter {
    private ArrayList<RecipeGroup> recipe_groups = new ArrayList<RecipeGroup>();
    private static final String TAG = "GNORTSMRA:recipes:RecipeGroupAdapter";

    public RecipeGroupAdapter(Context context) {
    }


    public void addItem(RecipeListAdapter recipes, String name) {
        RecipeGroup rg = new RecipeGroup(name, recipes);
        boolean added = false;
        //Remove old copy if present
        for (int i = 0; i < recipe_groups.size(); i++) {
            if (recipe_groups.get(i).toString().equals(name)) {
                recipe_groups.set(i, rg);
                added = true;
            }
        }
        if (!added)
            recipe_groups.add(rg);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipe_groups.size();
    }

    @Override
    public Object getItem(int position) {
        return recipe_groups.get(position).r;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = new TextView(parent.getContext());
        v.setText(recipe_groups.get(position).toString());
        v.setTextSize(23);
        return v;
    }

    private static class RecipeGroup implements Comparable<Object> {

        RecipeListAdapter r;
        String name;

        public RecipeGroup(String title, RecipeListAdapter recs) {
            r = recs;
            name = title;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(Object another) {
            return this.toString().compareTo(another.toString());
        }

    }

    public void sort() {
        Collections.sort(recipe_groups);
        this.notifyDataSetChanged();
    }
}

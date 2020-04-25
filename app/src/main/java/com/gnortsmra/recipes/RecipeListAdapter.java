package com.gnortsmra.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecipeListAdapter extends BaseAdapter {
	private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	private static final String TAG = "GNORTSMRA:recipes:RecipeListAdapter";
	/*private RequestQueue rq;
	private RequestQueue getRequestQueue() {
		if (rq == null)
			rq = Volley.newRequestQueue(getApplicationContext());
		return rq;
	}*/

	public RecipeListAdapter(Context context) {


	}

	public void addRecipe(Resources resources, String rec_name, JSONArray ingreds,
						  JSONArray directions) throws JSONException {
		//String rec_name = resources.getString(recName1);
		//String instructions = resources.getString(instructions1);
		//String[] ingreds = resources.getStringArray(ingredients1);
		//Log.d(TAG, "Adding recipe " + rec_name);
		ArrayList<String> i = new ArrayList<String>();
		for (int j = 0; j < ingreds.length(); j++) {
			i.add(ingreds.getString(j));
		}
		String instructions = "";
		for (int j = 0; j < directions.length(); j++) {
			instructions += directions.getString(j) + "\n\n";
		}

		this.addItem(new Recipe(rec_name, i, instructions));
		this.notifyDataSetChanged();
	}

	private void addItem(Recipe recipe) {
		recipes.add(recipe);
	}

	@Override
	public int getCount() {
		return recipes.size();
	}

	@Override
	public Object getItem(int position) {
		return recipes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView v = new TextView(parent.getContext());
		v.setText(recipes.get(position).toString());
		v.setTextSize(23);
		return v;
	}

	public void sort() {
		Collections.sort(recipes);
		this.notifyDataSetChanged();
	}

	public static class Recipe implements Comparable<Object> {

		public List<String> ingredients = new ArrayList<String>();
		/**
		 * An array of sample (dummy) items.
		 */
		public String instructions = "";

		public String name = "";

		public Recipe(String name, List<String> ingredients, String instructions) {
			this.name = name;
			this.ingredients = ingredients;
			this.instructions = instructions;
		}


		public String getDetails() {
			String res = name + "\n\n";
			for (String i : ingredients)
				res += i + "\n";
			res += instructions;
			return res;
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

}

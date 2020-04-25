package com.gnortsmra.recipes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gnortsmra.recipes.RecipeListAdapter.Recipe;




/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * RecipeListFragment and the item details
 * (if present) is a RecipeDetailFragment.
 * <p>
 * This activity also implements the required
 * RecipeListFragment.Callbacks interface
 * to listen for item selections.
 */
public class RecipeListActivity extends ListActivity {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	//private boolean mTwoPane;
	private RecipeListAdapter mAdapter;

	private static final String TAG = "GNORTSMRA:recipes:RecipeListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_recipe_list);

		Intent intent = getIntent();
		int group = intent.getIntExtra("position", 0);
		String name = intent.getStringExtra("name");
		mAdapter = RecipeGroupActivity.getRecipeList(group);


		//mAdapter = new RecipeListAdapter(getApplicationContext());
		//addRecipes();
		//Log.d(TAG, "Finished adding recipes");

		setListAdapter(mAdapter);

		ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
									long id) {
				Recipe rec = (Recipe) mAdapter.getItem(position);
				Intent detailIntent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
				detailIntent.putExtra("name", rec.name);
				String[] ingreds = new String[rec.ingredients.size()];
				detailIntent.putExtra("ingredients", rec.ingredients.toArray(ingreds));
				detailIntent.putExtra("instructions", rec.instructions);
				startActivity(detailIntent);
			}

		});

	}

}

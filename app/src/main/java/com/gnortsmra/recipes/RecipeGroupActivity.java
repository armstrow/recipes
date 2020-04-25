package com.gnortsmra.recipes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;




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
public class RecipeGroupActivity extends ListActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;
    private static RecipeGroupAdapter mAdapter;
    private Resources res;
    private RequestQueue mqueue;

    private static final String TAG = "RecipeGroupActivity";

    public static RecipeListAdapter getRecipeList(int position) {
        return (RecipeListAdapter) mAdapter.getItem(position);
    }

    private RequestQueue getRQ() {
        if (mqueue == null)
            mqueue = Volley.newRequestQueue(getApplicationContext());
        return mqueue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipe_list);
        mAdapter = new RecipeGroupAdapter(getApplicationContext());
        res = getApplicationContext().getResources();
        loadRecipes();

        Log.d(TAG, "Finished adding recipes");

        setListAdapter(mAdapter);

        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                Intent detailIntent = new Intent(RecipeGroupActivity.this, RecipeListActivity.class);
                detailIntent.putExtra("position", position);
                detailIntent.putExtra("name", mAdapter.getItem(position).toString());
                startActivity(detailIntent);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load saved ToDoItems, if necessary
        loadRecipes();
    }

    private void loadRecipes() {
        if (mAdapter.getCount() == 0) {
            String[] groups = res.getStringArray(R.array.groups);
            for (String page : groups) {
                loadCachedItems(page);
            }
			/*if (mAdapter.getCount() == 0) {//still empty after load
				addRecipesFromWeb();
			}*/
        }
    }


    private Response.Listener<JSONObject> myResponseListener = new Response.Listener<JSONObject>() {


        @Override
        public void onResponse(JSONObject response) {
            // Parsing json array response
            // loop through each json object
            JSONObject recgroup = (JSONObject) response;
            String groupname = (String) recgroup.keys().next();
            cacheToFile(recgroup, groupname+".json");
            loadCachedItems(groupname);
            mAdapter.sort();
            //hidepDialog();
        }
    };

    private Response.ErrorListener myErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            //Toast.makeText(getApplicationContext(),
            //        error.getMessage(), Toast.LENGTH_SHORT).show();
            //hidepDialog();
        }
    };


    private void addRecipesFromWeb() {
        String[] groups = res.getStringArray(R.array.groups);
        for (String page : groups) {
            getRecipeGroupFromWeb(page);
        }
    }

    private void getRecipeGroupFromWeb(String page) {
        RequestQueue queue = getRQ();
        String url = "https://script.google.com/macros/s/AKfycbyYDhgOPpGDCAfzXXXEWlZ9sQd4pfIHsTyFN6haclmry_z7rKU/exec?filename=" + page;
        //String url ="http://www.gnortsmra.com/Recipes/"+page+".json";
        JsonObjectRequest req = new JsonObjectRequest(url, null, myResponseListener, myErrorListener);
        // Adding request to request queue
        queue.add(req);
    }


    protected void cacheToFile(JSONObject recgroup, String fname) {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(fname, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));
            writer.println(recgroup.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    protected void loadCachedItems(String fname) {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(fname + ".json");
            reader = new BufferedReader(new InputStreamReader(fis));
            JSONObject results = new JSONObject(reader.readLine());

            addRecipesFromJSON(results, fname);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "Refreshed " + fname + " from web",
                    Toast.LENGTH_SHORT
            ).show();
            getRecipeGroupFromWeb(fname);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Reload Recipes");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                addRecipesFromWeb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addRecipesFromJSON(JSONObject recgroup, String groupname) {

        try {
            RecipeListAdapter r = new RecipeListAdapter(getApplicationContext());
            JSONArray recipes = recgroup.getJSONArray(groupname);
            for (int i = 0; i < recipes.length(); i++) {

                JSONObject recipe = (JSONObject) recipes.get(i);

                String name = recipe.getString("recipe_name");
                JSONArray ingreds = recipe.getJSONArray("ingredients");
                JSONArray dirs = new JSONArray();
                try {
                    dirs = recipe.getJSONArray("directions");
                } catch (JSONException e) {
                    //Do Nothing
                }
                r.addRecipe(res, name, ingreds, dirs);
            }
            r.sort(); //Sort Recipes by name
            Log.d(TAG, "Adding item " + groupname);
            //Log.d(TAG, "count: " + mAdapter.getCount());
            mAdapter.addItem(r, groupname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getListView().invalidate();
    }

}

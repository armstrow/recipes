package com.gnortsmra.recipes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a.
 */
public class RecipeDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_detail);

        Intent intent = getIntent();
        String rec_name = intent.getStringExtra("name");
        String rec_ins = intent.getStringExtra("instructions");
        String[] rec_ing = intent.getStringArrayExtra("ingredients");

        ((TextView) this.findViewById(R.id.recipe_name)).setText(rec_name);
        String ing = "";
        if (rec_ing != null) {
            for (String i : rec_ing){
                ing += i + "\n";
            }
        }
        ((TextView) this.findViewById(R.id.ingredients)).setText(ing);
        ((TextView) this.findViewById(R.id.instructions)).setText(rec_ins);

    }
}

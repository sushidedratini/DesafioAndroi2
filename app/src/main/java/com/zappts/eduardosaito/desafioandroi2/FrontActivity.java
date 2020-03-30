package com.zappts.eduardosaito.desafioandroi2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import com.evolve.backdroplibrary.BackdropContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrontActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> itemList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText editTextInsert;

    private Toolbar toolbar;
    private BackdropContainer backdropContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backdrop_main);

        loadSavedData();
        buildRecyclerView();

        toolbar = findViewById(R.id.testToolbar);
        backdropContainer = findViewById(R.id.backdropcontainer);
        editTextInsert = findViewById( R.id.edit_text_insert );
        editTextInsert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditText( "Criando uma nova tarefa!" );
                insertItem();
            }
        });



//      Edit text can be clickable, but not editable... at first
        editTextInsert.setFocusable( false );
        editTextInsert.setClickable( true );

//      Taken from dimen file, that sets the max height which the backdrop can go (downwards)
        int height = this.getResources().getDimensionPixelSize( R.dimen.sneek_height );
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(height)
                .build();

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById( R.id.recyclerView );
        mRecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mAdapter = new ExampleAdapter( itemList );

        mRecyclerView.setLayoutManager( mLayoutManager );
        mRecyclerView.setAdapter( mAdapter );

    }

    public void insertItem() {
        itemList.add(itemList.size(), new ExampleItem( "Line" + itemList.size(), "Line2"));
        mAdapter.notifyItemInserted(itemList.size());
        saveData();
    }

    public void removeItem(ArrayList<ExampleItem> items, int position) {

    }

    //  Function that changes the text when clicked on RV
    public void changeEditText(String text) {
        editTextInsert.setText( text );
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences( "shared preferences", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson( itemList );
        editor.putString( "task list", json );
        editor.apply();
    }

    public void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences( "shared preferences", MODE_PRIVATE );
        Gson gson = new Gson();
        String json = sharedPreferences.getString( "task list", null );
        Type type = new TypeToken<ArrayList<ExampleItem>>() {}.getType();
        itemList = gson.fromJson(json, type);

        if (itemList == null) {
            itemList = new ArrayList<>();
        }

//        itemList = new ArrayList<>();
//        mExampleList.add(new ExampleItem( "Line1", "Line2" ));
//        mExampleList.add(new ExampleItem( "Line3", "Line4" ));
//        mExampleList.add(new ExampleItem( "Line5", "Line6" ));
    }


}

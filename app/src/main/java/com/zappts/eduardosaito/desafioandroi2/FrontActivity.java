package com.zappts.eduardosaito.desafioandroi2;

import android.app.ActionBar;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.evolve.backdroplibrary.BackdropContainer;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrontActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    private Toolbar toolbar;
    private BackdropContainer backdropContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backdrop_main);

        createExampleList();
        buildRecyclerView();

        //buttonInsert = findViewById( R.id.button_insert );
//        buttonInsert.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                insertItem();
//            }
//        });
        toolbar = findViewById(R.id.testToolbar);
        backdropContainer = findViewById(R.id.backdropcontainer);
        editTextInsert = findViewById( R.id.edit_text_insert );
        editTextInsert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
            }
        });

        int height = this.getResources().getDimensionPixelSize( R.dimen.sneek_height );
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(height)
                .build();

    }

    public void insertItem() {
        mExampleList.add(mExampleList.size(), new ExampleItem( "Line" + mExampleList.size(), "Line2"));
        mAdapter.notifyItemInserted(mExampleList.size());
        System.out.println( "Inseriu!" );
    }

    public void changeItem(int position, String text) {
        mExampleList.get( position ).changeText1( text );
        mAdapter.notifyItemChanged( position );
    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
//        mExampleList.add(new ExampleItem( "Line1", "Line2" ));
//        mExampleList.add(new ExampleItem( "Line3", "Line4" ));
//        mExampleList.add(new ExampleItem( "Line5", "Line6" ));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById( R.id.recyclerView );
        mRecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mAdapter = new ExampleAdapter( mExampleList );

        mRecyclerView.setLayoutManager( mLayoutManager );
        mRecyclerView.setAdapter( mAdapter );

        mAdapter.setOnItemClickListener( new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem( position, "Clicked" );
            }
        } );

    }

}

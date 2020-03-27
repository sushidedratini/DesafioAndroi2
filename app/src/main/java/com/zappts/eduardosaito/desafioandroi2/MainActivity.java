package com.zappts.eduardosaito.desafioandroi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_front );

        createExampleList();
        buildRecyclerView();

        //buttonInsert = findViewById( R.id.button_insert );


//        buttonInsert.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                insertItem();
//            }
//        });
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

package com.zappts.eduardosaito.desafioandroi2;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.evolve.backdroplibrary.BackdropContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrontActivity extends AppCompatActivity implements ExampleAdapter.OnEditCListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "FrontActivity";
    public static int backdropHeight;
    public static boolean clickedEdit = false;
    public static int height;

    private ArrayList<ExampleItem> itemList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editTextInsert;
    private EditText editTodo;
    private ImageButton delete_btn;
    private ImageButton save_btn;
    private Toolbar toolbar;
    private BackdropContainer backdropContainer;
    private String[] priorities = {"Concluído", "Fazendo", "À fazer", "Outros"};
    private View viewColor;
    private int pos;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backdrop_main);

        loadSavedData();
        buildRecyclerView();

        toolbar = findViewById(R.id.testToolbar);
        backdropContainer = findViewById(R.id.backdropcontainer);

        delete_btn = findViewById( R.id.delete_btn );
        delete_btn.setVisibility( View.GONE );
        delete_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(itemList, pos );
            }
        } );

        save_btn = findViewById( R.id.save_btn );
        save_btn.setVisibility( View.GONE );
        save_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clickedEdit) {
                    insertItem();
                    editTextInsert.setText( "" );

                } else {
                    updateData(pos);
                    clickedEdit = false;
                    makeLayoutVisible();
                }
                save_btn.setVisibility( View.GONE );
            }
        } );

        viewColor = findViewById( R.id.view_color );
        editTextInsert = findViewById( R.id.edit_text_insert );
        editTextInsert.setFocusable( false );
        editTextInsert.setClickable( true );
        editTextInsert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditText( "Criando uma nova tarefa!" );
                save_btn.setVisibility( View.VISIBLE );
            }
        });
        editTodo = findViewById( R.id.editar_tarefa );

        mSpinner = findViewById( R.id.options_spin );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( R.layout.spinner_item );
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener( this );

        makeLayoutVisible();
        buildBackdrop();

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById( R.id.recyclerView );
        mRecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mAdapter = new ExampleAdapter( itemList, this );
        mRecyclerView.setLayoutManager( mLayoutManager );
        mRecyclerView.setAdapter( mAdapter );
    }

    public void buildBackdrop() {
        // Taken from dimen file, that sets the max height which the backdrop can go (downwards)
        height = getBackdropHeight();
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(height)
                .build();
    }

//    public void buildSpinner(int position) {
//        Spinner drop = findViewById( R.id.options_spin );
//        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>( getBaseContext(), R.layout.spinner_item, priorities );
//        drop.setAdapter( spin_adapter );
//        drop.setSelection( itemList.get( position ).getmPriority() );
//
//        switch(position) {
//            case 0:
//                viewColor.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
//                break;
//            case 1:
//                viewColor.getBackground().setColorFilter( Color.parseColor( "#e56333" ), PorterDuff.Mode.SRC_ATOP );
//                break;
//            case 2:
//                viewColor.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
//                break;
//            case 3:
//                viewColor.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
//                break;
//        }

//        drop.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = findViewById( R.id.custom_spin_item );
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                if (selectedItem.equals( "Concluído" )) {
//                    tv.setText( "Concluído" );
//                    viewColor.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
//                } else if (selectedItem.equals( "Fazendo" )) {
//                    tv.setText( "Fazendo" );
//                    viewColor.getBackground().setColorFilter( Color.parseColor( "#e56333" ), PorterDuff.Mode.SRC_ATOP );
//                } else if (selectedItem.equals( "À fazer" )) {
//                    tv.setText( "À Fazer" );
//                    viewColor.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
//                } else if (selectedItem.equals( "Outros" )) {
//                    tv.setText( "Outros" );
//                    viewColor.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        } );

//    }

    public void insertItem() {
        itemList.add(itemList.size(), new ExampleItem( "Criando uma nova tarefa!", 3));
        mAdapter.notifyItemInserted(itemList.size());
        saveData();
    }

    public void removeItem(ArrayList<ExampleItem> items, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Excluir" );
        builder.setMessage( "Deseja excluir a atividade selecionada?" );
        builder.setPositiveButton( "SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedEdit = false;

                itemList.remove(position);
                mAdapter.notifyItemRemoved(position);
                delete_btn.setVisibility( View.INVISIBLE );
                save_btn.setVisibility( View.INVISIBLE );
                makeLayoutVisible();
                saveData();
                dialog.dismiss();
            }
        } );
        builder.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickedEdit = false;
                delete_btn.setVisibility( View.INVISIBLE );
                save_btn.setVisibility( View.INVISIBLE );
                makeLayoutVisible();
                dialog.dismiss();
            }
        } );
        AlertDialog alert = builder.create();
        alert.show();

    }

    //  Function that changes the text when clicked on RV
    public void changeEditText(String text) {
        editTextInsert.setText( text );
    }

    public void updateData(int position) {
        itemList.get( position ).setmText( editTodo.getText().toString() );
        itemList.get( position ).setmPriority( mSpinner.getSelectedItemPosition() );
        mAdapter.notifyDataSetChanged();
        saveData();
    }

    public void editDataLoad(ArrayList<ExampleItem> items, int position) {
        editTodo.setText( items.get( position ).getmText() );
        mSpinner.setSelection( items.get( position ).getmPriority() );

        switch(items.get(position).getmPriority()) {
            case 0:
                viewColor.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
            case 1:
                viewColor.getBackground().setColorFilter( Color.parseColor( "#f9e843" ), PorterDuff.Mode.SRC_ATOP );
            case 2:
                viewColor.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
            case 3:
                viewColor.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
        }

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
        // itemList = new ArrayList<>();
        // mExampleList.add(new ExampleItem( "Line1", "Line2" ));
        // mExampleList.add(new ExampleItem( "Line3", "Line4" ));
        // mExampleList.add(new ExampleItem( "Line5", "Line6" ));
    }

    public void makeLayoutVisible() {
        LinearLayout ly_create = findViewById( R.id.criar_tarefa_layout );
        LinearLayout ly_edit = findViewById( R.id.editar_tarefa_layout );
        if (clickedEdit == false) {
            ly_create.setVisibility( View.VISIBLE );
            ly_edit.setVisibility( View.GONE );
        } else {
            ly_create.setVisibility( View.GONE );
            ly_edit.setVisibility( View.VISIBLE );
            delete_btn.setVisibility( View.VISIBLE );
        }
    }

    public int getBackdropHeight() {
        backdropHeight = this.getResources().getDimensionPixelSize( R.dimen.sneek_height );
//        if (!clickedEdit) {
//            backdropHeight = this.getResources().getDimensionPixelSize( R.dimen.sneek_height );
//            //Toast.makeText( this.getApplicationContext(), "Chegou aqui 1: "+ backdropHeight, Toast.LENGTH_SHORT ).show();
//        } else {
//            backdropHeight = this.getResources().getDimensionPixelSize( R.dimen.sneek_heigh_edit );
//            //Toast.makeText( this.getApplicationContext(), "Chegou aqui 2: "+ backdropHeight, Toast.LENGTH_SHORT ).show();
//        }
//        //Toast.makeText( this.getApplicationContext(), "Height: "+backdropHeight, Toast.LENGTH_SHORT ).show();

        return backdropHeight;
    }

    @Override
    public void onEditClick(int position) {
        clickedEdit = true;
        // Retrieve data from RecyclerView here!
        pos = position;
        //Log.d( TAG, "onEditClick: Chegouaqui!" );
        delete_btn.setVisibility( View.VISIBLE );
        save_btn.setVisibility( View.VISIBLE );
        makeLayoutVisible();
        editDataLoad( itemList, position );
        //buildBackdrop();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition( position ).toString();
        Toast.makeText( parent.getContext(), selectedItem, Toast.LENGTH_SHORT ).show();
        if (selectedItem.equals( "Concluído" )) {
            viewColor.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
        } else if (selectedItem.equals( "Fazendo" )) {
            viewColor.getBackground().setColorFilter( Color.parseColor( "#f9e843" ), PorterDuff.Mode.SRC_ATOP );
        } else if (selectedItem.equals( "À Fazer" )) {
            viewColor.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
        } else if (selectedItem.equals( "Outros" )) {
            viewColor.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

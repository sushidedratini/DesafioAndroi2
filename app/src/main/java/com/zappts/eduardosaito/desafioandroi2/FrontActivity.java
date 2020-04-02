package com.zappts.eduardosaito.desafioandroi2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.evolve.backdroplibrary.BackdropContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FrontActivity extends AppCompatActivity implements ExampleAdapter.OnEditCListener, AdapterView.OnItemSelectedListener, ExampleAdapter.OnAlarmCListener, TimePickerDialog.OnTimeSetListener {

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
    private String[] priorities = {"Concluída", "Fazendo", "À Fazer", "Incompleta"};
    private View viewColor;
    private int pos;
    private Spinner mSpinner;
    private NotificationHelper mNotificationHelper;

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
                delete_btn.setVisibility( View.GONE );
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

        // Notification and Alarm
        mNotificationHelper = new NotificationHelper(this);

        makeLayoutVisible();
        buildBackdrop();
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById( R.id.recyclerView );
        mRecyclerView.setHasFixedSize( true );
        mLayoutManager = new LinearLayoutManager( this );
        mAdapter = new ExampleAdapter( itemList, this , this);
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

    public void insertItem() {
        itemList.add(itemList.size(), new ExampleItem( "Criando uma nova tarefa!", 2, false));
        mAdapter.notifyItemInserted(itemList.size());
        sendOnChannel( "TO-DO: Nova Tarefa criada!", "Não se esqueça de realizá-la!" );
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
        int spin_sel = mSpinner.getSelectedItemPosition();

        if (spin_sel == 1) {
            for (int i = 0; i < itemList.size(); i++) {
                if (i != position) {
                    if (itemList.get( i ).getmPriority() == spin_sel) {
                        itemList.get( i ).setmPriority( 3 );
                    }
                }
            }
        }

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
        if (!clickedEdit) {
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
        delete_btn.setVisibility( View.VISIBLE );
        save_btn.setVisibility( View.VISIBLE );
        makeLayoutVisible();
        editDataLoad( itemList, position );
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set( Calendar.HOUR_OF_DAY, hourOfDay );
        c.set( Calendar.MINUTE, minute );
        c.set(Calendar.SECOND, 0);

        startAlarm(c);
    }

    @Override
    public void onAlarmClick(int position) {
        if (itemList.get( position ).ismAlarmSet()) {
            cancelAlarm();
        } else {
            DialogFragment timePicker = new TimerPickFragment();
            timePicker.show( getSupportFragmentManager(), "time fragment" );
        }
    }

    public void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Intent intent = new Intent( this, AlertReceiver.class );
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 1, intent, 0 );

        alarmManager.setExact( AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent );
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Intent intent = new Intent( this, AlertReceiver.class );
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 1, intent, 0 );

        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition( position ).toString();
        //Toast.makeText( getApplicationContext(), selectedItem, Toast.LENGTH_SHORT ).show();
        if (!selectedItem.equals( itemList.get( pos ).toString() )) {
            if (selectedItem.equals( "Concluída" )) {
                viewColor.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
            } else if (selectedItem.equals( "Fazendo" )) {
                viewColor.getBackground().setColorFilter( Color.parseColor( "#f9e843" ), PorterDuff.Mode.SRC_ATOP );
            } else if (selectedItem.equals( "À Fazer" )) {
                viewColor.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
            } else if (selectedItem.equals( "Incompleta" )) {
                viewColor.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void sendOnChannel(String title, String message) {
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification( title, message );
        mNotificationHelper.getManager().notify( 1, nb.build() );
    }



}

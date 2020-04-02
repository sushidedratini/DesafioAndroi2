package com.zappts.eduardosaito.desafioandroi2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public ArrayList<ExampleItem> itemList;

    private OnEditCListener mOnEditCListener;
    private OnAlarmCListener mOnAlarmCListener;
    private String items[] = {"Concluído", "Fazendo", "À fazer", "Outros"};

    public static class ExampleViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView1;
        public ImageButton mEditButton;
        public ImageButton mNotifButton;
        public View mPriorityView;
        OnEditCListener onEditCListener;
        OnAlarmCListener onAlarmCListener;

        public ExampleViewHolder(@NonNull View itemView, OnEditCListener onEditCListener, final OnAlarmCListener onAlarmCListener) {
            super( itemView );
            mPriorityView = itemView.findViewById( R.id.priority_color );
            mTextView1 = itemView.findViewById( R.id.title);
            mEditButton = itemView.findViewById( R.id.edit_btn );
            mNotifButton = itemView.findViewById( R.id.notif_btn );
            this.onEditCListener = onEditCListener;
            this.onAlarmCListener = onAlarmCListener;

            // Click on editbutton to send data to FrontActivity
            mEditButton.setOnClickListener( this );
            mNotifButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAlarmCListener.onAlarmClick( getAdapterPosition() );
                }
            } );
        }

        @Override
        public void onClick(View v) {
            onEditCListener.onEditClick( getAdapterPosition() );

        }

    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList, OnEditCListener onEditCListener, OnAlarmCListener onAlarmCListener) {
        this.itemList = exampleList;
        this.mOnEditCListener = onEditCListener;
        this.mOnAlarmCListener = onAlarmCListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.example_item , parent, false);
        ExampleViewHolder evh  = new ExampleViewHolder( v, mOnEditCListener, mOnAlarmCListener );
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        ExampleItem currentItem = itemList.get(position);
        holder.mTextView1.setText( currentItem.getmText() );
        if (!currentItem.ismAlarmSet()) {
            holder.mNotifButton.setImageResource( R.drawable.ic_notifications_black_24dp );
        } else {
            holder.mNotifButton.setImageResource( R.drawable.ic_notifications_off_black_24dp );
        }
        int pos = currentItem.getmPriority();
        switch(pos) {
            case 0:
                holder.mPriorityView.getBackground().setColorFilter( Color.parseColor( "#33e563" ), PorterDuff.Mode.SRC_ATOP );
                //holder.mPriorityView.setBackgroundColor( Color.parseColor( "#33e563"));
                break;
            case 1:
                holder.mPriorityView.getBackground().setColorFilter( Color.parseColor( "#e56333" ), PorterDuff.Mode.SRC_ATOP );
                //holder.mPriorityView.setBackgroundColor( Color.parseColor( "#e56333"));
                break;
            case 2:
                holder.mPriorityView.getBackground().setColorFilter( Color.parseColor( "#335ce5" ), PorterDuff.Mode.SRC_ATOP );
                //holder.mPriorityView.setBackgroundColor( Color.parseColor( "#335ce5"));
                break;
            case 3:
                holder.mPriorityView.getBackground().setColorFilter( Color.parseColor( "#e5335c" ), PorterDuff.Mode.SRC_ATOP );
                //holder.mPriorityView.setBackgroundColor( Color.parseColor( "#e5335c"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnEditCListener {

        void onEditClick(int position);
    }

    public interface OnAlarmCListener {
        void onAlarmClick(int position);
    }

}

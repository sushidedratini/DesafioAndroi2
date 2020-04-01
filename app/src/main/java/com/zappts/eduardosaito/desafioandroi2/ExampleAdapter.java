package com.zappts.eduardosaito.desafioandroi2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public ArrayList<ExampleItem> mExampleList;

    private OnEditCListener mOnEditCListener;
    private String items[] = {"Concluído", "Fazendo", "À fazer", "Outros"};

    public static class ExampleViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView1;
        public ImageButton mEditButton;
        public ImageButton mDeleteButton;
        public View mPriorityView;
        OnEditCListener onEditCListener;

        public ExampleViewHolder(@NonNull View itemView, OnEditCListener onEditCListener) {
            super( itemView );
            mPriorityView = itemView.findViewById( R.id.priority_color );
            mTextView1 = itemView.findViewById( R.id.title);
            mEditButton = itemView.findViewById( R.id.edit_btn );
            this.onEditCListener = onEditCListener;

            // Click on editbutton to send data to FrontActivity
            mEditButton.setOnClickListener( this );
            //itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            onEditCListener.onEditClick( getAdapterPosition() );
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList, OnEditCListener onEditCListener) {
        this.mExampleList = exampleList;
        this.mOnEditCListener = onEditCListener;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.example_item , parent, false);
        ExampleViewHolder evh  = new ExampleViewHolder( v, mOnEditCListener );
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText( currentItem.getmText() );
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
        return mExampleList.size();
    }

    public interface OnEditCListener {

        void onEditClick(int position);
    }

}

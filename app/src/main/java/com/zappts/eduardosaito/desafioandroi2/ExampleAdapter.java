package com.zappts.eduardosaito.desafioandroi2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    public ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends  RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public Button mButtonRemoveRow;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super( itemView );
            mTextView1 = itemView.findViewById( R.id.title);
            mTextView2 = itemView.findViewById( R.id.subtitle );
            mButtonRemoveRow = itemView.findViewById( R.id.remove_row );

            itemView.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   if (listener != null) {
                       int position = getAdapterPosition();
                       if (position != RecyclerView.NO_POSITION) {
                           listener.onItemClick( position );
                       }
                   }
               }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.example_item , parent, false);
        ExampleViewHolder evh  = new ExampleViewHolder( v, mListener );
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mTextView1.setText( currentItem.getmText1() );
        holder.mTextView2.setText( currentItem.getmText2() );

        holder.mButtonRemoveRow.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                mExampleList.remove(position);
                notifyItemRemoved( position );
                notifyItemRangeChanged(position, mExampleList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}

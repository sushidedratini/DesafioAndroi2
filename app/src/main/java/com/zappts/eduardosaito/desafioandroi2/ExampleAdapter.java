package com.zappts.eduardosaito.desafioandroi2;

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

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends  RecyclerView.ViewHolder {

        public TextView mTextView1;
        public ImageButton mEditButton;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super( itemView );
            mTextView1 = itemView.findViewById( R.id.title);
            mEditButton = itemView.findViewById( R.id.edit_btn );
            mEditButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText( v.getContext(), "Deu certo!", Toast.LENGTH_SHORT ).show();
                }
            } );

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

        //holder.mTextView2.setText( currentItem.getmText2() );

//        holder.mButtonRemoveRow.setOnClickListener( new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent intent = new Intent(v.getContext(), FrontActivity.class);
//                intent.putExtra( "position", position );
//                v.getContext().startActivity( intent );
////                mExampleList.remove(position);
//                notifyItemRemoved( position );
//                notifyItemRangeChanged(position, mExampleList.size());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}

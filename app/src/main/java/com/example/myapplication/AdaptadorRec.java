package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Recordatorio_registrado;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdaptadorRec extends RecyclerView.Adapter<AdaptadorRec.viewHolderRec> {

    ArrayList<Recordatorio_registrado> lista_recordatorio;

    private LayoutInflater layout;
    private Context context;
    final AdaptadorRec.OnNoteListener mOnNoteListener;


    public AdaptadorRec(ArrayList<Recordatorio_registrado> itemList, Context context, OnNoteListener listener) {
        this.layout = layout;
        this.lista_recordatorio = itemList;
        this.mOnNoteListener = listener;
        this.context = context;


    }

    @Override
    public viewHolderRec onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = layout.from(parent.getContext()).inflate(R.layout.lista_items, parent, false);
        viewHolderRec vh = new viewHolderRec(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolderRec holder, int position) {

        holder.idtitulo.setText(lista_recordatorio.get(position).getTitulo());
        holder.iddescripcion.setText(lista_recordatorio.get(position).getDescripcion());
        holder.idIconos.setImageResource(lista_recordatorio.get(position).getIcono());
        holder.bind(lista_recordatorio.get(position),mOnNoteListener);


    }

    @Override
    public int getItemCount() {

        return lista_recordatorio.size();
    }


    public class viewHolderRec extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView idtitulo, iddescripcion;
        ImageView idIconos;
        OnNoteListener onNoteListener;


        public viewHolderRec(View itemView) {
            super(itemView);
            idtitulo = itemView.findViewById(R.id.id_titulo);
            iddescripcion = itemView.findViewById(R.id.id_descripcion);
            idIconos = itemView.findViewById(R.id.idIcono);
            //onNoteListener = mOnNoteListener;


        }

        void bind(final Recordatorio_registrado item,final OnNoteListener listener) {
            idtitulo.setText(item.getTitulo());
            iddescripcion.setText(item.getDescripcion());
            idIconos.setImageResource(item.getIcono());
           //itemView.setOnClickListener(v -> { mOnNoteListener.onNoteClick(item);
           //});


        }
        @Override
        public void onClick(View v) {


        }
    }

    public interface OnNoteListener {
        void onNoteClick(Recordatorio_registrado item);

    }


}

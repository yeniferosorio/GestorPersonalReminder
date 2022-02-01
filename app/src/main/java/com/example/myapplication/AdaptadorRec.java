package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Recordatorio;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRec extends RecyclerView.Adapter<AdaptadorRec.viewHolderRec> {

    List<Recordatorio> lista_recordatorio;
    private LayoutInflater layout;
    private Context context;
    final AdaptadorRec.OnNoteListener mOnNoteListener;


    public AdaptadorRec(ArrayList<Recordatorio> itemList, Context context, OnNoteListener listener) {
        this.layout = layout;
        this.lista_recordatorio = itemList;
        this.mOnNoteListener = listener;
        this.context = context;


    }

    @Override
    public viewHolderRec onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layout.from(parent.getContext()).inflate(R.layout.lista_items, parent, false);
        viewHolderRec vholder = new viewHolderRec(view);
        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolderRec holder, int position) {
        Recordatorio lista= lista_recordatorio.get(position);
        holder.idtitulo.setText(lista.getTitulo());
        holder.iddescripcion.setText(lista.getDescripcion());
        holder.id_hora.setText(lista.getHora().toString());
        holder.id_fecha.setText(lista.getFecha().toString());
        //  holder.idIconos.setImageResource(lista_recordatorio.get(position).getIcono());
        holder.bind(lista_recordatorio.get(position), mOnNoteListener);
    }

    @Override
    public int getItemCount() {

        return lista_recordatorio.size();
    }


    public class viewHolderRec extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView idtitulo, iddescripcion,id_hora,id_fecha;


        // ImageView idIconos;
        OnNoteListener onNoteListener;

        //1
        public viewHolderRec(View itemView) {
            super(itemView);
            idtitulo = itemView.findViewById(R.id.id_titulo);
            iddescripcion = itemView.findViewById(R.id.id_descripcion);
            id_hora=itemView.findViewById(R.id.id_hora);
            id_fecha=itemView.findViewById(R.id.id_fecha);
            //   idIconos = itemView.findViewById(R.id.idIcono);
            //onNoteListener = mOnNoteListener;


        }

        void bind(final Recordatorio item, final OnNoteListener listener) {
            idtitulo.setText(item.getTitulo());
            iddescripcion.setText(item.getDescripcion());
            id_hora.setText(item.getHora().toString());
            id_fecha.setText(item.getFecha().toString());

            // idIconos.setImageResource(item.getIcono());
            itemView.setOnClickListener(v -> {
                mOnNoteListener.onNoteClick(item);
            });


        }

        @Override
        public void onClick(View v) {


        }
    }

    public interface OnNoteListener {
        void onNoteClick(Recordatorio item);

    }


}

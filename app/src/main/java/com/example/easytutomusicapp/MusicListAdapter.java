package com.example.easytutomusicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>{

    ArrayList<AudioModel> songsList;
    ArrayList<AudioModel> listaOriginal;
    Context context;

    //constructor para lo que vamos a recibir
    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(songsList);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.ViewHolder(view);
    }



    public void filtrado(String txtBuscar){
        int longitud = txtBuscar.length();
        if (longitud == 0)
        {
            songsList.clear();
            songsList.addAll(listaOriginal);
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<AudioModel> collection = songsList.stream().filter(i -> i.getTitle().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                songsList.clear();
                songsList.addAll(collection);
            }else{
                for (AudioModel c: listaOriginal) {
                    if (c.getTitle().toLowerCase().contains(txtBuscar.toLowerCase())){
                        songsList.clear();
                        songsList.add(c);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AudioModel songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        holder.album.setText(songData.getAlbum());
        holder.musicfav.setImageResource(songData.getFav());
        //Log.e("albumid", String.valueOf(songData.getIdAlbum()));
        //Log.e("uri de imagen",songData.getImage());
        Uri imagen = Uri.parse(songData.getImage());
        //holder.cancionEnproceso.setText(songData.getTitle());
        //holder.artistaEnproceso.setText(songData.getAlbum());


        if (imagen!= null  ){
            holder.iconImageView.setImageURI(imagen);
            if (holder.iconImageView.getDrawable() == null)
            {
                holder.iconImageView.setImageResource(R.drawable.music_icon);
            }
        }

        if(MyMediaPlayer.currentIndex==position){
            //canción que se está reproduciendo

            holder.titleTextView.setTextColor(Color.parseColor("#FDFEFE"));
            holder.album.setTextColor(Color.parseColor("#FDFEFE"));
            holder.fondo.setBackgroundColor(Color.parseColor("#173040"));
        }else{
            //demás canciones
            holder.titleTextView.setTextColor(Color.parseColor("#FDFEFE"));
            holder.album.setTextColor(Color.parseColor("#00BCD4"));
            holder.fondo.setBackgroundColor(Color.parseColor("#1F1F1F"));

            //holder.cancionEnproceso.setText(songData.getTitle());
            //holder.artistaEnproceso.setText(songData.getAlbum());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to another acitivty
                // se llama al musicPlayer activity
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",songsList);
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.musicfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songData.getFav()==R.drawable.heart_empty)
                {
                    songData.setFav(R.drawable.heart_filled);
                    holder.musicfav.setImageResource(R.drawable.heart_filled);
                }else
                {
                    songData.setFav(R.drawable.heart_empty);
                    holder.musicfav.setImageResource(R.drawable.heart_empty);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView album;
        ImageView iconImageView;
        ImageView musicfav;
        RelativeLayout fondo;



        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.music_title_text);
            iconImageView = itemView.findViewById(R.id.icon_view);
            musicfav = itemView.findViewById(R.id.fav_music);
            album = itemView.findViewById(R.id.album);
            fondo = itemView.findViewById(R.id.fondo);

            //cancionEnproceso = itemView.findViewById(R.id.cancionEncurso);
            //artistaEnproceso = itemView.findViewById(R.id.artistaEnCurso);
            //iconProceso = itemView.findViewById(R.id.imagenEnProceso);
        }
    }
}

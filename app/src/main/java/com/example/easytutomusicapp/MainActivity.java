package com.example.easytutomusicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscar;
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    MusicListAdapter adapter;
    TextView cancionEnproceso;
    TextView artistaEnproceso;
    ImageView iconProceso;
    AudioModel current;
    ImageView pausaplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_songs_text);
        txtBuscar = findViewById(R.id.txtbuscar);
        cancionEnproceso = findViewById(R.id.cancionEncurso);
        artistaEnproceso = findViewById(R.id.artistaEnCurso);
        iconProceso = findViewById(R.id.imagenEnProceso);
        pausaplay = findViewById(R.id.pausaplay);




        Uri mediaStoreUri;
        if(checkPermission() == false){
            requestPermission();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            mediaStoreUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        }else
        {
            mediaStoreUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        //creamos la proyección
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media._ID
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + "DESC";

        Cursor cursor = getContentResolver().query(mediaStoreUri,projection,null,null,null);
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int durationCoumn =cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
        int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int albumColum = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

        //una ves obtenida la información creamos nuestra lista
        while(cursor.moveToNext()){

            Long id = cursor.getLong(albumIdColumn);
            Log.e("uris del cursor", String.valueOf(cursor));
            Uri albumatwork = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),id);

            AudioModel songData = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2),cursor.getString(3),R.drawable.heart_empty,albumatwork.toString(),cursor.getString(5));
            if(new File(songData.getPath()).exists())
                songsList.add(songData);


        }
        //si no se encuentra ninguna canción lo que se hace es desplegar el texto de error
        if(songsList.size()==0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            //recyclerview es nuestra tabla
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MusicListAdapter(songsList,getApplicationContext());
            recyclerView.setAdapter(adapter);
        }

        txtBuscar.setOnQueryTextListener(this);
        pausaplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
        });

    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("song", String.valueOf(songsList));


        if(recyclerView!=null || songsList.size()!=0){
            adapter.filtrado("");
            //obtener la canción actual

            if (MyMediaPlayer.instance!=null)
            {

                //MyMediaPlayer.currentIndex = 2;
                current = songsList.get(MyMediaPlayer.currentIndex);
                //current = songsList.get(3);
                cancionEnproceso.setText(current.getTitle());
                artistaEnproceso.setText(current.getAlbum());
                iconProceso.setImageURI(Uri.parse(current.getImage()));
            }

       }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        adapter.filtrado(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }
}
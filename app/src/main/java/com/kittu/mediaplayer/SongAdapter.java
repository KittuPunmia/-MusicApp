package com.kittu.mediaplayer;


        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import java.util.ArrayList;

/**
 * Created by user on 02-11-2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {  //provide a binding from an app specified data
    // set to views that are displayed within recycler view

    private ArrayList<SongModel> songs;
    private Context context;
    private OnItemClickListener i;
    SongAdapter(Context context,ArrayList<SongModel> songs){
        this.context=context;
        this.songs=songs;
    }
    public interface OnItemClickListener{
        void OnItemClick(Button b,View v,SongModel obj,int pos);


    }
    public void setOnItemClickListener(OnItemClickListener onitemClickListener){
        this.i=onitemClickListener;
    }

    @Override
    public SongAdapter.SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {  //creates new recyclerview.viewholder
        View v= LayoutInflater.from(context).inflate(R.layout.raw_songs,parent,false); //connect to the particular xml
        return new SongHolder(v);


    }

    @Override
    public void onBindViewHolder(final SongHolder holder, final int pos) {// to update recyclerview.viewholder contents acc to position
        final SongModel c=songs.get(pos);
        holder.songName.setText(c.getSongname());
        holder.artistName.setText(c.getArtistname());
        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i!=null)
                {
                    i.OnItemClick(holder.btnAction,view,c,pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView songName,artistName;
        Button btnAction;
        public SongHolder(View itemview){
            super(itemview);
            songName=(TextView)itemview.findViewById(R.id.tvSongName);
            artistName=(TextView)itemview.findViewById(R.id.tvArtistName);
            btnAction=(Button) itemview.findViewById(R.id.btnAction);
        }

    }
}

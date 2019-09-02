package com.example.exemplofirebird;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.exemplofirebird.model.Note;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private Activity context;
    List<Note> notes;

    public NoteAdapter(Activity context, List<Note> notes) {
        super(context, R.layout.layout_note_adapter, notes);
        this.context = context;
        this.notes = notes;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_note_adapter, null, true);

        TextView txt_title = listViewItem.findViewById(R.id.txt_title);
        TextView txt_note = listViewItem.findViewById(R.id.txt_note);

        Note note = notes.get(position);
        txt_title.setText(note.getTitle());
        txt_note.setText(note.getNote());

        return listViewItem;
    }
}

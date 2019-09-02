package com.example.exemplofirebird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.exemplofirebird.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notes extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private DatabaseReference notesRef;

    private ListView lst_notes;
    private Button btn_novo;

    private ProgressBar progress;

    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        lst_notes = findViewById(R.id.lst_notes);
        btn_novo = findViewById(R.id.btn_novo);

        progress = findViewById(R.id.progress);

        notes = new ArrayList<>();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        notesRef = database.getReference("notes/" + firebaseUser.getUid());

        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recarregarLista(dataSnapshot);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro ao buscar as notas", Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        });

        lst_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = notes.get(position);
                editNote(note);
            }
        });

        lst_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = notes.get(position);
                notesRef.child(note.getId()).removeValue();
                Toast.makeText(getApplicationContext(), "Nota removida", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        btn_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) this.finish();

        return super.onOptionsItemSelected(item);
    }

    private void recarregarLista(DataSnapshot dataSnapshot) {

        notes.clear();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

            Note note = postSnapshot.getValue(Note.class);

            notes.add(note);

        }

        NoteAdapter lancamentoAdapter = new NoteAdapter(Notes.this, notes);

        lst_notes.setAdapter(lancamentoAdapter);

    }

    private void editNote(Note note) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_note, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Editar nota");

        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText edt_title = dialogView.findViewById(R.id.edt_title);
        final EditText edt_note = dialogView.findViewById(R.id.edt_note);
        final Button btn_salvar = dialogView.findViewById(R.id.btn_salvar);

        final String noteId = note.getId();

        edt_title.setText(note.getTitle());
        edt_note.setText(note.getNote());

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Note note = new Note();

                note.setId(noteId);
                note.setTitle(edt_title.getText().toString());
                note.setNote(edt_note.getText().toString());

                notesRef.child(note.getId()).setValue(note);

                alertDialog.dismiss();

                Toast.makeText(Notes.this, "Nota alterada", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();

    }

    private void newNote() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.layout_note, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Nova nota");

        final AlertDialog alertDialog = dialogBuilder.create();

        final EditText edt_title = dialogView.findViewById(R.id.edt_title);
        final EditText edt_note = dialogView.findViewById(R.id.edt_note);
        final Button btn_salvar = dialogView.findViewById(R.id.btn_salvar);

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Note note = new Note();

                note.setId(notesRef.push().getKey());
                note.setTitle(edt_title.getText().toString());
                note.setNote(edt_note.getText().toString());

                notesRef.child(note.getId()).setValue(note);

                alertDialog.dismiss();

                Toast.makeText(Notes.this, "Nota criada", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();

    }

}

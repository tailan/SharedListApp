package com.trucolotecnologia.sharedlistapp.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.trucolotecnologia.sharedlistapp.R;
import com.trucolotecnologia.sharedlistapp.adapters.SharedListArrayAdapter;
import com.trucolotecnologia.sharedlistapp.models.SharedList;

import java.util.ArrayList;

public class SharedListsActivity extends BaseActivity {

    ListView lvSharedLists;
    ArrayList<String> sharedLists;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_lists);
        findViews();
        loadListView();
        setTitle(getString(R.string.app_name));
    }

    private void loadListView() {

        dbRef = getDataBase().child("users").child(getCurrentUser().getUID()).child("lists");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sharedLists = new ArrayList<>();
                for (DataSnapshot list : dataSnapshot.getChildren()) {
                    sharedLists.add(list.getKey());
                }

                lvSharedLists.setAdapter(new SharedListArrayAdapter(SharedListsActivity.this, android.R.layout.simple_list_item_1, sharedLists));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void findViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvSharedLists = findViewById(R.id.lvSharedLists);
        lvSharedLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showMessage(SharedListsActivity.this, "Excluir Lista?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String listSelected = sharedLists.get(position);
                        dbRef.child(listSelected).removeValue();

                        final DatabaseReference listRef = getDataBase().child("lists").child(listSelected);
                        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // get total available quest
                                if (dataSnapshot.getChildrenCount() == 1) ;
                                listRef.removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                return false;
            }
        });

        lvSharedLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listSelected = sharedLists.get(position);
                openActivity(SharedListItemsActivity.class, "listName", listSelected);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModalNewList();
            }
        });
    }

    private void createModalNewList() {
        final EditText edittext = new EditText(SharedListsActivity.this);

        AlertDialog.Builder alert = new AlertDialog.Builder(SharedListsActivity.this);
        edittext.setHint("Digite o nome da lista");
        alert.setTitle("Nova Lista");
        alert.setView(edittext);
        alert.setPositiveButton("Criar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = edittext.getText().toString();
                SharedList list = new SharedList(title);
                getDataBase().child("lists").child(title).setValue(list);
                getDataBase().child("lists").child(title).child("users").child(getCurrentUser().getUID()).setValue(true);
                getDataBase().child("users").child(getCurrentUser().getUID()).child("lists").child(list.title).setValue(true);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


}

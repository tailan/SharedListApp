package com.trucolotecnologia.sharedlistapp.views;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.trucolotecnologia.sharedlistapp.models.Item;
import com.trucolotecnologia.sharedlistapp.models.SharedList;

import java.util.ArrayList;

public class SharedListItemsActivity  extends BaseActivity  {

    ListView lvSharedListItems;
    ArrayList<String> sharedListItems;
    DatabaseReference dbRef;
    String sharedListName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_list_items);

        Intent i = getIntent();
        sharedListName = i.getStringExtra("listName");
        loadListView();
        findViews();

    }

    private void loadListView() {

        dbRef = getDataBase().child("items").child(sharedListName);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sharedListItems = new ArrayList<>();
                for (DataSnapshot list : dataSnapshot.getChildren()) {
                    sharedListItems.add(list.getKey());
                }

                lvSharedListItems.setAdapter(new SharedListArrayAdapter(SharedListItemsActivity.this, android.R.layout.simple_list_item_1, sharedListItems));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void findViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvSharedListItems = findViewById(R.id.lvSharedListItems);
        lvSharedListItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                showMessage(SharedListItemsActivity.this, "Excluir item?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String listSelected = sharedListItems.get(position);
                        dbRef.child(listSelected).removeValue();

                        final DatabaseReference listRef = getDataBase().child("items").child(listSelected);
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

        lvSharedListItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setEnabled(!view.isEnabled());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNewList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createModalNewItem();
            }
        });
    }

    private void createModalNewItem() {
        final EditText edittext = new EditText(SharedListItemsActivity.this);

        AlertDialog.Builder alert = new AlertDialog.Builder(SharedListItemsActivity.this);
        edittext.setHint("Digite o nome do item");
        alert.setTitle("Novo Item");
        alert.setView(edittext);
        alert.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = edittext.getText().toString();
                Item item = new Item(title);
                getDataBase().child("items").child(sharedListName).child(title).setValue(item);
                //TODO: somar o total price no child lists
                //getDataBase().child("lists").child(title).child("users").child(getCurrentUser().getUID()).setValue(true);
                //getDataBase().child("users").child(getCurrentUser().getUID()).child("lists").child(list.title).setValue(true);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


}

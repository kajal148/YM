package com.kajal.ym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private RecyclerView mRecyclerView;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    ArrayList<Places> arrayList = new ArrayList<>();

    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mRecyclerView = (RecyclerView) findViewById(R.id.result_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //for search text change
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
            }
        });

        // for search button click
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mSearchField.getText().toString();
                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }

            }
        });

    }

    private void setAdapter(final String searchString) {
        databaseReference.child("Locations").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for every new search
                arrayList.clear();
                mRecyclerView.removeAllViews();

                int counter = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String name = snapshot.child("name").getValue(String.class);
                    String link = snapshot.child("link").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    if (name.startsWith(searchString)){
                        arrayList.add(new Places(name, link, description));
                        counter++;
                    }

                    //for the top 15 results
                    if (counter  == 15){
                        break;
                    }

                }

                searchAdapter = new SearchAdapter(SearchActivity.this, arrayList, mSearchField,mRecyclerView);
                mRecyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
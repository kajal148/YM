package com.kajal.ym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mCountTextView;
    EditText mCountEditText;

    //Spinner days,nights;
    String[] days ={"1","2","3","4","5","6","7","8","9","10"};
    String[] nights ={"1","2","3","4","5","6","7","8","9","10"};

    RecyclerView gallery,brochure;
    ImageView mGalleryClick, mBrochureClick;
    
    Dialog mDialog;
    EditText mEditField;
    String add_field;
    List<Uri> galleryList = new ArrayList<>();
    List<Uri> brochureList = new ArrayList<>();
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    private List<com.kajal.ym.AddList> mServicesList= new ArrayList<>();
    RecyclerView mServiceRecyclerView;
    private List<com.kajal.ym.AddList> mCategoryList= new ArrayList<>();
    RecyclerView mCategoryRecyclerView;

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
        setContentView(R.layout.activity_main);

        mGalleryClick = findViewById(R.id.icon_gallery);
        mBrochureClick = findViewById(R.id.icon_brochure);
        mDialog = new Dialog(this);

        brochure = findViewById(R.id.brochure);
        gallery = findViewById(R.id.gallery);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mCountEditText =  findViewById(R.id.edit_tripSubject);
        mCountTextView = findViewById(R.id.text_tripCounter);

        mCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int length = mCountEditText.length();
                String convert = String.valueOf(length);
                if(length==0)
                {
                    mCountTextView.setText(" 0/100");
                }else{
                    mCountTextView.setText(" "+convert+"/100");
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });


                    //DAYS AND NIGHTS
            ArrayAdapter<String> days_adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,days);
            AutoCompleteTextView days_act =  (AutoCompleteTextView)findViewById(R.id.spinner_days);
            days_act.setThreshold(1);
            days_act.setAdapter(days_adapter);
            days_act.setTextColor(Color.BLACK);

            ArrayAdapter<String> nights_adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,nights);
            AutoCompleteTextView nights_act =  (AutoCompleteTextView)findViewById(R.id.spinner_nights);
            nights_act.setThreshold(1);
            nights_act.setAdapter(nights_adapter);
            nights_act.setTextColor(Color.BLACK);

            //Search PLACE RECYCLE VIEW LIST

        //mRecyclerView = (RecyclerView) findViewById(R.id.result_list);
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        //getting search place from SEARCHACTIVITY
        Intent intent = getIntent();
        String name = intent.getStringExtra("place");
        mSearchField.setText(name);

        //for search text change
        /*mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mSearchField.getText().toString();
                if (!s.toString().isEmpty()){
                    setAdapter(s.toString());
                }

            }
        });
        */
                //SPINNER FOR DAYS AND NIGHT

        /*days = findViewById(R.id.spinner_days);
        nights = findViewById(R.id.spinner_months);

        List<String> array_days = new ArrayList<>();
        array_days.add(0,"Days");
        array_days.add("1");
        array_days.add("2");
        array_days.add("3");
        array_days.add("4");
        array_days.add("5");
        array_days.add("6");
        array_days.add("7");
        array_days.add("8");
        array_days.add("9");
        array_days.add("10");

        ArrayAdapter<String> arrayAdapter_days = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_days);
        arrayAdapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        days.setAdapter(arrayAdapter_days);
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Days")){
                    String item = parent.getItemAtPosition(position).toString();
                    //optional later remove it
                    //Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        List<String> array_nights= new ArrayList<>();
        array_nights.add(0,"Nights");
        array_nights.add("1");
        array_nights.add("2");
        array_nights.add("3");
        array_nights.add("4");
        array_nights.add("5");
        array_nights.add("6");
        array_nights.add("7");
        array_nights.add("8");
        array_nights.add("9");
        array_nights.add("10");

        ArrayAdapter<String> arrayAdapter_nights = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_nights);
        arrayAdapter_nights.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nights.setAdapter(arrayAdapter_nights);
        nights.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Nights")){
                    String item = parent.getItemAtPosition(position).toString();
                    //optional later remove it
                    //Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/


                                                    // RECYCLE VIEW FOR GALLERY AND BROCHURE

        mGalleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), 5);
            }
        });

        mBrochureClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), 6);
            }
        });


      /*  //for saving all the images
        imageList = new ArrayList<>();
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));*/

        //use setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_brochure = new LinearLayoutManager(this);
        manager_brochure.setOrientation(LinearLayoutManager.HORIZONTAL);
        brochure.setLayoutManager(manager_brochure);

        LinearLayoutManager manager_gallery = new LinearLayoutManager(this);
        manager_gallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallery.setLayoutManager(manager_gallery);


                        //RECYCLE VIEW FOR ADDITIONAL SERVICES
        

        mServiceRecyclerView = findViewById(R.id.services_list);

        mServicesList = new ArrayList<>();
        mServicesList.add(new AddList("Kajal"));

        //use setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_services= new LinearLayoutManager(this);
        manager_services.setOrientation(LinearLayoutManager.HORIZONTAL);
        mServiceRecyclerView.setLayoutManager(manager_services);

        ListAdapterClass services_adapter = new ListAdapterClass(this,mServicesList);
        mServiceRecyclerView.setAdapter(services_adapter);

                    //RECYCLEVIEW FOR CATEGORY

        mCategoryRecyclerView = findViewById(R.id.category_list);

        mCategoryList = new ArrayList<>();
        mCategoryList.add(new AddList("Varma"));

        //use setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_category= new LinearLayoutManager(this);
        manager_category.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCategoryRecyclerView.setLayoutManager(manager_category);

        ListAdapterClass category_adapter = new ListAdapterClass(this,mCategoryList);
        mCategoryRecyclerView.setAdapter(category_adapter);
        
    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case 5:
                if (resultcode == RESULT_OK) {
                    ClipData selectedImages = imagereturnintent.getClipData();
                    if(selectedImages != null) {
                        for(int count =0; count < selectedImages.getItemCount(); count++)
                        galleryList.add(selectedImages.getItemAt(count).getUri());
                    } else if(imagereturnintent.getData() != null){
                        galleryList.add(imagereturnintent.getData());
                    }
                   loadImageViewGallery(galleryList);
                }
                break;
            case 6:
                if(resultcode == RESULT_OK) {
                    ClipData selectedImages = imagereturnintent.getClipData();
                    if (selectedImages != null) {
                        for (int count = 0; count < selectedImages.getItemCount(); count++)
                            brochureList.add(selectedImages.getItemAt(count).getUri());
                    } else if (imagereturnintent.getData() != null) {
                            brochureList.add(imagereturnintent.getData());
                    }
                    loadImageViewBrochure(brochureList);
                }
        }
    }

    public void removeImage(int position,List<Uri> imageList) {
        imageList.remove(position);
        loadImageViewGallery(imageList);
    }

    private void loadImageViewGallery(List<Uri> imageList) {
        AdapterClass adapter = new AdapterClass(this,imageList);
        gallery.setAdapter(adapter);
    }

    private void loadImageViewBrochure(List<Uri> imageList) {
        AdapterClass adapter = new AdapterClass(this,imageList);
        brochure.setAdapter(adapter);
    }



    /*private void setAdapter(final String searchString) {
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

                searchAdapter = new SearchAdapter(MainActivity.this, arrayList, mSearchField,mRecyclerView);
                mRecyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    public void popUpService(View v){
        final Button mButton;
        mDialog.setContentView(R.layout.popup_text);
        mButton = mDialog.findViewById(R.id.btn_submit);
        mEditField = mDialog.findViewById(R.id.edit_services);
        //mEditField.setText("");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_field= mEditField.getText().toString();
                if(add_field.length()!=0){
                    mServicesList.add(new AddList(add_field));
                }
                //Toast.makeText(getApplicationContext(), add_field, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void popUpCategory(View v){
        final Button mButton;
        mDialog.setContentView(R.layout.popup_text);
        mButton = mDialog.findViewById(R.id.btn_submit);
        mEditField= mDialog.findViewById(R.id.edit_services);
        //mEditField.setText("");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_field = mEditField.getText().toString();
                if(add_field.length()!=0){
                    mCategoryList.add(new AddList(add_field));
                }
                //Toast.makeText(getApplicationContext(), add_field, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void tripSubjectTip(View v){
        final ImageView mImageView;
        final TextView mtextView;
        mDialog.setContentView(R.layout.tips_popup);
        mImageView = mDialog.findViewById(R.id.tipButton);
        mtextView= mDialog.findViewById(R.id.tipText);
        mtextView.setText("write the subject of the trip");
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void tripDescriptionTip(View v){
        final ImageView mImageView;
        final TextView mtextView;
        mDialog.setContentView(R.layout.tips_popup);
        mImageView = mDialog.findViewById(R.id.tipButton);
        mtextView= mDialog.findViewById(R.id.tipText);
        mtextView.setText("say a few words about your trip");
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

}

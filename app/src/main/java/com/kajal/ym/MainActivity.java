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
import android.provider.MediaStore;
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

    public final int GALLERY_SELECTION_REQUEST=5;
    public final int BROCHURE_SELECTION_REQUEST=6;

    Button mSubmit;

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

        //getting search place from SEARCHACTIVITY
        Intent intent = getIntent();
        final String locationName = intent.getStringExtra("place");
        mSearchField.setText(locationName);

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

                if (!s.toString().isEmpty() && s.toString().length()>=3 && !s.toString().equalsIgnoreCase(locationName)){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("searchTerm", s.toString());
                    startActivity(intent);
                }
            }
        });
      /*  mSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });*/

        //getting search place from SEARCHACTIVITY
        intent = getIntent();
        String name = intent.getStringExtra("place");
        mSearchField.setText(name);
                                                    // RECYCLE VIEW FOR GALLERY AND BROCHURE

        mGalleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent imgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imgIntent.setType("image/* video/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), GALLERY_SELECTION_REQUEST);
            }
        });

        mBrochureClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent imgIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imgIntent.setType("image/* video/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), BROCHURE_SELECTION_REQUEST);
            }
        });

                //use setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_brochure = new LinearLayoutManager(this);
        manager_brochure.setOrientation(LinearLayoutManager.HORIZONTAL);
        brochure.setLayoutManager(manager_brochure);

        LinearLayoutManager manager_gallery = new LinearLayoutManager(this);
        manager_gallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallery.setLayoutManager(manager_gallery);

        loadImageViewBrochure(brochureList);
        loadImageViewGallery(galleryList);


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

        //SUBMIT BUTTON

        mSubmit = findViewById(R.id.btn_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TravelDetails.class);
                startActivity(intent);
            }
        });
        
    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    Intent imagereturnintent) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent);
        switch (requestcode) {
            case GALLERY_SELECTION_REQUEST:
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
            case BROCHURE_SELECTION_REQUEST:
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

    public void removeImage(int position, List<Uri> imageList, Boolean isBrochure) {

        if(imageList != null){
            imageList.remove(position);
        }
        if(isBrochure) {
            loadImageViewBrochure(imageList);
        } else {
            loadImageViewGallery(imageList);
        }
    }

    private void loadImageViewGallery(List<Uri> imageList) {
        if(imageList != null && imageList.size()>0) {
            AdapterClass adapter = new AdapterClass(this,imageList, false);
            gallery.setAdapter(adapter);
          /*  findViewById(R.id.icon_gallery).setVisibility(View.GONE);
            findViewById(R.id.icon_gallery_aside).setVisibility(View.VISIBLE);*/
        }
    }

    private void loadImageViewBrochure(List<Uri> imageList) {
        if(imageList != null && imageList.size()>0) {
            AdapterClass adapter = new AdapterClass(this, imageList, true);
            brochure.setAdapter(adapter);
            /*findViewById(R.id.icon_brochure).setVisibility(View.GONE);
            findViewById(R.id.icon_brochure_aside).setVisibility(View.VISIBLE);*/
        }

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

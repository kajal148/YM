package com.kajal.ym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner days,nights;
    RecyclerView gallery,brochure;
    List<Image> imageList;
    ImageView mGallery, mBrochure;
    
    Dialog mDialog;
    EditText mEditField;
    String add_field;
    
    private List<com.kajal.ym.AddList> mServicesList= new ArrayList<>();
    RecyclerView mServiceRecyclerView;
    private List<com.kajal.ym.AddList> mCategoryList= new ArrayList<>();
    RecyclerView mCategoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGallery = findViewById(R.id.icon_gallery);
        mBrochure = findViewById(R.id.icon_brochure);
        mDialog = new Dialog(this);

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), 1);
            }
        });

        mBrochure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent imgIntent = new Intent();
                imgIntent.setType("image/*");
                imgIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                imgIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(imgIntent,"Select Photo"), 1);
            }
        });

        days = findViewById(R.id.spinner_days);
        nights = findViewById(R.id.spinner_months);

        List<String> array_days = new ArrayList<>();
        array_days.add(0,"Days");
        array_days.add("1");
        array_days.add("2");
        array_days.add("3");
        array_days.add("4");
        array_days.add("5");

        ArrayAdapter<String> arrayAdapter_days = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_days);
        arrayAdapter_days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        days.setAdapter(arrayAdapter_days);
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Days")){
                    String item = parent.getItemAtPosition(position).toString();
                    //optional later remove it
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
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

        ArrayAdapter<String> arrayAdapter_nights = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_nights);
        arrayAdapter_nights.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nights.setAdapter(arrayAdapter_nights);
        nights.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).equals("Nights")){
                    String item = parent.getItemAtPosition(position).toString();
                    //optional later remove it
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


                                                    // RECYCLE VIEW FOR GALLERY AND BROCHURE
        
        brochure = findViewById(R.id.brochure);
        gallery = findViewById(R.id.gallery);

        //for saving all the images
        imageList = new ArrayList<>();
        imageList.add(new Image(R.drawable.ic_launcher_background));
        imageList.add(new Image(R.drawable.ic_launcher_background));

        //use setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager manager_brochure = new LinearLayoutManager(this);
        manager_brochure.setOrientation(LinearLayoutManager.HORIZONTAL);
        brochure.setLayoutManager(manager_brochure);

        LinearLayoutManager manager_gallery = new LinearLayoutManager(this);
        manager_gallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallery.setLayoutManager(manager_gallery);


        AdapterClass b_adapter = new AdapterClass(this,imageList);
        brochure.setAdapter(b_adapter);

        AdapterClass g_adapter = new AdapterClass(this,imageList);
        gallery.setAdapter(g_adapter);

                        //RECYCLEVIEW FOR ADDITIONAL SERVICES
        

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

        ListAdapterClass services_category = new ListAdapterClass(this,mCategoryList);
        mCategoryRecyclerView.setAdapter(services_adapter);
        
    }

    public void popUpService(View v){
        final Button mButton;
        mDialog.setContentView(R.layout.popup_text);
        mButton = mDialog.findViewById(R.id.btn_submit);
        mEditField = mDialog.findViewById(R.id.edit_services);
        mEditField.setText("");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_field= mEditField.getText().toString();
                mServicesList.add(new AddList(add_field));
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
        mEditField.setText("");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_field= mEditField.getText().toString();
                mCategoryList.add(new AddList(add_field));
                //Toast.makeText(getApplicationContext(), add_field, Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}

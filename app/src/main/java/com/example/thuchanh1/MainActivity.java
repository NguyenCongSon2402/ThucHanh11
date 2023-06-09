package com.example.thuchanh1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thuchanh1.model.ObAdapter;
import com.example.thuchanh1.model.Object;
import com.example.thuchanh1.model.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ObAdapter.ObItemListener {
    public Spinner spinner, mySpinner;
    boolean isAllFieldsChecked = false;
    private int[] imgs = {R.drawable.a, R.drawable.b};
    private RecyclerView recyclerView;
    private ObAdapter obAdapter;
    private EditText txtName, txtlaixuat;
    private Button btnAdd, btnUpdate;
    private CheckBox checkBox;
    private int positionCurrent;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "You selected " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        obAdapter = new ObAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(obAdapter);
        obAdapter.setClickListener(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = new Object();
                String i = spinner.getSelectedItem().toString();
                String name = txtName.getText().toString();
                String hinhthuc = object.OFF;
                if (checkBox.isChecked()) {
                    hinhthuc = object.ONLINE;
                }
                String kyhan = mySpinner.getSelectedItem().toString();
                String laixuat = txtlaixuat.getText().toString();
                int img = R.drawable.a;
                double p = 0;
                try {
                    img = imgs[Integer.parseInt(i)];
                    p = Double.parseDouble(laixuat);
                } catch (NumberFormatException e) {

                }
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    object.setImg(img);
                    object.setHinhthuc(hinhthuc);
                    object.setKyhan(kyhan);
                    object.setLaixuat(p);
                    object.setName(name);
                    obAdapter.add(object);
                }
            }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = new Object();
                String i = spinner.getSelectedItem().toString();
                String name = txtName.getText().toString();
                String kyhan = mySpinner.getSelectedItem().toString();
                String hinhthuc = object.OFF;
                if (checkBox.isChecked()) {
                    hinhthuc = object.ONLINE;

                }
                String laixuat = txtlaixuat.getText().toString();
                int img = R.drawable.a;
                double p = 0;
                try {
                    img = imgs[Integer.parseInt(i)];
                    p = Double.parseDouble(laixuat);
                } catch (NumberFormatException e) {

                }
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    object.setImg(img);
                    object.setHinhthuc(hinhthuc);
                    object.setKyhan("2");
                    object.setLaixuat(p);
                    object.setName(name);
                    obAdapter.Update(positionCurrent, object);
                    btnAdd.setEnabled(true);
                    btnUpdate.setEnabled(false);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Object> filterlist = new ArrayList<>();
                for (Object i : obAdapter.getBackUp()) {
                    if (i.getName().toUpperCase().contains(s.toUpperCase())) {
                        filterlist.add(i);

                    }
                    if (filterlist.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_LONG).show();
                    } else {
                        obAdapter.filterList(filterlist);
                    }
                }
                return false;
            }
        });
    }


    private void initView() {
        spinner = findViewById(R.id.img);
        mySpinner = findViewById(R.id.kyhan);
        SpinnerAdapter adapter = new SpinnerAdapter(this);
        spinner.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.my_spinner_values, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter1);
        recyclerView = findViewById(R.id.recyclerview);
        txtName = findViewById(R.id.txtName);
        txtlaixuat = findViewById(R.id.txtLaiXuat);
        checkBox = findViewById(R.id.hinhthuc);
        //txtDes = findViewById(R.id.txtDes);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setEnabled(false);
        searchView = findViewById(R.id.btnSearch);
    }

    @Override
    public void onItemClick(View view, int position) {
        btnAdd.setEnabled(false);
        btnUpdate.setEnabled(true);
        positionCurrent = position;
        Object object = obAdapter.getItem(position);
        int img = object.getImg();
        int p = 0;
        for (int i = 0; i < imgs.length; i++) {
            if (img == imgs[i]) {
                p = i;
                break;
            }
        }
        spinner.setSelection(p);
        txtName.setText(object.getName());
        if (object.getHinhthuc() == Object.ONLINE)
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
        txtlaixuat.setText(object.getLaixuat() + "");
    }

    private boolean CheckAllFields() {
        if (txtName.length() == 0) {
            txtName.setError("This field is required");
            return false;
        }

        if (txtlaixuat.length() == 0) {
            txtlaixuat.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;
    }
}
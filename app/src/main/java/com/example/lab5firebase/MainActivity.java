package com.example.lab5firebase;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                products.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Product product = postSnapshot.getValue(Product.class);

                    products.add(product);
                }


                ProductList productsAdapter = new ProductList(MainActivity.this, products);

                listViewProducts.setAdapter(productsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addProduct() {

        String name = editTextName.getText().toString().trim();
        double price = Double.parseDouble(String.valueOf(editTextPrice.getText().toString()));


        if (!TextUtils.isEmpty(name)) {


            String id = databaseProducts.push().getKey();


            Product product = new Product(id, name, price);


            databaseProducts.child(id).setValue(product);


            editTextName.setText("");
            editTextPrice.setText("");


            Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    private void updateProduct(String id, String name, double price) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);


        Product product = new Product(id, name, price);
        dR.setValue(product);

        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
    }

    private void deleteProduct(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);


        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
    }

}
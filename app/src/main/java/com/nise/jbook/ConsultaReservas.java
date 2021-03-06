package com.nise.jbook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nise.jbook.Objetos.AdapterReserva;
import com.nise.jbook.Objetos.FirebaseReferences;
import com.nise.jbook.Objetos.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ConsultaReservas extends AppCompatActivity {

    RecyclerView rv;
    List<Reserva> reservas;

    AdapterReserva adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_reservas);

        rv = (RecyclerView) findViewById(R.id.recycler);
        //rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        //rv.setItemAnimator(new DefaultItemAnimator());
        //rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        reservas = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        adapter = new AdapterReserva(reservas);

        rv.setAdapter(adapter);
        Log.i("ADAPTER", "Adapter creado");
        final DatabaseReference proyectoRef = database.getReference(FirebaseReferences.PROYECTO_REFERENCE);
        DatabaseReference reservaRef= proyectoRef.child(FirebaseReferences.RESERVA_REFERENCE);
        Log.i("ADAPTER", "Parent "+ reservaRef.toString());

        reservaRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("ADAPTER", "DataSnapshot"+dataSnapshot.toString());
                reservas.removeAll(reservas);
                Log.i("ADAPTER", "Se removieron las reservas");
                int i = 0;
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()
                     ) {
                    Reserva reserva = snapshot.getValue(Reserva.class);
                    Log.i("ADAPTER","Reserva"+ reserva.getRecurso());
                    reservas.add(reserva);
                    i++;
                }
                adapter.notifyDataSetChanged();
                Log.i("ADAPTER", "Se agregaron las reservas y se notifico" + i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

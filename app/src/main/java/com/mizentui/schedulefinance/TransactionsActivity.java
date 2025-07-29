package com.mizentui.schedulefinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class TransactionsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);

		Intent userIntent = getIntent();
		String username = userIntent.getStringExtra("username");

		setTitle(username);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<Transaction> transactions = new ArrayList<>();
				for (DataSnapshot user : snapshot.getChildren()) {
					if (Objects.equals(user.getKey(), username)) {
						for (DataSnapshot transaction : user.getChildren()) {
							transactions.add(new Transaction(Long.valueOf(Objects.requireNonNull(transaction.getKey())), Long.valueOf(Objects.requireNonNull(transaction.getValue()).toString())));
						}
					}
				}
				Comparator<Transaction> transactionComparator = (o1, o2) -> {
					if (o1.getTime() - o2.getTime() > 0) {
						return 1;
					} else if (o1.getTime() - o2.getTime() < 0) {
						return -1;
					} else {
						return 0;
					}
				};
				transactions.sort(transactionComparator);
				Collections.reverse(transactions);
				ListView transactionsListView = findViewById(R.id.transactionsListView);
				transactionsListView.setAdapter(new TransactionsAdapter(TransactionsActivity.this, transactions));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}
}
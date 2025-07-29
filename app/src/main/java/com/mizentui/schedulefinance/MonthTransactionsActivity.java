package com.mizentui.schedulefinance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class MonthTransactionsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_transactions);

		Intent userIntent = getIntent();
		int month = userIntent.getIntExtra("month", 0);
		int year = userIntent.getIntExtra("year", 0);
		String username = userIntent.getStringExtra("username");
		Calendar monthDate = Calendar.getInstance();
		monthDate.set(Calendar.YEAR, year);
		monthDate.set(Calendar.MONTH, month);
		DateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

		String monthString = monthFormat.format(monthDate.getTime());
		setTitle(String.format("%s%s: %s", monthString.substring(0, 1).toUpperCase(), monthString.substring(1), username));

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<Transaction> transactions = new ArrayList<>();
				for (DataSnapshot user : snapshot.getChildren()) {
					if (Objects.equals(user.getKey(), username)) {
						for (DataSnapshot transaction : user.getChildren()) {
							Calendar currentDate = Calendar.getInstance();
							currentDate.setTimeInMillis(Math.abs(Long.parseLong(Objects.requireNonNull(transaction.getKey()))));
							if (currentDate.get(Calendar.YEAR) == monthDate.get(Calendar.YEAR) && String.valueOf(currentDate.get(Calendar.MONTH)).equals(String.valueOf(monthDate.get(Calendar.MONTH)))) {
								transactions.add(new Transaction(Long.valueOf(Objects.requireNonNull(transaction.getKey())), Long.valueOf(Objects.requireNonNull(transaction.getValue()).toString())));
							}
						}
					}
				}
				Collections.reverse(transactions);
				ListView transactionsListView = findViewById(R.id.monthTransactionsListView);
				transactionsListView.setAdapter(new TransactionsAdapter(MonthTransactionsActivity.this, transactions));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}
}
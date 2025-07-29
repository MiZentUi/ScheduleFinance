package com.mizentui.schedulefinance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);

		Intent usersIntent = getIntent();
		int month = usersIntent.getIntExtra("month", 0);
		int year = usersIntent.getIntExtra("year", 0);
		Calendar monthDate = Calendar.getInstance();
		monthDate.set(Calendar.YEAR, year);
		monthDate.set(Calendar.MONTH, month);
		DateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

		String monthString = monthFormat.format(monthDate.getTime());
		setTitle(String.format("%s%s",monthString.substring(0, 1).toUpperCase(), monthString.substring(1)));

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<User> usersList = new ArrayList<>();
				for (DataSnapshot userInfo : snapshot.getChildren()) {
					LinkedHashMap<Long, Long> transactions = new LinkedHashMap<>();
					boolean userAdd = false;
					for (DataSnapshot transaction : userInfo.getChildren()) {
						transactions.put(Long.valueOf(Objects.requireNonNull(transaction.getKey())), Long.valueOf(Objects.requireNonNull(transaction.getValue()).toString()));
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTimeInMillis(Math.abs(Long.parseLong(Objects.requireNonNull(transaction.getKey()))));
						if (currentDate.get(Calendar.YEAR) == monthDate.get(Calendar.YEAR) && String.valueOf(currentDate.get(Calendar.MONTH)).equals(String.valueOf(monthDate.get(Calendar.MONTH)))) {
							userAdd = true;
						}
					}
					if (userAdd) {
						usersList.add(new User(userInfo.getKey(), transactions));
					}
				}
				TextView totalUsers = findViewById(R.id.totalMonthUsers);
				totalUsers.setText(String.valueOf(usersList.size()));
				ListView usersListView = findViewById(R.id.monthUsersListView);
				usersListView.setAdapter(new UsersAdapter(UsersActivity.this, usersList));
				usersListView.setOnItemClickListener((parent, view1, position, id) -> {
					Intent userIntent = new Intent(UsersActivity.this, MonthTransactionsActivity.class);
					userIntent.putExtra("month", month);
					userIntent.putExtra("year", year);
					userIntent.putExtra("username", usersList.get(position).getUsername());
					startActivity(userIntent);
				});
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}
}
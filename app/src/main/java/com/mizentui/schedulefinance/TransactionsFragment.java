package com.mizentui.schedulefinance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

public class TransactionsFragment extends Fragment {

	public TransactionsFragment() {
		super(R.layout.fragment_transactions);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<User> usersList = new ArrayList<>();
				int totalTransactions = 0;
				for (DataSnapshot userInfo : snapshot.getChildren()) {
					LinkedHashMap<Long, Long> transactions = new LinkedHashMap<>();
					for (DataSnapshot transaction : userInfo.getChildren()) {
						transactions.put(Math.abs(Long.parseLong(Objects.requireNonNull(transaction.getKey()))), Long.valueOf(Objects.requireNonNull(transaction.getValue()).toString()));
						totalTransactions++;
					}
					usersList.add(new User(userInfo.getKey(), transactions));
				}
				TextView totalTransactionsText = view.findViewById(R.id.totalTransactions);
				totalTransactionsText.setText(String.valueOf(totalTransactions));
				LinkedHashSet<MonthTransactions> monthTransactionsSet = new LinkedHashSet<>();
				for (User user : usersList) {
					for (Long time : user.getTransactions().keySet()) {
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTimeInMillis(time);
						monthTransactionsSet.add(new MonthTransactions(currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR)));
					}
				}
				ArrayList<MonthTransactions> monthTransactionsList = new ArrayList<>();
				ArrayList<String> monthList = new ArrayList<>();
				for (MonthTransactions monthTransactions : monthTransactionsSet) {
					int transactionsQuantity = 0;
					for (User user : usersList) {
						for (Long time : user.getTransactions().keySet()) {
							Calendar currentDate = Calendar.getInstance();
							currentDate.setTimeInMillis(time);
							if (currentDate.get(Calendar.YEAR) == monthTransactions.getYear() && String.valueOf(currentDate.get(Calendar.MONTH)).equals(String.valueOf(monthTransactions.getMonth()))) {
								transactionsQuantity++;
							}
						}
					}
					if (!monthList.contains(String.format("%d%02d", monthTransactions.getYear(), monthTransactions.getMonth()))) {
						monthTransactionsList.add(new MonthTransactions(monthTransactions.getMonth(), monthTransactions.getYear(), transactionsQuantity));
						monthList.add(String.format("%d%02d", monthTransactions.getYear(), monthTransactions.getMonth()));
					}
				}
				Comparator<MonthTransactions> monthTransactionsComparator = (o1, o2) -> Integer.parseInt(String.format("%d%02d", o2.getYear(), o2.getMonth())) - Integer.parseInt(String.format("%d%02d", o1.getYear(), o1.getMonth()));
				monthTransactionsList.sort(monthTransactionsComparator);
				ListView transactionsListView = view.findViewById(R.id.transactionsListView);
				if (getActivity() != null) transactionsListView.setAdapter(new MonthAdapter(getActivity(), monthTransactionsList));
				transactionsListView.setOnItemClickListener((parent, view1, position, id) -> {
					Intent usersIntent = new Intent(requireContext(), UsersActivity.class);
					usersIntent.putExtra("month", monthTransactionsList.get(position).getMonth());
					usersIntent.putExtra("year", monthTransactionsList.get(position).getYear());
					startActivity(usersIntent);
				});
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}
}
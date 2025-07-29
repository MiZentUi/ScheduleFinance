package com.mizentui.schedulefinance;

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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Objects;

public class UsersFragment extends Fragment {

	public UsersFragment() {
		super(R.layout.fragment_users);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				ArrayList<User> usersList = new ArrayList<>();
				for (DataSnapshot userInfo : snapshot.getChildren()) {
					LinkedHashMap<Long, Long> transactions = new LinkedHashMap<>();
					for (DataSnapshot transaction : userInfo.getChildren()) {
						transactions.put(Long.valueOf(Objects.requireNonNull(transaction.getKey())), Long.valueOf(Objects.requireNonNull(transaction.getValue()).toString()));
					}
					usersList.add(new User(userInfo.getKey(), transactions));
				}
				Comparator<User> userComparator = Comparator.comparing(User::getUsername);
				usersList.sort(userComparator);
				TextView totalUsers = view.findViewById(R.id.totalUsers);
				totalUsers.setText(String.valueOf(usersList.size()));
				ListView usersListView = view.findViewById(R.id.usersListView);
				if (getActivity() != null) usersListView.setAdapter(new UsersAdapter(getActivity(), usersList));
				usersListView.setOnItemClickListener((parent, view1, position, id) -> {
					Intent userIntent = new Intent(requireContext(), TransactionsActivity.class);
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
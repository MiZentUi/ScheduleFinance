package com.mizentui.schedulefinance;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public class InfoFragment extends Fragment {

	public InfoFragment() {
		super(R.layout.fragment_info);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference databaseReference = database.getReference("Info");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				int totalUsers = 0;
				int totalTransactions = 0;
				int totalDays = 0;
				for (DataSnapshot userInfo : snapshot.getChildren()) {
					totalUsers++;
					for (DataSnapshot transaction : userInfo.getChildren()) {
						totalTransactions++;
						if (Integer.parseInt(Objects.requireNonNull(transaction.getValue()).toString()) > 0) totalDays += Integer.parseInt(Objects.requireNonNull(transaction.getValue()).toString());
					}
				}
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
				TextView totalUsersText = view.findViewById(R.id.totalUsersInfo);
				totalUsersText.setText(String.valueOf(totalUsers));
				TextView totalTransactionsText = view.findViewById(R.id.totalTransactionsInfo);
				totalTransactionsText.setText(String.valueOf(totalTransactions));
				TextView totalProfitText = view.findViewById(R.id.totalProfitInfo);
				totalProfitText.setText(String.format("%s BYN", decimalFormat.format(totalDays * 0.05)));
				TextView koalkoTotalProfitText = view.findViewById(R.id.koalkoTotalProfitInfo);
				koalkoTotalProfitText.setText(String.format("%s BYN", decimalFormat.format(totalDays * 0.02375)));
				TextView mizentuiTotalProfitText = view.findViewById(R.id.mizentuiTotalProfitInfo);
				mizentuiTotalProfitText.setText(String.format("%s BYN", decimalFormat.format(totalDays * 0.01875)));
				TextView fliantressTotalProfitText = view.findViewById(R.id.fliantressTotalProfitInfo);
				fliantressTotalProfitText.setText(String.format("%s BYN", decimalFormat.format(totalDays * 0.0075)));
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});
	}
}
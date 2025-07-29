package com.mizentui.schedulefinance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class UsersAdapter extends ArrayAdapter<User> {

	private final Context context;
	private final ArrayList<User> users;

	public UsersAdapter(Context context, ArrayList<User> users) {
		super(context, R.layout.user_info, users);
		this.context = context;
		this.users = users;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.user_info, parent, false);
		}
		TextView username = convertView.findViewById(R.id.usernameText);
		username.setText(this.users.get(position).getUsername());
		TextView endDaysText = convertView.findViewById(R.id.endDaysText);
		long dayMillis = 86400000;
		LinkedHashMap<Long, Long> currentEndDays = new LinkedHashMap<>();
		for (Map.Entry<Long, Long> entry : users.get(position).getTransactions().entrySet()) {
			if (entry.getKey() + Math.abs(entry.getValue()) * dayMillis > System.currentTimeMillis()) {
					currentEndDays.put(entry.getKey(), Math.abs(entry.getValue()));
			}
		}
		try {
			long endDays = Math.round((double) (new ArrayList<>(currentEndDays.keySet()).get(0) + currentEndDays.values().stream().mapToLong(Long::longValue).sum() * dayMillis - System.currentTimeMillis()) / dayMillis);
			if (endDays > 0) {
				endDaysText.setText(String.valueOf(endDays));
				if (endDays > 10) {
					endDaysText.setTextColor(Color.GREEN);
				} else if (endDays > 5) {
					endDaysText.setTextColor(Color.YELLOW);
				} else {
					endDaysText.setTextColor(Color.RED);
				}
			} else {
				endDaysText.setText("0");
				endDaysText.setTextColor(Color.RED);
			}
		} catch (IndexOutOfBoundsException exception) {
			endDaysText.setText("0");
			endDaysText.setTextColor(Color.RED);
		}
		return convertView;
	}
}
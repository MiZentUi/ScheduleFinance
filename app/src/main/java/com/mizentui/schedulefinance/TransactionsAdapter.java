package com.mizentui.schedulefinance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {

	private final Context context;
	private final ArrayList<Transaction> transactions;

	public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
		super(context, R.layout.transaction_info, transactions);
		this.context = context;
		this.transactions = transactions;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.transaction_info, parent, false);
		}
		DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
		TextView dateText = convertView.findViewById(R.id.dateText);
		TextView daysText = convertView.findViewById(R.id.daysText);
		if (transactions.get(position).getTime() < 0) {
			dateText.setText(dateFormat.format(new Date(-transactions.get(position).getTime())));
			daysText.setText(R.string.unban);
		} else if (transactions.get(position).getDays() <= 0) {
			dateText.setText(dateFormat.format(new Date(transactions.get(position).getTime())));
			daysText.setText(String.format("Gift " + -transactions.get(position).getDays()));
		} else {
			dateText.setText(dateFormat.format(new Date(transactions.get(position).getTime())));
			daysText.setText(String.valueOf(transactions.get(position).getDays()));
		}
		return convertView;
	}
}
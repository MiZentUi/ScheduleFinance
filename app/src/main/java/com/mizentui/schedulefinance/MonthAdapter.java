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
import java.util.Calendar;
import java.util.Locale;

public class MonthAdapter extends ArrayAdapter<MonthTransactions> {

	private final Context context;
	private final ArrayList<MonthTransactions> monthTransactionsList;

	public MonthAdapter(Context context, ArrayList<MonthTransactions> monthTransactions) {
		super(context, R.layout.month_info, monthTransactions);
		this.context = context;
		this.monthTransactionsList = monthTransactions;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.month_info, parent, false);
		}
		TextView monthText = convertView.findViewById(R.id.monthText);
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.YEAR, monthTransactionsList.get(position).getYear());
		currentDate.set(Calendar.MONTH, monthTransactionsList.get(position).getMonth());
		DateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
		String monthString = monthFormat.format(currentDate.getTime());
		monthText.setText(String.format("%s%s",monthString.substring(0, 1).toUpperCase(), monthString.substring(1)));
		TextView quantityText = convertView.findViewById(R.id.quantityText);
		quantityText.setText(String.valueOf(monthTransactionsList.get(position).getTransactionsQuantity()));
		return convertView;
	}
}

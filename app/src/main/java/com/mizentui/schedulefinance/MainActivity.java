package com.mizentui.schedulefinance;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

	public ActionBarDrawerToggle actionBarDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentTransaction firstFragmentTransaction = getSupportFragmentManager().beginTransaction();
		firstFragmentTransaction.replace(R.id.mainFrame, new UsersFragment());
		firstFragmentTransaction.commit();

		DrawerLayout mainDrawer = findViewById(R.id.mainDrawer);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, R.string.open, R.string.close);
		mainDrawer.addDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		NavigationView mainNavigation = findViewById(R.id.mainNavigation);
		mainNavigation.setNavigationItemSelectedListener(item -> {
			if (item.getItemId() == R.id.usersItem) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.mainFrame, new UsersFragment());
				fragmentTransaction.commit();
				mainDrawer.close();
				return true;
			} else if (item.getItemId() == R.id.transactionsItem) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.mainFrame, new TransactionsFragment());
				fragmentTransaction.commit();
				mainDrawer.close();
				return true;
			} else if (item.getItemId() == R.id.infoItem) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.mainFrame, new InfoFragment());
				fragmentTransaction.commit();
				mainDrawer.close();
				return true;
			}
			return false;
		});
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
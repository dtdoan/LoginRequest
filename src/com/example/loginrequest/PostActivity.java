package com.example.loginrequest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PostActivity extends Activity {
    
	Button btnLogout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        createActionBar();
        Intent newIntent = getIntent();
        String username = newIntent.getStringExtra(MainActivity.USERNAME);
        int loginCount = newIntent.getIntExtra(MainActivity.LOGIN_COUNTER, 1);
        
        String messageToDisplay = "Welcome " + username + "\nYou have logged in " + loginCount + " times.";
        TextView displayMessage = (TextView) findViewById(R.id.loginCounter);
        displayMessage.setText(messageToDisplay);
     
        btnLogout=(Button)this.findViewById(R.id.btnlogout);
        
    }    
    
        /** Called when the user clicks the Logout button */
        public void attemptUserLogout(View v) {
    		Intent newIntent = new Intent(this, MainActivity.class);
    		startActivity(newIntent);
    	}
        
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    	private void createActionBar() {
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    			getActionBar().setDisplayHomeAsUpEnabled(true);
    		}
    	}

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
       
}
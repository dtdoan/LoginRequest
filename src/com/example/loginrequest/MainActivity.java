package com.example.loginrequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {
	public final static String USERNAME = "com.example.loginrequest.USERNAME";
	public final static String LOGIN_COUNTER = "com.example.loginrequest.LOGIN_COUNTER";
	
	EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;
    Button btnCancel;
    Button btnAddUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsername=(EditText)this.findViewById(R.id.txtusername);
        txtPassword=(EditText)this.findViewById(R.id.txtpass);
        btnLogin=(Button)this.findViewById(R.id.btnLogin);
        btnAddUser=(Button)this.findViewById(R.id.btnAddUser);
        txtUsername.requestFocus();
        
    }

		     /** Called when the user clicks the Login button */
		    public void attemptLogin(View v) {
		    	 EditText newUsername = (EditText) findViewById(R.id.txtusername);
		 		 String username = newUsername.getText().toString();
		 		 EditText newPassword = (EditText) findViewById(R.id.txtpass);
		 		 String password = newPassword.getText().toString();
		    	/*
		    	 String username = txtUsername.getText().toString();
		    	 String password = txtPassword.getText().toString();
		 		 */
		 		 LoginTask logintasks = new LoginTask();
		 		 String[] params = {username, password, "login"};
		 		 logintasks.execute(params);
		     }
     
     
     
	        /** Called when the user clicks the Add User button */
		     public void attemptAddUser(View v) {
		         EditText newUsername = (EditText) findViewById(R.id.txtusername);
		 		 String username = newUsername.getText().toString();
		 		 EditText newPassword = (EditText) findViewById(R.id.txtpass);
		 		 String password = newPassword.getText().toString();
		 		  		 
		 		 LoginTask tasks = new LoginTask();
		 		 String[] params = {username, password, "add"};
		 		 tasks.execute(params);
		     }
    
		    @Override
		 	public boolean onCreateOptionsMenu(Menu menu) {
		 		getMenuInflater().inflate(R.menu.main, menu);
		 		return true;
		 	}

 	public void messageInterface(String[] result) {
 		TextView defaultMessage = (TextView) findViewById(R.id.defaultMessage);
 		
 		try {
 			
 			JSONObject obj = new JSONObject(result[0]);
 			int errCode = obj.getInt("errCode");
 			
 			if (errCode == 1) {
 				int count = obj.getInt("count");
 				Intent intent = new Intent(this, PostActivity.class);
 				intent.putExtra(USERNAME, result[1]);
 				intent.putExtra(LOGIN_COUNTER, (int) count);
 				startActivity(intent);
 			} else {
 				if (errCode == -1) {
 					defaultMessage.setText("Invalid username and password combination. Please try again.");
 				} else if (errCode == -2) {
 					defaultMessage.setText("This user name already exists. Please try again.");
 				} else if (errCode == -3) {
 					defaultMessage.setText("The user name should be non-empty and at most 128 characters long. Please try again.");
 				} else if (errCode == -4) {
 					defaultMessage.setText("The password should be at most 128 characters long. Please try again.");
 				}
 			}
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}
 	
 	
 	class LoginTask extends AsyncTask<String, Void, String[]> {		
 		@Override
 	    protected String[] doInBackground(String... params) {
 	        String username = params[0];
 	        String password = params[1];
 	        DefaultHttpClient httpClient = null;
 			HttpPost httpPost = null;
 			HttpResponse response = null;

 			try {
 				httpClient = new DefaultHttpClient();
 				
 				// using a groupmate's heroku due to personal heroku malfunctions; approved by Prof.
 				httpPost = new HttpPost(
 						"http://peaceful-anchorage-1078.herokuapp.com/users/"+ params[2]);

 				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
 				nvps.add(new BasicNameValuePair("content-type", "application/json"));

 				StringEntity userinput = new StringEntity(
 						"{\"user\": \"" + username + "\",\"password\": \"" + password + "\"}");
 				userinput.setContentType("application/json");
 				httpPost.setEntity(userinput);

 				for (NameValuePair x : nvps) {
 					httpPost.addHeader(x.getName(), x.getValue());
 				}

 				response = httpClient.execute(httpPost);

 				BufferedReader buffread = new BufferedReader(new InputStreamReader(
 						(response.getEntity().getContent())));
 				
 				String output;
 				String responseMessage = "";
 				while ((output = buffread.readLine()) != null) {
 					responseMessage += output;
 				}
 				String[] finalOutput = new String[2];
 				finalOutput[0] = responseMessage;
 				finalOutput[1] = username;
 				return finalOutput;
 				
 			} catch (MalformedURLException e) {
 				String[] emptyarray = new String[0];
 				return emptyarray;

 			} catch (IOException e) {
 				String[] emptyarray = new String[0];
 				return emptyarray;
 			}
 	    }

 		
 	    @Override
 	    protected void onProgressUpdate(Void... values) {
 	    }
 	    
 	    @Override
	    protected void onPreExecute() {
	    }
 	   
 	    @Override
 	    protected void onPostExecute(String[] result) {
 	    	messageInterface(result);
 	    }

 	}

      
} 
package com.oranz.golagol;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmScore extends Activity {
	
	private TextView tvArenaConfirm;
	private TextView tvQuantityConfirm;
	private TextView tvDateConfirm;
	private Button btCancel;
	private Button btOk;
	
	// FS - Facebook Stuff
//	private static final String APP_ID = "318684344914920";
//	private static final String[] PERMISSIONS = new String[] {"publish_stream"};
//	private static final String TOKEN = "access_token";
//    private static final String EXPIRES = "expires_in";
//    private static final String KEY = "facebook-credentials";
//	private Facebook facebook;
//	private String messageToPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_score);
        
        tvArenaConfirm = (TextView) findViewById(R.id.tvArenaConfirm);
        tvQuantityConfirm = (TextView) findViewById(R.id.tvQuantityConfirm);
        tvDateConfirm = (TextView) findViewById(R.id.tvDateConfirm);
        btCancel = (Button) findViewById(R.id.btCancel);
        btOk = (Button) findViewById(R.id.btOk);
        
        final String arena = SaveScore.getArena();
        tvArenaConfirm.setText(arena);
        final int quantity = SaveScore.getQuantity();
        tvQuantityConfirm.setText("" + quantity);
        final Calendar date = SaveScore.getDate();
        tvDateConfirm.setText(date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.YEAR));

        String nickname = SaveScore.getNickname();
        
    	if(quantity <= 2){
    		Toast.makeText(this, "Só " + quantity + ", " + nickname + "? Que fase hein!", Toast.LENGTH_LONG).show();
    	}else if(quantity == 3){
    		Toast.makeText(this, "Aqui é assim, marcou 3. Pede música.", Toast.LENGTH_LONG).show();
    	}else if(quantity > 9){
    		Toast.makeText(this, quantity + " gols? Póhan. Se isso é estar na pior. Que é que é estar bem então!?", Toast.LENGTH_LONG).show();
    	}
        
        btCancel.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
        btOk.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {
        	SaveScore.registerScoreConfirmed(arena, quantity, date);
        	finish();
        	}
        });
    }
    
	public void postOnFacebook(View view){
        switch (view.getId()) {
        case R.id.ivFacebookLogo:
        	Toast.makeText(this, "Breve", Toast.LENGTH_LONG).show();
        }
	}
        /*
        	
        	// FS - Facebook Stuff
    		facebook = new Facebook(APP_ID);
    		restoreCredentials(facebook);

    		setContentView(R.layout.facebook_dialog);

    		String facebookMessage = getIntent().getStringExtra("facebookMessage");
    		if (facebookMessage == null){
    			facebookMessage = "Testando :)";
    		}
    		messageToPost = facebookMessage;
        	
	    	//Toast.makeText(getApplicationContext(), "Publicado com sucesso.", Toast.LENGTH_LONG).show();	    	
        }
    }
    */
    
    public void openConfig(View view){
        switch (view.getId()) {
        case R.id.ivConfig:	    	
	    	Intent nextScreen = new Intent(getApplicationContext(), Config.class);	    	
	    	startActivity(nextScreen);
        }
    }
    
    /*
    // FS - More Facebook Stuff
	public boolean saveCredentials(Facebook facebook) {
    	Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
    	editor.putString(TOKEN, facebook.getAccessToken());
    	editor.putLong(EXPIRES, facebook.getAccessExpires());
    	return editor.commit();
	}
	
	public boolean restoreCredentials(Facebook facebook) {
    	SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
    	facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
    	facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
    	return facebook.isSessionValid();
	}
	
	protected void facebookIntegration(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.facebook_dialog);

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null){
			facebookMessage = "Test wall post";
		}
		messageToPost = facebookMessage;
	}
	
	public void doNotShare(View button){
		finish();
	}
	
	public void share(View button){
		if (! facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
		}
	}
	
	public void loginAndPostToWall(){
		 facebook.authorize(this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
	}

	public void postToWall(String message){
		Bundle parameters = new Bundle();
                parameters.putString("message", message);
                parameters.putString("description", "topic share");
                try {
        	        facebook.request("me");
			String response = facebook.request("me/feed", parameters, "POST");
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") ||
			        response.equals("false")) {
				showToast("Blank response.");
			}
			else {
				showToast("Message posted to your facebook wall!");
			}
			finish();
		} catch (Exception e) {
			showToast("Failed to post to wall!");
			e.printStackTrace();
			finish();
		}
	}
	
	class LoginDialogListener implements DialogListener {
	    public void onComplete(Bundle values) {
	    	saveCredentials(facebook);
	    	if (messageToPost != null){
			postToWall(messageToPost);
		}
	    }
	    public void onFacebookError(FacebookError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onError(DialogError error) {
	    	showToast("Authentication with Facebook failed!");
	        finish();
	    }
	    public void onCancel() {
	    	showToast("Authentication with Facebook cancelled!");
	        finish();
	    }
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
     */
}

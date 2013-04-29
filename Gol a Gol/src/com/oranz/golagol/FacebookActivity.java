package com.oranz.golagol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookActivity extends Activity implements DialogListener,
        OnClickListener
{

    private Facebook facebookClient;
    private Button btCancel;
    private Boolean isLogged;
    private ImageView btPostToFb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_facebook);//my layout xml
        
        btPostToFb = (ImageView) findViewById(R.id.btPostOnFb);
        btPostToFb.setVisibility(View.INVISIBLE);
        
        isLogged = false;
        
        btCancel = (Button) findViewById(R.id.btCancelFb);
        
        btCancel.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});

    }

    @SuppressWarnings("deprecation")
	public void onComplete(Bundle values)
    {

        if (values.isEmpty())
        {
            //"skip" clicked ?
            return;
        }

        // if facebookClient.authorize(...) was successful, this runs
        // this also runs after successful post
        // after posting, "post_id" is added to the values bundle
        // I use that to differentiate between a call from
        // faceBook.authorize(...) and a call from a successful post
        // is there a better way of doing this?
        if (!values.containsKey("post_id"))
        {
            try
            {
                Bundle parameters = new Bundle();
                parameters.putString("message", "this is a test");// the message to post to the wall
                facebookClient.dialog(this, "stream.publish", parameters, this);// "stream.publish" is an API call
            }
            catch (Exception e)
            {
                // TODO: handle exception
                System.out.println(e.getMessage());
            }
        }
    }

    public void onError(DialogError e)
    {
        System.out.println("Error: " + e.getMessage());
    }

    public void onFacebookError(FacebookError e)
    {
        System.out.println("Error: " + e.getMessage());
    }

    public void onCancel()
    {
    }

    @SuppressWarnings("deprecation")
	public void onClick(View v)
    {
            facebookClient = new Facebook((getResources().getString(R.string.app_id)));
            // replace APP_API_ID with your own
            facebookClient.authorize(this, new String[] {"publish_stream"}, this);
    	  	btPostToFb.setVisibility(View.VISIBLE);
    	  	isLogged = true;
    	  	//ivFacebookLogoFb.setImageResource(R.drawable.fb_greenlogo);
    	  	Toast.makeText(this,"Conectando...", Toast.LENGTH_LONG).show();
    }
    
    @SuppressWarnings("deprecation")
	public void post(View v){
    	if (isLogged){
	        try
	        {
	            Bundle parameters = new Bundle();
	            parameters.putString("name", "Gol a Gol Mobile");
	            parameters.putString("link", "https://play.google.com/store/apps/details?id=com.oranz.golagol");
	            parameters.putString("caption", "É Rede!");
	            parameters.putString("description", SaveScore.getMessage());
	            parameters.putString("picture", "https://lh6.ggpht.com/sr7ZH7i6faeE2XbTPu29zp5S3rhnVFaeA1Yu2RXg-wGsseX6p-y7mFukknhD92U3wA=w124");
	
	            facebookClient.dialog(this, "stream.publish", parameters, this);// "stream.publish" is an API call
	            Toast.makeText(this,"Publicando...", Toast.LENGTH_LONG).show();
	        }
	        catch (Exception e)
	        {
	            System.out.println(e.getMessage());
	        }
    	}else{
    		Toast.makeText(this,"Você precisa logar antes de postar!", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void openMenu(View view){
        switch (view.getId()) {
        case R.id.ivMenu:
        	openOptionsMenu();
        }
    }
    
    public void openConfig(View view){
	    	Intent nextScreen = new Intent(getApplicationContext(), Config.class);	    	
	    	startActivity(nextScreen);
    }
}
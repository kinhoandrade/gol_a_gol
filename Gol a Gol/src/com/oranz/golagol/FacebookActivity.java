package com.oranz.golagol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookActivity extends Activity implements DialogListener,
        OnClickListener
{

    private Facebook facebookClient;
    private Button btCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_facebook);//my layout xml
        
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
    }
}
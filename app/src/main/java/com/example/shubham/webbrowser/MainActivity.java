package com.example.shubham.webbrowser;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText url;
    WebView web_view;
    Button btn;
    String Url;
    protected int i = 0;
    private ProgressDialog pd;
    private WebChromeClient mWebChromeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openUrl();
    }

    public void openUrl(){

        url = (EditText)findViewById(R.id.url);
        web_view = (WebView)findViewById(R.id.webView);
        btn = (Button)findViewById(R.id.button);
        pd = ProgressDialog.show(this, "Load", "Page Loading", true);
        if( i == 0){
            load("http://google.com");
        }

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 1;
                        Url = "http://google.com";
                        String temp_url = url.getText().toString();
                        String[] u1 = temp_url.split("\\.");
                        try {
                            System.out.println(u1[1]);

                            if (u1[0].startsWith("http") || u1[0].startsWith("https")) {
                                Url = temp_url;
                            } else {
                                Url = "http://" + temp_url;
                            }
                        } catch (Exception e) {
                            Url = "http://google.com/search?q=" + temp_url;
                        }
                        load(Url);
                    }
                }
        );
    }
    @Override
    public  void onBackPressed() {
        pd.dismiss();
        if (web_view.canGoBack()) {
            web_view.goBack();

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Url = url.getText().toString();
            load(Url);

        }

        return super.onOptionsItemSelected(item);
    }

    public void load(String link){

        web_view.setWebViewClient(new WebViewClient(){

                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);

                                          System.out.println("your current url when webpage loading.." + url);
                                      }

                                      @Override
                                      public void onPageFinished(WebView view, String url) {
                                          pd.dismiss();
                                          System.out.println("your current url when webpage loading.. finish" + url);
                                          super.onPageFinished(view, url);
                                      }

                                      @Override
                                      public void onLoadResource(WebView view, String url) {
                                          // TODO Auto-generated method stub
                                          super.onLoadResource(view, url);
                                      }
                                      public boolean shouldOverrideUrlLoading(WebView view, String uri) {
                                          url.setText(uri.toString());
                                          return super.shouldOverrideUrlLoading(view, uri);
                                      }
                                  }


        );

        mWebChromeClient = new WebChromeClient();
        web_view.setWebChromeClient(mWebChromeClient);
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setDomStorageEnabled(true);
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setAllowFileAccess(true);
        web_view.getSettings().setBuiltInZoomControls(true);
        web_view.getSettings().setPluginState(WebSettings.PluginState.ON);
        web_view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        pd.show();
        web_view.loadUrl(link);
        web_view.requestFocus();
        web_view.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
}

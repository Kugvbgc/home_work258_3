package com.khair.home_work258_3;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>>arrayList=new ArrayList<>();
    HashMap<String,String>hashMap;

    ProgressBar progressBar;
    ListView listView;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.list_item);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.title);


        String URL="https://dummyjson.com/quotes";

        RequestQueue queue =Volley.newRequestQueue(MainActivity.this);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            JSONArray jsonArray=response.getJSONArray("quotes");
                            for (int x=0;x<jsonArray.length();x++){
                               JSONObject jsonObject=jsonArray.getJSONObject(x);
                               String quotes=jsonObject.getString("quote");
                               String author=jsonObject.getString("author");

                               hashMap=new HashMap<>();
                               hashMap.put("quote",quotes);
                               hashMap.put("author",author);
                               arrayList.add(hashMap);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        MyAdapter myAdapter=new MyAdapter();
                        listView.setAdapter(myAdapter);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);

                        textView.setText(error.getMessage());

                    }
                }) {

            };
        queue.add(jsObjRequest);














    }
//======================================================================================================
    public class MyAdapter extends BaseAdapter {

    private boolean isLiked=false;

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView=layoutInflater.inflate(R.layout.layout,parent,false);

        TextView tv_quotes=myView.findViewById(R.id.tv_quotes);
        TextView tv_author=myView.findViewById(R.id.tv_author);
        ImageView image_copy=myView.findViewById(R.id.image_copy);
        ImageView image_share=myView.findViewById(R.id.image_share);
        ImageView  image_like=myView.findViewById(R.id.image_like);
        CardView layout=myView.findViewById(R.id.main_2);



        HashMap<String,String>hashMap=arrayList.get(position);
        String quotes=hashMap.get("quote");
        String author=hashMap.get("author");

        tv_quotes.setText(quotes);
        tv_author.setText(author);

        Animation loadAnimation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.right_to_left);
        layout.startAnimation(loadAnimation);


        image_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String combinedText = tv_quotes.getText().toString() + "\n" +
                        tv_author.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("All TextView Texts", combinedText);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MainActivity.this, "Copy Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String combinedText = tv_quotes.getText().toString() + "\n" +
                        tv_author.getText().toString();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, combinedText);

                // Create a chooser to show the share options
                Intent chooser = Intent.createChooser(shareIntent, "Share text via");
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });








        image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    image_like.setImageResource(R.drawable.img_3);
                } else {
                    image_like.setImageResource(R.drawable.img_2);
                }
                isLiked = !isLiked;
            }
        });








        return myView;
    }
}




//======================================================================================================
}
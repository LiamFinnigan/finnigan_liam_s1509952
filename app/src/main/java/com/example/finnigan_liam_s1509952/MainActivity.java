package com.example.finnigan_liam_s1509952;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.util.Xml;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    ListView listView;
    ArrayList<RoadworkMessage> messages = new ArrayList<RoadworkMessage>();;
    private Button startButton;
    private ArrayAdapter<RoadworkMessage> adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Date date;
    // Traffic Scotland URLs
    String urls[] = {
            "https://trafficscotland.org/rss/feeds/currentincidents.aspx",
            "https://trafficscotland.org/rss/feeds/roadworks.aspx",
            "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        listView=(ListView)findViewById(R.id.listView);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        adapter = new ArrayAdapter<RoadworkMessage>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, messages);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                RoadworkMessage itemValue = (RoadworkMessage) listView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("RoadworkMessage.class", itemValue);
                startActivity(intent);

            }

        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_main, menu);
        getMenuInflater().inflate(R.menu.filter_main, menu);
        return true;
    }



    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.filter_menu) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    ArrayList<RoadworkMessage> tempMessages;
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, dayOfMonth, 0, 0);
                    date = cal.getTime();
                    tempMessages = messages.stream()
                            .filter(messages -> messages.getStartDate() != null && messages.getEndDate() != null && messages.getStartDate().before(date) && messages.getEndDate().after(date))
                            .collect(Collectors.toCollection(ArrayList::new));
                    messages.clear();
                    messages.addAll(tempMessages);
                    adapter.notifyDataSetChanged();
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if(id == R.id.search_menu) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Road Search");
            alert.setMessage("Enter the name of a road");

            final EditText input = new EditText(this);
            alert.setView(input);
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String searchQuery = input.getText().toString();
                    ArrayList<RoadworkMessage> tempMessages;
                    tempMessages = messages.stream()
                            .filter(messages -> messages.getTitle().contains(searchQuery))
                            .collect(Collectors.toCollection(ArrayList::new));
                    messages.clear();
                    messages.addAll(tempMessages);
                    adapter.notifyDataSetChanged();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task()).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;


        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            InputStream in = null;
            String inputLine = "";
            ArrayList<RoadworkMessage> tempMessages = new ArrayList<RoadworkMessage>();

            Log.e("MyTag","in run");

            try
            {

                Log.e("MyTag","in try");
                for(String url : urls) {
                    aurl = new URL(url);
                    yc = aurl.openConnection();
                    in = yc.getInputStream();
                    tempMessages.addAll(parse(in));
                }

                messages.clear();
                messages.addAll(tempMessages);
            }
            catch (IOException | XmlPullParserException | ParseException ae)
            {
                Log.e("MyTag", "ioexception");
            }



            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public ArrayList<RoadworkMessage> parse(InputStream in) throws IOException, XmlPullParserException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return parseFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList parseFeed(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        ArrayList<RoadworkMessage> messages = new ArrayList<RoadworkMessage>();
        String incidentType = "";
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            RoadworkMessage roadworkMessage = new RoadworkMessage();
            //Starts by looking for the item tag
            if (tagName.equals("item")) {
                roadworkMessage = readMessage(parser);
                roadworkMessage.setIncidentType(incidentType);
                messages.add(roadworkMessage);
            } else if (tagName.equals("channel")) {
                // ignore channel tag
                continue;
            } else if (tagName.equals("title")) {
                // set incident type
                incidentType = readTag(parser, tagName);
            } else {
                skip(parser);
            }
        }
        return messages;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private RoadworkMessage readMessage(XmlPullParser parser) throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        RoadworkMessage roadworkMessage = new RoadworkMessage();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            switch (tagName) {
                case "title":
                    roadworkMessage.setTitle(readTag(parser, tagName));
                    break;
                case "description":
                    String description = readTag(parser, tagName);

                    if(description.contains("Start Date")) {
                        Log.e("magazine", description);
                        String startDate = description.substring(12, description.indexOf("00:00") + 5);
                        String endDate = description.substring(description.indexOf("End Date: ") + 10, description.lastIndexOf("00:00") + 5);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy - kk:ss");

                        roadworkMessage.setStartDate(dateFormat.parse(startDate));
                        roadworkMessage.setEndDate(dateFormat.parse(endDate));
                    }

                    roadworkMessage.setDescription(description);
                    break;
                case "link":
                    roadworkMessage.setLink(readTag(parser, tagName));
                    break;
                case "georss:point":
                    roadworkMessage.setGeoPoint(readTag(parser, tagName));
                    break;
                case "author":
                    roadworkMessage.setAuthor(readTag(parser, tagName));
                    break;
                case "comments":
                    roadworkMessage.setComments(readTag(parser, tagName));
                    break;
                case "pubDate":
                    String date = readTag(parser, tagName);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("E, dd MMM yyyy kk:mm:ss z");
                    roadworkMessage.setPublishedDate(dateFormatter.parse(date));
                    break;
                default:
                    break;
            }
        }
        return roadworkMessage;
    }

    private String readTag(XmlPullParser parser, String tagName) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        String tagText = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tagName);
        return tagText;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

} // End of MainActivity

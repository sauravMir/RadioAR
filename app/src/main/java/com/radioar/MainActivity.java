package com.radioar;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.educareapps.mylibrary.BaseActivity;
import com.educareapps.mylibrary.DialogNavBarHide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ExpandableListView simpleExpandableListView;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private LinkedHashMap<String, RadioCategory> subjects = new LinkedHashMap<String, RadioCategory>();
    private ArrayList<RadioCategory> rdCategoryLst = new ArrayList<RadioCategory>();
    private RadioAdapter listAdapter;
    MainActivity activity;

    private static final int RECORDER_SAMPLERATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    private ImageButton ibtnPlay, ibtnStop, ibtnStartRecord, ibtnStopRecord;
    private MediaPlayer player;
    Recorder recorderNew;
    String fileName;
    boolean isPlaying = false;
    private TextView tvRadioStation, tvTimer;
    private String stationName = "";
    private String stationLink = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        tvRadioStation = (TextView) findViewById(R.id.tvRadioStation);

        recorderNew = new Recorder("dsf");
        initializeUIElements();
        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        System.out.println("BUFFER SIZE VALUE IS " + bufferSize);
        RadioStation radioStation = (RadioStation) listAdapter.getChild(0, 0);
        stationName = radioStation.getName();
        stationLink = radioStation.getLink();
        initializeMediaPlayer(stationLink);
        tvRadioStation.setText(stationName);

    }

    View previousSelectedItem;

    private void addDrawerItems() {

        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new RadioAdapter(MainActivity.this, rdCategoryLst);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                RadioCategory headerInfo = rdCategoryLst.get(groupPosition);
                //get the child info

                if (previousSelectedItem != null) {
                    previousSelectedItem.setBackgroundColor(Color.parseColor("#ffeeeeee"));
                }
                previousSelectedItem = v;
                v.setBackgroundColor(getResources().getColor(R.color.appColor));

                RadioStation detailInfo = headerInfo.getRadioStationList().get(childPosition);
                stationName = detailInfo.getName();
                stationLink = detailInfo.getLink();
                tvRadioStation.setText(stationName);
                if (isPlaying) {
                    stopRecording();
                    stopPlaying();
                    initializeMediaPlayer(stationLink);
                    isPlaying = true;
                    startPlaying();
                    mDrawerLayout.closeDrawers();

                } else {
                    initializeMediaPlayer(stationLink);
                    isPlaying = true;
                    startPlaying();
                    mDrawerLayout.closeDrawers();

                }

                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                RadioCategory headerInfo = rdCategoryLst.get(groupPosition);

                return false;
            }
        });


    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        /*if (id == R.id.action_settings) {

            return true;
        }*/

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData() {

        addRadioStation(getString(R.string.international_radio), "Accu Radio", "http://s4.voscast.com:8432");
        addRadioStation(getString(R.string.international_radio), "BBC Local Radio", "https://en.wikipedia.org/wiki/List_of_Internet_radio_stations");
        addRadioStation(getString(R.string.international_radio), "Shoutcast ", "http://s4.voscast.com:8432");
        addRadioStation(getString(R.string.international_radio), "BCB", "http://173.208.157.101:8020 ");

        addRadioStation(getString(R.string.local_radio), "Radio Plus", "http://s4.voscast.com:8432");
        addRadioStation(getString(R.string.local_radio), "Radio One", "http://173.208.157.101:8020 ");
        addRadioStation(getString(R.string.local_radio), "Top Fm", "http://webcast.orange.mu:1935");
        addRadioStation(getString(R.string.local_radio), "MBC radio", "http://www.mbcradio.tv/sites/all/themes/mbcradiotv/templates/radio-mauritius.html");

        addRadioStation(getString(R.string.local_radio), "Taal FM ", "http://www.mbcradio.tv/sites/all/themes/mbcradiotv/templates/taalfm.html");
        addRadioStation(getString(R.string.local_radio), "Kool Fm", "http://www.mbcradio.tv/sites/all/themes/mbcradiotv/templates/koolfm.html");
        addRadioStation(getString(R.string.local_radio), "Music FM", "http://www.mbcradio.tv/sites/all/themes/mbcradiotv/templates/musicfm.html");
        addRadioStation(getString(R.string.local_radio), "Best fm ", "http://www.mbcradio.tv/sites/all/themes/mbcradiotv/templates/bestfm.html");
    }


    //here we maintain our products in various departments
    private int addRadioStation(String rdCategory, String title, String link) {

        int groupPosition = 0;

        //check the hash map if the group already exists
        RadioCategory headerInfo = subjects.get(rdCategory);
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = new RadioCategory();
            headerInfo.setName(rdCategory);
            subjects.put(rdCategory, headerInfo);
            rdCategoryLst.add(headerInfo);
        }

        //get the children for the group
        ArrayList<RadioStation> productList = headerInfo.getRadioStationList();
      /*  //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;*/

        //create a new child and add that to the group
        RadioStation aRadio = new RadioStation();
        aRadio.setLink(link);
        aRadio.setName(title);
        productList.add(aRadio);
        headerInfo.setRadioStationList(productList);

        //find the group position inside the list
        groupPosition = rdCategoryLst.indexOf(headerInfo);
        return groupPosition;
    }


    private void initializeUIElements() {

        tvRadioStation = (TextView) findViewById(R.id.tvRadioStation);
        tvTimer = (TextView) findViewById(R.id.tvTimer);


        ibtnStartRecord = (ImageButton) findViewById(R.id.ibtnStartRecord);
        ibtnStartRecord.setOnClickListener(this);

        ibtnStopRecord = (ImageButton) findViewById(R.id.ibtnStopRecord);
        ibtnStopRecord.setOnClickListener(this);

        ibtnPlay = (ImageButton) findViewById(R.id.ibtnPlay);
        ibtnPlay.setOnClickListener(this);


        ibtnStop = (ImageButton) findViewById(R.id.ibtnStop);
        ibtnStop.setOnClickListener(this);

    }


    private void startPlaying() {
        ibtnStop.setVisibility(View.VISIBLE);
        ibtnPlay.setVisibility(View.GONE);
        player.prepareAsync();
        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();

            }
        });

    }

    Timer timer;
    int counter = 0;
    int minCounter = 0;

    void startCounter() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null && player.isPlaying()) {
                            tvTimer.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvTimer.setText(String.valueOf(minCounter + " : " + counter) + "s");
                                    counter++;
                                    if (counter > 59) {
                                        minCounter++;
                                        counter = 0;
                                    }
                                }
                            });
                        } else {
                            timer.cancel();
                            timer.purge();
                            Toast.makeText(activity, "Not Playing", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            if (timer != null)
                timer.cancel();
            counter = 0;
            minCounter = 0;
            tvTimer.setText("");
            player.stop();
            player.release();
            initializeMediaPlayer(stationLink);
        }

        ibtnPlay.setVisibility(View.VISIBLE);
        ibtnStop.setVisibility(View.GONE);
    }

    private void stopRecording() {
        if (recorderNew.isRecording()) {
            recorderNew.stop();
            counter = 0;
            minCounter = 0;
            timer.cancel();
            tvTimer.setText("");
            recorderNew = new Recorder("df");
            ibtnStartRecord.setVisibility(View.VISIBLE);
            ibtnStopRecord.setVisibility(View.GONE);


        }
    }

    private void initializeMediaPlayer(String channelLink) {
        player = new MediaPlayer();
        try {
            //player.setDataSource("http://usa8-vn.mixstream.net:8138");
            //player.setDataSource("http://server2.crearradio.com:8371");
            if (!TextUtils.isEmpty(channelLink)) {
                player.setDataSource(channelLink);

            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlaying = false;
        ibtnStartRecord.setVisibility(View.VISIBLE);

        stopRecording();
        stopPlaying();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onClick(View v) {
        //Animanation.blink(v);
        if (v == ibtnPlay) {
            isPlaying = true;
            startPlaying();
        } else if (v == ibtnStop) {
            isPlaying = false;
            stopRecording();
            stopPlaying();
        } else if (v == ibtnStartRecord) {
            if (isPlaying) {
                dialogFileName();

            } else
                Toast.makeText(activity, "please Play Radio first.", Toast.LENGTH_LONG).show();

        } else if (v == ibtnStopRecord) {
            stopRecording();
        }

    }


    public void dialogFileName() {
        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_share_file_name);
        dialog.setCancelable(false);

        final EditText etFileName = (EditText) dialog.findViewById(R.id.etFileName);

        Button btNo = (Button) dialog.findViewById(R.id.btNo);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fileName = etFileName.getText().toString().trim();
                if (fileName != null && !TextUtils.isEmpty(fileName)) {
                    try {
                        if (recorderNew != null) {
                            startCounter();
                            ibtnStartRecord.setVisibility(View.GONE);
                            ibtnStopRecord.setVisibility(View.VISIBLE);
                            recorderNew.start(fileName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(activity, "Please add file name first!", Toast.LENGTH_SHORT).show();
                }


            }

        });
        DialogNavBarHide.navBarHide(this, dialog);


    }


}

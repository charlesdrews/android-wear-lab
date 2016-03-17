package com.charlesdrews.wearlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.CircularButton;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int SPEECH_REQUEST_CODE = 0;

    private WearableListView mListView;
    private CircularButton mButton;
    private ArrayList<String> mToDoItems;
    private WearableListView.Adapter mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mListView = (WearableListView) stub.findViewById(R.id.wearListView);
                mToDoItems = new ArrayList<>();

                mAdapter = new WearableListView.Adapter() {
                    @Override
                    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new WearableListView.ViewHolder(view);
                    }

                    @Override
                    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1))
                                .setText(mToDoItems.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return mToDoItems.size();
                    }

                };
                mListView.setAdapter(mAdapter);

                mButton = (CircularButton) stub.findViewById(R.id.addButton);
                mButton.setImageResource(android.R.drawable.ic_input_add);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displaySpeechRecognizer();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            // Do something with spokenText
            mToDoItems.add(spokenText);
            mAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
}

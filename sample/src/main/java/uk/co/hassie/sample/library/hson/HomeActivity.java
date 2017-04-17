/*
 * Copyright ©2017 Hassie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.hassie.sample.library.hson;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import uk.co.hassie.library.hson.Callback;
import uk.co.hassie.library.hson.HSON;
import uk.co.hassie.library.versioninfomdialog.VersionInfoMDialog;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupButtonListeners();

    }

    private void setupButtonListeners() {

        Button downloadJSON = (Button) findViewById(R.id.buttonDownloadJSON);
        Button convertToJSON = (Button) findViewById(R.id.buttonConvertToJSON);

        downloadJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HSON.Downloader()
                        .setURL("https://raw.githubusercontent.com/hassie-dash/hson/master/sample_json.json")
                        .download(new Callback.DownloadCallback() {
                            @Override
                            public void onStart() {
                                // Do any work here required before the JSON is downloaded.
                                // In this example, a progress dialog is shown.
                                progressDialog = new ProgressDialog(HomeActivity.this);
                                progressDialog.setMessage("Downloading JSON");
                                progressDialog.setIndeterminate(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                            @Override
                            public void onFinish(JSONObject json) {
                                // JSON download successful. Do work here.

                                String exampleSpecialChars = null;

                                try {
                                    exampleSpecialChars = json.getJSONObject("object").getString("string");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                progressDialog.dismiss();

                                new AlertDialog.Builder(HomeActivity.this)
                                        .setMessage("JSON download successful. Sample data extracted: " +
                                                exampleSpecialChars)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                            @Override
                            public void onFail() {
                                // JSON download failed. Handle error here if required.
                                // In this example a error dialog is shown.
                                progressDialog.dismiss();
                                new AlertDialog.Builder(HomeActivity.this)
                                        .setMessage("JSON download failed")
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        });
            }
        });

        convertToJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get json from assets and convert to string.
                String jsonString = "";
                try {

                    InputStream inputStream = getAssets().open("sample_json.json");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = inputStream.read(buffer)) != -1)
                        byteArrayOutputStream.write(buffer, 0, length);

                    jsonString = byteArrayOutputStream.toString();

                } catch (IOException e) {
                    // Do nothing.
                }

                new HSON.Converter()
                        .setString(jsonString)
                        .convert(new Callback.ConversionCallback() {
                            @Override
                            public void onStart() {
                                // Do any work here required before the JSON is converted.
                                // In this example, a progress dialog is shown.
                                progressDialog = new ProgressDialog(HomeActivity.this);
                                progressDialog.setIndeterminate(false);
                                progressDialog.setMessage("Converting asset file to JSON");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                            @Override
                            public void onFinish(JSONObject json) {
                                // Do work with JSON here as conversion was successful.

                                String exampleSpecialChars = null;

                                try {
                                    exampleSpecialChars = json.getJSONObject("object").getString("string");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                progressDialog.dismiss();

                                new AlertDialog.Builder(HomeActivity.this)
                                        .setMessage("Asset file converted to JSON successfully - Sample extracted data: " +
                                                exampleSpecialChars)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                            @Override
                            public void onFail() {
                                // JSON conversion failed, handle error here if required.
                                progressDialog.dismiss();
                                new AlertDialog.Builder(HomeActivity.this)
                                        .setMessage("Asset file failed to convert to JSON")
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        });
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new VersionInfoMDialog.Builder(this)
                    .setCopyrightText(R.string.about_app_copyright)
                    .setVersionPrefix(R.string.about_version_prefix)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

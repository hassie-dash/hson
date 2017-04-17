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

package uk.co.hassie.library.hson;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hassie on 16/04/2017.
 */

public class HSON {

    public static class Downloader extends HSON {

        private String mURL;

        public Downloader setURL(@NonNull String url) {
            mURL = url;
            return this;
        }

        public Downloader setURL(@StringRes int urlResId, @NonNull Context context) {
            if (urlResId == 0)
                return this;
            mURL = context.getResources().getString(urlResId);
            return this;
        }

        public Downloader download(@NonNull Callback.DownloadCallback downloadCallback) {
            new DownloadTask(downloadCallback).execute();
            return this;
        }

        private class DownloadTask extends AsyncTask<Void, Void, Void> {

            private boolean mIsSuccessful = false;
            private Callback.DownloadCallback mDownloadCallback;
            private JSONObject mJSON;

            private DownloadTask(Callback.DownloadCallback downloadCallback) {
                this.mDownloadCallback = downloadCallback;
            }

            @Override
            protected void onPreExecute() {
                mDownloadCallback.onStart();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {

                    URL url = new URL(mURL);
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.setRequestMethod("GET");
                    request.connect();

                    if (request.getInputStream() != null) {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length = request.getInputStream().read(buffer);
                        while (length != -1) {
                            byteArrayOutputStream.write(buffer, 0, length);
                            length = request.getInputStream().read(buffer);
                        }

                        mJSON = new JSONObject(byteArrayOutputStream.toString("UTF-8"));

                        mIsSuccessful = true;

                    } else {
                        mJSON = null;
                        mIsSuccessful = false;
                    }

                } catch (Exception e) {
                    mJSON = null;
                    mIsSuccessful = false;
                    e.printStackTrace();
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mIsSuccessful) {
                    mDownloadCallback.onFinish(mJSON);
                } else {
                    mDownloadCallback.onFail();
                }
            }

        }

    }

    public static class Converter extends HSON {

        private String convertString;

        public Converter setString(@NonNull String string) {
            convertString = string;
            return this;
        }

        public Converter convert(@NonNull Callback.ConversionCallback conversionCallback) {
            new ConvertTask(conversionCallback).execute();
            return this;
        }

        private class ConvertTask extends AsyncTask<Void, Void, Void> {

            private Callback.ConversionCallback mConversionCallback;
            private boolean mIsSuccessful = false;
            private JSONObject mJSON;

            private ConvertTask(Callback.ConversionCallback conversionCallback) {
                this.mConversionCallback = conversionCallback;
            }

            @Override
            protected void onPreExecute() {
                mConversionCallback.onStart();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {

                    byte[] conversionString = convertString.getBytes("UTF-8");
                    String encodedString = new String(conversionString, "UTF-8");

                    mJSON = new JSONObject(encodedString);
                    mIsSuccessful = true;

                } catch (Exception e) {
                    mJSON = null;
                    mIsSuccessful = false;
                    e.printStackTrace();
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (mIsSuccessful) {
                    mConversionCallback.onFinish(mJSON);
                } else {
                    mConversionCallback.onFail();
                }
            }
        }

    }

}

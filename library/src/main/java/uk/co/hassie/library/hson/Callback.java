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

import org.json.JSONObject;

/**
 * Created by Hassie on 16/04/2017.
 */

public interface Callback {

    interface DownloadCallback {
        void onStart();
        void onFinish(JSONObject json);
        void onFail();
    }

    interface ConversionCallback {
        void onStart();
        void onFinish(JSONObject json);
        void onFail();
    }

}
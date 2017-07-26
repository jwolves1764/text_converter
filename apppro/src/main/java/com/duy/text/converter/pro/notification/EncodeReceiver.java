/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.text.converter.pro.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.duy.sharedcode.ClipboardUtil;
import com.duy.sharedcode.codec.ASCIITool;
import com.duy.sharedcode.codec.Base32Tool;
import com.duy.sharedcode.codec.Base64Tool;
import com.duy.sharedcode.codec.BinaryTool;
import com.duy.sharedcode.codec.HexTool;
import com.duy.sharedcode.codec.Md5Tool;
import com.duy.sharedcode.codec.MorseTool;
import com.duy.sharedcode.codec.OctalTool;
import com.duy.sharedcode.codec.ReverserTool;
import com.duy.sharedcode.codec.Sha2Tool;
import com.duy.sharedcode.codec.SubScriptText;
import com.duy.sharedcode.codec.SupScriptText;
import com.duy.sharedcode.codec.URLTool;
import com.duy.sharedcode.codec.UpperLowerTool;
import com.duy.sharedcode.codec.UpsideDownTool;
import com.duy.sharedcode.codec.ZalgoBigTool;
import com.duy.sharedcode.codec.ZalgoMiniTool;
import com.duy.sharedcode.codec.ZalgoNormalTool;
import com.duy.sharedcode.fragment.EncodeMethod;
import com.duy.text.converter.pro.R;

/**
 * Created by Duy on 26-Jul-17.
 */

public class EncodeReceiver extends BroadcastReceiver {
    private static final String TAG = "StyleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
        if (intent != null) {
            String name = "";
            if (intent.getAction().equals(StyleNotification.ACTION_ENCODE_STYLE_1)) {
                name = preferences.getString("pref_encode_style_1", "");
            } else if (intent.getAction().equals(StyleNotification.ACTION_ENCODE_STYLE_2)) {
                name = preferences.getString("pref_encode_style_2", "");
            } else if (intent.getAction().equals(StyleNotification.ACTION_ENCODE_STYLE_3)) {
                name = preferences.getString("pref_encode_style_3", "");
            } else if (intent.getAction().equals(StyleNotification.ACTION_ENCODE_STYLE_4)) {
                name = preferences.getString("pref_encode_style_4", "");
            } else if (intent.getAction().equals(StyleNotification.ACTION_ENCODE_STYLE_5)) {
                name = preferences.getString("pref_encode_style_5", "");
            }
            if (name.isEmpty()) {
                complainNotSet(context);
                return;
            }
            String[] array = context.getResources().getStringArray(R.array.convert_style);
            int pos;
            for (pos = 0; pos < array.length; pos++) {
                String s = array[pos];
                if (s.equals(name)) {
                    break;
                }
            }
            EncodeMethod encodeMethod = EncodeMethod.values()[pos];
            String inp = ClipboardUtil.getClipboard(context);
            if (inp == null) {
                Toast.makeText(context, "Clipboard is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (encodeMethod) {
                case ASCII:
                    setText(context, new ASCIITool().encode(inp));
                    break;
                case OCTAL:
                    setText(context, new OctalTool().encode(inp));
                    break;
                case BINARY:
                    setText(context, new BinaryTool().encode(inp));
                    break;
                case HEX:
                    setText(context, new HexTool().encode(inp));
                    break;
                case UPPER:
                    setText(context, UpperLowerTool.upperText(inp));
                    break;
                case LOWER:
                    setText(context, UpperLowerTool.lowerText(inp));
                    break;
                case REVERSER:
                    setText(context, ReverserTool.reverseText(inp));
                    break;
                case UPSIDE_DOWNSIDE:
                    setText(context, UpsideDownTool.textToUpsideDown(inp));
                    break;
                case SUPPER_SCRIPT:
                    setText(context, new SupScriptText().encode(inp));
                    break;
                case SUB_SCRIPT:
                    setText(context, new SubScriptText().encode(inp));
                    break;
                case MORSE_CODE:
                    setText(context, new MorseTool().encode(inp));
                    break;
                case BASE_64:
                    setText(context, new Base64Tool().encode(inp));
                    break;
                case ZALGO_MINI:
                    setText(context, new ZalgoMiniTool().encode(inp));
                    break;
                case ZALGO_NORMAL:
                    setText(context, new ZalgoNormalTool().encode(inp));
                    break;
                case ZALGO_BIG:
                    setText(context, new ZalgoBigTool().encode(inp));
                    break;
                case BASE32:
                    setText(context, new Base32Tool().encode(inp));
                    break;
                case MD5:
                    setText(context, new Md5Tool().encode(inp));
                    break;
                case SHA_2:
                    setText(context, new Sha2Tool().encode(inp));
                    break;
                case URL:
                    setText(context, new URLTool().encode(inp));
                    break;
            }
        }
        closeStatusBar(context);
    }

    private void closeStatusBar(Context context) {

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    private void setText(Context context, String encode) {
        ClipboardUtil.setClipboard(context, encode);
    }

    private void complainNotSet(Context context) {
        Toast.makeText(context, "Unset", Toast.LENGTH_SHORT).show();
    }
}

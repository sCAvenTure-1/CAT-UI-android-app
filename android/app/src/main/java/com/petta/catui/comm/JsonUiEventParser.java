package com.petta.catui.comm;

import org.json.JSONObject;
import org.json.JSONException;

import com.petta.catui.core.ExpressionState;

public class JsonUiEventParser {

    public static UiEvent parse(String jsonString) {

        try {
            JSONObject json = new JSONObject(jsonString);

            int faceId = json.optInt("face", 0);
            String text = json.optString("text", "");
            double reset = json.optDouble("reset_after", -1);

            return new UiEvent(
                ExpressionState.fromId(faceId),
                text,
                reset
            );

        } catch (JSONException e) {
            // パース失敗時は安全なデフォルトにする
            System.err.println("[JsonUiEventParser] Invalid JSON: " + jsonString);
            e.printStackTrace();

            return new UiEvent(
                ExpressionState.NORMAL,
                "",
                -1
            );
        }
    }
}
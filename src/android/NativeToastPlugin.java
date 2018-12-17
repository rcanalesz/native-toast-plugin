package com.rcanales.cordova.plugin;
// The native Toast API
import android.widget.Toast;

// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NativeToastPlugin extends CordovaPlugin {
  private static final String DURATION_LONG = "long";

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
      
      // Verify that the user sent a 'show' action
      if (action.equals("show")) {      
        String message;
        String duration;
        try {
            JSONObject options = args.getJSONObject(0);
            message = options.getString("message");
            duration = options.getString("duration");
        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
            return false;
        }
        // Create the toast
        Toast toast = Toast.makeText(cordova.getActivity(), message,
            DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);        
        // Display toast
        toast.show();
        // Send a positive result to the callbackContext
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return true;
      }else if(action.equals("call")){

        sendPost();

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return true;
        

      }
      else{
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }
  }




  public void sendPost() {
      Thread thread = new Thread(new Runnable() {
          @Override
          public void run() {
              try {
                  URL url = new URL("http://mobileaws2.entel.pe/mobileservices2/rest/GetDefaultNumber");
                  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                  conn.setRequestMethod("POST");
                  conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                  conn.setRequestProperty("Accept","application/json");
                  conn.setDoOutput(true);
                  conn.setDoInput(true);

                  /*
                  JSONObject jsonParam = new JSONObject();
                  jsonParam.put("timestamp", 1488873360);
                  jsonParam.put("uname", message.getUser());
                  jsonParam.put("message", message.getMessage());
                  jsonParam.put("latitude", 0D);
                  jsonParam.put("longitude", 0D);

                  Log.i("JSON", jsonParam.toString());*/
                  DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                  //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                  //os.writeBytes(jsonParam.toString());

                  os.flush();
                  os.close();

                  Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                  Log.i("MSG" , conn.getResponseMessage());

                  conn.disconnect();
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      });

      thread.start();
  }
}
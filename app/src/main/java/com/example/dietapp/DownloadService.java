package com.example.dietapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    static final String ACTION_FOOD = "com.example.dietapp.action.FOOD";
    private static final String ACTION_DIET = "com.example.dietapp.action.DIET";
    private static final String ACTION_FOODCARD="com.example.dietapp.action.FOODCARD";
    public final static String RESULT_INFO="query_result";
    public final static String RESULT_CARD="card_result";
    public final static String RESULT_EMPTYDIET="empty_diet_response";
    public final static String RESULT_SUCCESSDIET="good_diet_respone";



    private final String APIkey = "c4c180798c104131b0a7cd89ea97123b";



    // TODO: Rename parameters
    private static final String EXTRA_PARAMFOOD = "com.example.dietapp.extra.PARAMFOOD";
    private static final String EXTRA_PARAMCARD = "com.example.dietapp.extra.PARAMCARD1";
    private static final String EXTRA_PARAMCARDTIME = "com.example.dietapp.extra.PARAMCARDTIME";
    private static final String EXTRA_PARAMDIET = "com.example.dietapp.extra.PARAMDIET";
    private static final String EXTRA_PARAMDIETTIME = "com.example.dietapp.extra.PARAMDIETTIME";

    public DownloadService() {
        super("DownloadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFood(Context context, Bundle datas) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_FOOD);
        intent.putExtra(EXTRA_PARAMFOOD, datas);
        context.startService(intent);
    }

    public static void startActionFoodCard(Context context, String datas,String time) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_FOODCARD);
        intent.putExtra(EXTRA_PARAMCARD, datas);
        intent.putExtra(EXTRA_PARAMCARDTIME,time);
        context.startService(intent);
    }

    public static void startActionDiet(Context context, String param,int override_time) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DIET);

        intent.putExtra(EXTRA_PARAMDIET, param);
        intent.putExtra(EXTRA_PARAMDIETTIME,override_time);
        context.startService(intent);

    }

    @Override
    protected void onHandleIntent(Intent intent) { //kezeli a kapott intentet
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOOD.equals(action)) { //complex food fetch
                final Bundle param1 = intent.getBundleExtra(EXTRA_PARAMFOOD);
                handleActionFood(param1);
            } else if (ACTION_DIET.equals(action)) { //diet fetch
                final String param1 = intent.getStringExtra(EXTRA_PARAMDIET);
                final int param2=intent.getIntExtra(EXTRA_PARAMDIETTIME,0);
                handleActionDiet(param1,param2);

            }
            else if (ACTION_FOODCARD.equals(action)) { //food by id
                final String paramcard=intent.getStringExtra(EXTRA_PARAMCARD);
                final String paramcardtime=intent.getStringExtra(EXTRA_PARAMCARDTIME);
                handleActionFoodCard(paramcard,paramcardtime);
            }
        }
    }

    /**
     * Handle action Food in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFood(Bundle param1) {
        String query_limit="20";
        String url_string="https://api.spoonacular.com/recipes/complexSearch?number=" + query_limit + "&apiKey=" + APIkey;

        if (Objects.requireNonNull(param1.getString("query")).length()>0) url_string+="&query="+param1.getString("query");

        String url_diet="&diet=";
        int size_url_diet=url_diet.length();
        if (param1.getBoolean("vegetarian")) url_diet+="vegetarian,";
        if (param1.getBoolean("vegan")) url_diet+="vegan,";
        if (param1.getBoolean("glutenfree")) url_diet+="glutenfree,";
        if (param1.getBoolean("keto")) url_diet+="ketogenic,";
        if (param1.getBoolean("lactov")) url_diet+="lacto-vegetarian,";
        if (param1.getBoolean("ovov")) url_diet+="ovo-vegetarian,";
        if (param1.getBoolean("pesce")) url_diet+="pescetarian,";
        if (param1.getBoolean("paleo")) url_diet+="paleo,";
        if (param1.getBoolean("primal")) url_diet+="primal,";
        if (param1.getBoolean("whole")) url_diet+="whole30,";
        if (url_diet.length()>size_url_diet) {
            url_string+=url_diet.substring(0,url_diet.length()-1);
        }
        String url_country="&cuisine=";
        int size_url_country=url_country.length();
        if (param1.getBoolean("eu")) url_country+="european,";
        if (param1.getBoolean("br")) url_country+="british,";
        if (param1.getBoolean("fr")) url_country+="french,";
        if (param1.getBoolean("ge")) url_country+="german,";
        if (param1.getBoolean("ch")) url_country+="chinese,";
        if (param1.getBoolean("am")) url_country+="american,";
        if (param1.getBoolean("in")) url_country+="indian,";
        if (param1.getBoolean("it")) url_country+="italian,";
        if (param1.getBoolean("gr")) url_country+="greek,";
        if (param1.getBoolean("ir")) url_country+="irish,";
        if (param1.getBoolean("ko")) url_country+="korean,";
        if (param1.getBoolean("sp")) url_country+="spanish,";
        if (param1.getBoolean("je")) url_country+="jewish,";
        if (param1.getBoolean("th")) url_country+="thai,";
        if (param1.getBoolean("vi")) url_country+="vietnamese,";
        if (param1.getBoolean("mex")) url_country+="mexican,";
        if (param1.getBoolean("med")) url_country+="mediterranean,";
        if (param1.getBoolean("mi")) url_country+="middleeastern,";
        if (param1.getBoolean("no")) url_country+="nordic,";
        if (param1.getBoolean("af")) url_country+="african,";
        if (param1.getBoolean("la")) url_country+="latinamerican,";
        if (param1.getBoolean("caj")) url_country+="cajun,";
        if (param1.getBoolean("ea")) url_country+="easterneuropean,";
        if (param1.getBoolean("car")) url_country+="caribbean,";
        if (url_country.length()>size_url_country) {
            url_string+=url_country.substring(0,url_country.length()-1);
        }
        String url_type="&type=";
        int size_url_type=url_type.length();
        if (param1.getBoolean("main")) url_type+="main course,";
        if (param1.getBoolean("dess")) url_type+="dessert,";
        if (param1.getBoolean("appet")) url_type+="appetizer,";
        if (param1.getBoolean("salad")) url_type+="salad,";
        if (param1.getBoolean("breakfast")) url_type+="breakfast,";
        if (param1.getBoolean("soup")) url_type+="soup,";
        if (param1.getBoolean("sauce")) url_type+="sauce,";
        if (param1.getBoolean("snack")) url_type+="snack,";
        if (param1.getBoolean("drink")) url_type+="drink,";
        if (url_type.length()>size_url_type) {
            url_string+=url_type.substring(0,url_type.length()-1);
        }
        String url_intolerance="&intolerances=";
        int size_url_intol=url_intolerance.length();
        if (param1.getBoolean("dairy")) url_intolerance+="dairy,";
        if (param1.getBoolean("egg")) url_intolerance+="egg,";
        if (param1.getBoolean("wheat")) url_intolerance+="gluten,";
        if (param1.getBoolean("gluten")) url_intolerance+="grain,";
        if (param1.getBoolean("grain")) url_intolerance+="peanut,";
        if (param1.getBoolean("pea")) url_intolerance+="seafood,";
        if (param1.getBoolean("sea")) url_intolerance+="sesame,";
        if (param1.getBoolean("ses")) url_intolerance+="shellish,";
        if (param1.getBoolean("shell")) url_intolerance+="soy,";
        if (param1.getBoolean("soy")) url_intolerance+="sulfite,";
        if (param1.getBoolean("sul")) url_intolerance+="treenut,";
        if (param1.getBoolean("tree")) url_intolerance+="wheat,";
        if (url_intolerance.length()>size_url_intol) {
            url_string+=url_intolerance.substring(0,url_intolerance.length()-1);
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(url_string);

            Log.i("url", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);
            }
            JSONObject json = new JSONObject(buffer.toString());

            JSONArray array = (JSONArray) json.get("results");
            JSONObject temp;

            ArrayList<MiniFood> list = new ArrayList<>();
            MiniFood f;
            for (int i = 0; i < array.length(); i++) {
                temp = (JSONObject) array.get(i);
                String id=temp.getString("id");
                String name = temp.getString("title");
                String image=temp.getString("image");
                f = new MiniFood(id,name,image);
                list.add(f);

            }
            sendResultback(list);


        } catch (IOException | JSONException | StackOverflowError e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Egy adott ételt kérdez le id alapján
     */
    private void handleActionFoodCard(String paramcard,String time) {

        String url_string="https://api.spoonacular.com/recipes/"+paramcard+"/information?apiKey=" + APIkey;


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(url_string);

            Log.i("url", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);
            }
            JSONObject json = new JSONObject(buffer.toString());
            String name=json.getString("title");
            Boolean vega=json.getBoolean("vegetarian");
            Boolean vegan=json.getBoolean("vegan");
            Boolean glutenfree=json.getBoolean("glutenFree");
            Boolean healthy=json.getBoolean("veryHealthy");
            Boolean cheap=json.getBoolean("cheap");
            Boolean popular=json.getBoolean("veryPopular");
            Boolean sus=json.getBoolean("sustainable");
            String summary=json.getString("summary");
            String ready=String.valueOf(json.getInt("readyInMinutes"));
            String image=json.getString("image");
            String id=String.valueOf(json.getInt("id"));
            String link=json.getString("sourceUrl");
            summary = summary.replaceAll("<(.*?)\\>"," ");//Removes all items in brackets
            summary = summary.replaceAll("<(.*?)\\\n"," ");//Must be undeneath
            summary = summary.replaceFirst("(.*?)\\>", " ");//Removes any connected item to the last bracket
            summary = summary.replaceAll("&nbsp;"," ");
            summary = summary.replaceAll("&amp;"," ");
            Food f=new Food(id,name,vega,vegan,glutenfree,healthy,cheap,popular,sus,link,summary,ready,image);
            sendFoodCardIntent(f,time);


        } catch (IOException | JSONException | StackOverflowError e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Lekér egy napi diétás ételt
     */
    private void handleActionDiet(String param_url, int param_time) {

        String url_string=param_url;
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(url_string);

            Log.i("url", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);
            }
            JSONObject json = new JSONObject(buffer.toString());

            JSONArray meals=json.getJSONArray("meals");



            if (meals.length()==0) {
                sendEmptyDietIntent();
                return;
            }
            else {
                sendSuccessDietIntent();
                Calendar morning,evening,now;
                morning=Calendar.getInstance();
                evening=Calendar.getInstance();
                now=Calendar.getInstance();
                morning.set(Calendar.HOUR_OF_DAY,11);
                evening.set(Calendar.HOUR_OF_DAY,18);
                int index;
                String time;
                if (now.before(morning) ) {
                    time="B"; //for breakfast
                    index=0;
                }
                else if (now.after(morning) && now.before(evening)) {
                    time="L"; //for lunch
                    index=1;
                }
                else {
                    time="D"; //for dinner
                    index=2;
                }
                if (param_time>-1 && param_time<=2) {
                    index=param_time;
                    if (param_time==0) time="B";
                    else if(param_time==1) time="L";
                    else time="D";
                }


                String id= String.valueOf(meals.getJSONObject(index).get("id"));
                startActionFoodCard(getApplicationContext(),id,time);

            }



        } catch (IOException | JSONException | StackOverflowError e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSuccessDietIntent() {
        Intent intent=new Intent();
        intent.setAction(RESULT_SUCCESSDIET);
        sendBroadcast(intent);
    }

    private void sendEmptyDietIntent() {
        Intent intent=new Intent();
        intent.setAction(RESULT_EMPTYDIET);
        sendBroadcast(intent);

    }

    //visszatér egy adott étellel
    private void sendFoodCardIntent(Food food,String time) {
        Intent intent=new Intent();
        intent.setAction(RESULT_CARD);
        Bundle card=new Bundle();
        card.putString("id",food.id);
        card.putString("name",food.name);
        card.putString("instruction",food.instruction);
        card.putString("ready",food.ready);
        card.putString("image",food.image);
        card.putBoolean("vegetarian",food.vegetarian);
        card.putBoolean("vegan",food.vegan);
        card.putBoolean("very_popular",food.very_popular);
        card.putBoolean("very_healthy",food.veryHealthy);
        card.putBoolean("cheap",food.cheap);
        card.putBoolean("dairyfree",food.dairyFree);
        card.putBoolean("glutenfree",food.glutenFree);
        card.putBoolean("favorit",food.favorit);
        card.putString("link",food.link);
        card.putString("time",time);

        intent.putExtra("result",card);
        sendBroadcast(intent);
    }

    //Visszatér a keresés eredméynével
    private void sendResultback(ArrayList<MiniFood> result) {
        Intent intent=new Intent();
        intent.setAction(RESULT_INFO);
        Bundle all=new Bundle();
        Bundle part;
        for(int i=0;i<result.size();i++) {
            part=new Bundle();
            part.putString("id",result.get(i).id);
            part.putString("title",result.get(i).title);
            part.putString("image",result.get(i).image);
            all.putBundle(String.valueOf(i),part);
        }
        all.putInt("size",result.size());
        intent.putExtra("result",all);
        sendBroadcast(intent);
    }
}

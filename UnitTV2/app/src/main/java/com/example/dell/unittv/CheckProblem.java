package com.example.dell.unittv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.unittv.item.CheckItem;
import com.example.dell.unittv.tools.HttpUtils;
import com.example.dell.unittv.tools.TimeManager;
import com.example.dell.unittv.tools.downImageTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dell-pc on 2016/3/6.
 */
public class CheckProblem extends Activity {

    private String unitId,untiName,cardNo,time,picUrl,Note,Statue,Fac,Position;
    private ListView lvCheckCard;
    String urlCheckCard;
    private ProgressDialog pDialog;
    private String checkPeriod;
    private ArrayList<CheckItem> checkList;
    private CheckListAdapter adapter;

    //public static final String GET_CHECKCARD_URL = "http://124.133.16.38:8093/test/mr/xfwlw/html/getCheckProblemJSON.php";
    public static final String GET_CHECKCARD_URL = "http://m.xiaofang365.cn/home/index/Problemxj";
    private void CheckCard() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_problem);
        lvCheckCard = (ListView) findViewById(R.id.lvCheckCard);


        checkList = new ArrayList<CheckItem>();
        adapter = new CheckListAdapter(this, checkList);
        lvCheckCard.setAdapter(adapter);

        unitId = getIntent().getStringExtra("unitId");
        untiName = getIntent().getStringExtra("unitName");

        checkPeriod = getIntent().getStringExtra("checkPeriod");
        urlCheckCard = GET_CHECKCARD_URL + "?unitId=" + unitId + "&startTime=" + checkPeriod ;
        TextView tvUnitName = (TextView) findViewById(R.id.tvUnitName);
        tvUnitName.setText(untiName);
        System.out.println(urlCheckCard);
        HttpUtils.getJSON(urlCheckCard, getCheckProblemHandler);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvCheckCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckItem checkItem = checkList.get(position);
                cardNo = checkItem.getCardNo();
                time = checkItem.getDateTime();
                picUrl = checkItem.getImageName();
                Note = checkItem.getNote();
                Statue = checkItem.getStatue();
                Fac = checkItem.getFac();
                Position = checkItem.getPosition();

                new GetCheckInfo().execute();


            }
        });



    }

    private Handler getCheckProblemHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            HttpURLConnection connInfo,connPic;
            InputStream isInfo,isPic;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String cardNo = object.getString("cardno");
                    String position = object.getString("position");
                    String fac = object.getString("fac");
                    String time = object.getString("time");
                    String statue = object.getString("statue");
                    String imageName = "http://m.xiaofang365.cn/public" + object.getString("imagename");
                    String note = object.getString("note");
                    System.out.println(cardNo);
                    checkList.add(new CheckItem(cardNo, position, time, imageName,fac,note,statue));
                 }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };


    public class CheckListAdapter extends BaseAdapter {

        private Context context;
        private List<CheckItem> checkList;

        private Map<Integer, View> viewMap = new HashMap<Integer, View>();

        public CheckListAdapter(Context context, List<CheckItem> checkList) {
            this.context = context;
            this.checkList = checkList;
        }

        @Override
        public int getCount() {
            return checkList.size();
        }

        @Override
        public CheckItem getItem(int position) {
            return checkList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.check_item, null);
            }
            ViewHolder holder = new ViewHolder();
            holder.mPhoto = (ImageView) convertView.findViewById(R.id.ivCheck);

            TextView tvCardNo = (TextView) convertView.findViewById(R.id.tvCardNoItem);
            TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
            TextView tvDateTime = (TextView) convertView.findViewById(R.id.tvTime);
            TextView tvFac = (TextView) convertView.findViewById(R.id.tvFac);
            TextView tvStatue = (TextView) convertView.findViewById(R.id.tvStatue);

            convertView.setTag(holder);

            CheckItem checkItem = checkList.get(position);

            tvCardNo.setText(checkItem.getCardNo());
            tvPosition.setText(checkItem.getPosition());
            String time = checkItem.getDateTime();
            time = TimeManager.getStrTime4(time);
            tvDateTime.setText(time);
            tvFac.setText(checkItem.getFac());

            String pic_url = checkItem.getImageName();

            if (!holder.mPhoto.isDrawingCacheEnabled()) {
                holder.mPhoto.setTag(pic_url);
                new downImageTask().execute(holder.mPhoto);
                holder.mPhoto.setDrawingCacheEnabled(true);
                viewMap.put(position, convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HttpUtils.setPicBitmap(holder.mPhoto, pic_url);

            String statue = checkItem.getStatue();
            if(statue.contains("不正常")){
                tvStatue.setTextColor(0xffff0000);
            }
            else {
                tvStatue.setTextColor(0xff0E583A);
            }
            statue = statue.replaceAll("_"," ");
            tvStatue.setText(statue);

            String picName = checkItem.getImageName();
            System.out.println(picName);

            //Bitmap bitmap = getDiskBitmap("/storage/emulated/0/checkpic/" + picName);
            //ivCheck.setImageBitmap(bitmap);

            if (position % 2 == 1) {
                convertView.setBackgroundColor(0xFFFFFFFF);
            } else {
                convertView.setBackgroundColor(0xFFF7F7F7);
            }

            return convertView;

        }
        public class ViewHolder {
            public TextView mNameText;
            public ImageView mPhoto;
        }

    }

    class GetCheckInfo extends AsyncTask<String, String, String> {
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckProblem.this);
            pDialog.setMessage("请稍后...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Log.d("Log_tag", "onPreExecute...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connInfo,connPic;
            InputStream isInfo,isPic;

            try {

                connPic = (HttpURLConnection) new URL(picUrl).openConnection();
                connPic.connect();
                isPic = connPic.getInputStream();
                bitmap = BitmapFactory.decodeStream(isPic);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if(bitmap != null) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog,
                        (ViewGroup) findViewById(R.id.dialog));
                ImageView ivCheck = (ImageView) layout.findViewById(R.id.ivCheck);
                ivCheck.setImageBitmap(bitmap);
                TextView tvCardNo = (TextView)layout.findViewById(R.id.tvCardNo);
                tvCardNo.setText(cardNo);
                TextView tvFac = (TextView)layout.findViewById(R.id.tvFac);
                tvFac.setText(Fac);
                TextView tvPosition = (TextView)layout.findViewById(R.id.tvPosition);
                tvPosition.setText(Position);
                TextView tvTime = (TextView)layout.findViewById(R.id.tvTime);
                time = TimeManager.getStrTime4(time);
                tvTime.setText(time);
                TextView tvStatue = (TextView)layout.findViewById(R.id.tvStatue);
                if(Statue.contains("不正常")){
                    tvStatue.setTextColor(0xffff0000);
                }
                else {
                    tvStatue.setTextColor(0xff0E583A);
                }
                Statue = Statue.replaceAll("_"," ");
                tvStatue.setText(Statue);
                TextView tvNote = (TextView)layout.findViewById(R.id.tvNote);
                tvNote.setText(Note);

                AlertDialog.Builder builder = new AlertDialog.Builder(CheckProblem.this);
                final AlertDialog dialog = builder.setView(layout).show();

                layout.findViewById(R.id.llCheckInfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }

        }
    }

}

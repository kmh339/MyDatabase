package org.androidtown.mydatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import db.DBHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateDatabase;
    private Button btnInsertDatabase;
    private Button btnSelectAllData;
    private ListView lvPeople;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateDatabase = (Button) findViewById(R.id.btnCreateButton);
        btnCreateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText etDBName = new EditText(MainActivity.this);
                etDBName.setHint("DB명을 입력하세요");

                //Dialog로 db의 이름 입력 받음
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Database 이름을 입력하세요.")
                        .setMessage("Database 이름")
                        .setView(etDBName)
                        .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if(etDBName.getText().toString().length() > 0){
                                    dbHelper = new DBHelper(
                                            MainActivity.this,
                                            etDBName.getText().toString(),
                                            null, 1
                                    );
                                    dbHelper.testDB();
                                }
                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });

        //데이터 추가 버튼
        btnInsertDatabase = findViewById(R.id.btnInsertButton);
        btnInsertDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText etName = new EditText(MainActivity.this);
                etName.setHint("이름 입력 : ");

                final EditText etAge = new EditText(MainActivity.this);
                etAge.setHint("나이 입력 : ");

                final EditText etPhone = new EditText(MainActivity.this);
                etPhone.setHint("전화번호 입력 : ");

                layout.addView(etName);
                layout.addView(etAge);
                layout.addView(etPhone);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("정보를 입력하세요")
                        .setView(layout)
                        .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                String name = etName.getText().toString();
                                String age = etAge.getText().toString();
                                String phone = etPhone.getText().toString();

                                if(dbHelper == null){
                                    dbHelper = new DBHelper(MainActivity.this, "TEST", null, 1);

                                }

                                Person person = new Person();
                                person.setName(name);
                                person.setAge(age);
                                person.setPhone(phone);

                                dbHelper.addPerson(person);

                            }
                        })
                        .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });


        //데이터 가져오기
        lvPeople = (ListView) findViewById(R.id.lvPeople);
        btnSelectAllData = findViewById(R.id.btnSelectAllData);
        btnSelectAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ListView를 보여준다.
                lvPeople.setVisibility(View.VISIBLE);

                if(dbHelper == null){
                    dbHelper = new DBHelper(MainActivity.this, "TEST", null, 1);
                }

                //1. 데이터 가져오기
                List people = dbHelper.getAllPersonData();

                //2. 데이터 보여주기
                lvPeople.setAdapter(new PersonListAdapter(people, MainActivity.this));
            }
        });




    }

    private class PersonListAdapter extends BaseAdapter {
        private List people;
        private Context context;

        /**
         * 생성자 * @param people : Person List * @param context
         */
        public PersonListAdapter(List people, Context context) {
            this.people = people;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.people.size();
        }

        @Override
        public Object getItem(int position) {
            return this.people.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;

            if (convertView == null) {
                // convertView가 없으면 초기화합니다.
                convertView = new LinearLayout(context);
                ((LinearLayout) convertView)
                        .setOrientation(LinearLayout.HORIZONTAL);

                TextView tvId = new TextView(context);
                tvId.setPadding(10, 0, 20, 0);
                tvId.setTextColor(Color.rgb(0, 0, 0));

                TextView tvName = new TextView(context);
                tvName.setPadding(20, 0, 20, 0);
                tvName.setTextColor(Color.rgb(0, 0, 0));

                TextView tvAge = new TextView(context);
                tvAge.setPadding(20, 0, 20, 0);
                tvAge.setTextColor(Color.rgb(0, 0, 0));

                TextView tvPhone = new TextView(context);
                tvPhone.setPadding(20, 0, 20, 0);
                tvPhone.setTextColor(Color.rgb(0, 0, 0));

                ((LinearLayout) convertView).addView(tvId);
                ((LinearLayout) convertView).addView(tvName);
                ((LinearLayout) convertView).addView(tvAge);
                ((LinearLayout) convertView).addView(tvPhone);

                holder = new Holder();
                holder.tvId = tvId;
                holder.tvName = tvName;
                holder.tvAge = tvAge;
                holder.tvPhone = tvPhone;

                convertView.setTag(holder);
            } else {
                // convertView가 있으면 홀더를 꺼냅니다.
                holder = (Holder) convertView.getTag();
            }

            // 한명의 데이터를 받아와서 입력합니다.
            final Person person = (Person) getItem(position);
            holder.tvId.setText(person.get_id() + "");
            holder.tvName.setText(person.getName());
            holder.tvAge.setText(person.getAge() + "");
            holder.tvPhone.setText(person.getPhone());

            //상세화면으로 이동하기
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("_id", person.get_id());
                    startActivity(intent);
                }
            });

            return convertView;
        }


        private class Holder {
            public TextView tvId;
            public TextView tvName;
            public TextView tvAge;
            public TextView tvPhone;
        }


    }

}

package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.androidtown.mydatabase.Person;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    public static final int DB_VERSION = 2;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    //DB가 존재하지 않을때 딱 한번 실행됨, DB를 만드는 역할을 함

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String보다 StringBuffer가 쿼리만들기 편함
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" CREATE TABLE TEST_TABLE( ");
        stringBuffer.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuffer.append(" NAME TEXT, ");
        stringBuffer.append(" AGE INTEGER, ");
        stringBuffer.append(" PHONE TEXT ) ");

        //SQLite db로 쿼리 실행
        db.execSQL(stringBuffer.toString());

        Toast.makeText(context, "Table 생성 완료", Toast.LENGTH_SHORT).show();
    }

    //어플리케이션의 버전이 올라가서 테이블구조가 변경될때 실행
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }

    public void addPerson(Person person) {
        //1. DB 가져오기
        SQLiteDatabase db = getWritableDatabase();

        //2. Insert Person Data
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" INSERT INTO TEST_TABLE ( ");
        stringBuffer.append(" NAME, AGE, PHONE ) ");
        stringBuffer.append(" VALUES ( ?, ?, ? ) ");

        //AGE 는 Int 이므로 홀따옴표를 주지 않는다.

        db.execSQL(stringBuffer.toString(),
                new Object[]{
                        person.getName(),
                        person.getAge(),
                        person.getPhone()
                });
        Toast.makeText(context, "Insert Complete", Toast.LENGTH_SHORT).show();
    }

    public List getAllPersonData() {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT _ID, NAME, AGE, PHONE FROM TEST_TABLE ");
        // 읽기 전용 DB 객체를 만든다.//
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List people = new ArrayList();
        Person person = null;

        // moveToNext 다음에 데이터가 있으면 true 없으면 false
        while (cursor.moveToNext()) {
            person = new Person();
            person.set_id(cursor.getInt(0));
            person.setName(cursor.getString(1));
            person.setAge(cursor.getString(2));
            person.setPhone(cursor.getString(3));
            people.add(person);
        }
        return people;
    }

    public Person getPersonById(int _id){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" SELECT NAME, AGE, PHONE FROM TEST_TABLE WHERE _ID = ?");

        //읽기 전용 DB객체를 만든다.
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(stringBuffer.toString(), new String[]{_id + ""});

        Person person = new Person();
        if(cursor.moveToNext()){
            person.setName(cursor.getString(0));
            person.setAge(cursor.getString(1));
            person.setAge(cursor.getString(2));
            person.setPhone(cursor.getString(3));
        }
        return person;
    }

}

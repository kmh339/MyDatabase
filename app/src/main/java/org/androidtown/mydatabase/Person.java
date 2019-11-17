package org.androidtown.mydatabase;

public class Person {

    //PK
    private int _id;

    private String name;
    private String age;
    private String phone;

    public int get_id(){
        return _id;
    }

    public String getName(){
        return name;
    }

    public String getAge(){
        return age;
    }

    public String getPhone(){
        return phone;
    }

    public void set_id(int _id){
        this._id = _id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(String age){
        this.age = age;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}

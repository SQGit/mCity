package mcity.com.mcity;

/**
 * Created by Admin on 08-11-2016.
 */
public class PlaceDetails {

    int _id;
    String _station_name;
    String _lat;
    String _lng;
    String address;
    String _time;
    String phone1;
    String phone2;
    String phone3;
    String name1;
    String name2;
    String name3;


    public PlaceDetails(){

    }

    public PlaceDetails(String _station_name, String lat,String lng, String address,String _time,String phone1,String phone2,String phone3,String name1,String name2,String name3){
        this._station_name = _station_name;
        this._lat = lat;
        this._lng = lng;
        this.address = address;
        this._time = _time;
        this.phone1=phone1;
        this.phone2=phone2;
        this.phone3=phone3;
        this.name1=name1;
        this.name2=name2;
        this.name3=name3;

    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getPlaceName(){
        return this._station_name;
    }

    // setting name
    public void setPlaceName(String name){
        this._station_name = name;
    }

    public String getLatitude(){
        return this._lat;
    }

    public void setLatitude(String lat){
        this._lat = lat;
    }

    public String getLongitude(){
        return this._lng;
    }

    // setting phone number
    public void setLongitude(String lng){
        this._lng = lng;
    }

    public String getAddress(){
        return this.address;
    }

    // setting phone number
    public void setAddress(String address){
        this.address = address;
    }
    public String getTime(){
        return this._time;
    }

    // setting phone number
    public void setTime(String _time){

        this._time = _time;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }



    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }


    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }




    public String getname1() {
        return name1;
    }

    public void setname1(String name1) {
        this.name1 = name1;
    }


    public String getname2() {
        return name2;
    }

    public void setname2(String name2) {
        this.name2 = name2;
    }


    public String getname3() {
        return name3;
    }

    public void setname3(String name3) {
        this.name3 = name3;
    }



}



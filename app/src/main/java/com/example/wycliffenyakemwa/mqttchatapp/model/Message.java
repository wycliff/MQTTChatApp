package com.example.wycliffenyakemwa.mqttchatapp.model;

public class Message {

    private String _payload;
    private String  _topic;
    private int _qos;
    private boolean _belongsToCurrentUser;

    public Message(String payload, String topic, int qos , boolean belongsToCurrentUser) {
        this._payload = payload;
        this._qos = qos;
        this._topic= topic;
        this._belongsToCurrentUser= belongsToCurrentUser;
    }

    public Message(){

    }

    public String getPyload() {
        return _payload;
    }

    public String getTopic() {
        return _topic;
    }

    public int getQos() {
        return _qos;
    }

    public boolean isBelongsToCurrentUser () {
        return _belongsToCurrentUser;
    }



    public void set_payload( String payload){

        this._payload = payload;
    }

    public void set_topic(String topic){

        this._topic = topic;
    }

    public void set_qos(int qos){
        this._qos = qos;

    }

}

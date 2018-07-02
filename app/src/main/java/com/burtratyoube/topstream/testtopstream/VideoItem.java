package com.burtratyoube.topstream.testtopstream;

/**
 * Created by Vlad on 07.06.2018.
 */

public class VideoItem {
    public String name;
    public String uri;

    VideoItem(){}

    VideoItem(String name, String uri){
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

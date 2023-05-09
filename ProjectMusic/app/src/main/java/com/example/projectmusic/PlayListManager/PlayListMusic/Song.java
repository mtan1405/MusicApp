package com.example.projectmusic.PlayListManager.PlayListMusic;

import android.graphics.Bitmap;

public class Song {
  private   String linkImage = null ;
  private   String linkSong = null ;
  private String name = null ;
  private String author = null;
  public Bitmap image = null ;
  public Song (String linkImage , String linkSong , String name , String author )
  {
      this.linkImage = linkImage ;
      this.linkSong = linkSong ;
      this.name = name ;
      this.author = author ;

  }

    public String getName() {
        return name;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkImage(String linkImage) {
        if (linkImage.startsWith("https://"))
        this.linkImage = linkImage;
        else this.linkImage= "https://"+linkImage ;
    }

    public void setLinkSong(String linkSong) {
        if (linkSong.startsWith("https://"))
            this.linkSong = linkSong;
        else this.linkSong= "https://"+linkSong ;
    }
    public void setName (String name)
    {
        this.name = name ;
    }
    public String getAuthor (){return author ; }
}

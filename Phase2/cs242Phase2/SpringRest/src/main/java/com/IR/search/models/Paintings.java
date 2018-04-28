package com.IR.search.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="documentData")
public class Paintings
{
    private String[] tags;

    private String medium;

    private String url;

    private String size;

    private String title;

    private String price;

    private String idname;

    private String[] color;

    private String views;

    private String description;

    private String likes;

    private String imgsrc;

    private String filename;

    private String artistURL;

    private String artist;

    public String[] getTags ()
    {
        return tags;
    }

    public void setTags (String[] tags)
    {
        this.tags = tags;
    }

    public String getMedium ()
    {
        return medium;
    }

    public void setMedium (String medium)
    {
        this.medium = medium;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getSize ()
    {
        return size;
    }

    public void setSize (String size)
    {
        this.size = size;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getidname ()
    {
        return idname;
    }

    public void setid (String id)
    {
        this.idname = idname;
    }

    public String[] getColor ()
    {
        return color;
    }

    public void setColor (String[] color)
    {
        this.color = color;
    }

    public String getViews ()
    {
        return views;
    }

    public void setViews (String views)
    {
        this.views = views;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getLikes ()
    {
        return likes;
    }

    public void setLikes (String likes)
    {
        this.likes = likes;
    }

    public String getImgsrc ()
    {
        return imgsrc;
    }

    public void setImgsrc (String imgsrc)
    {
        this.imgsrc = imgsrc;
    }

    public String getFilename ()
    {
        return filename;
    }

    public void setFilename (String filename)
    {
        this.filename = filename;
    }

    public String getArtistURL ()
    {
        return artistURL;
    }

    public void setArtistURL (String artistURL)
    {
        this.artistURL = artistURL;
    }

    public String getArtist ()
    {
        return artist;
    }

    public void setArtist (String artist)
    {
        this.artist = artist;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tags = "+tags+", medium = "+medium+", url = "+url+", size = "+size+", title = "+title+", price = "+price+", idname = "+idname+", color = "+color+", views = "+views+", description = "+description+", likes = "+likes+", imgsrc = "+imgsrc+", filename = "+filename+", artistURL = "+artistURL+", artist = "+artist+"]";
    }
}

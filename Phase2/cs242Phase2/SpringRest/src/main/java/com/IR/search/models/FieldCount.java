package com.IR.search.models;

public class FieldCount
{
    private int tags;

    private int title;

    private int desc;

    private int color;

    private int artist;

    private int medium;

    public int getTags ()
    {
        return tags;
    }

    public void setTags (int tags)
    {
        this.tags = tags;
    }

    public int getTitle ()
    {
        return title;
    }

    public void setTitle (int title)
    {
        this.title = title;
    }

    public int getDesc ()
    {
        return desc;
    }

    public void setDesc (int desc)
    {
        this.desc = desc;
    }

    public int getColor ()
    {
        return color;
    }

    public void setColor (int color)
    {
        this.color = color;
    }

    public int getArtist ()
    {
        return artist;
    }

    public void setArtist (int artist)
    {
        this.artist = artist;
    }

    public int getMedium ()
    {
        return medium;
    }

    public void setMedium (int medium)
    {
        this.medium = medium;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tags = "+tags+", title = "+title+", desc = "+desc+", color = "+color+", artist = "+artist+", medium = "+medium+"]";
    }
}
package com.IR.search.models;


public class Document
{
    private String docid;


    private FieldCount fieldCount;

    public String getId ()
    {
        return docid;
    }

    public void setId (String id)
    {
        this.docid = id;
    }

    public FieldCount getFieldCount ()
    {
        return fieldCount;
    }

    public void setFieldCount (FieldCount fieldCount)
    {
        this.fieldCount = fieldCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [docid = "+docid+", fieldCount = "+fieldCount+"]";
    }
}

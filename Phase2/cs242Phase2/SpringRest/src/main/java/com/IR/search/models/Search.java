package com.IR.search.models;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="irsearch")
//@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Search {

    private String word;

    @NotBlank
    //@Size(max=100)
    @Indexed(unique=true)

    private com.IR.search.models.Document[] document;


   // private String word;

    public com.IR.search.models.Document[] getDocument ()
    {
        return document;
    }

    public void setDocument (com.IR.search.models.Document[] document)
    {
        this.document = document;
    }

    public String getword ()
    {
        return word;
    }

    public void setWord (String word)
    {
        this.word = word;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [document = "+document+", word = "+word+"]";
    }

}

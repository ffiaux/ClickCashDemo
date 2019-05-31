package tech.ffiaux.clickcashdemo.model;

import java.util.Date;

public class Advertisement
{
    private Long id;
    private String title;
    private String description;
    private String thumbUrl;
    private Date publishedOn;
    private Date expiresOn;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl)
    {
        this.thumbUrl = thumbUrl;
    }

    public Date getPublishedOn()
    {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn)
    {
        this.publishedOn = publishedOn;
    }

    public Date getExpiresOn()
    {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn)
    {
        this.expiresOn = expiresOn;
    }

    @Override
    public String toString()
    {
        return this.title;
    }
}

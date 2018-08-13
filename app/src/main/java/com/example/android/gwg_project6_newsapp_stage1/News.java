package com.example.android.gwg_project6_newsapp_stage1;

/**
 * {@link News} represents every article.
 * Each object has 6 properties: Section Name, Headline Title, Short Desc,
 * Author name, website link, thumbnail image.
 */
public class News {
    // News Article section name
    private String sectionName;

    // News Article headline title
    private String headTitle;

    // News Article short description
    private String shortDesc;

    // News Article author name
    private  String authorName;

    //News Article publication day
    private String publicationDay;

    // News Article website link
    private String websiteLink;

    //News Article thumbnail image
    private String thumbnailImage;

    /**
     * cConstruct a new {@link News} object.
     *
     * @param thumbnailImage is the thumbnail image of the news.
     * @param sectionName is the section news (e.g. Trends, World, Sports)
     * @param headTitle is the header title
     * @param shortDesc short brief of the news content
     * @param authorName is the athor name
     * @param publicationDay is the publication day
     * @param websiteLink is the url to the website news
     */
    public News(String thumbnailImage, String sectionName, String headTitle, String shortDesc, String authorName, String publicationDay, String websiteLink) {
        this.sectionName = sectionName;
        this.headTitle = headTitle;
        this.shortDesc = shortDesc;
        this.authorName = authorName;
        this.websiteLink = websiteLink;
        this.publicationDay = publicationDay;
        this.thumbnailImage = thumbnailImage;
    }

    /**
     *
     * @return Section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     *
     * @return main header title
     */
    public String getHeadTitle() {
        return headTitle;
    }

    /**
     *
     * @return short brief of the news content
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     *
     * @return the author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     *
     * @return website url
     */
    public String getWebsiteLink() {
        return websiteLink;
    }

    /**
     *
     * @return publivation date
     */
    public String getPublicationDay() {
        return publicationDay;
    }

    /**
     *
     * @return url of the thumbnail image
     */
    public String getThumbnailImage() {
        return thumbnailImage;
    }
}

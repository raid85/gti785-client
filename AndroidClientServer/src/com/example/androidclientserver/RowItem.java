package com.example.androidclientserver;

public class RowItem {
    private int imageId;
    private String title;
    private String desc;
     
    public RowItem(int imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }
    public RowItem(Integer imageId2, String title2, String desc2) {
		// TODO Auto-generated constructor stub
    	
	}
	public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }   
}
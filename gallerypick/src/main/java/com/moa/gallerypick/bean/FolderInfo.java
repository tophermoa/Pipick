package com.moa.gallerypick.bean;

import java.util.List;


public class FolderInfo {

    public String name;                         // Nama folder
    public String path;                         // Jalur folder
    public PhotoInfo photoInfo;
    public List<PhotoInfo> photoInfoList;       // list foto

    @Override
    public boolean equals(Object object) {
        try {
            FolderInfo other = (FolderInfo) object;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(object);
    }

    @Override
    public String toString() {
        return "FolderInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", photoInfo=" + photoInfo +
                ", photoInfoList=" + photoInfoList +
                '}';
    }
}

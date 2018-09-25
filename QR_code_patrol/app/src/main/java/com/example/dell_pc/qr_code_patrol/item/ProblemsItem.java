package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ProblemsItem {
    private String id;
    private String type;


    public ProblemsItem(String id, String type) {
        setId(id);
        setType(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }
}


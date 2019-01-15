package com.blackwing.easyExploration.items;

public class Items {
    private static Items ourInstance = new Items();

    public static Items getInstance() {
        return ourInstance;
    }

    private Items() {
    }
}

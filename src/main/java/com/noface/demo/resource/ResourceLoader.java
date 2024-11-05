package com.noface.demo.resource;

public class ResourceLoader {
    private static ResourceLoader resourceLoader;
    public static ResourceLoader getInstance(){
        if(resourceLoader == null){
            resourceLoader = new ResourceLoader();
        }
        return resourceLoader;
    }

}

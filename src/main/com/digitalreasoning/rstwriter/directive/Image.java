package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

import java.util.Arrays;
import java.util.HashMap;
import static com.digitalreasoning.rstwriter.directive.BaseDirective.escapeString;

/**
 * Created by creynolds on 7/17/15.
 */
public class Image implements Directive {
    public enum Alignment {
        TOP, MIDDLE, BOTTOM, LEFT, CENTER, RIGHT
    }
    private BaseDirective directive;
    private HashMap<String, String> options;
    
    public Image(String imagePath){
        directive = new BaseDirective("image");
        directive.addArgument(imagePath);
        options = new HashMap<>();
    }

    public Image setHeight(int height){
        options.put("height", "" + height);
        return this;
    }
    
    public Image setWidth(int width){
        options.put("width", "" + width);
        return this;
    }

    public Image setScale(int scale){
        options.put("scale", "" + scale);
        return this;
    }
    
    public Image setAlignment(Alignment value){
        options.put("align", value.name().toLowerCase());
        return this;
    }
    
    public Image setAlternateText(String alternate){
        options.put("alt", escapeString(alternate));
        return this;
    }
    
    public Image setTarget(String targetPath){
        options.put("target", escapeString(targetPath));
        return this;
    }

    protected HashMap<String,String> getOptionMap(){
        return options;
    }

    @Override
    public String write(){
        String[] arr = new String[options.keySet().size()];
        arr = options.keySet().toArray(arr);
        Arrays.sort(arr);
        for(String str : arr){
            directive.addOption(str, options.get(str));
        }
        return directive.write();
    }

}

package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;
import com.digitalreasoning.rstwriter.Inline;

import java.util.HashMap;

/**
 * Created by creynolds on 7/17/15.
 */
public class Figure implements Directive {
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int FIGWIDTH_IMAGE = -1;
    
    private BaseDirective directive;
    private Image image;
    private int figwidth = -5;
    private String figAlign;

    public Figure(String imagePath){
        directive = new BaseDirective("figure");
        directive.addArgument(imagePath);
        image = new Image(imagePath);
    }

    public Figure setImageHeight(int height){
        image.setHeight(height);
        return this;
    }

    public Figure setImageWidth(int width){
        image.setWidth(width);
        return this;
    }

    public Figure setImageScale(int scale){
        image.setScale(scale);
        return this;
    }

    public Figure setImageAlternateText(String alternate){
        image.setAlternateText(alternate);
        return this;
    }

    public Figure setImageTarget(String targetPath){
        image.setTarget(targetPath);
        return this;
    }
    
    public Figure setFigureWidth(int width){
        figwidth = width;
        return this;
    }
    
    public Figure setFigureAlignment(int align){
        switch(align){
            case ALIGN_LEFT:
                figAlign = "left";
                break;
            case ALIGN_CENTER:
                figAlign = "center";
                break;
            case ALIGN_RIGHT:
                figAlign = "right";
                break;
            default:
                throw new IllegalArgumentException("Unrecognized alignment code");
        }
        return this;
    }

    public Figure addLegend(String content, Inline... inlines){
        directive.addContent(new Paragraph(content, inlines));
        return this;
    }

    public Figure addLegend(RstBodyElement element){
        directive.addContent(element);
        return this;
    }

    @Override
    public String write(){
        if(figAlign != null) {
            directive.addOption("align", figAlign);
        }
        if(figwidth == FIGWIDTH_IMAGE){
            directive.addOption("figwidth", "image");
        }
        else if(figwidth >= 0){
            directive.addOption("figwidth", "" + figwidth);
        }
        for(HashMap.Entry<String,String> entry : image.getOptionMap().entrySet()){
            directive.addOption(entry.getKey(), entry.getValue());
        }
        return directive.write();
    }
}

package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;
import com.digitalreasoning.rstwriter.bodyelement.Paragraph;
import com.digitalreasoning.rstwriter.RstBodyElement;
import com.digitalreasoning.rstwriter.Inline;

import java.util.HashMap;

/**
 * The Figure directive contains a picture and content afterward. The directive takes an argument containing the picture's
 * location, options to modify the figure and contained image, and content to place after the picture.
 */
public class Figure implements Directive {
    /**
     * Denotes figure alignment to the left
     */
    public static final int ALIGN_LEFT = 0;
    /**
     * Denotes figure alignment in the center
     */
    public static final int ALIGN_CENTER = 1;
    /**
     * Denotes figure alignment to the left
     */
    public static final int ALIGN_RIGHT = 2;
    /**
     * Special value for figure width, defined as :figwidth: image
     */
    public static final int FIGWIDTH_IMAGE = -1;
    
    private BaseDirective directive;
    private Image image;
    private int figwidth = -5;
    private String figAlign;

    /**
     * creates a figure with the image at the location specified
     * @param imagePath the location of the image to be included in the figure
     */
    public Figure(String imagePath){
        directive = new BaseDirective("figure");
        directive.addArgument(imagePath);
        image = new Image(imagePath);
    }

    /**
     * sets the height of the image
     * @param height the height of the image
     * @return this Figure with the image height set
     */
    public Figure setImageHeight(int height){
        image.setHeight(height);
        return this;
    }

    /**
     * sets the width of the image
     * @param width the width of the image
     * @return this Figure with the image width set
     */
    public Figure setImageWidth(int width){
        image.setWidth(width);
        return this;
    }

    /**
     * sets the scale of the image
     * @param scale the scale of the image
     * @return this Figure with the image scale set
     */
    public Figure setImageScale(int scale){
        image.setScale(scale);
        return this;
    }

    /**
     * sets the alternate text of the image
     * @param alternate the alternate text of the image
     * @return this Figure with the image alternate text set
     */
    public Figure setImageAlternateText(String alternate){
        image.setAlternateText(alternate);
        return this;
    }

    /**
     * Sets a link that will be followed when the image is clicked on
     * @param targetPath destination of the link assigned to the image
     * @return this Figure with the image link destination set
     */
    public Figure setImageTarget(String targetPath){
        image.setTarget(targetPath);
        return this;
    }

    /**
     * Sets the width of this figure. FIGWIDTH_IMAGE is a special constant to denote :figwidth: image
     * @param width the desired width of the figure
     * @return this Figure with the figure's width set
     */
    public Figure setFigureWidth(int width){
        figwidth = width;
        return this;
    }

    /**
     * Sets the alignment of the figure. Predefined values are ALIGN_LEFT, ALIGN_CENTER, and ALIGN_RIGHT
     * @param align the code of the desired alignment
     * @return this Figure with the figure alignment set
     */
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

    /**
     * Adds to the existing content below the image in this figure
     * @param content the content to be added below the image (will be processed for inline markup)
     * @param inlines optional arguments specifying inline markup in the content
     * @return this figure with the content added
     */
    public Figure addLegend(String content, Inline... inlines){
        directive.addContent(new Paragraph(content, inlines));
        return this;
    }

    /**
     * Adds to the existing content below the image in this figure
     * @param element the content to be added below the image
     * @return this figure with the content added
     */
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

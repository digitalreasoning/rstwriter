package com.digitalreasoning.rstwriter.directive;

import com.digitalreasoning.rstwriter.Directive;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import static com.digitalreasoning.rstwriter.directive.BaseDirective.escapeString;

/**
 * The Image directive is used to create images in reStructuredText by specifying the path to the image, whether on a file
 * system or by a URL. The directive takes the path as an argument and options to modify the image, but no content
 */
public class Image implements Directive {
    /**
     * All possible values for the image alignment
     */
    public enum Alignment {
        TOP, MIDDLE, BOTTOM, LEFT, CENTER, RIGHT
    }
    private BaseDirective directive;
    private Map<String, String> options;

    /**
     * Creates an image directive with the specified image
     * @param imagePath path to the desired image
     */
    public Image(String imagePath){
        directive = new BaseDirective("image");
        directive.addArgument(imagePath);
        options = new TreeMap<>();
    }

    /**
     * sets the height of the image
     * @param height the height of the image
     * @return this image with the height set
     */
    public Image setHeight(int height){
        options.put("height", "" + height);
        return this;
    }

    /**
     * sets the width of the image
     * @param width the width of the image
     * @return this image with the width set
     */
    public Image setWidth(int width){
        options.put("width", "" + width);
        return this;
    }

    /**
     * sets the scale of the image
     * @param scale the scale of the image
     * @return this image with the scale set
     */
    public Image setScale(int scale){
        options.put("scale", "" + scale);
        return this;
    }

    /**
     * Sets the alignment of the figure. Predefined values are in the enum Alignment
     * @param value the desired alignment
     * @return this image with the alignment set
     */
    public Image setAlignment(Alignment value){
        options.put("align", value.name().toLowerCase());
        return this;
    }

    /**
     * sets the alternate text of the image
     * @param alternate the alternate text of the image
     * @return this image with the alternate text set
     */
    public Image setAlternateText(String alternate){
        options.put("alt", escapeString(alternate));
        return this;
    }

    /**
     * Sets a link that will be followed when the image is clicked on
     * @param targetPath destination of the link assigned to the image
     * @return this image with the link destination set
     */
    public Image setTarget(String targetPath){
        options.put("target", escapeString(targetPath));
        return this;
    }

    protected Map<String,String> getOptionMap(){
        return options;
    }

    @Override
    public String write(){
        for(Map.Entry<String, String> entry : options.entrySet()){
            directive.addOption(entry.getKey(), entry.getValue());
        }
        return directive.write();
    }

}

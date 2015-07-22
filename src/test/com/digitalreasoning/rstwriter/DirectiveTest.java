package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.directive.Admonition;
import com.digitalreasoning.rstwriter.directive.Figure;
import com.digitalreasoning.rstwriter.directive.Image;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/21/15.
 */
public class DirectiveTest {

    @Test
    public void simpleTest(){
        Admonition a = new Admonition(Admonition.Type.ATTENTION, "This is an admonition");
        String result = ".. attention:: \n\n    This is an admonition\n";
        assertEquals(result, a.write());
        a.addParagraph("Yessir it is.");
        result = ".. attention:: \n\n    This is an admonition\n    \n    Yessir it is.\n";
        assertEquals(result, a.write());
        a.addParagraph("New line in\n the content");
        result = ".. attention:: \n\n    This is an admonition\n    \n    Yessir it is.\n    \n    New line in\n     the content\n";
        assertEquals(result, a.write());
    }

    @Test
    public void argsAndOptionsTest(){
        Image i = new Image("http://image.jpeg");
        String result = ".. image:: http://image.jpeg\n";
        assertEquals(result, i.write());
        i.setAlternateText("text$").setWidth(300).setAlignment(Image.Alignment.TOP);
        result = ".. image:: http://image.jpeg\n    " +
                ":align:    top\n    " +
                ":alt:    text$\n    " +
                ":width:    300\n";
        assertEquals(result, i.write());
    }

    @Test
    public void fullDirectiveTest(){
        Figure f = new Figure("http://img.png").setImageTarget("http://google.com").setImageAlternateText("Alt")
                .setFigureAlignment(Figure.ALIGN_CENTER).setImageHeight(300).addLegend("Legend test\nwith multiple lines");
        String figure = f.write();
        assertEquals(".. figure:: http://img.png", figure.substring(0, figure.indexOf('\n')));
        String[] options = figure.substring(figure.indexOf('\n') +1, figure.indexOf("\n    Legend")).split("\n");
        assertTrue(options.length == 4);
        ArrayList<String> expOptions = new ArrayList<>();
        expOptions.add("    :align:    center"); expOptions.add("    :alt:    Alt");
        expOptions.add("    :height:    300"); expOptions.add("    :target:    http://google.com");

        for(String opt : options){
            assertTrue(expOptions.contains(opt));
            expOptions.remove(opt);
        }
        assertTrue(expOptions.isEmpty());
        assertEquals("    Legend test\n    with multiple lines\n", figure.substring(figure.indexOf("\n    Legend") + 1));
    }
}

package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.directive.Image;
import com.digitalreasoning.rstwriter.directive.Replace;
import com.digitalreasoning.rstwriter.bodyelement.LinkDefinition;
import com.digitalreasoning.rstwriter.bodyelement.SubstitutionDefinition;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/20/15.
 */
public class ExtraElementsTest {

    @Test
    public void transitionTest(){
        String transition = new Transition().write();
        assertTrue(transition.length() > 4);
        for(int i = 0; i<transition.length() -1; i++){
            assertEquals('-', transition.charAt(i));
        }
        assertTrue(transition.endsWith("\n"));
    }

    @Test
    public void linkDefinitionTest(){
        String str = new LinkDefinition("name", "http://www.google.com").write();
        assertEquals(".. _name: http://www.google.com\n", str);

        str = new LinkDefinition("name", "").write();
        assertEquals(".. _name: \n", str);

        str = new LinkDefinition("my link", "`name`_").write();
        assertEquals(".. _my link: `name`_\n", str);
    }

    @Test
    public void substitutionDefTest(){
        Replace r = new Replace("hhhh");
        String sub = new SubstitutionDefinition("tar", r).write();
        assertEquals(".. |tar| replace:: hhhh\n", sub);

        Image i = new Image("https://img.png").setAlignment(Image.Alignment.BOTTOM);
        sub = new SubstitutionDefinition("sub", i).write();
        assertEquals(".. |sub| image:: https://img.png\n    :align:    bottom\n", sub);
    }
}

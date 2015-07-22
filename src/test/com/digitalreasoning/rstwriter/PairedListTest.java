package com.digitalreasoning.rstwriter;

import com.digitalreasoning.rstwriter.bodyelement.DefinitionList;
import com.digitalreasoning.rstwriter.bodyelement.FieldList;
import com.digitalreasoning.rstwriter.bodyelement.OptionList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by creynolds on 7/20/15.
 */
public class PairedListTest {
    final String INDENT = "    ";
    @Test
    public void definitionListTest(){
        DefinitionList list = new DefinitionList();
        list.addItem("term", "definition").addItem("term2", "definition");
        String[] classifiers = {"classifier", "cat"};
        list.addItem("class", classifiers, "definition").addItem("item", "multi\nline\ndef");
        String result = "term\n%definition\nterm2\n%definition\nclass : classifier : cat\n%definition\nitem\n%multi\n%line\n%def\n";
        result = result.replaceAll("%", INDENT);
        assertEquals("Definition test", result, list.write());
    }

    @Test
    public void optionListTest(){
        OptionList options = new OptionList();
        options.addItem("-a", "definition").addItem("-b", "nextDef")
                .buildItem("-n").addAlias("--alias").addDefinition("aliasdef").addToList()
                .buildItem("-a").addArgument("arg").addArgument("arg2").addDefinition("Arguments").addToList()
                .buildItem("-f").addArgument("num").addAlias("--nickname").addDefinition("def").addToList()
                .addItem("--go", "multi\nliner").addItem("/dos", "dos style").addItem("/empty", "");
        String result = ("-a%definition\n-b%nextDef\n-n, --alias%aliasdef\n-a <arg, arg2>%Arguments\n" +
                "-f, --nickname <num>%def\n--go%multi\n    %liner\n/dos%dos style\n/empty%\n").replaceAll("%", INDENT);
        assertEquals("optionList fail", result, options.write());
    }

    @Test
    public void fieldListTest(){
        FieldList fields = new FieldList();
        fields.addItem("field", "def").addItem("second", "longer definition").addItem("test", "multi\nliner\ndef")
                .addItem("emptyDef", "");
        String result = ":field:%def\n:second:%longer definition\n:test:%multi\n      %liner\n      %def\n:emptyDef:%\n"
                .replaceAll("%", INDENT);
        assertEquals("field lists", result, fields.write());
    }

    @Test
    public void errorTest(){
        try{
            new FieldList().addItem("", "def");
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new DefinitionList().addItem("", "def");
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new OptionList().addItem("", "def");
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new OptionList().addItem("illegal", "syntax");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().addItem("-ill egal", "syntax");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().addItem("#illegal", "syntax");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addAlias("-rr rr");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addAlias("rrr");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addArgument("arg ument");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addAlias("");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addArgument("");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new OptionList().buildItem("-x").addArgument("arg\nument");
            fail();
        }catch(IllegalArgumentException e){}

        try{
            new OptionList().addItem("-xx\nyy", "bad");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new FieldList().addItem("-xx\nyy", "bad");
            fail();
        }catch(IllegalArgumentException e){}
        try{
            new DefinitionList().addItem("-xx\nyy", "bad");
            fail();
        }catch(IllegalArgumentException e){}

    }
}

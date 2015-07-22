package com.digitalreasoning.rstwriter.bodyelement;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * Created by creynolds on 7/17/15.
 */
public class OptionList extends PairedList {
    
    public OptionList(){
        super(Constants.INDENT);
    }

    public OptionList addItem(String option, String definition, Inline... inlines){
        validateSyntax(option);
        super.addItem(option, definition, inlines);
        return this;
    }

    public OptionList addItem(String option, RstBodyElement definition){
        validateSyntax(option);
        super.addItem(option, definition);
        return this;
    }

    public ItemBuilder buildItem(String option){
        return new ItemBuilder(option, this);
    }

    @Override
    public String write() {
        try{
            return super.write();
        }catch(IllegalStateException e){
            throw new IllegalStateException(e.getMessage().replace("left", "fields").replace("right", "definitions"));
        }
    }

    private void validateSyntax(String option){
        if(option.contains(" ") || option.contains("\t") || option.contains("\n"))
            throw new IllegalArgumentException("White space not allowed in options: " + option);
        if( !(option.startsWith("-") || option.startsWith("/")) )
            throw new IllegalArgumentException("Beginning syntax not recognized in option: " + option);

    }

    private void addCustom(String optionLeft, String definition){
        super.addItem(optionLeft, definition);
    }

    public class ItemBuilder {
        private String name;
        private String args;
        private String aliases;
        private String definition;
        private OptionList optList;

        private ItemBuilder(String option, OptionList list){
            validateSyntax(option);
            name = option;
            args = "<";
            aliases = "";
            definition = "";
            optList = list;
        }

        public ItemBuilder addArgument(String argument){
            if(argument.contains(" ") || argument.contains("\t") || argument.contains("\n"))
                throw new IllegalArgumentException("Argument can't contain white space");
            if(argument.equals("")) throw new IllegalArgumentException("Empty arguments not allowed");
            args += argument + ", ";
            return this;
        }

        public ItemBuilder addAlias(String alias){
            validateSyntax(alias);
            aliases += ", " + alias;
            return this;
        }

        public ItemBuilder addDefinition(String definition, Inline... inlines){
            this.definition += new Paragraph(definition, inlines).getText();
            return this;
        }

        public ItemBuilder addDefinition(RstBodyElement element){
            if(definition.length()>0)
                definition += "\n\n";
            definition += element.write();
            return this;
        }

        public OptionList addToList(){
            if(args.equals("<"))
                args = "";
            else
                args = " " + args.substring(0, args.length()-2) + ">";
            String optionLeft = name + aliases + args;
            optList.addCustom(optionLeft, definition);
            return optList;
        }

    }
}

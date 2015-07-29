package com.digitalreasoning.rstwriter.bodyelement;

import java.util.Map;

import com.digitalreasoning.rstwriter.Inline;
import com.digitalreasoning.rstwriter.RstBodyElement;

/**
 * The OptionList class supports the creation of lists of command-line options and their definitions. The reStructuredText
 * option lists are flexible, allowing the "option" side of the list to have arguments and aliases, so an additional class
 * ItemBuilder is supplied to support as much flexibility as possible without a need to know a lot of reStructuredText. 
 * Definitions of options are equally flexible, possibly consisting of single lines, multiple lines, body elements or even
 * no text at all. ReStructuredText supports UNIX-style options ("-a", "--help") and DOS-style options ("/V"). A 
 * reStructuredText representation of an option list looks like:
 * {@code
 * -a   Adds a number
 * -b   Subtracts a number
 * -c arg   Takes an argument
 * -d, --delete   deletes the data
 * /E   runs a command
 * }
 */
public class OptionList extends PairedList {

    /**
     * Creates an empty option list
     */
    public OptionList(){
        super(Utils.INDENT);
    }

    /**
     * creates an option list from the provided map. Items are checked for correct syntax before being added. Order is 
     * determined by the map's foreach implementation. Options will be the map's keys, definitions will be the map's values
     * @param map key value pairs to be added as items to the list
     * @throws IllegalArgumentException if option is empty, contains white space, or doesn't start with '-' or '/'
     */
    public OptionList(Map<String, String> map){
        super(Utils.INDENT);
        addItems(map);
    }
    
    /**
     * Adds a option/definition pair to the list.
     * @param option a command line option being defined
     * @param definition the definition of the option. The definition is processed for inline markup
     * @param inlines optional arguments specifying inline markup in the definition
     * @return this list with the option/definition added
     * @throws IllegalArgumentException if option is empty, contains white space, or doesn't start with '-' or '/'
     */
    public OptionList addItem(String option, String definition, Inline... inlines){
        validateSyntax(option);
        super.addItem(option, definition, inlines);
        return this;
    }

    /**
     * Adds a option/definition pair to the list.
     * @param option the option being defined
     * @param definition the definition of the option.
     * @return this list with the option/definition added
     * @throws IllegalArgumentException if option is empty, contains white space, or doesn't start with '-' or '/'
     */
    public OptionList addItem(String option, RstBodyElement definition){
        validateSyntax(option);
        super.addItem(option, definition);
        return this;
    }

    /**
     * adds the items to the option list. The order of items will be determined by the map's foreach implementation. The
     * map's keys will be the left elements, values will be right
     * @param map items to add to the list
     * @return this option list with the items added
     */
    public OptionList addItems(Map<String, String> map){
        for(Map.Entry<String, String> entry : map.entrySet()){
            addItem(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * returns an ItemBuilder to allow the construction of a custom option/definition pair. The item isn't added to the
     * list until {@code addToList} is called.
     * @param option the option to be defined
     * @return an ItemBuilder for the parameter option
     */
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

    /**
     * The ItemBuilder class is designed to allow further customization of the left side item in an option list. It
     * supports addition of aliases and arguments of defined options. The option defined in this manner will look as
     * follows:
     * [option][, alias, alias...][&lt;arg1, arg2...&gt;]    [definition]
     *
     * An instance of ItemBuilder can be obtained by calling {@code buildItem} from an instance of OptionList
     */
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

        /**
         * Adds an argument to the list of arguments for this option.
         * @param argument argument to be included with this option
         * @return this ItemBuilder with the argument added
         * @throws IllegalArgumentException if the argument contains whitespace or is empty
         */
        public ItemBuilder addArgument(String argument){
            if(argument.contains(" ") || argument.contains("\t") || argument.contains("\n"))
            {
                throw new IllegalArgumentException("Argument can't contain white space");
            }
            if(argument.equals(""))
            {
                throw new IllegalArgumentException("Empty arguments not allowed");
            }
            args += argument + ", ";
            return this;
        }

        /**
         * Adds an alias to the list of aliases for this option.
         * @param alias alias for this option
         * @return this ItemBuilder with the alias added
         * @throws IllegalArgumentException if the alias is empty, contains whitespace, or doesn't start with '-' or '/'
         */
        public ItemBuilder addAlias(String alias){
            validateSyntax(alias);
            aliases += ", " + alias;
            return this;
        }

        /**
         * Adds a definition to the right side of this item in the option list. The added definition is concatenated to
         * any already existing definition
         * @param definition text to be added to the definition of this option (the text is processed for inline markup)
         * @param inlines optional arguments specifying inline markup in the definition
         * @return this ItemBuilder with the definition added
         */
        public ItemBuilder addDefinition(String definition, Inline... inlines){
            this.definition += new Paragraph(definition, inlines).getText();
            return this;
        }

        /**
         * Adds a definition to the right side of this item in the option list. The added definition is concatenated to
         * any already existing definition
         * @param element the content to be added to the definition of this item
         * @return this ItemBuilder with the definition added
         */
        public ItemBuilder addDefinition(RstBodyElement element){
            if(definition.length()>0)
                definition += "\n\n";
            definition += element.write();
            return this;
        }

        /**
         * Similar to a {@code build} method, addToList creates a fully formed item and adds it to the OptionList
         * that provided this ItemBuilder
         * @return the OptionList that created this ItemBuilder with the built item added to the list
         */
        public OptionList addToList(){
            if(args.equals("<"))
            {
                args = "";
            }
            else
            {
                args = " " + args.substring(0, args.length() - 2) + ">";
            }
            String optionLeft = name + aliases + args;
            optList.addCustom(optionLeft, definition);
            return optList;
        }

    }
}

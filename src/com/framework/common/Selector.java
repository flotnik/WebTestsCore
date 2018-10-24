package com.framework.common;

/***
 *
 * @author nshevtsov
 *
 */
public class Selector {

    public final String xpath;
    public final String name;

    public Selector(String xpath, String name) {
        this.xpath = xpathFormatter(xpath, name);
        this.name = name;
    }

    /**
     * Return <code>xpath</code> trailed with the "comment"<p>
     * <code>
     * For example result might look like this:
     * "/tag/tag[2]/tag/tag['comment']"
     * </code>
     * <p>
     * <i><b>Note.</b> This is not an official way, but <u>it works</u></i>
     * <p>
     *
     * @param xpath   source xpath
     * @param comment comment
     * @return formatted xpath or <code>""</code> if source xpath empty or null
     */
    private String xpathFormatter(String xpath, String comment) {
        //TODO  check global  property and add "comment" by condition
        // but while have this stupid  logic:
        boolean needComment = true;
        /*******
         *   !!! Restriction 1 !!!
         *  Chrome is not  parse xpath-construction like "/..['comment']" or "/..['comment']"
         *   !!! Restriction 2 !!!
         *  For any Selector name need to replace all unallowed symbols (Java reserved or xPath reserved) with '_'
         *******/
        /*******
         * regex note:
         * 1st Alternative %s
         * %s matches the characters %s literally (case sensitive)
         * 2nd Alternative =
         * = matches the character = literally (case sensitive)
         * 3rd Alternative &
         * & matches the character & literally (case sensitive)
         * 4th Alternative '
         * ' matches the character ' literally (case sensitive)
         * 5th Alternative "
         * " matches the character " literally (case sensitive)
         *******/
        comment = needComment ? "['#" + comment.replaceAll("%s|=|&|'|\"", "_") + "']" : "";
        if (null == xpath || xpath.isEmpty()) {
            return "" + comment;
        } else {
            if (xpath.substring(xpath.length() - 1).equalsIgnoreCase(".")) return xpath;
            return xpath + comment;
        }
    }

    public Selector child(String xpath, String name) {
        return new Selector(this.xpath + xpath, name);
    }

    /***
     * Если по xpath возвращается список элементов, то для того,чтобы получить
     * селектор для каждого в списке нужно использовать эту функцию
     *
     * @param position
     * @return (xpath)[position]
     */
    public Selector element_in_position(int position) {
        return new Selector("(" + this.xpath + ")[" + position + "]", name + "position = " + position);
    }

    public String toString() {
        return xpath;
    }

    /**
     * Возвращает <code>xpath</code> обработанный правилами java.util.Formatter
     * <p>
     * Предполагается что <code>xpath</code> может  содержать постановочный символ <i>%s</i>, это может  быть полезно
     * при работе с однотипными объектами.
     * Например, все кнопки в <code>AbstractButtonsBlock</code> имеют  <code>xpath</code> отличающийся
     * только названием кнопки(файлом-иконкой). Для доступа к любой из них достаточно применить данный метод.
     *
     * @param customString Строка которая подставится вмеcто <i>%s</i> в текущем <code>xpath</code>
     */
    public Selector custom(String customString) {
        return new Selector(String.format(this.xpath, customString), this.name);
    }

    //TODO Urgent! Must be documented!
    public Selector customXpath(String... customString) {
        Object[] customObject = (Object[]) customString;
        return new Selector(String.format(this.xpath, customObject), this.name);
    }

    //TODO Urgent! Must be documented!
    public Selector customName(String... customString) {
        Object[] customObject = (Object[]) customString;
        return new Selector(this.xpath, String.format(this.name, customObject));
    }
}

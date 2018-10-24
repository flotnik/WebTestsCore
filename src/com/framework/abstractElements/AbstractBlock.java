package com.framework.abstractElements;

import com.framework.common.Selector;
import com.framework.common.WebActionBot;

/***
 *
 * @author nshevtsov
 *
 */
public abstract class AbstractBlock {

    protected WebActionBot bot;

    protected String blockName;
    protected Selector root;

    protected AbstractBlock(WebActionBot params, Selector root, String blockName) {
        this.bot = params;
        this.blockName = blockName;
        this.root = root;
    }

    public boolean isBlockPresent() {
        return bot.isElementPresent(root);
    }

    public abstract void waitLoading() throws Exception;

    public Selector getRoot() {
        return root;
    }
}

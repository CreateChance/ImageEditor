package com.createchance.imageeditor.ops;

/**
 * ${DESC}
 *
 * @author gaochao1-iri
 * @date 2018/10/28
 */
public class FilterOperator extends AbstractOperator {

    private static final String TAG = "FilterOperator";

    public FilterOperator() {
        super(FilterOperator.class.getSimpleName(), OP_FILTER);
    }

    @Override
    public boolean checkRational() {
        return false;
    }

    @Override
    public void exec() {

    }
}

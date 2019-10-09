package org.docksidestage.javatry.basic.st6.dbms;

public abstract class St6Sql {
    final public String buildPagingQuery(int pageSize, int pageNumber) {
        int offset = pageSize * (pageNumber - 1);
        return getOffsetPrefix() + offset + getPageSizePrefix() + pageSize;
    }

    protected abstract String getOffsetPrefix();

    protected abstract String getPageSizePrefix();
}

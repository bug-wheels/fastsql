/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 zyndev zyndev@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.zyndev;


import java.io.Serializable;
import java.util.List;

/**
 * The type Page list content.
 *
 * @param <T> the type parameter
 *
 * @author yunan.zhang zyndev@gmail.com
 */
public class PageListContent<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<T> content;
    private int pageNum;
    private int pageSize;
    private int totalNum;
    private int totalPages;

    /**
     * Instantiates a new Page list content.
     */
    public PageListContent() {
    }

    /**
     * Sets data.
     *
     * @param pageNum  the page num
     * @param pageSize the page size
     * @param totalNum the total num
     * @param content  the content
     * @return the data
     */
    public PageListContent<T> setData(int pageNum, int pageSize, int totalNum, List<T> content) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.totalPages = (totalNum + pageSize - 1) / pageSize;
        this.content = content;
        return this;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public List<T> getContent() {
        return this.content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Gets page num.
     *
     * @return the page num
     */
    public int getPageNum() {
        return this.pageNum;
    }

    /**
     * Sets page num.
     *
     * @param pageNum the page num
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * Gets page size.
     *
     * @return the page size
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * Sets page size.
     *
     * @param pageSize the page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets total num.
     *
     * @return the total num
     */
    public int getTotalNum() {
        return this.totalNum;
    }

    /**
     * Sets total num.
     *
     * @param totalNum the total num
     */
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * Gets total pages.
     *
     * @return the total pages
     */
    public int getTotalPages() {
        return this.totalPages;
    }

    /**
     * Sets total pages.
     *
     * @param totalPages the total pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

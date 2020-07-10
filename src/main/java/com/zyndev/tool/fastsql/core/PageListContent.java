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

package com.zyndev.tool.fastsql.core;


import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Page list content.
 *
 * @param <T> the type parameter
 * @author yunan.zhang zyndev@gmail.com
 * @version 0.0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageListContent<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<T> content;
  private int pageNum;
  private int pageSize;
  private int totalNum;
  private int totalPages;


  /**
   * Sets data.
   *
   * @param pageNum  the page num
   * @param pageSize the page size
   * @param totalNum the total num
   * @param content  the content
   * @return the data
   */
  public static <T> PageListContent<T> setData(int pageNum, int pageSize, int totalNum, List<T> content) {
    PageListContent<T> result = new PageListContent<>();
    result.pageNum = pageNum;
    result.pageSize = pageSize;
    result.totalNum = totalNum;
    result.totalPages = (totalNum + pageSize - 1) / pageSize;
    result.content = content;
    return result;
  }

  public int getTotalPages() {
    return (totalNum + pageSize - 1) / pageSize;
  }

  public int getOffset() {
    return (pageNum - 1) * pageSize;
  }

}

package com.av.common.utils;

import com.av.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.av.common.core.page.PageDomain;
import com.av.common.core.page.TableSupport;
import com.av.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author long
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}

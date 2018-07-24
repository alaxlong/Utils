package util;

import java.util.HashMap;
import java.util.Map;

/**
 * 计算分页参数
 * Created by saifenghuang on 2018/6/6.
 */
public class PageUtil {

    //每页条数
    public final static int PAGE_SIZE = 20;

    /**
     * 根据传入的页数计算起始和结束值
     * @param page
     * @return
     */
    public static Map<String,String> getPageStartAndEnd(int page){
        Map<String,String> map = new HashMap<>();
        int start = (page-1) * PAGE_SIZE + 1 ;
        int end = page * PAGE_SIZE;
        map.put("start",start+"");
        map.put("end",end+"");
        return map;
    }

}

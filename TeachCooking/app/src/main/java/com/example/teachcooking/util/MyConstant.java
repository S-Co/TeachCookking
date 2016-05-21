package com.example.teachcooking.util;

public interface MyConstant {

	/**聚合数据api key**/
	String API_KEY = "c9314c2e73e68740ae7699dca1dc96fd";

	/**请求的格式**/
	String URL_GET_ALL_TYPE = "http://apis.juhe.cn/cook/category?key="+API_KEY;
	/**每页的数量**/
	int PAGE_ITEM_COUNT = 30;
	
	public static final String URL_SEARCH = "http://apis.juhe.cn/cook/query?key="+API_KEY+"&menu=search_key&pn=page&rn="+PAGE_ITEM_COUNT;
	public static final String URL_NORMAL = "http://apis.juhe.cn/cook/index?key="+API_KEY+"&cid=cid_num&pn=page&rn="+PAGE_ITEM_COUNT;
	
	int TAG_SEARCH = 1;
	int TAG_NOT_SEARCH = 2;
	
	String KEY = "key";
	int TAG_ALL_COLLECT = 1;
	int TAG_ALL_NEARBY = 2;
}

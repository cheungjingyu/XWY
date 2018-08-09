package com.rupeng.mapper;

import java.util.List;

public interface IMapper<T> {
	int insert(T pojo);//插入一个对象
	int update(T pojo);//根据id更新一个对象
	int delete(Long id);//根据id删除一个对象
	List<T> select(T pojo);//根据对象里的条件进行查询

}

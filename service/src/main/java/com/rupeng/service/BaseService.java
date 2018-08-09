package com.rupeng.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.IMapper;

public class BaseService<T> {
	@Autowired
	private IMapper<T> mapper;
    //可以自动根据泛型T的具体值注入对应的mapper！！！比如如果T是Subject，那么注入的mapper就是SubjectMapper的对象
	@SuppressWarnings("all")
	protected T createInstanceAndSetIdOfFirstGeneric(Long id) {
        //为了避免错误的将id设为null时把整张表都删除，在此检查
        if (id == null) {
            throw new RuntimeException("id不能为null");
        }
        try {
            //通过反射创建泛型类对象并调用其setId方法设置id字段值
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) type.getActualTypeArguments()[0];
            T t = (T) clazz.newInstance();
            clazz.getDeclaredMethod("setId", Long.class).invoke(t, id);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
	public int insert(T pojo){
		return mapper.insert(pojo);
	}
	public int update(T pojo){
		return mapper.update(pojo);
	}
	public int delete(Long id){
		if(id==null){
			throw new RuntimeException("id不能为空");
		}
		return mapper.delete(id);
	}
	public List<T> selectList(){
		return mapper.select(null);
	}
	// 条件查询
	public List<T> selectList(T pojo){
		return mapper.select(pojo);
	}
	// 条件排序查询
	public List<T> selectList(T pojo,String orderBy){
		PageHelper.orderBy(orderBy);
		return mapper.select(pojo);
	}
	// 条件查询
	public T selectOne(T pojo){
		List<T> list=mapper.select(pojo);
		if(list==null || list.size()==0){
			return null;
		}
		if(list.size()>1){
			throw new RuntimeException("查询的数据多余一条");
		}
		return list.get(0);
	}
	// 根据id进行查询
	public T selectOne(Long id){
		T pojo = createInstanceAndSetIdOfFirstGeneric(id);
		return selectOne(pojo);
	}
	// 条件分页查询
	public PageInfo<T> page(int pageNum,int pageSize,T pojo){
		PageHelper.startPage(pageNum, pageSize);
		List<T> list = mapper.select(pojo);
		return new PageInfo<T>(list);
	}
	// 条件分页排序查询
	public PageInfo<T> page(int pageNum,int pageSize,T pojo,String orderBy){
		PageHelper.startPage(pageNum, pageSize);
		PageHelper.orderBy(orderBy);
		List<T> list = mapper.select(pojo);
		return new PageInfo<T>(list);
	}
	// 判断是否存在
	public boolean 	isExisted(T pojo){
		List<T> list=selectList(pojo);
		if(list==null || list.size()==0){
			return false;
		}
		return true;
	}
}

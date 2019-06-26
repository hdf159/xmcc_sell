package com.xmcc.dao.impl;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.xmcc.dao.BatchDao;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {
    @PersistenceContext
    private EntityManager em;


    @Override
    public void batchInsert(List<T> list) {
   int size =list.size();
   //循环存入缓存区
   for (int i =0;i<size;i++){
       em.persist(list.get(i));
       //每100条写入数据库，如果不足100条直接写入数据库
       if (i%100==0 || i==size-1){
em.flush();
em.clear();
       }
   }
    }
}

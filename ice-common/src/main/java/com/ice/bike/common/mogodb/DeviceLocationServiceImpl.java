package com.ice.bike.common.mogodb.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.ice.bike.common.mogodb.DeviceLocationService;
import com.ice.bike.common.mogodb.DeviceLocationVo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.xcd.bike.api.mogodb.BeanUtil;

import net.sf.json.JSONObject;
public class DeviceLocationServiceImpl implements DeviceLocationService {
	
    private MongoTemplate mongoTemplate;
    private final static String COLLECTION_NAME = "device_location"; 

	@Override
	public List<DeviceLocationVo> getItemInfo(JSONObject json) throws Exception {
		  List<DeviceLocationVo> list = new ArrayList<DeviceLocationVo>();
	        // 判断查询的json中传递过来的参数
	        DBObject query = new BasicDBObject();

	        if(json.has("deviceId")){            
	            query.put("deviceId", json.getString("deviceId"));
	        }else if(json.has("id")){
	            query.put("id", json.getString("id"));
	        }
	        
	        DBCursor results = mongoTemplate.getCollection(COLLECTION_NAME).find(query);
	        if(null != results){
	            Iterator<DBObject> iterator = results.iterator();
	            while(iterator.hasNext()){
	                BasicDBObject obj = (BasicDBObject) iterator.next();
	                DeviceLocationVo itemInfo = new DeviceLocationVo();
	                itemInfo = BeanUtil.dbObject2Bean(obj, itemInfo);
	                list.add(itemInfo);
	            }
	        }
	        return list;
	}

	@Override
	public int save(DeviceLocationVo itemInfo) throws Exception {
		 DBCollection collection = this.mongoTemplate.getCollection(COLLECTION_NAME);
	     int result = 0;
	     DBObject iteminfoObj = BeanUtil.bean2DBObject(itemInfo);
	        //iteminfoObj.removeField("serialVersionUID");
	        //result = collection.insert(iteminfoObj).getN();
	     WriteResult writeResult = collection.save(iteminfoObj);
	     result = writeResult.getN();
	     return result;
	}

	@Override
	public void update(DeviceLocationVo intemInfo) throws Exception {
		  DBCollection collection = this.mongoTemplate.getCollection(COLLECTION_NAME);
		  DeviceLocationVo queryItemInfo = new DeviceLocationVo();
	      queryItemInfo.setDeviceId(intemInfo.getDeviceId());
	      DBObject itemInfoObj = BeanUtil.bean2DBObject(intemInfo);
	      DBObject query =  BeanUtil.bean2DBObject(queryItemInfo);
	      collection.update(query, itemInfoObj);  
	}

	@Override
	public DeviceLocationVo getDeviceInfo(JSONObject json) throws Exception {
		List<DeviceLocationVo> list = new ArrayList<DeviceLocationVo>();
        // 判断查询的json中传递过来的参数
        DBObject query = new BasicDBObject();

        if(json.has("deviceId")){            
            query.put("deviceId", json.getString("deviceId"));
        }else if(json.has("id")){
            query.put("id", json.getString("id"));
        }
        
        DBCursor results = mongoTemplate.getCollection(COLLECTION_NAME).find(query);
        if(null != results){
            Iterator<DBObject> iterator = results.iterator();
            while(iterator.hasNext()){
                BasicDBObject obj = (BasicDBObject) iterator.next();
                DeviceLocationVo itemInfo = new DeviceLocationVo();
                itemInfo = BeanUtil.dbObject2Bean(obj, itemInfo);
                list.add(itemInfo);
            }
        }
        return list.get(0);
	}

}

package com.ice.bike.common.mogodb;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ice.bike.common.mogodb.dao.DeviceLocationVo;
import com.ice.bike.common.mogodb.dao.MongoGeoDao;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;

public class MongoDaoImpl implements MongoGeoDao {

	private static Logger logger = LoggerFactory.getLogger(MongoDaoImpl.class);

	private MongoTemplate mongoTemplate;

	public DBObject findOne(String collection, DBObject query, DBObject fields) {
		return mongoTemplate.getCollection(collection).findOne(query, fields);
	}

	public List<DBObject> find(String collection, DBObject query, DBObject fields, DBObject orderBy, int pageNum,
			int pageSize) {
		List<DBObject> list = new ArrayList<>();
		Cursor cursor = mongoTemplate.getCollection(collection).find(query, fields).skip((pageNum - 1) * pageSize)
				.limit(pageSize).sort(orderBy);
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list.size() > 0 ? list : null;
	}

	@Override
	public List<DBObject> find(String collection, DBObject query, DBObject fields, DBObject orderBy, int limit) {
		List<DBObject> list = new ArrayList<>();
		Cursor cursor = mongoTemplate.getCollection(collection).find(query, fields).sort(orderBy).limit(limit);
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list.size() > 0 ? list : null;
	}

	@Override
	public void delete(String collection, DBObject dbObject) {
		mongoTemplate.getCollection(collection).remove(dbObject);
	}

	@Override
	public void save(String collection, DBObject dbObject) {
		mongoTemplate.getCollection(collection).save(dbObject);
	}

	@Override
	public void update(String collection, DBObject query, DBObject update, boolean upsert, boolean multi) {
		mongoTemplate.getCollection(collection).update(query, update, upsert, multi);
	}

	@Override
	public Long count(String collection, DBObject query) {
		return mongoTemplate.getCollection(collection).count(query);
	}

	@Override
	public List<?> distinct(String collection, String key, DBObject query) {
		return mongoTemplate.getCollection(collection).distinct(key, query);
	}

	@Override
	public List<DBObject> geoNear(String collection, DBObject query, DeviceLocationVo point, int limit,
			long maxDistance) {
		if (query == null)
			query = new BasicDBObject();

		List<DBObject> pipeLine = new ArrayList<>();
		BasicDBObject aggregate = new BasicDBObject("$geoNear",
				new BasicDBObject("near",
						new BasicDBObject("type", "Point").append("coordinates",
								new double[] { 118.783799, 31.979234 })).append("distanceField", "dist.calculated")
										.append("query", new BasicDBObject()).append("num", 5)
										.append("maxDistance", 5000).append("spherical", true));
		pipeLine.add(aggregate);
		Cursor cursor = mongoTemplate.getCollection(collection).aggregate(pipeLine,
				AggregationOptions.builder().build());
		List<DBObject> list = new LinkedList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	@Override
	public List<DBObject> withinCircle(String collection, String locationField, DeviceLocationVo center, long radius,
			DBObject fields, DBObject query, int limit) {
		LinkedList<Object> circle = new LinkedList<>();
		// Set the center coordinate
		circle.addLast(new double[] { Double.valueOf(center.getLongitude()), Double.valueOf(center.getLatitude()) });
		// Set the radius. unit:meter
		circle.addLast(radius / 6378137.0);

		if (query == null)
			query = new BasicDBObject();
		query.put(locationField, new BasicDBObject("$geoWithin", new BasicDBObject("$centerSphere", circle)));
		logger.info("withinCircle:{}", query.toString());
		return mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
	}

	@Override
	public List<DBObject> nearSphere(String collection, String locationField, DeviceLocationVo center, long minDistance,
			long maxDistance, DBObject query, DBObject fields, int limit) {
		if (query == null)
			query = new BasicDBObject();

		query.put(locationField, new BasicDBObject("$nearSphere",
				new BasicDBObject("$geometry", new BasicDBObject("type", "Point").append("coordinates",
						new double[] { Double.valueOf(center.getLongitude()), Double.valueOf(center.getLatitude()) }))
								.append("$minDistance", minDistance).append("$maxDistance", maxDistance)));
		logger.info("nearSphere:{}", query.toString());
		return mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
	}

	@Override
	public List<DBObject> withinPolygon(String collection, String locationField, List<double[]> polygon,
			DBObject fields, DBObject query, int limit) {
		if (query == null)
			query = new BasicDBObject();

		List<List<double[]>> polygons = new LinkedList<>();
		polygons.add(polygon);
		query.put(locationField, new BasicDBObject("$geoWithin",
				new BasicDBObject("$geometry", new BasicDBObject("type", "Polygon").append("coordinates", polygons))));
		logger.info("withinPolygon:{}", query.toString());
		return mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
	}

	@Override
	public List<DBObject> withinMultiPolygon(String collection, String locationField, List<List<double[]>> polygons,
			DBObject fields, DBObject query, int limit) {
		if (query == null)
			query = new BasicDBObject();

		List<List<List<double[]>>> list = new LinkedList<>();
		for (List<double[]> polygon : polygons) {
			List<List<double[]>> temp = new LinkedList<>();
			temp.add(polygon);
			list.add(temp);
		}
		query.put(locationField, new BasicDBObject("$geoWithin",
				new BasicDBObject("$geometry", new BasicDBObject("type", "MultiPolygon").append("coordinates", list))));
		logger.info("withinMultiPolygon:{}", query.toString());
		return mongoTemplate.getCollection(collection).find(query, fields).limit(limit).toArray();
	}

	// @Override
	// public List<DBObject> withinBox(String collection, String locationField,
	// DeviceLocationVo bottomLeft, DeviceLocationVo upperRight, DBObject fields,
	// DBObject query, int limit) {
	// if(query==null)
	// query = new BasicDBObject();
	//
	// LinkedList<double[]> box = new LinkedList<>();
	// box.add(new double[]{bottomLeft.getLng(), bottomLeft.getLat()});
	// box.add(new double[]{upperRight.getLng(), upperRight.getLat()});
	//
	// query.put(locationField, new BasicDBObject("$geoWithin", new
	// BasicDBObject("$box", box)));
	// logger.info("withinBox:{}",query.toString());
	// return mongoTemplate.getCollection(collection).find(query,
	// fields).limit(limit).toArray();
	// }

}

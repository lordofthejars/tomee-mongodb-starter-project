package org.superbiz.persistence;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@ApplicationScoped
public class MongoDBProducer {

	@Resource(name = "mongoUri")
	private MongoClientURI mongoClientURI;
	
	private DB db;

	@PostConstruct
	public void init() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		db =  mongoClient.getDB(mongoClientURI.getDatabase());
	}

	@Produces
	public DB createDB() {
		return db;
	}

}

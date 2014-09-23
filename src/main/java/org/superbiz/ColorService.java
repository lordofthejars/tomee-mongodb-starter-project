package org.superbiz;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jongo.MongoCollection;
import org.superbiz.persistence.Find;
import org.superbiz.persistence.FindById;
import org.superbiz.persistence.FindOne;
import org.superbiz.persistence.Insert;
import org.superbiz.persistence.JongoCollection;
import org.superbiz.persistence.PersistenceHandler;
import org.superbiz.persistence.Remove;

@Singleton
@Lock(LockType.READ)
public abstract class ColorService implements InvocationHandler {

	@JongoCollection("color")
	@Inject
	MongoCollection colorMongoCollection;
	
	@Insert
	public abstract Color createColor(Color c);
	
	@Remove
	public abstract int removeAllColors();
	
	@FindById
	public abstract Color findColorById(String id);
	
	@FindOne("{name:#}")
	public abstract Color findColorByColorName(String colorName);
	
	@Find("{r:#}")
	public abstract Iterable<Color> findColorByRed(int r);
	
	public long countColors() {
		return colorMongoCollection.count();
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return PersistenceHandler.invoke(colorMongoCollection, method, args);
	}
	
}

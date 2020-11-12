package javaConcurrentDemo;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class MyCache//资源类
{
	private volatile Map<String,Object> map = new HashMap<String,Object>();
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	//写操作
	public  void put(String key,Object value) {
		rwLock.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()+"\t 正在写入"+key);
			//暂停线程，模拟网络拥堵
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			}catch(InterruptedException e ) {
				e.printStackTrace();
			}
			map.put(key, value);
			System.out.println(Thread.currentThread().getName()+"\t 写入完成");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			rwLock.writeLock().unlock();
		}
		
	}
	
	
	//读操作
	public void get(String key) {
		rwLock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()+"\t 正在读取");
		    //暂停线程
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object result = map.get(key);
			System.out.println(Thread.currentThread().getName()+"\t 读取完成"+result);
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			rwLock.readLock().unlock();
		}
		
	
	}
	
	//模拟清除缓存
	  public void clear() {
		  map.clear();
	  }
	
}

/**
 * 读写锁
 * 多个线程同时读取一个资源类没有任何问题，所以为了满足并发量，读取共享资源应该可以同时进行；
 * 但是，如果有一个线程想去写共享资源，就不应该再有其他线程可以对该资源进行读或者写！
 * 小总结：
 * 读-读能共存
 * 读-写不能共存
 * 写-写不能共存
 * 
 * 写操作：原子和独占。整个过程必须都是一个完整的统一，中间不许分割，被打断
 * @author Administrator
 *
 */

public class ReadAndWriteLock {
  public static void main(String[] args) {
	  MyCache myCache = new MyCache();
	   //5个线程读
	   for(int i=0;i<=5;i++) {
		   final int tempInt = i;
		   new Thread(()-> {
			   myCache.put(tempInt+"", tempInt+"");
		   },String.valueOf(i)).start();
	  }
	   
	   //5个线程取
	   for(int i=0;i<=5;i++) {
		   final int tempInt = i;
		   new Thread(()-> {
			   myCache.get(tempInt+"");
		   },String.valueOf(i)).start();
	   }
	   
	   
  }
  
  
 
}

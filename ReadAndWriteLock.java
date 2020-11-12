package javaConcurrentDemo;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class MyCache//��Դ��
{
	private volatile Map<String,Object> map = new HashMap<String,Object>();
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	//д����
	public  void put(String key,Object value) {
		rwLock.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()+"\t ����д��"+key);
			//��ͣ�̣߳�ģ������ӵ��
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			}catch(InterruptedException e ) {
				e.printStackTrace();
			}
			map.put(key, value);
			System.out.println(Thread.currentThread().getName()+"\t д�����");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			rwLock.writeLock().unlock();
		}
		
	}
	
	
	//������
	public void get(String key) {
		rwLock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName()+"\t ���ڶ�ȡ");
		    //��ͣ�߳�
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object result = map.get(key);
			System.out.println(Thread.currentThread().getName()+"\t ��ȡ���"+result);
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			rwLock.readLock().unlock();
		}
		
	
	}
	
	//ģ���������
	  public void clear() {
		  map.clear();
	  }
	
}

/**
 * ��д��
 * ����߳�ͬʱ��ȡһ����Դ��û���κ����⣬����Ϊ�����㲢��������ȡ������ԴӦ�ÿ���ͬʱ���У�
 * ���ǣ������һ���߳���ȥд������Դ���Ͳ�Ӧ�����������߳̿��ԶԸ���Դ���ж�����д��
 * С�ܽ᣺
 * ��-���ܹ���
 * ��-д���ܹ���
 * д-д���ܹ���
 * 
 * д������ԭ�ӺͶ�ռ���������̱��붼��һ��������ͳһ���м䲻��ָ�����
 * @author Administrator
 *
 */

public class ReadAndWriteLock {
  public static void main(String[] args) {
	  MyCache myCache = new MyCache();
	   //5���̶߳�
	   for(int i=0;i<=5;i++) {
		   final int tempInt = i;
		   new Thread(()-> {
			   myCache.put(tempInt+"", tempInt+"");
		   },String.valueOf(i)).start();
	  }
	   
	   //5���߳�ȡ
	   for(int i=0;i<=5;i++) {
		   final int tempInt = i;
		   new Thread(()-> {
			   myCache.get(tempInt+"");
		   },String.valueOf(i)).start();
	   }
	   
	   
  }
  
  
 
}

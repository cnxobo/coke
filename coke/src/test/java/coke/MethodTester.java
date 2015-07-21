package coke;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.xobo.coke.utility.MethodUtils;

public class MethodTester {

	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, InstantiationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "Bing");
		parameters.put("address", "Shanghai");
		parameters.put("age", 28);

		Map<String, Object> subUserInfo = new HashMap<String, Object>();
		subUserInfo.put("name", "xiaobu");
		subUserInfo.put("address", "Zhengzhou");
		subUserInfo.put("age", 25);

		parameters.put("userInfo", subUserInfo);

		MethodUtils.invokeMethod(new MethodTester(), "test", parameters);

	}

	public void test(String name, String address, int age, UserInfo userInfo) {
		System.out.println("name: " + name + "\taddress: " + address + "\t age: " + age);
		System.out.println(userInfo);
	}

}

package org.javalog.bean;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BeanFactory {

	private Map beansMap = new TreeMap<>();

	private NodeList beanList;

	public BeanFactory(String filePath) {
		try {
			File file = new File(filePath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			doc.getDocumentElement().normalize();
			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
			beanList = doc.getElementsByTagName("bean");
			// System.out.println(beanList);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("hata");
		}
	}

	public Object getBean(String beanName) {

		if (this.beansMap.containsKey(beanName))
			return beansMap.get(beanName);

		for (int i = 0; i < beanList.getLength(); i++) {

			Element bean = (Element) beanList.item(i);
			String id = bean.getAttribute("id");

			if (id.equals(beanName)) { // aradigimiz bean' i xml de bulduk
										// demektir.
				try {
					// found the bean and create it

					String className = bean.getAttribute("class");
					Object beanInstance = Class.forName(className).newInstance();

					NodeList propertyListOfBean = bean.getElementsByTagName("property");

					if (propertyListOfBean.getLength() > 0) {

						Class beanClass = beanInstance.getClass();

						Map<String, Method> methodMap = new HashMap<>();
						Method[] methods = beanClass.getDeclaredMethods();
						// eger model sinifimizdaki metod ismi 'set' ile
						// basliyor ise bu metodu bir map' e atiyoruz.
						// Daha sonra xml'den alidigimiz degerleri bu map' i
						// kullanarak degerleri set etmek icin kullanacagiz

						for (int j = 0; j < methods.length; j++) {

							String methodName = methods[j].getName();
							if (methodName.startsWith("set")) {
								methodName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
								methodMap.put(methodName, methods[j]);

							}
						}

						// bean altindaki property'ler tek tek geziliyor
						for (int j = 0; j < propertyListOfBean.getLength(); j++) {

							// Uzerinde islem yapabilmek icin her property' i
							// element olarak ele aliyoruz
							Element propertyElement = (Element) propertyListOfBean.item(j);

							String propertyName = propertyElement.getAttribute("name");
							String propertyValue = propertyElement.getAttribute("value");
							String ref = null;
							if (propertyValue == null || propertyValue.equals("")) { // eger
																						// property'
																						// nin
																						// value
																						// parametresi
																						// yok
																						// ise
																						// referans
																						// vardir
								ref = propertyElement.getAttribute("ref"); // referans
																			// alinan
																			// bean'
																			// in
																			// id
																			// si
																			// alindi
							}

							// set metodlarini isme gore cagirarak deger atamasi
							// yapiliyor
							if (methodMap.containsKey(propertyName)) {
								Method method = (Method) methodMap.get(propertyName);
								// set metodlarinin aldiklari parametrelere
								// bakilarak, property' lerden alinan degerler
								// ataniyor
								Class[] parameterTypes = method.getParameterTypes();
								String parameterName = parameterTypes[0].getCanonicalName();
								Object param = null;

								if (parameterName.equals("java.lang.String")) {
									param = propertyValue;
								} else if (parameterName.equals("int")) {
									param = new Integer(propertyValue);
								} else {
									if (ref != null) {
										param = getBean(ref);
									}
								}

								method.invoke(beanInstance, param);

							}

						}
					}

					this.beansMap.put(id, beanInstance);

					return beanInstance;

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}

				
			}

		}

		return null;
	}

}

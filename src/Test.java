import org.javalog.bean.BeanFactory;
import org.javalog.model.Company;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		BeanFactory beanFactory = new BeanFactory("src/config/beans.xml");
		
		
		
		Company company = (Company)beanFactory.getBean("Javalog Inc");
		System.out.println(company);
	}

}

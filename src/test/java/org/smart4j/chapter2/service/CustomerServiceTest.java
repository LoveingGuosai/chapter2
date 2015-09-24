package org.smart4j.chapter2.service;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.smart4j.chapter2.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiyang on 15-9-21.
 */
public class CustomerServiceTest extends TestCase {
    private final CustomerService customerService;

    public CustomerServiceTest(){
        customerService=new CustomerService();
    }

    @Before
    public void init(){
        //TODO 初始化数据库
    }
    public void testGetCustomerList() throws Exception {
        List<Customer> customers = customerService.getCustomerList();
        Assert.assertEquals(2, customers.size());
    }

    public void testGetCustomer() throws Exception {
        Customer customer = customerService.getCustomer(1);
        Assert.assertNotNull(customer);
    }

    public void testCreateCustomer() throws Exception {
        Map<String,Object> fieldMap = new HashMap<>();
        fieldMap.put("name","cunstomer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","13512345678");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    public void testUpdateCustomer() throws Exception {
        long id=1;
        Map<String,Object> fieldMap  =  new HashMap<>();
        fieldMap.put("contact","Eric");
        boolean result =customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }

    public void testDeleteCustomer() throws Exception {
        long id=1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }

    public void testGetCustomerList1() throws Exception {
        String keyword="J";
        List<Customer> customers = customerService.getCustomerList(keyword);
        Assert.assertEquals(2,customers.size());
    }
}
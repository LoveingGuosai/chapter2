package org.smart4j.chapter2.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiyang on 15-9-21.
 */
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest(){
        customerService=new CustomerService();
    }

    @Before
    public void init() throws Exception{
        System.out.println(11);
        DatabaseHelper.executeSqlFIle("sql/customer_init.sql");
    }
    @Test
    public void testGetCustomerList() throws Exception {
        List<Customer> customers = customerService.getCustomerList();
        Assert.assertEquals(2, customers.size());
    }
    @Test
    public void testGetCustomer() throws Exception {
        Customer customer = customerService.getCustomer(1);
        Assert.assertNotNull(customer);
    }
    @Test
    public void testCreateCustomer() throws Exception {
        Map<String,Object> fieldMap = new HashMap<>();
        fieldMap.put("name","cunstomer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","13512345678");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }
    @Test
    public void testUpdateCustomer() throws Exception {
        long id=1;
        Map<String,Object> fieldMap  =  new HashMap<>();
        fieldMap.put("contact","Eric");
        boolean result =customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }
    @Test
    public void testDeleteCustomer() throws Exception {
        long id=1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }
    @Test
    public void testGetCustomerList1() throws Exception {
        String keyword="customer";
        List<Customer> customers = customerService.getCustomerList(keyword);
        Assert.assertEquals(2,customers.size());
    }
}
package com.sandeepkaul.config;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestConfig extends TestCase{

  public void test1() {

    ConfigReader
        .initialize("/Users/sandeep.kaul/workspace/tests/config/src/main/resources/conf.txt");

    String value = ConfigReader.getValue("Section-1::Sub-Section-2::Var2");
    ConfigReader.print();
    System.out.println("Cal:"+value);
    
    Assert.assertEquals( "Value2.2",value);
    Assert.assertEquals( "Value1.0",ConfigReader.getValue("Section-2::Sub-Section-5::Var1"));
    Assert.assertEquals( "Value2",ConfigReader.getValue("Var2"));
    Assert.assertEquals( "Value2.1",ConfigReader.getValue("Section-2::Sub-section-1::Var2"));
    Assert.assertEquals( "Value1.1",ConfigReader.getValue("Section-2::Sub-section-1::Var1"));
    Assert.assertEquals( "Value1.1_1",ConfigReader.getValue("Section-1::Sub-section-1::Sub-section-1_1::Var1"));
    Assert.assertEquals( "Value2.1",ConfigReader.getValue("Section-1::Sub-section-1::Sub-section-1_1::Var2"));
  }
}

package com.m_landalex.dataconvert.servicepersistence.configuration;

import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.m_landalex.dataconvert.annotation.DataSets;

public class ServiceTestExecutionListener implements TestExecutionListener {

	private IDatabaseTester databaseTester;
	
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		DataSets dataSetsAnnotation = testContext.getTestMethod().getAnnotation(DataSets.class);
		
		if(dataSetsAnnotation == null) return;
		
		String dataSetName = dataSetsAnnotation.setUpDataSet();
		if(!dataSetName.equals("")) {
			
			databaseTester = (IDatabaseTester) testContext.getApplicationContext().getBean("dataSourceDatabaseTester");
			XlsDataFileLoader xlsDataFileLoader = (XlsDataFileLoader) testContext.getApplicationContext().getBean("xlsDataFileLoader");
			
			IDataSet dataSet = xlsDataFileLoader.load(dataSetName);
			databaseTester.setDataSet(dataSet);
			databaseTester.onSetup();
		}
	}
	
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		if(databaseTester != null) {
			databaseTester.onTearDown();
		}
	}
	
}

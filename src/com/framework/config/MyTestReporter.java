package com.framework.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.internal.TestResult;
import org.testng.xml.XmlSuite;

public class MyTestReporter implements IReporter {

    public MyTestReporter() {

    }
    
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
	Map<String, ISuiteResult> m = suites.get(0).getResults();
	HashMap<String, MethodResult> results = new HashMap<String, MethodResult>();
	
	for(Entry<String, ISuiteResult> e:m.entrySet()){
	    Object[] passed_results_array = e.getValue().getTestContext().getPassedTests().getAllResults().toArray();
	    Object[] failed_results_array = e.getValue().getTestContext().getFailedTests().getAllResults().toArray();
	    
	    Object[] both = ArrayUtils.addAll(passed_results_array, failed_results_array);	    
	    for (int i = 0; i < both.length; i++) {
		TestResult r = (TestResult) both[i];
		String name = r.getTestClass().getXmlClass().getName() + "." + r.getMethod().getMethodName();
		String finish_time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(r.getEndMillis()));
		new Date(r.getEndMillis()).toString();
		String result = (r.isSuccess() ? "PASS" : "FAIL");		
		MethodResult mr = new MethodResult(name, result, finish_time);
		results.put(name, mr);		
	    }
	}

	// записываем результаты в нужном формате в файл для последующей
	// загрузки в портал
	Document for_portal = DocumentHelper.createDocument();
	Element root = for_portal.addElement("testCaseResultList");
	for (Entry<String, MethodResult> e : results.entrySet()) {
	    // преобразуем строчки результатов в формат для тест портала
	    String status = e.getValue().getResult();
	    if ("PASS".equalsIgnoreCase(status)) {
		status = "passed";
	    } else if ("FAIL".equalsIgnoreCase(status)) {
		status = "failed";
	    } else if ("SKIP".equalsIgnoreCase(status)) {
		status = "not_executed";
	    } else {
		status = "failed";
	    }

	    Element CaseResult = root.addElement("testCaseResult");
	    CaseResult.addElement("result").setText(status);	    
	    CaseResult.addElement("time").setText(e.getValue().getFinish_time());

	    Element testCase = CaseResult.addElement("testCase");
	    testCase.addElement("externalId").setText(e.getValue().getName());
	}

	// lets write to a file
	XMLWriter writer;
	try {
	    for_portal.normalize();
	    File folder_for_file = new File(outputDirectory + FileSystems.getDefault().getSeparator() + suites.get(0).getName(), "for_portal.xml");
	    writer = new XMLWriter(new FileWriter(folder_for_file));
	    writer.write(for_portal);
	    writer.close();
	} catch (IOException e1) {
	    System.err.println("ERROR during writing for_portal.xml file to disk " + e1.getLocalizedMessage());
	    return;
	}
    }

    class MethodResult{
	String name;
	String result;
	String finish_time;
		
	public MethodResult(String name, String result, String finish_time) {
	    super();
	    this.name = name;
	    this.result = result;
	    this.finish_time = finish_time;
	}
	public String getName() {
	    return name;
	}
	public void setName(String name) {
	    this.name = name;
	}
	public String getResult() {
	    return result;
	}
	public void setResult(String result) {
	    this.result = result;
	}
	public String getFinish_time() {
	    return finish_time;
	}
	public void setFinish_time(String finish_time) {
	    this.finish_time = finish_time;
	}	
    }
}
/*// Разбираем testng-results.xml и собираем результат в results
SAXReader reader = new SAXReader();
int timeout = 60;
Document document;
try {
    File results_file = new File(outputDirectory, "testng-results.xml");	    
    FileUtils.waitFor(results_file, timeout);
    document = reader.read(results_file);
} catch (DocumentException e2) {
    System.err.println("ERROR during reading testng-results.xml after wait for "+ timeout+ " : " + e2.getLocalizedMessage());
    return;
}

HashMap<String, MethodResult> results = new HashMap<String, MethodResult>();

List<Node> classes = document.selectNodes("//class");
for (Node classs : classes) {
    String class_name = classs.valueOf("@name");
    List<Node> methods = classs.selectNodes("test-method[not(@is-config)]");
    for (Node method : methods) {
	String name = class_name + "." + method.valueOf("@name");
	String finish_time = method.valueOf("@finished-at").replace("T", " ").replace("Z", "");
	String result = method.valueOf("@status");		
	MethodResult mr = new MethodResult(name, result, finish_time);
	results.put(name, mr);
    }
}*/	
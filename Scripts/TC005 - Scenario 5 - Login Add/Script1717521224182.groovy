import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

/* Additional Import */
import java.util.Date
import java.text.SimpleDateFormat
import com.test.util.Constants
import com.test.util.Utils
import com.kms.katalon.core.util.KeywordUtil

/** set test data from data binding */
String name = "${name}"
String date = "${date}"
String type = "${type}"
String description = "${description}"
String uploadFile = "${isUploadFile}"
boolean isUploadFile = false
if(uploadFile.equalsIgnoreCase("true")) {
	isUploadFile = true
}

/* Init date format */
String pattern = "dd MMMM yyyy" //date format 1
String pattern2 = "yyyy-MM-dd" //date format 2
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern2)
if(date.equals("")) {
	date = simpleDateFormat.format(new Date())
}
Date d = simpleDateFormat.parse(date)
simpleDateFormat.applyPattern(pattern)
String expectedDate = simpleDateFormat.format(d)

/* Login */
Utils.login(Constants.USERNAME, Constants.PASSWORD)

/* Add Steps */
Mobile.tap(findTestObject('Object Repository/Home Page/buttonAddTodo'), GlobalVariable.timeout)
Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/buttonAddTodo'), GlobalVariable.timeoutNextPage)
if(!name.equals("")) {
	Mobile.setText(findTestObject('Object Repository/Add Page/editTextInputName'), name, GlobalVariable.timeout)
}
Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseDate'), GlobalVariable.timeout)
Utils.chooseDate(date)
Mobile.verifyElementText(findTestObject('Object Repository/Add Page/editTextDate'), expectedDate)
if(!type.equals("")) {
	Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseType'), GlobalVariable.timeout)
	Utils.chooseFormatType(type)
}
if(!description.equals("")) {
	Mobile.setText(findTestObject('Object Repository/Add Page/editTextInputDescription'), description, GlobalVariable.timeout)	
}

KeywordUtil.logInfo(isUploadFile.toString())
if(isUploadFile) {
	Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseFile'), GlobalVariable.timeout)
	Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/File Page/image'), GlobalVariable.timeoutNextPage)
	Mobile.tap(findTestObject('Object Repository/Add Page/File Page/image'), GlobalVariable.timeout)
	Mobile.verifyElementExist(findTestObject('Object Repository/Add Page/imageViewFile'), GlobalVariable.timeout)
}else {
	Mobile.verifyElementNotExist(findTestObject('Object Repository/Add Page/imageViewFile'), 2)	
}

if(name.equals("") || type.equals("") || description.equals("")) {
	/* Verify If Nama, Type, or Description is Empty */
	Mobile.verifyElementAttributeValue(findTestObject('Object Repository/Add Page/buttonAddTodo'), "enabled", "false", GlobalVariable.timeout)
	
	/* Back to Home Page */
	Mobile.tap(findTestObject('Object Repository/Add Page/buttonBack'), GlobalVariable.timeout)
}else {
	Mobile.tap(findTestObject('Object Repository/Add Page/buttonAddTodo'), GlobalVariable.timeout)
	/* Verify List After Add Todo */
	Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), GlobalVariable.timeoutNextPage)
	Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/buttonViewDetailTodo'), GlobalVariable.timeout)
	if(isUploadFile) {
		Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/imageViewTodo'), GlobalVariable.timeout)
	}else {
		Mobile.verifyElementNotExist(findTestObject('Object Repository/Home Page/Todo List/imageViewTodo'), 2)
	}
	Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewNameTodo'), name)
	Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewDateTodo'), expectedDate)
	Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewTypeTodo'), type)
	Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewDescriptionTodo'), description)
	Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), GlobalVariable.timeout)
	
	Utils.deleteTodo()
}





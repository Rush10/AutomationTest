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

/* Init date format */
String pattern = "dd MMMM yyyy" //date format 1
String pattern2 = "yyyy-MM-dd" //date format 2
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern2)

/* Init data */
String name = "MST QA Test"
String chooseDate = "2023-09-01"
Date d = simpleDateFormat.parse(chooseDate)
simpleDateFormat.applyPattern(pattern)
String expectedDate = simpleDateFormat.format(d)
String type = "Other"
String description = "Testing"
boolean isChooseFile = true


/* Login */
Utils.login(Constants.USERNAME, Constants.PASSWORD)

/* Add Steps */
Mobile.tap(findTestObject('Object Repository/Home Page/buttonAddTodo'), GlobalVariable.timeout)
Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/buttonAddTodo'), GlobalVariable.timeoutNextPage)
Mobile.setText(findTestObject('Object Repository/Add Page/editTextInputName'), name, GlobalVariable.timeout)
Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseDate'), GlobalVariable.timeout)
Utils.chooseDate(chooseDate)
Mobile.verifyElementText(findTestObject('Object Repository/Add Page/editTextDate'), expectedDate)
Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseType'), GlobalVariable.timeout)
Utils.chooseFormatType(type)
Mobile.setText(findTestObject('Object Repository/Add Page/editTextInputDescription'), description, GlobalVariable.timeout)
if(isChooseFile) {
	Mobile.tap(findTestObject('Object Repository/Add Page/buttonChooseFile'), GlobalVariable.timeout)
	Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/File Page/image'), GlobalVariable.timeoutNextPage)
	Mobile.tap(findTestObject('Object Repository/Add Page/File Page/image'), GlobalVariable.timeout)
	Mobile.verifyElementExist(findTestObject('Object Repository/Add Page/imageViewFile'), GlobalVariable.timeout)
}else {
	Mobile.verifyElementNotExist(findTestObject('Object Repository/Add Page/imageViewFile'), 2)	
}
Mobile.tap(findTestObject('Object Repository/Add Page/buttonAddTodo'), GlobalVariable.timeout)

/* Verify List After Add Todo */
Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), GlobalVariable.timeoutNextPage)
Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/buttonViewDetailTodo'), GlobalVariable.timeout)
if(isChooseFile) {
	Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/imageViewTodo'), GlobalVariable.timeout)
}else {
	Mobile.verifyElementNotExist(findTestObject('Object Repository/Home Page/Todo List/imageViewTodo'), 2)
}
Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewNameTodo'), name)
Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewDateTodo'), expectedDate)
Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewTypeTodo'), type)
Mobile.verifyElementText(findTestObject('Object Repository/Home Page/Todo List/textViewDescriptionTodo'), description)
Mobile.verifyElementExist(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), GlobalVariable.timeout)

/* Go to Detail Todo */
Mobile.tap(findTestObject('Object Repository/Home Page/Todo List/buttonViewDetailTodo'), GlobalVariable.timeout)
Mobile.waitForElementPresent(findTestObject('Object Repository/Detail Page/buttonBack'), GlobalVariable.timeoutNextPage)
Mobile.tap(findTestObject('Object Repository/Detail Page/buttonBack'), GlobalVariable.timeout)

Utils.deleteTodo()

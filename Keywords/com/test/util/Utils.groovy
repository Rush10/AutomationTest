package com.test.util

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

/** additional imports */
import internal.GlobalVariable
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import io.appium.java_client.AppiumDriver
import org.apache.commons.io.FilenameUtils
import com.kms.katalon.core.testobject.ConditionType
import java.util.Date
import java.util.Calendar
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.time.*

public class Utils {
	public static def startApp(String startMethod) {
		switch(startMethod) {
			case "app id":
				try {
					/** start app using app id */
					KeywordUtil.logInfo("App ID: " + GlobalVariable.appId)

					Mobile.startExistingApplication(GlobalVariable.appId)
				} catch (Exception e) {
					KeywordUtil.logInfo(e.message.toString())
					/** start app using apk, if apk is not found (startExistingApplication doesn't work) */
					startApp("apk")
				}
				break
			case "apk":
			/** CONFIGURATION: Add folder 'Automation Test/APK' in Downloads folder on your PC. In that folder,
			 * add apk file that you want to run for the test !!
			 * note: you can change folder path and name in global variable apkPath */

				List fileNameList = fileNameList()

				if(fileNameList.size() == 0) {
					KeywordUtil.markErrorAndStop("Error: There is no apk file in selected folder !!")
				}else {
					String home = System.getProperty("user.home")

					String apkPath = home + GlobalVariable.apkPath + fileNameList.get(GlobalVariable.apkIndex) + ".apk"

					KeywordUtil.logInfo("APK Local Path: " + apkPath)

					/** start app using apk */
					Mobile.startApplication(apkPath, GlobalVariable.isUninstallApp)
				}
				break
			default:
				KeywordUtil.markErrorAndStop("Error: startMethod must be 'app id' or 'apk' !!")
				break
		}
	}

	public static def closeApp(Boolean isDeleteSession) {
		if(isDeleteSession) {
			/** close app and delete session */
			AppiumDriver driver = MobileDriverFactory.getDriver()

			driver.terminateApp(GlobalVariable.appId)
			driver.resetApp()
			driver.quit()
		}else {
			/** close app without delete session */
			Mobile.closeApplication()
		}
	}

	public static List fileNameList() {
		String home = System.getProperty("user.home")
		File folder = new File(home + GlobalVariable.apkPath) //set folder using path

		File[] listOfFiles = folder.listFiles() //get all file in folder
		List fileNameList = new ArrayList<String>()

		try {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String fileName = listOfFiles[i].getName() //get file name with extension
					if(FilenameUtils.getExtension(fileName).equals("apk")) {
						/** if file extension is apk */

						fileNameList.add(fileName.substring(0, fileName.length()-4)) //store file name in the list
					}
				}
			}
		} catch (Exception e) {
			print(e.message)
			KeywordUtil.markErrorAndStop("Error: there is no such folder!!")
		}

		return fileNameList
	}

	public static def login(String username, String password) {
		/** method to login */
		Mobile.waitForElementPresent(findTestObject('Object Repository/Login Page/buttonLogin'), GlobalVariable.timeoutNextPage)
		Mobile.setText(findTestObject('Object Repository/Login Page/editTextUsername'), username, GlobalVariable.timeout)
		Mobile.setText(findTestObject('Object Repository/Login Page/editTextPassword'), password, GlobalVariable.timeout)
		Mobile.tap(findTestObject('Object Repository/Login Page/buttonLogin'), GlobalVariable.timeout)
		Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/buttonAddTodo'), GlobalVariable.timeoutNextPage)
	}

	public static def chooseFormatType(String type) {
		/** to check todo type format and choose todo type */
		switch(type) {
			case 'Primary':
			/* choose primary type steps */
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonPrimary'), GlobalVariable.timeoutNextPage)
				Mobile.tap(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonPrimary'), GlobalVariable.timeout)
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/editTextChooseType'), GlobalVariable.timeoutNextPage)
				Mobile.verifyElementText(findTestObject('Object Repository/Add Page/editTextChooseType'), 'Primary')
				break
			case 'Secondary':
			/* choose secondary type steps */
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonSecondary'), GlobalVariable.timeoutNextPage)
				Mobile.tap(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonSecondary'), GlobalVariable.timeout)
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/editTextChooseType'), GlobalVariable.timeoutNextPage)
				Mobile.verifyElementText(findTestObject('Object Repository/Add Page/editTextChooseType'), 'Secondary')
				break
			case 'Other':
			/* choose other type steps */
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonOther'), GlobalVariable.timeoutNextPage)
				Mobile.tap(findTestObject('Object Repository/Add Page/Bottomsheet Type/buttonOther'), GlobalVariable.timeout)
				Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/editTextChooseType'), GlobalVariable.timeoutNextPage)
				Mobile.verifyElementText(findTestObject('Object Repository/Add Page/editTextChooseType'), 'Other')
				break
			default:
				KeywordUtil.markErrorAndStop("Error: Todo type format must be ['Primary','Secondary','Other'] !!")
				break
		}
	}

	public static def chooseDate(String chooseDate) {
		/** method to choose date */
		String pattern = "yyyy-MM-dd" //date format
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern)
		String todayDate = simpleDateFormat.format(new Date()) //today date

		/* Init date xpath */
		String dateXpath = "//*[@class = 'android.widget.Button' and @resource-id = 'native.calendar.SELECT_DATE_SLOT-" + chooseDate + "']"

		Date date1 = simpleDateFormat.parse(chooseDate) //date 1 = choose date
		Date date2 = simpleDateFormat.parse(todayDate) //date 2 = today date

		if(date1.after(date2)){
			int monthsBetween = ChronoUnit.MONTHS.between(
					YearMonth.from(LocalDate.parse(todayDate)),
					YearMonth.from(LocalDate.parse(chooseDate))
					)

			Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/Modal Date/buttonRight'), GlobalVariable.timeout)
			for(int i = 0 ; i < monthsBetween ; i++) {
				Mobile.tap(findTestObject('Object Repository/Add Page/Modal Date/buttonRight'), GlobalVariable.timeout)
			}
		}

		if(date1.before(date2)){
			int monthsBetween = ChronoUnit.MONTHS.between(
					YearMonth.from(LocalDate.parse(chooseDate)),
					YearMonth.from(LocalDate.parse(todayDate))
					)

			Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/Modal Date/buttonLeft'), GlobalVariable.timeout)
			for(int i = 0 ; i < monthsBetween ; i++) {
				Mobile.tap(findTestObject('Object Repository/Add Page/Modal Date/buttonLeft'), GlobalVariable.timeout)
			}
		}

		Mobile.tap(Utils.createTestObject(dateXpath), GlobalVariable.timeout)
		Mobile.tap(findTestObject('Object Repository/Add Page/Modal Date/buttonOK'), GlobalVariable.timeout)
		Mobile.waitForElementPresent(findTestObject('Object Repository/Add Page/editTextDate'), GlobalVariable.timeout)
	}

	public static def deleteTodo() {
		/* method delete todo */
		try {
			Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), 3)
			Mobile.tap(findTestObject('Object Repository/Home Page/Todo List/buttonDeleteTodo'), 3)
			Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/Todo List/Modal Delete/buttonYes'), 3)
			Mobile.tap(findTestObject('Object Repository/Home Page/Todo List/Modal Delete/buttonYes'), 3)
			Mobile.verifyElementNotExist(findTestObject('Object Repository/Home Page/Todo List/buttonViewDetailTodo'), 2)
		} catch (Exception e) {
			KeywordUtil.markWarning(e.message)
		}

	}

	public static def logout() {
		/* method to logout */
		try {
			Mobile.waitForElementPresent(findTestObject('Object Repository/Home Page/buttonLogout'), 3)
			Mobile.tap(findTestObject('Object Repository/Home Page/buttonLogout'), 3)
			Mobile.waitForElementPresent(findTestObject('Object Repository/Login Page/buttonLogin'), 3)
			Mobile.verifyElementExist(findTestObject('Object Repository/Login Page/buttonLogin'), 3)
		} catch (Exception e) {
			KeywordUtil.markWarning(e.message)
		}
	}

	public static TestObject createTestObject(String xpath) {
		TestObject tObj = new TestObject(xpath)
		tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		return tObj
	}
}

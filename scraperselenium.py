from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.common.by import By
import time


driver = webdriver.Remote(
   command_executor='http://127.0.0.1:4444/wd/hub',
   desired_capabilities=DesiredCapabilities.FIREFOX)

driver.get("https://www.ticketea.com/conciertos/proximos/")

time.sleep(2)

driver.find_element(By.ID, 'cookies-acceptance').click()
concerts = driver.find_elements(By.CLASS_NAME, 'card__item')
titles = driver.find_elements(By.CLASS_NAME, 'card__title')

for concert in concerts:
    titol = concert.find_element(By.CLASS_NAME, 'card__title')
    dataDiv = concert.find_element(By.CLASS_NAME, 'date-module--small')
    dataMetas = dataDiv.find_elements(By.TAG_NAME, 'meta')
    print(titol.text)
    for data in dataMetas:
        print(data.get_attribute('content'))


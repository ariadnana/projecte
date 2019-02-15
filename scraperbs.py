import requests
from bs4 import BeautifulSoup
import time
import MySQLdb

i = 1;
page = requests.get('https://altaveu.cat/concerts?page=1')
soup = BeautifulSoup(page.content, 'lxml')
concerts = soup.findAll("div", {"class": "concert-card"})
while (len(concerts)!=0):
    for sibling in concerts:
        data = sibling["data-date"]
        hora = sibling.find("div", {"class": "concert-card__hour"}).get_text().strip()
        print(data+" "+hora)
        nom = sibling.find("h3")
        print(nom.text)
        lloc = sibling.find("div", {"class": "concert-card__city"}).get_text().strip()
        print(lloc)
    i+=1
    page = requests.get('https://altaveu.cat/concerts?page='+str(i))
    soup = BeautifulSoup(page.content, 'lxml')
    concerts = soup.findAll("div", {"class": "concert-card"})
    time.sleep(5)



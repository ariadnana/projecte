import time

import requests
from bs4 import BeautifulSoup
import pymysql.cursors


connection = pymysql.connect(host='localhost',
                             user='root',
                             password='1234',
                             db='projecte',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)

i = 1;
page = requests.get('https://altaveu.cat/concerts?page=1')
soup = BeautifulSoup(page.content, 'lxml')
concerts = soup.findAll("div", {"class": "concert-card"})
while (len(concerts)!=0):
    for sibling in concerts:
        lloc = sibling.find("div", {"class": "concert-card__city"}).get_text().strip()
        print(lloc)
        with connection.cursor() as cursor:
            # Read a single record
            sql = "SELECT `id` FROM `localitzacions` WHERE `nom`=%s"
            cursor.execute(sql, lloc)
            result = cursor.fetchone()

        if(result==None):
            with connection.cursor() as cursor:
                # Create a new record
                sql = "INSERT INTO `localitzacions` (`nom`) VALUES (%s)"
                cursor.execute(sql, lloc)
            connection.commit()
            id = cursor.lastrowid
        else:
            id = result['id']

        data = sibling["data-date"]
        hora = sibling.find("div", {"class": "concert-card__hour"}).get_text().strip()
        print(data+" "+hora)
        nom = sibling.find("h3")
        print(nom.text)

        print (id)
        with connection.cursor() as cursor:
            # Read a single record
            sql = "SELECT `id` FROM `concerts` WHERE `nom`=%s AND `data`=%s AND `localitzacio_id`=%s"
            cursor.execute(sql, (nom.text, data+" "+hora, id))
            result = cursor.fetchone()
            if(result==None):
                with connection.cursor() as cursor:
                    # Create a new record
                    sql = "INSERT INTO `concerts` (`nom`, `data`, `localitzacio_id`) VALUES (%s, %s, %s)"
                    cursor.execute(sql, (nom.text, data + " " + hora, id))

                connection.commit()

    i+=1
    page = requests.get('https://altaveu.cat/concerts?page='+str(i))
    soup = BeautifulSoup(page.content, 'lxml')
    concerts = soup.findAll("div", {"class": "concert-card"})
    time.sleep(5)

connection.close()



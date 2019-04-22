import time

import requests
from bs4 import BeautifulSoup
import pymysql.cursors

connection = pymysql.connect(host='localhost',
                             user='ariadnatfg',
                             password='1234',
                             db='projecte',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)

"""connection = pymysql.connect(host='localhost',
                             user='root',
                             password='',
                             db='projecte',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)"""

i = 1;
page = requests.get('https://altaveu.cat/concerts?page=1')
soup = BeautifulSoup(page.content, 'lxml')
concerts = soup.findAll("div", {"class": "concert-card"})
while (len(concerts)!=0):
    for sibling in concerts:
        poblacio = sibling.find("div", {"class": "concert-card__city"}).get_text().strip()
        print(poblacio)

        with connection.cursor() as cursor:
            # Read a single record
            sql = "SELECT `id` FROM `poblacions` WHERE `nom`=%s"
            cursor.execute(sql, poblacio)
            result = cursor.fetchone()

        if (result == None):
            with connection.cursor() as cursor:
                # Create a new record
                sql = "INSERT INTO `poblacions` (`nom`) VALUES (%s)"
                cursor.execute(sql, poblacio)
            connection.commit()
            poblacio_id = cursor.lastrowid
        else:
            poblacio_id = result['id']

        data = sibling["data-date"]
        hora = sibling.find("div", {"class": "concert-card__hour"}).get_text().strip()
        print(data+" "+hora)
        nomcard = sibling.find("h3").text
        print(nomcard)


        with connection.cursor() as cursor:
            # Read a single record
            sql = "SELECT `id` FROM `concerts` WHERE `nomcard`=%s AND `data`=%s"
            cursor.execute(sql, (nomcard, data+" "+hora))
            result = cursor.fetchone()
        if(result==None):
            link = sibling.find("a", {"class": "concert-card__link"}).get('href')
            print(link)

            time.sleep(5)
            pageconcert = requests.get(link)
            content = BeautifulSoup(pageconcert.content, 'lxml')

            nom = content.find("h1", {"class": "title-title2"})
            if(nom==None):
                nom=nomcard
            else:
                nom=nom.text
            print(nom)

            preu = content.find("li", {"class": "tickets"})
            if(preu!=None):
                preu=preu.get_text()
            print(preu)

            desc = content.find("div", {"class": "txt-p2"})
            if(desc!=None):
                desc = desc.get_text().encode("utf-8")
                print(desc)

            webcard = content.find("div", {"class": "concert-content__web"})
            if(webcard!=None):
                web = webcard.find("a").get('href')
                print(web)
            else:
                web=None

            address = content.find("li", {"class": "address"})
            parts = address.findAll("span")
            lloc = ""
            for p in parts:
                lloc += p.get_text()+" "
            print(lloc)

            url_lloc = content.find("div", {"class": "concert-content__map"})
            if(url_lloc!=None):
                urllloc = url_lloc.find("iframe").get('src').replace(" ", "%20")
                urllloc = urllloc[:urllloc.find("&ie=")];
                print(urllloc)
            else:
                urllloc=None

            with connection.cursor() as cursor:
                # Read a single record
                sql = "SELECT `id` FROM `localitzacions` WHERE `nom`=%s AND `poblacio_id`=%s"
                cursor.execute(sql, (lloc, poblacio_id))
                result = cursor.fetchone()

            if(result==None):
                with connection.cursor() as cursor:
                    # Create a new record
                    sql = "INSERT INTO `localitzacions` (`nom`,`poblacio_id`,`url` ) VALUES (%s, %s, %s)"
                    cursor.execute(sql, (lloc, poblacio_id, urllloc))
                connection.commit()
                localitzacio_id = cursor.lastrowid
            else:
                localitzacio_id = result['id']

            artistes=content.findAll("div", {"class": "band__name title-title5"});
            artistes_id=[]
            for a in artistes:
                noma = a.get_text().replace("\n", " ").replace("\t", " ").strip()
                print(noma)
                with connection.cursor() as cursor:
                    # Read a single record
                    sql = "SELECT `id` FROM `artistes` WHERE `nom`=%s"
                    cursor.execute(sql, noma)
                    result = cursor.fetchone()

                if (result == None):
                    with connection.cursor() as cursor:
                        # Create a new record
                        sql = "INSERT INTO `artistes` (`nom`) VALUES (%s)"
                        cursor.execute(sql, noma)
                    connection.commit()
                    artistes_id.append(cursor.lastrowid)
                else:
                    artistes_id.append(result['id'])
            print(artistes_id)

            with connection.cursor() as cursor:
                # Create a new record
                sql = "INSERT INTO `concerts` (`nom`, `data`, `localitzacio_id`, `desc`, `web`, `preu`, `nomcard`) VALUES (%s, %s, %s, %s, %s, %s, %s)"
                cursor.execute(sql, (nom, data + " " + hora, localitzacio_id, desc, web, preu, nomcard))

            connection.commit()
            id = cursor.lastrowid
            for a in artistes_id:

                with connection.cursor() as cursor:
                    # Create a new record
                    sql = "INSERT INTO `concerts_artistes` (`concert_id`, `artista_id`) VALUES (%s, %s)"
                    cursor.execute(sql, (id, a))

                connection.commit()




    i+=1
    page = requests.get('https://altaveu.cat/concerts?page='+str(i))
    soup = BeautifulSoup(page.content, 'lxml')
    concerts = soup.findAll("div", {"class": "concert-card"})
    time.sleep(5)

connection.close()



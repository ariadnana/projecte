from lxml import html
import requests

page = requests.get('https://altaveu.cat/concerts?page=1')
tree = html.fromstring(page.content)
xpath = '//div[@class="concerts-list__wrap"]/div'
concerts = tree.xpath(xpath)
i = 1
for concert in concerts:
    data = concert.attrib['data-date']
    nom = concert.xpath(xpath + '[' + str(i) + ']//h3/text()')
    print(data)
    print(nom[0])
    i=i+1

print("hehe")


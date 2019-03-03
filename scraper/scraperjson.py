import requests
import json

page = requests.get("https://www.ticketmaster.es/api/category/10001/events?f_pleasenote=false&f_handlingfee=false&f_feebreakdown=true&f_deliveryoptions=true&f_resaledeliveryoptions=false&f_verifiedtickets=false&f_alwaysshowresale=true&f_mtt_variant=true&f_showResaleOnListView=true&f_showLowDemandBasketCollapsible=true&f_showIntegratedResale=false&f_forceResale=false&f_orderprocessingfee=false&f_showAccessibleInfo=false&f_hideGeneralOnsale=false&f_show_suggestions=false&countries=724&page=0&size=10000")
content = json.loads(page.content)
for concert in content['events']:
    if 'dateStart' in concert and 'localDate' in concert['dateStart']:
        print(concert['title'])
        print(concert['dateStart']['localDate'])
<?php

namespace app\controllers;

use Yii;
use DateTime;
use yii\filters\AccessControl;
use yii\web\Controller;
use yii\web\Response;
use yii\filters\VerbFilter;
use app\models\Artistes;
use app\models\Concerts;
use app\models\Localitzacions;
use app\models\Poblacions;
use yii\db\Query;

class ConcertsController extends Controller
{
    /**
     * {@inheritdoc}
     */
    public function behaviors()
    {
        return [
            'access' => [
                'class' => AccessControl::className(),
                'only' => ['logout'],
                'rules' => [
                    [
                        'actions' => ['logout'],
                        'allow' => true,
                        'roles' => ['@'],
                    ],
                ],
            ],
            'verbs' => [
                'class' => VerbFilter::className(),
                'actions' => [
                    'logout' => ['post'],
                ],
            ],
        ];
    }

    /**
     * {@inheritdoc}
     */
    public function actions()
    {
        return [
            'error' => [
                'class' => 'yii\web\ErrorAction',
            ],
            'captcha' => [
                'class' => 'yii\captcha\CaptchaAction',
                'fixedVerifyCode' => YII_ENV_TEST ? 'testme' : null,
            ],
        ];
    }

    /**
     * Displays homepage.
     *
     * @return string
     */
    public function actionIndex($artista=null, $data=null, $poblacio=null, $gratuit=0)
    {
        //http://localhost/API/public/web/concerts
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $query = new Query;
        $query	->select(['concerts.id', 'concerts.nom', 'day(data) as dia', 
        'CASE WHEN month(data) =  "1" THEN "Gen" WHEN month(data) =  "2" THEN "Feb" WHEN month(data) =  "3" THEN "Març" WHEN month(data) =  "4" THEN "Abr" WHEN month(data) =  "5" THEN "Maig" WHEN month(data) =  "6" THEN "Juny" WHEN month(data) =  "7" THEN "Jul" WHEN month(data) =  "8" THEN "Ago" WHEN month(data) =  "9" THEN "Set" WHEN month(data) =  "10" THEN "Oct" WHEN month(data) =  "11" THEN "Nov" ELSE "Des" END as mes', 
        'substring(time(data), 1, 5)  as hora', 'preu', 'poblacions.nom as localitzacio'])  
                ->from('concerts')
                ->join('LEFT JOIN', 'localitzacions',
                    'concerts.localitzacio_id =localitzacions.id')
                ->join('LEFT JOIN', 'poblacions',
                    'localitzacions.poblacio_id =poblacions.id')
                ->join('LEFT JOIN', 'concerts_artistes',
                    'concerts_artistes.concert_id =concerts.id')
                    ->join('LEFT JOIN', 'artistes',
                        'concerts_artistes.artista_id =artistes.id')
                ->where('data>"'.date("Y-m-d").' 00:00:00"');
        if(isset($artista)) $query = $query->andWhere(['artistes.nom' => $artista]);
        if(isset($data)) $query = $query->andWhere('concerts.data like "'.$data.'%"');
        if(isset($poblacio)) $query = $query->andWhere(['poblacions.nom' => $poblacio]);
        if($gratuit==1) $query = $query->andWhere(['preu' => 'Gratuït']);
        $query = $query->groupBy('concerts.id')->orderBy('data'); 
        $command = $query->createCommand();
        $obj = (object) [
            'items' => $command->queryAll()
        ];
        return $obj;
    }

    /**
     * Displays homepage.
     *
     * @return string
     */
    public function actionConcert($id)
    {
        //http://localhost/API/public/web/concerts
        $mesos =["Gen", "Feb", "Març", "Abr", "Maig", "Juny", "Jul", "Ago", "Set", "Oct", "Nov", "Des"];
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $concert = Concerts::findOne($id);
        $artistes = Artistes::find()
            ->select(['nom'])
            ->leftJoin('concerts_artistes', 'artista_id=artistes.id')
            ->where(['concert_id' => $id])
            ->asArray()
            ->all();
        $obj = (object) [
            'nom' => $concert->nom,
            'dia' => DateTime::createFromFormat("Y-m-d H:i:s", $concert->data)->format("d"),
            'mes' => $mesos[intval(DateTime::createFromFormat("Y-m-d H:i:s", $concert->data)->format("m"))-1],
            'hora' => DateTime::createFromFormat("Y-m-d H:i:s", $concert->data)->format("H:i"),
            'desc' => $concert->desc,
            'localitzacio' => $concert->localitzacio->nom,
            'poblacio' => $concert->poblacio->nom,
            'web' => $concert->web,
            'preu' => $concert->preu,
            'artistes' => $concert->artistes,  
            'mapa' => $concert->localitzacio->url,
        ];
        return $obj;
    }

    public function actionFiltres()
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $artistes = Artistes::find()
            ->select(['artistes.nom'])
            ->leftJoin('concerts_artistes', 'artista_id=artistes.id')
            ->leftJoin('concerts', 'concert_id=concerts.id')
            ->where('data>"'.date("Y-m-d").' 00:00:00"')
            ->orderBy('nom')
            ->distinct()
            ->asArray()
            ->all();
        $poblacions = Poblacions::find()
        ->select(['poblacions.nom'])
        ->leftJoin('localitzacions', 'poblacions.id=poblacio_id')
        ->leftJoin('concerts', 'localitzacions.id=localitzacio_id')
        ->where('data>"'.date("Y-m-d").' 00:00:00"')
        ->orderBy('nom')
        ->distinct()
        ->asArray()
        ->all();
        $obj = (object) [
            'artistes' => array_column($artistes, 'nom'),            
            'poblacions' => array_column($poblacions, 'nom'),
        ];
        return $obj;
    }

    public function actionFavs($favs)
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $query = new Query;
        $query	->select(['concerts.id', 'concerts.nom', 'day(data) as dia', 
        'CASE WHEN month(data) =  "1" THEN "Gen" WHEN month(data) =  "2" THEN "Feb" WHEN month(data) =  "3" THEN "Març" WHEN month(data) =  "4" THEN "Abr" WHEN month(data) =  "5" THEN "Maig" WHEN month(data) =  "6" THEN "Juny" WHEN month(data) =  "7" THEN "Jul" WHEN month(data) =  "8" THEN "Ago" WHEN month(data) =  "9" THEN "Set" WHEN month(data) =  "10" THEN "Oct" WHEN month(data) =  "11" THEN "Nov" ELSE "Des" END as mes', 
        'substring(time(data), 1, 5)  as hora', 'preu', 'poblacions.nom as localitzacio'])  
                ->from('concerts')
                ->join('LEFT JOIN', 'localitzacions',
                    'concerts.localitzacio_id =localitzacions.id')
                ->join('LEFT JOIN', 'poblacions',
                    'localitzacions.poblacio_id =poblacions.id')
                ->join('LEFT JOIN', 'concerts_artistes',
                    'concerts_artistes.concert_id =concerts.id')
                    ->join('LEFT JOIN', 'artistes',
                        'concerts_artistes.artista_id =artistes.id')
                ->where('data>"'.date("Y-m-d").' 00:00:00"')
                ->andWhere("concerts.id in (".substr($favs, 0, -1).")");
        $query = $query->groupBy('concerts.id')->orderBy('data'); 
        $command = $query->createCommand();
        $obj = (object) [
            'items' => $command->queryAll()
        ];
        return $obj;

    }
}

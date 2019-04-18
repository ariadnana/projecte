<?php

namespace app\controllers;

use Yii;
use yii\filters\AccessControl;
use yii\web\Controller;
use yii\web\Response;
use yii\filters\VerbFilter;
use app\models\Artistes;
use app\models\Concerts;
use app\models\Localitzacions;
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
        $query	->select(['concerts.id', 'concerts.nom', 'data', 'poblacions.nom as localitzacio'])  
                ->from('concerts')
                ->join('LEFT JOIN', 'localitzacions',
                    'concerts.localitzacio_id =localitzacions.id')
                ->join('LEFT JOIN', 'poblacions',
                    'localitzacions.poblacio_id =poblacions.id')
                ->join('LEFT JOIN', 'concerts_artistes',
                    'concerts_artistes.concert_id =concerts.id')
                ->where('data>"'.date("Y-m-d").' 00:00:00"');
        if(isset($artista)) $query = $query->andWhere(['artista_id' => $artista]);
        if(isset($data)) $query = $query->andWhere('concerts.data like "'.$data.'%"');
        if(isset($poblacio)) $query = $query->andWhere(['poblacio_id' => $poblacio]);
        if($gratuit==1) $query = $query->andWhere(['preu' => 'Gratuït']);
        $query = $query->groupBy('concerts.id')->orderBy('data'); 
        $command = $query->createCommand();
        $obj = (object) [
            'items' => $command->queryAll()
        ];
        echo json_encode($obj);
        exit();
    }

    /**
     * Displays homepage.
     *
     * @return string
     */
    public function actionConcert($id)
    {
        //http://localhost/API/public/web/concerts
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
            'data' => $concert->data,
            'desc' => $concert->desc,
            'localitzacio' => $concert->localitzacio->nom,
            'poblacio' => $concert->poblacio->nom,
            'web' => $concert->web,
            'preu' => $concert->preu,
            'artistes' => $artistes,  
        ];
        echo json_encode($obj);
        exit();
    }
}

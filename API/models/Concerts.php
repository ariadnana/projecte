<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "concerts".
 *
 * @property int $id
 * @property string $nom
 * @property string $data
 * @property string $desc
 * @property int $localitzacio_id
 * @property string $web
 * @property string $preu
 * @property string $imatge
 * @property string $nomcard
 *
 * @property Localitzacions $localitzacio
 * @property ConcertsArtistes[] $concertsArtistes
 */
class Concerts extends \yii\db\ActiveRecord
{
    /**
     * {@inheritdoc}
     */
    public static function tableName()
    {
        return 'concerts';
    }

    /**
     * {@inheritdoc}
     */
    public function rules()
    {
        return [
            [['nom'], 'required'],
            [['data'], 'safe'],
            [['desc'], 'string'],
            [['localitzacio_id'], 'integer'],
            [['nom', 'nomcard'], 'string', 'max' => 2048],
            [['web'], 'string', 'max' => 2000],
            [['preu', 'imatge'], 'string', 'max' => 255],
            [['localitzacio_id'], 'exist', 'skipOnError' => true, 'targetClass' => Localitzacions::className(), 'targetAttribute' => ['localitzacio_id' => 'id']],
        ];
    }

    /**
     * {@inheritdoc}
     */
    public function attributeLabels()
    {
        return [
            'id' => 'ID',
            'nom' => 'Nom',
            'data' => 'Data',
            'desc' => 'Desc',
            'localitzacio_id' => 'Localitzacio ID',
            'web' => 'Web',
            'preu' => 'Preu',
            'imatge' => 'Imatge',
            'nomcard' => 'Nomcard',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getLocalitzacio()
    {
        return $this->hasOne(Localitzacions::className(), ['id' => 'localitzacio_id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPoblacio()
    {
        return $this->hasOne(Poblacions::className(), ['id' => 'poblacio_id'])
            ->viaTable('localitzacions', ['id' => 'localitzacio_id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getArtistes()
    {
        return $this->hasMany(Artistes::className(), ['id' => 'artista_id'])
            ->viaTable('concerts_artistes', ['concert_id' => 'id']);
    }
}

<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "localitzacions".
 *
 * @property int $id
 * @property string $nom
 * @property double $lat
 * @property double $lng
 * @property int $poblacio_id
 *
 * @property Concerts[] $concerts
 * @property Poblacions $poblacio
 */
class Localitzacions extends \yii\db\ActiveRecord
{
    /**
     * {@inheritdoc}
     */
    public static function tableName()
    {
        return 'localitzacions';
    }

    /**
     * {@inheritdoc}
     */
    public function rules()
    {
        return [
            [['lat', 'lng'], 'number'],
            [['poblacio_id'], 'integer'],
            [['nom'], 'string', 'max' => 2048],
            [['poblacio_id'], 'exist', 'skipOnError' => true, 'targetClass' => Poblacions::className(), 'targetAttribute' => ['poblacio_id' => 'id']],
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
            'lat' => 'Lat',
            'lng' => 'Lng',
            'poblacio_id' => 'Poblacio ID',
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getConcerts()
    {
        return $this->hasMany(Concerts::className(), ['localitzacio_id' => 'id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getPoblacio()
    {
        return $this->hasOne(Poblacions::className(), ['id' => 'poblacio_id']);
    }
}

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
 *
 * @property Concerts[] $concerts
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
            [['nom'], 'string', 'max' => 2048],
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
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getConcerts()
    {
        return $this->hasMany(Concerts::className(), ['localitzacio_id' => 'id']);
    }
}

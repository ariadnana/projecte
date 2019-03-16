<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "poblacions".
 *
 * @property int $id
 * @property string $nom
 *
 * @property Localitzacions[] $localitzacions
 */
class Poblacions extends \yii\db\ActiveRecord
{
    /**
     * {@inheritdoc}
     */
    public static function tableName()
    {
        return 'poblacions';
    }

    /**
     * {@inheritdoc}
     */
    public function rules()
    {
        return [
            [['nom'], 'required'],
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
        ];
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getLocalitzacions()
    {
        return $this->hasMany(Localitzacions::className(), ['poblacio_id' => 'id']);
    }

    /**
     * @return \yii\db\ActiveQuery
     */
    public function getConcerts()
    {
        return $this->hasMany(Concerts::className(), ['localitzacio_id' => 'id'])
        ->viaTable('localitzacions', ['poblacio_id' => 'id']);
    }
}

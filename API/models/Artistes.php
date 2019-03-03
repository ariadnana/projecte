<?php

namespace app\models;

use Yii;

/**
 * This is the model class for table "artistes".
 *
 * @property int $id
 * @property string $nom
 *
 * @property ConcertsArtistes[] $concertsArtistes
 */
class Artistes extends \yii\db\ActiveRecord
{
    /**
     * {@inheritdoc}
     */
    public static function tableName()
    {
        return 'artistes';
    }

    /**
     * {@inheritdoc}
     */
    public function rules()
    {
        return [
            [['nom'], 'string', 'max' => 255],
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
    public function getConcerts()
    {
        return $this->hasMany(Concerts::className(), ['id' => 'concert_id'])
            ->viaTable('concerts_artistes', ['artista_id' => 'id']);
    }
}

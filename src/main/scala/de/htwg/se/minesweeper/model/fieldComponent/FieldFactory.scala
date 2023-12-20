package de.htwg.se.minesweeper.model.fieldComponent

import de.htwg.se.minesweeper.model.fieldComponent.field.RandomFieldFactory

trait FieldFactory {
	def createField(width: Int, height: Int, bomb_chance: Float): FieldInterface;
}

object FieldFactory {
	given defaultFieldFactory: RandomFieldFactory = RandomFieldFactory(scala.util.Random)
}
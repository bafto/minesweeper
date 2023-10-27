package minesweeper.model

import scala.util.Random

class Field(rows: Int, cols: Int, genbomb: (Int, Int) => Cell) {
	val matrix = Vector.tabulate(rows, cols) {(x, y) => genbomb(x, y)}

	override def toString(): String = {
		matrix.map(r => r.mkString(" ")).mkString("\n")
	}

	def withRevealed(x: Int, y: Int): Field = {
		val newMatrix = matrix.updated(y, matrix(y).updated(x, Cell(true, matrix(y)(x).isBomb)))
		Field(rows, cols, (x: Int, y: Int) => newMatrix(x)(y))
	}

	def isInBounds(x: Int, y: Int): Boolean = {
		matrix.length > y && matrix(y).length > x
	}
}

object Field {
	def getRandBombGen(rand: Random, bomb_chance: Float): (Int, Int) => Cell =
		(_, _) => Cell(false, rand.nextInt((1/bomb_chance).toInt) == 0)
}
package de.htwg.se.minesweeper.controller

import de.htwg.se.minesweeper.model.fieldComponent.FieldInterface
import de.htwg.se.minesweeper.observer.Observable
import scala.util.Try
import de.htwg.se.minesweeper.controller.baseController.*
import de.htwg.se.minesweeper.model.fieldComponent.FieldFactory
import de.htwg.se.minesweeper.model.fieldComponent.field.RandomFieldFactory
import scala.util.Random

trait ControllerInterface extends Observable[Event] {
    def getMaxUndos: Int
    def getUndos: Int
    def getField: FieldInterface
    def cantUndo: Boolean
    def cantRedo: Boolean
    def getBombChance: Float

    def setup(): Unit
    def startGame(width: Int, height: Int, bomb_chance: Float, undos: Int): Unit
    def reveal(x: Int, y: Int): Try[Unit]
    def flag(x: Int, y: Int): Try[Unit]
    def undo(): Try[Unit]
    def redo(): Try[Unit]
    def exit(): Unit
}

object ControllerInterface {
    given baseController: BaseController = BaseController()
}
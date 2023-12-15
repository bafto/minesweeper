package de.htwg.se.minesweeper.controller.baseController

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import de.htwg.se.minesweeper.model._
import de.htwg.se.minesweeper.observer._
import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.model.fieldComponent.FieldFactory
import de.htwg.se.minesweeper.model.fieldComponent.FieldInterface

class BaseController(val base_undos: Int, val factory: FieldFactory) extends Observable[Event] with ControllerInterface {
	private[baseController] var width: Int = 0
	private[baseController] var height: Int = 0
	private[baseController] var bomb_chance: Float = 0

	private[baseController] var field: FieldInterface = factory.createField(0, 0, 0)
	private[baseController] var state: BaseControllerState = FirstMoveBaseControllerState(this)
	private var undos = base_undos

	override def getUndos: Int = undos
	override def getField: FieldInterface = field

	override def getBombChance: Float = bomb_chance

	private[baseController] def changeState(newState: BaseControllerState): Unit = {
		state = newState
	}

	override def setup(): Unit = {
		undos = base_undos
		state = FirstMoveBaseControllerState(this)
		undoStack = List.empty
		redoStack = List.empty
		field = factory.createField(width, height, bomb_chance)
		notifyObservers(SetupEvent())
	}

	override def startGame(width: Int, height: Int, bomb_chance: Float): Unit = {
		this.width = width
		this.height = height
		this.bomb_chance = bomb_chance
		field = factory.createField(width, height, bomb_chance)
		state = FirstMoveBaseControllerState(this)
		notifyObservers(StartGameEvent(field))
	}

	override def reveal(x: Int, y: Int): Try[Unit] = execute(RevealCommand(this, x, y))

	override def flag(x: Int, y: Int): Try[Unit] = execute(FlagCommand(this, x, y))

	private[baseController] def flag_impl(x: Int, y: Int): Try[Unit] = {
		field.withToggledFlag(x, y) match {
			case Success(newField) => {
				field = newField
				Try(notifyObservers(FieldUpdatedEvent(field)))
			}
			case Failure(exception) => Failure(exception)
		}
	}

	override def exit(): Unit = {
		notifyObservers(ExitEvent())
	}

	private[baseController] var undoStack: List[Command] = List.empty
	private[baseController] var redoStack: List[Command] = List.empty

	private def execute(command: Command): Try[Unit] = {
		undoStack = command :: undoStack
		redoStack = List.empty
		command.execute() match {
			case Success(_) => Success(())
			case Failure(exception) => {
				undoStack = undoStack.tail
				Failure(exception)
			}
		}
	}

	override def undo(): Try[Unit] = {
		undoStack match {
			case Nil => Failure(new NoSuchElementException("Nothing to undo!"))
			case head :: tail => {
				if undos <= 0 then return Failure(new RuntimeException("No more undo's left!"))
				head.undo()
				undoStack = tail
				redoStack = head :: redoStack
				undos -= 1
				Success(())
			}
		}
	}

	override def redo(): Try[Unit] = {
		redoStack match {
			case Nil => Failure(new NoSuchElementException("Nothing to redo!"))
			case head :: tail => {
				head.redo()
				redoStack = tail
				undoStack = head :: undoStack
				Success(())
			}
		}
	}

	override def cantRedo: Boolean = redoStack.isEmpty
}
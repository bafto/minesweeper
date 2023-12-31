package de.htwg.se.minesweeper.controller.baseController

import scala.util.{Try, Success, Failure}
import de.htwg.se.minesweeper.controller._

private trait Command {
    def execute(): Try[Unit]
    def undo(): Unit
    def redo(): Unit
}

private class RevealCommand(controller: BaseController, x: Int, y: Int) extends Command {
    private var field = controller.getField
    override def execute(): Try[Unit] = {
        controller.state.reveal(x, y) match {
            case Success(field) => {
                this.field = field
                Success(())
            }
            case Failure(exception) => Failure(exception)
        }
    }
    override def undo(): Unit = {
        controller.field = field
        controller.notifyObservers(FieldUpdatedEvent(field))
    }
    override def redo(): Unit = controller.state.reveal(x, y)
}

private class FlagCommand(controller: BaseController, x: Int, y: Int) extends Command {
    override def execute(): Try[Unit] = controller.flag_impl(x, y)
    override def undo(): Unit = controller.flag_impl(x, y)
    override def redo(): Unit = controller.flag_impl(x, y)
}

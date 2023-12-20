package de.htwg.se.minesweeper.controller.spyController

import de.htwg.se.minesweeper.controller._
import de.htwg.se.minesweeper.controller.baseController.BaseController
import de.htwg.se.minesweeper.model.fieldComponent.FieldFactory

class SpyController(using factory: FieldFactory) extends BaseController() with ControllerInterface {
    var didWin = false
    var didLose = false
    var didExit = false
    
    override def setup(): Unit = {
        didWin = false
        didLose = false
        didExit = false
        super.setup()
    }

    override def exit(): Unit = {
        didExit = true
        super.exit()
    }
}
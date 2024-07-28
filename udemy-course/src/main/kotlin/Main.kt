import tictactoe.Player
import tictactoe.TicTacToe

fun main() {
    println("Hello! Welcome to the game!")
    println("Please enter your name:")
    val name = readln()
    val humanPlayer = Player(name, TicTacToe.Board.MARKER.FIRST_PLAYER)
    val computerPlayer = Player("ciko", TicTacToe.Board.MARKER.SECOND_PLAYER)
    val ticTacToe = TicTacToe(humanPlayer, computerPlayer)

    println("let the game begin...")
    do {
        getPlayersMove(ticTacToe, humanPlayer)
        if (ticTacToe.board.canTheGameContinue()) {
            makeComputersMove(ticTacToe, computerPlayer)
        }
    } while (ticTacToe.board.canTheGameContinue())

    ticTacToe.printResult()
}

private fun getPlayersMove(ticTacToe: TicTacToe, player: Player) {
    var validTargetMarked = false
    do {
        try {
            println("current board:")
            ticTacToe.board.printBoardLayout()
            val target = getAValidValue()
            ticTacToe.board.mark(target, player.getMarker())
            validTargetMarked = true
        } catch (ex: Throwable) {
            println("error: ${ex.message} ")
        }
    } while (!validTargetMarked)
}

private fun getAValidValue(): Int {
    var target: Int = -1
    var validValueEntered = false
    do {
        try {
            println("please mark your next target (0-8):")
            target = readln().toInt()
            validValueEntered = true
        } catch (ex: Throwable) {
            println("error: ${ex.message} ")
        }
    } while (!validValueEntered)
    return target
}


fun makeComputersMove(ticTacToe: TicTacToe, computerPlayer: Player) {
    ticTacToe.board.markARandomEmptyPlace(computerPlayer.getMarker())
}

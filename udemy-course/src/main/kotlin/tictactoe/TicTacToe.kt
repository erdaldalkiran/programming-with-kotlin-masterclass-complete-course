package tictactoe

import kotlin.random.Random

class TicTacToe(val firstPlayer: Player, val secondPlayer: Player) {
    val board: Board = Board()

    class Board {
        enum class MARKER(val marker: String) {
            EMPTY("-"), FIRST_PLAYER("X"), SECOND_PLAYER("O");
        }

        companion object {
            const val ROW_COUNT = 3
            const val COLUMN_COUNT = 3
        }

        private val board = Array(3) { _ -> Array(3) { _ -> MARKER.EMPTY } }

        fun mark(target: Int, marker: MARKER) {
            val rowColumn = getRowColum(target)
            require(isTargetEmpty(rowColumn)) { "please choice a target which is empty" }
            require(isAnyEmptySpaceLeft()) { "the game has been ended. no space left to mark." }
            board[rowColumn.first][rowColumn.second] = marker
        }

        fun canTheGameContinue(): Boolean {
            return !this.isThereAWinner().first && this.isAnyEmptySpaceLeft()
        }

        fun isThereAWinner(): Pair<Boolean, MARKER?> {
            for (row in board) {
                if (isAllTheSameMarker(row.toList())) {
                    return Pair(true, row.first())
                }
            }

            for (i in 0..<COLUMN_COUNT) {
                val markers = getMarkersOfTheColumn(i)
                if (isAllTheSameMarker(markers)) {
                    return Pair(true, markers.first())
                }
            }

            var markers = getMarkersOfTheDiagonal()
            if (isAllTheSameMarker(markers)) {
                return Pair(true, markers.first())
            }

            markers = getMarkersOfTheReverseDiagonal()
            if (isAllTheSameMarker(markers)) {
                return Pair(true, markers.first())
            }

            return Pair(false, null)
        }

        fun printBoardLayout() {
            for (row in board) {
                for (column in row) {
                    print("${column.marker} ")
                }
                println()
            }
        }

        private fun isAnyEmptySpaceLeft(): Boolean {
            return board.any { row -> row.any { cell -> cell == MARKER.EMPTY } }
        }

        private fun getMarkersOfTheDiagonal(): Collection<MARKER> {
            val result = mutableListOf<MARKER>()
            for ((rowIndex, row) in board.withIndex()) {
                for ((columnIndex, value) in row.withIndex()) {
                    if (rowIndex == columnIndex) {
                        result.add(value)
                    }
                }
            }
            return result
        }

        private fun getMarkersOfTheReverseDiagonal(): Collection<MARKER> {
            val result = mutableListOf<MARKER>()
            for ((rowIndex, row) in board.withIndex()) {
                for ((columnIndex, value) in row.withIndex()) {
                    if (rowIndex + columnIndex == ROW_COUNT - 1) {
                        result.add(value)
                    }
                }
            }
            return result
        }

        private fun getMarkersOfTheColumn(targetColumn: Int): Collection<MARKER> {
            val result = mutableListOf<MARKER>()
            for (row in board) {
                for ((index, value) in row.withIndex()) {
                    if (index == targetColumn) {
                        result.add(value)
                    }
                }
            }
            return result
        }

        private fun isAllTheSameMarker(markers: Collection<MARKER>): Boolean {
            val notEmptyOnes = markers.filterNot { marker -> marker == MARKER.EMPTY }
            val notEmptyCount = notEmptyOnes.count()
            val distinctCount = notEmptyOnes.distinct().count()
            return notEmptyCount == ROW_COUNT && distinctCount == 1
        }

        private fun isTargetEmpty(rowColumn: Pair<Int, Int>): Boolean {
            return board[rowColumn.first][rowColumn.second] == MARKER.EMPTY
        }

        private fun getRowColum(target: Int): Pair<Int, Int> {
            require(target in 0..8) { "target must be between 0 and 8" }
            return Pair(target / ROW_COUNT, target % COLUMN_COUNT)
        }

        fun markARandomEmptyPlace(marker: MARKER) {
            val count = board.sumOf { row -> row.count { cell -> cell == MARKER.EMPTY } }
            val target = Random.nextInt(0, count)
            var iterationCount = 0
            for ((rowIndex, row) in board.withIndex()) {
                for ((columnIndex, value) in row.withIndex()) {
                    if (value == MARKER.EMPTY) {
                        if (iterationCount == target) {
                            board[rowIndex][columnIndex] = marker
                            return
                        }
                        iterationCount++
                    }
                }
            }
        }
    }

    fun printResult() {
        require(!board.canTheGameContinue()) { "You can not announce the result while the game is continuing" }
        if (board.isThereAWinner().first) {
            when (board.isThereAWinner().second) {
                TicTacToe.Board.MARKER.FIRST_PLAYER -> println("${firstPlayer.getName()} wins!!")
                TicTacToe.Board.MARKER.SECOND_PLAYER -> println("${secondPlayer.getName()} wins!!")
                else -> throw Exception("shouldn't be here any how!")
            }
        } else {
            println("the game ended in a draw!!")
        }
        board.printBoardLayout()
    }

}


class Player(private val name: String, private val marker: TicTacToe.Board.MARKER) {
    fun getName(): String = name
    fun getMarker(): TicTacToe.Board.MARKER = marker
}
'''
Created on Sep 20, 2015

@author: Cuyler Quint <deanquint@gmail.com>


Tic Tac Toe
    Requirements
        -python version 1.7
        -pygame version 1.9

    Description:
        This is a clone of the famous tic tac Toe
        with implemetation of a computer to play agaisnt
        at three different levels.

        level 1: random moves
        level 2: blocks winning moves
        level 3: level 2 and attemps to win

        command line arguments are very straigt forward. Enjoy!

'''

import Game


def playAgain():
    """
    Asks the user if they would like to play another game
    and sets the difficulty of the computer
    """
    playAgain = raw_input()
    if playAgain == 'y':
        print('\nSelect Difficulty\n1)level 0\n2)level 1\n3)level 2')
        difficulty = input()
        myGame.setGame()
        myGame.Difficulty = difficulty
        play()

    elif playAgain == 'n':
        raise SystemExit

def play():
    """
    Main game loop that determines who has won and Controls
    game play between player and computer.  Handles tie cases.
    """
    while myGame.Playing:
        if (myGame.turn == 'user'):
            myGame.printBox(myGame.TTTbox)
            myGame.userPick(myGame.TTTbox,myGame.UserLetter)
            if myGame.Win(myGame.TTTbox,myGame.UserLetter):
                myGame.printBox(myGame.TTTbox)
                print('***YOU WON***\nPlay Again:(y/n)?')
                myGame.Playing = False
                playAgain();

            if (myGame.turnCount == 9):
                myGame.printBox(myGame.TTTbox)
                print('tie game Play Again:(y/n)?')
                playAgain();
            else:
                myGame.turn = 'AI'
                myGame.turnCount += 1
        elif (myGame.turn == 'AI'):
            myGame.makeAIMove(myGame.TTTbox,myGame.AILetter,myGame.Difficulty)
            if myGame.Win(myGame.TTTbox,myGame.AILetter):
                myGame.printBox(myGame.TTTbox)
                myGame.Playing = False
                print('***AI WON***\nPlay Again:(y/n)?')
                playAgain();
                myGame.Playing = False
            if ((myGame.turnCount == 9) or ((myGame.turnCount == 7) and ((myGame.Difficulty == 2) or(myGame.Difficulty == 3)))):
                    myGame.printBox(myGame.TTTbox)
                    myGame.Playing = False
                    print('Tie game play Again:(y/n)?')

                    playAgain();

            else:
                myGame.turn = 'user'
                myGame.turnCount += 1

user = Game.Player('user','X')
AI = Game.Player('AI','O')
myGame = Game.Game(user,AI)
myGame.setGame()
play()
